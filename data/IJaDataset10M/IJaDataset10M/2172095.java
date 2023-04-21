package net.sourceforge.javautil.common.reflection.proxy;

import java.util.Iterator;
import java.util.List;

/**
 * This allows a parameterized {@link Iterator} to be proxified.
 * 
 * @version $Id: ReflectiveIterator.java 1536 2009-12-03 22:51:08Z ponderator $
 * @author elponderador
 * @author $Author: ponderator $
 */
public class ReflectiveIterator implements Iterator {

    private Iterator original;

    private Class returnType;

    private ReflectiveProxy proxy;

    public ReflectiveIterator(ReflectiveProxy proxy, Class returnType, Iterator original) {
        this.original = original;
        this.proxy = proxy;
        this.returnType = returnType;
    }

    public boolean hasNext() {
        return original.hasNext();
    }

    public Object next() {
        return proxy.createProxyForResult(original.next(), returnType, null);
    }

    public void remove() {
        original.remove();
    }
}
