package org.mxeclipse.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface IXMLPersistable {

    void toXML(Document doc, Node node);
}
