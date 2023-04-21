package de.barmenia.cs.jcr.util;

import java.io.PrintWriter;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.SimpleCredentials;
import javax.jcr.LoginException;
import javax.jcr.Value;

public class Helper {

    /**
	* Dumps the contents of the given node to standard output.
	*
	* @param node the node to be dumped
	* @throws RepositoryException on repository errors
	*/
    public static void dump(Node node, PrintWriter out) throws RepositoryException {
        out.println(node.getPath());
        PropertyIterator properties = node.getProperties();
        while (properties.hasNext()) {
            Property property = properties.nextProperty();
            out.print(property.getPath() + "=");
            if (property.getDefinition().isMultiple()) {
                Value[] values = property.getValues();
                for (int i = 0; i < values.length; i++) {
                    if (i > 0) {
                        out.println(",");
                    }
                    out.println(values[i].getString());
                }
            } else {
                out.print(property.getString());
            }
            out.println();
        }
        NodeIterator nodes = node.getNodes();
        while (nodes.hasNext()) {
            Node child = nodes.nextNode();
            dump(child, out);
        }
    }
}
