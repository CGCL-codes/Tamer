package org.dbe.composer.wfengine.wsdl.def;

import javax.xml.namespace.QName;

/**
 * This interface represents a Partner Link Type element.  It contains
 * information about operations associated with this Partner Link Type.
 */
public interface IWsdlType {

    public QName getElementType();

    public void setElementType(QName value);

    public String getElName();

    public void setElName(String value);

    public String getId();

    public void setId(String value);
}
