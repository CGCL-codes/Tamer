package org.japano.util;

/**
 Simple Object-To-XML interface.
 
 @author Sven Helmberger ( sven dot helmberger at gmx dot de )
 @version $Id: XMLNode.java,v 1.2 2005/09/27 21:30:51 fforw Exp $
 #SFLOGO# 
 */
public interface XMLNode {

    /**
   Creates an XMLElement from this XMLNode and its children.
   @return XMLElement
   */
    public XMLElement toXML();
}
