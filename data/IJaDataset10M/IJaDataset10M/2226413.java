package jStaticAnalizer.jStaticEngine.jVisitor;

/**
 * Write a description of class JVisitorSemanticNode here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class JVisitorSemanticNode implements JIVisitorNode {

    private int x;

    /**
     * Constructor for objects of class JVisitorSemanticNode
     */
    public JVisitorSemanticNode() {
        x = 0;
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public int sampleMethod(int y) {
        return x + y;
    }
}
