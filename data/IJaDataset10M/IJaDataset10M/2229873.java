package jlibs.xml.sax.dog.expr.nodset;

/**
 * @author Santhosh Kumar T
 */
public final class Last extends Positional {

    public Last() {
        super(false);
    }

    @Override
    public String toString() {
        return "last()";
    }
}
