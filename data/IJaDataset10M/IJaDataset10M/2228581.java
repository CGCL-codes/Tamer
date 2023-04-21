package nl.gridshore.samples.jcr;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Node;
import java.io.IOException;

/**
 * @author Jettro Coenradie
 *
 * <p>This class is used to demonstrate creating a new node in the repository</p>
 */
public class StoreData extends Base {

    public static void main(String[] args) {
        StoreData storeData = new StoreData();
        storeData.run();
    }

    @Override
    protected void doRun() throws Exception {
        Session session = getSession();
        Node rootNode = session.getRootNode();
        Node node = rootNode.addNode("Jettro");
        node.setProperty("title", "Chief Architect");
        node.setProperty("email", "jettro@jteam.nl");
        node.setProperty("age", 36);
        session.save();
        logout(session);
    }
}
