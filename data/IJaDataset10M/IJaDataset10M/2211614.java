package org.apache.commons.collections.primitives.decorators;

import org.apache.commons.collections.primitives.DoubleIterator;
import org.apache.commons.collections.primitives.DoubleListIterator;

/**
 * 
 * @since Commons Primitives 1.0
 * @version $Revision: 1.1.4.1 $ $Date: 2008/02/24 14:28:53 $
 * 
 * @author Rodney Waldhoff
 */
abstract class ProxyDoubleListIterator extends ProxyDoubleIterator implements DoubleListIterator {

    ProxyDoubleListIterator() {
    }

    public boolean hasPrevious() {
        return getListIterator().hasPrevious();
    }

    public int nextIndex() {
        return getListIterator().nextIndex();
    }

    public double previous() {
        return getListIterator().previous();
    }

    public int previousIndex() {
        return getListIterator().previousIndex();
    }

    @Override
    protected final DoubleIterator getIterator() {
        return getListIterator();
    }

    protected abstract DoubleListIterator getListIterator();
}
