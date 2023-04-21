package testing;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestHibernateTreeStructure {

    private static final String ENTITY_NODE = Node.class.getName();

    private static int idRoot1, idRoot2, idRoot3;

    public static void main(String[] argv) {
        System.out.println(Node.class.getName());
        Configuration cfg = new Configuration().configure("testing/hibernateTreeStructure/hibernate.cfg.xml");
        SessionFactory factory = cfg.buildSessionFactory();
        testObjectCreation(factory);
        testObjectCreation2(factory);
        testObjectCreation3(factory);
        testObjectRetrieval(factory);
        testObjectDeletion(factory);
    }

    static void testObjectDeletion(SessionFactory factory) {
        System.out.println("========== testObjectDeletion ==========");
        Session session = factory.openSession();
        Node root3 = (Node) session.get(ENTITY_NODE, idRoot3);
        session.delete(ENTITY_NODE, root3.getChildren().remove(0));
        session.flush();
        session.close();
    }

    private static void testObjectRetrieval(SessionFactory factory) {
        System.out.println("========== testObjectRetrieval ==========");
        Session session = factory.openSession();
        Node root1 = (Node) session.get(ENTITY_NODE, idRoot1);
        Node root2 = (Node) session.get(ENTITY_NODE, idRoot2);
        Node root3 = (Node) session.get(ENTITY_NODE, idRoot3);
        session.flush();
        session.close();
    }

    private static void testObjectCreation(SessionFactory factory) {
        System.out.println("========== testObjectCreation ==========");
        Session session = factory.openSession();
        Node root1 = new Node("ROOT_1");
        session.save(ENTITY_NODE, root1);
        idRoot1 = root1.getId();
        Node child1 = new Node("CHILD_1");
        child1.setParent(root1);
        session.save(ENTITY_NODE, child1);
        root1 = (Node) session.get(ENTITY_NODE, root1.getId());
        Node child2 = new Node("CHILD_2");
        child2.setParent(root1);
        session.save(ENTITY_NODE, child2);
        root1 = (Node) session.get(ENTITY_NODE, root1.getId());
        session.flush();
        session.close();
    }

    private static void testObjectCreation2(SessionFactory factory) {
        System.out.println("========== testObjectCreation2 ==========");
        Session session = factory.openSession();
        Node root2 = new Node("ROOT_2");
        session.save(ENTITY_NODE, root2);
        idRoot2 = root2.getId();
        Node child1 = new Node("CHILD_1");
        child1.setParent(root2);
        session.save(ENTITY_NODE, child1);
        root2 = (Node) session.get(ENTITY_NODE, root2.getId());
        Node child12 = new Node("CHILD_1_2");
        child12.setParent(child1);
        session.save(ENTITY_NODE, child12);
        root2 = (Node) session.get(ENTITY_NODE, root2.getId());
        session.flush();
        session.close();
    }

    private static void testObjectCreation3(SessionFactory factory) {
        System.out.println("========== testObjectCreation3 ==========");
        Session session = factory.openSession();
        Node root3 = new Node("ROOT_3");
        session.save(ENTITY_NODE, root3);
        idRoot3 = root3.getId();
        Node child1 = new Node("CHILD_1");
        root3.addChild(child1);
        session.save(ENTITY_NODE, child1);
        session.update(ENTITY_NODE, root3);
        Node child2 = new Node("CHILD_2");
        root3.addChild(child2);
        session.save(ENTITY_NODE, child2);
        session.update(ENTITY_NODE, root3);
        Node child11 = new Node("CHILD_1_1");
        child1.addChild(child11);
        session.save(ENTITY_NODE, child11);
        session.update(ENTITY_NODE, root3);
        session.update(ENTITY_NODE, child1);
        Node child12 = new Node("CHILD_1_2");
        child1.addChild(child12);
        session.save(ENTITY_NODE, child12);
        session.update(ENTITY_NODE, root3);
        session.update(ENTITY_NODE, child1);
        session.flush();
        session.close();
    }

    public static class Node {

        private int id;

        private String name;

        private Node parent = null;

        private List<Node> children;

        public Node() {
        }

        public Node(String name) {
            setName(name);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public List<Node> getChildren() {
            return children;
        }

        public void setChildren(List<Node> children) {
            this.children = children;
        }

        public synchronized void addChild(Node child) {
            if (children == null) {
                children = new ArrayList<Node>();
            }
            children.add(child);
            child.setParent(this);
        }
    }
}
