package org.knowledgemanager.model.nonpersistent;

import org.knowledgemanager.model.Content;
import org.knowledgemanager.model.Graph;
import org.knowledgemanager.model.Node;
import org.knowledgemanager.model.TextContent;
import org.log4j.Category;
import java.util.Vector;

public class GraphImp implements Graph {

    private static Category cat = Category.getInstance(GraphImp.class.getName());

    private Vector roots = new Vector();

    /**CONFIG_OPTION*/
    public static final String EMPTY_GRAPH = "empty";

    /** returns a default model*/
    public GraphImp() {
        Node root = createNode(new TextContent("root"));
        Node child1 = createNode(new TextContent("child1"));
        root.addNode(child1);
        Node child11 = createNode(new TextContent("child11"));
        child1.addNode(child11);
        Node child111 = createNode(new TextContent("child111"));
        child11.addNode(child111);
        Node child112 = createNode(new TextContent("child112"));
        child11.addNode(child112);
        Node child113 = createNode(new TextContent("child113"));
        child11.addNode(child113);
        Node child1131 = createNode(new TextContent("child1131"));
        child113.addNode(child1131);
        Node child1132 = createNode(new TextContent("child1132"));
        child113.addNode(child1132);
        Node child1133 = createNode(new TextContent("child1133"));
        child113.addNode(child1133);
        Node child1134 = createNode(new TextContent("child1134"));
        child113.addNode(child1134);
        Node child114 = createNode(new TextContent("child114"));
        child11.addNode(child114);
        Node child12 = createNode(new TextContent("child12"));
        child1.addNode(child12);
        Node child13 = createNode(new TextContent("child13"));
        child1.addNode(child13);
        Node child2 = createNode(new TextContent("child2"));
        root.addNode(child2);
        Node child21 = createNode(new TextContent("child21"));
        child2.addNode(child21);
        Node child22 = createNode(new TextContent("child22"));
        child2.addNode(child22);
        Node child23 = createNode(new TextContent("child23"));
        child2.addNode(child23);
        Node child3 = createNode(new TextContent("child3"));
        root.addNode(child3);
        child1.addNode(child2);
        root.addNode(child23);
        child21.addNode(root);
    }

    public GraphImp(String configOption) {
    }

    public Node[] getRoots() {
        Node[] result = new Node[roots.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (Node) roots.elementAt(i);
        }
        return result;
    }

    public void removeRoot(Node toRemove) {
        roots.remove(toRemove);
    }

    public Node createNode(Content content) {
        Node node = new NodeImp(this, content);
        roots.add(node);
        return node;
    }

    public void addRoot(Node root) {
        if (!roots.contains(root)) {
            roots.add(root);
        }
    }

    public static void main(String[] argv) {
        Graph graph = new GraphImp();
    }
}
