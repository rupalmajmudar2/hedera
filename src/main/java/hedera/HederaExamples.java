package hedera;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.HederaPreCheckStatusException;
import com.hedera.hashgraph.sdk.HederaReceiptStatusException;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountBalance;
//import io.github.cdimascio.dotenv.Dotenv;

import java.util.concurrent.TimeoutException;

public class HederaExamples {

    public static void main(String[] args) throws TimeoutException, HederaPreCheckStatusException, Exception {

        //Grab your Hedera testnet account ID and private key
        AccountId myAccountId = AccountId.fromString("0.0.15656129"); //temp Dotenv.load().get("MY_ACCOUNT_ID"));
        PrivateKey myPrivateKey = PrivateKey.fromString("302e020100300506032b6570042204202bdec14e0d6f5de6b4b066c563534bf049e1d3d340dbd87e25044f261d54d753"); //temp Dotenv.load().get("MY_PRIVATE_KEY"));

        //Create your Hedera testnet client
        Client client = Client.forTestnet();
        client.setOperator(myAccountId, myPrivateKey);

        // Generate a new key pair
        PrivateKey newAccountPrivateKey = PrivateKey.generate();
        PublicKey newAccountPublicKey = newAccountPrivateKey.getPublicKey();

        //Create new account and assign the public key
        TransactionResponse newAccount = new AccountCreateTransaction()
                .setKey(newAccountPublicKey)
                .setInitialBalance( Hbar.fromTinybars(1000))
                .execute(client);

        // Get the new account ID
        AccountId newAccountId = newAccount.getReceipt(client).accountId;

        System.out.println("The new account ID is: " +newAccountId);

        //Check the new account's balance
        AccountBalance accountBalance = new AccountBalanceQuery()
                .setAccountId(newAccountId)
                .execute(client);

        System.out.println("The new account balance is: " +accountBalance.hbars);

    }
}