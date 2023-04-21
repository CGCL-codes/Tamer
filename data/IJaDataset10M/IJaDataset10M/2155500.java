package com.worldware.misc;

import java.util.*;

/** An Enumeration that allows you to push back one element
 */
public class PushbackEnum implements Enumeration {

    Enumeration m_baseEnum;

    private static final Object marker = new Object();

    private Object curToken = marker;

    /** @param baseEnum Must be an Enumeration of Strings
	 */
    public PushbackEnum(Enumeration baseEnum) {
        m_baseEnum = baseEnum;
    }

    public Object nextElement() {
        if (hasPushback()) {
            Object t = curToken;
            curToken = marker;
            return t;
        }
        return m_baseEnum.nextElement();
    }

    public boolean hasMoreElements() {
        if (hasPushback()) return true;
        return m_baseEnum.hasMoreElements();
    }

    public void pushBack(Object o) {
        if (hasPushback()) throw new RuntimeException("PushbackEnum: can't push more than one Object back");
        curToken = o;
    }

    public boolean hasPushback() {
        return (curToken != marker);
    }
}
