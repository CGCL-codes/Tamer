package com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSListSimpleType;
import com.sun.xml.xsom.XSRestrictionSimpleType;
import com.sun.xml.xsom.XSUnionSimpleType;

/**
 * Function object that works on {@link com.sun.xml.xsom.XSSimpleType}
 * and its derived interfaces.
 * 
 * @author
 *     Kohsuke Kawaguchi (kohsuke,kawaguchi@sun.com)
 */
public interface XSSimpleTypeFunction<T> {

    T listSimpleType(XSListSimpleType type);

    T unionSimpleType(XSUnionSimpleType type);

    T restrictionSimpleType(XSRestrictionSimpleType type);
}
