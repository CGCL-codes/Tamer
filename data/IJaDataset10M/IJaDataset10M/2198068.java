package com.liferay.portal.kernel.xml;

/**
 * <a href="Attribute.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public interface Attribute extends Node {

    public Object getData();

    public Namespace getNamespace();

    public String getNamespacePrefix();

    public String getNamespaceURI();

    public QName getQName();

    public String getQualifiedName();

    public String getValue();

    public void setData(Object data);

    public void setNamespace(Namespace namespace);

    public void setValue(String value);
}
