package org.gvsig.gpe.containers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.namespace.QName;
import org.gvsig.gpe.parser.IAttributesIterator;

/**
* @author Carlos S�nchez Peri��n
*/
public class AttributesIterator implements IAttributesIterator {

    private HashMap attributes;

    private Iterator keys = null;

    private int next = 0;

    private QName currentAttibuteName = null;

    public AttributesIterator(QName name, Object value) {
        attributes = new HashMap();
        attributes.put(name, value);
        initialize();
    }

    public AttributesIterator(QName[] names, String[] values) {
        attributes = new HashMap();
        for (int i = 0; i < names.length; i++) {
            attributes.put(names[i], values[i]);
        }
        initialize();
    }

    public boolean hasNext() throws IOException {
        currentAttibuteName = null;
        return (keys.hasNext());
    }

    public int getNumAttributes() {
        return attributes.size();
    }

    public Object nextAttribute() throws IOException {
        setAttributeName();
        return attributes.get(currentAttibuteName);
    }

    public QName nextAttributeName() {
        setAttributeName();
        return currentAttibuteName;
    }

    private void setAttributeName() {
        if (currentAttibuteName == null) {
            currentAttibuteName = (QName) keys.next();
        }
    }

    public void initialize() {
        keys = attributes.keySet().iterator();
    }
}
