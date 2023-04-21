package com.bluebrim.layout.impl.server.xml;

import org.apache.crimson.tree.*;
import com.bluebrim.xml.shared.*;

/**
 * Super class of all page item XML builders.
 * Has common behavior for specific types of attributes and elements
 * Creation date: (1999-09-17 15:37:57)
 * @author: Mikael Printz
 */
public abstract class CoPageItemXmlBuilder extends CoXmlBuilder {

    /**
 * CoPageItemXmlBuilder constructor comment.
 */
    public CoPageItemXmlBuilder() {
        super();
    }

    /**
 * getTag method comment.
 */
    protected String getTag() {
        return null;
    }

    /**
 * CoPageItemXmlBuilder constructor comment.
 * @param xmlDoc com.sun.xml.tree.XmlDocument
 */
    public CoPageItemXmlBuilder(XmlDocument xmlDoc) {
        super(xmlDoc);
    }

    /**
 * CoPageItemXmlBuilder constructor comment.
 * @param xmlDoc XmlDocument
 * @param builder com.bluebrim.xml.shared.CoXmlBuilderIF
 */
    public CoPageItemXmlBuilder(XmlDocument xmlDoc, com.bluebrim.xml.shared.CoXmlBuilderIF builder) {
        super(xmlDoc, builder);
    }
}
