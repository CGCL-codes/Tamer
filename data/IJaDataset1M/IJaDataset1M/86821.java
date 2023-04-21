package com.javector.adaptive.framework.interfaces;

import com.javector.soaj.SoajException;
import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * User: kishore
 * Date: Feb 12, 2006
 * Time: 9:38:09 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Mapping {

    protected QName typeOrName;

    protected String javaClassName;

    protected Mapping(QName typeOrName, String javaClassName) {
        this.typeOrName = typeOrName;
        this.javaClassName = javaClassName;
    }

    public abstract QName getXmlType() throws SoajException;

    public abstract QName getXmlName() throws SoajException;

    public abstract String getJavaClassName();
}
