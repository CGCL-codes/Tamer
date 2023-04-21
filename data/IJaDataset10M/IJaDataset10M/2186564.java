package edu.berkeley.guir.quill.util;

import java.util.*;

/** 
 * <P>
 * This software is distributed under the 
 * <A HREF="http://guir.cs.berkeley.edu/projects/COPYRIGHT.txt">
 * Berkeley Software License</A>.
 
*/
public class CollectionEvent extends EventObject {

    public static final int ELEMENT_ADDED = 0;

    public static final int ELEMENT_REMOVED = 1;

    public static final int ELEMENT_CHANGED = 2;

    private int type;

    private Object[] elements;

    private int startIndex;

    public CollectionEvent(Object source, int eventType, Object element, int startIndex) {
        super(source);
        Object[] elements = { element };
        init(eventType, elements, startIndex);
    }

    public CollectionEvent(Object source, int eventType, Object[] elements, int startIndex) {
        super(source);
        init(eventType, elements, startIndex);
    }

    public CollectionEvent(Object source, int eventType, Collection elements, int startIndex) {
        this(source, eventType, elements.toArray(), startIndex);
    }

    private void init(int eventType, Object[] elements, int startIndex) {
        type = eventType;
        this.elements = elements;
        this.startIndex = startIndex;
    }

    /** only useful if only one thing was added/removed/changed */
    public Object getElement() {
        return elements[0];
    }

    public Object[] getElements() {
        return elements;
    }

    public int getElementCount() {
        return elements.length;
    }

    public int getType() {
        return type;
    }

    public int getStartIndex() {
        return startIndex;
    }
}
