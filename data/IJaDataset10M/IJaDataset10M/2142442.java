package net.sf.jdsc;

import static java.lang.String.valueOf;

/**
 * @author <a href="mailto:twyss@users.sourceforge.net">twyss</a>
 * @version 1.0
 */
public abstract class AbstractEnumerator<T> implements IEnumerator<T> {

    @Override
    public IEnumerator<T> enumerator() {
        return this;
    }

    @Override
    public Iterator<T> iterator() {
        return new EnumeratorAdapter<T>(this);
    }

    @Override
    public String toString() {
        return valueOf(getCurrent());
    }
}
