package oosnippets.learn.apache.jackrabbit;

import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Node;
import javax.jcr.Value;
import org.apache.jackrabbit.core.TransientRepository;

/**
 * Second hop example. Stores, retrieves, and removes example content.
 */
public class MyFirstHop {

    /**
     * The main entry point of the example application.
     *
     * @param args command line arguments (ignored)
     * @throws Exception if an error occurs
     */
    public static void main(String[] args) throws Exception {
        Repository repository = new TransientRepository();
        Session session = repository.login(new SimpleCredentials("username", "password".toCharArray()));
        try {
            Node root = session.getRootNode();
            Node hello = root.addNode("hello");
            Node world = hello.addNode("world");
            world.setProperty("message", "Hello, World!");
            session.save();
            Node node = root.getNode("hello/world");
            System.out.println(node.getPath());
            System.out.println(node.getProperty("message").getString());
            dump(root);
            root.getNode("hello").remove();
            session.save();
        } finally {
            session.logout();
        }
    }

    /** Recursively outputs the contents of the given node. */
    private static void dump(Node node) throws RepositoryException {
        System.out.println(node.getPath());
        if (node.getName().equals("jcr:system")) {
            return;
        }
        PropertyIterator properties = node.getProperties();
        while (properties.hasNext()) {
            Property property = properties.nextProperty();
            if (property.getDefinition().isMultiple()) {
                Value[] values = property.getValues();
                for (int i = 0; i < values.length; i++) {
                    System.out.println(property.getPath() + " = " + values[i].getString());
                }
            } else {
                System.out.println(property.getPath() + " = " + property.getString());
            }
        }
        NodeIterator nodes = node.getNodes();
        while (nodes.hasNext()) {
            dump(nodes.nextNode());
        }
    }
}
