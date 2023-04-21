package net.sf.jdsc;

/**
 * @author <a href="mailto:twyss@users.sourceforge.net">twyss</a>
 * @version 1.0
 */
public class ReverseArrayEnumerator<T> extends ArrayEnumerator<T> {

    public ReverseArrayEnumerator(T[] array, int offset, int length) {
        super(array, offset, length);
    }

    public ReverseArrayEnumerator(T[] array, int length) {
        super(array, length);
    }

    public ReverseArrayEnumerator(T[] array) {
        super(array);
    }

    @Override
    public boolean moveNext() {
        return super.movePrevious();
    }

    @Override
    public boolean movePrevious() {
        return super.moveNext();
    }
}
