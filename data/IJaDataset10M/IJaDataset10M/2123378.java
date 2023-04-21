package org.gvsig.xmlschema.som;

import java.util.Collection;
import javax.xml.namespace.QName;

/**
 * All the schema subelement must to implement this class.
 * It is used to know which is the schema for a schema 
 * component.
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public interface IXSComponent extends IXSNode {

    public QName getQName();

    public IXSSchema getSchema();

    public Collection getItems();
}
