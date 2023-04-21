package org.openorb.compiler.idl.reflect;

/**
 * This class implements an enumeration to provide all interface IDL object of an IDL object
 *
 * @author Jerome Daniel
 * @version $Revision: 1.2 $ $Date: 2004/02/10 21:02:38 $
 */
public class idlEnumeration implements java.util.Enumeration {

    /**
     * Internale list
     */
    private java.util.Vector _list;

    /**
     * Internal index
     */
    private int _index;

    /**
     * Constructor
     */
    public idlEnumeration(java.util.Vector list) {
        _list = list;
        _index = 0;
    }

    /**
     * Return true if there is more elements
     */
    public boolean hasMoreElements() {
        if (_list == null) return false;
        if (_index < _list.size()) return true;
        return false;
    }

    /**
     * Return the next element if any
     */
    public Object nextElement() {
        if (_list == null) return null;
        if (_index < _list.size()) {
            return _list.elementAt(_index++);
        }
        return null;
    }
}
