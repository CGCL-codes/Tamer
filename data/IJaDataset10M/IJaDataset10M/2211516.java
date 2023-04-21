package jStaticAnalizer.jStaticEngine.jNode;

/**
 * Write a description of class JDeclarationNode here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class JDeclarationNode implements JNode {

    private int x;

    /**
     * Constructor for objects of class JDeclarationNode
     */
    public JDeclarationNode() {
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
