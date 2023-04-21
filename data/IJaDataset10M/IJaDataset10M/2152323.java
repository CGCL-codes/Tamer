package org.wikiup.core.imp.iterator;

import java.util.Iterator;
import org.wikiup.core.imp.attribute.BeanProperty;
import org.wikiup.core.inf.Attribute;

public class BeanPropertyIterator implements Iterator<Attribute> {

    private Iterator<String> iterator;

    private Object pojo;

    public BeanPropertyIterator(Object pojo) {
        iterator = new BeanPropertyNameIterator(pojo.getClass());
        this.pojo = pojo;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public Attribute next() {
        String name = iterator.next();
        return new BeanProperty(pojo, name);
    }

    public void remove() {
        iterator.remove();
    }
}
