package edu.pattern.visitor;

public class TestVisitor {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        testVisitor();
    }

    public static void testVisitor() {
        ConcreteVisitor v = new ConcreteVisitor();
        Node a = new ConcreteNodeA();
        Node b = new ConcreteNodeB();
        a.accept(v);
        b.accept(v);
        DifferentNode d = new DifferentNode();
        d.accept(v);
    }
}
