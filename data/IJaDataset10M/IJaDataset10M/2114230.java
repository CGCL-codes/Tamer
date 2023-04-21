package org.apache.commons.collections.primitives.decorators;

import org.apache.commons.collections.primitives.CharCollection;
import org.apache.commons.collections.primitives.CharList;
import org.apache.commons.collections.primitives.CharListIterator;

/**
 * 
 * @since Commons Primitives 1.0
 * @version $Revision: 1.1.4.3 $ $Date: 2008/04/26 11:00:55 $
 * 
 * @author Rodney Waldhoff
 */
abstract class BaseProxyCharList extends BaseProxyCharCollection implements CharList {

    protected abstract CharList getProxiedList();

    @Override
    protected final CharCollection getProxiedCollection() {
        return getProxiedList();
    }

    protected BaseProxyCharList() {
    }

    public void add(final int index, final char element) {
        getProxiedList().add(index, element);
    }

    public boolean addAll(final int index, final CharCollection collection) {
        return getProxiedList().addAll(index, collection);
    }

    public char get(final int index) {
        return getProxiedList().get(index);
    }

    public int indexOf(final char element) {
        return getProxiedList().indexOf(element);
    }

    public int lastIndexOf(final char element) {
        return getProxiedList().lastIndexOf(element);
    }

    public CharListIterator listIterator() {
        return getProxiedList().listIterator();
    }

    public CharListIterator listIterator(final int index) {
        return getProxiedList().listIterator(index);
    }

    public char removeElementAt(final int index) {
        return getProxiedList().removeElementAt(index);
    }

    public char set(final int index, final char element) {
        return getProxiedList().set(index, element);
    }

    public CharList subList(final int fromIndex, final int toIndex) {
        return getProxiedList().subList(fromIndex, toIndex);
    }
}
