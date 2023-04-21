package test;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.transaction.UserTransaction;
import net.sf.archimede.model.UserTransactionImpl;
import org.apache.jackrabbit.core.TransientRepository;

public class TransactionTest {

    public static void main(String[] args) throws Exception {
        TransientRepository repository = new TransientRepository();
        for (int i = 0; i < 10000; i++) {
            Session session = repository.login(new SimpleCredentials("username", "password".toCharArray()));
            try {
                for (int j = 0; j < 10; j++) {
                    Node rootNode = session.getRootNode();
                    rootNode.addNode("" + System.currentTimeMillis());
                    session.save();
                }
            } finally {
                session.logout();
            }
        }
    }
}
