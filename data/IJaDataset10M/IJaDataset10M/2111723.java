package fr.jussieu.gla.wasa.core.junit;

/**
 * A JavaBean holding an <tt>int</tt> value.
 *
 * @author Laurent Caillette
 * @version $Revision: 1.1.1.1 $ $Date: 2002/02/19 22:12:02 $
 */
public class IntBean {

    private int value = 0;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
