package net.sf.refactorit.audit.rules.j2se5;

/**
 *
 * @author  Arseni Grigorjev
 */
public class GenericsNodeUnresolvableException extends Exception {

    GenericsNode node;

    /** Creates a new instance of GenericsRemoveFromGraphException */
    public GenericsNodeUnresolvableException(final String msg) {
        super(msg);
        node = null;
    }

    public GenericsNodeUnresolvableException(final String msg, final GenericsNode node) {
        super(msg);
        this.node = node;
    }

    public GenericsNode getNode() {
        return node;
    }

    public void setNode(GenericsNode node) {
        this.node = node;
    }
}
