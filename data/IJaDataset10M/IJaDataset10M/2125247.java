package org.jomper.container.config.parser;

import org.jomper.container.config.ContainerConstants;
import org.jomper.xml.XMLConstants;
import org.jomper.xml.XMLParser;

/**
 * @author Jomper.Chow
 * @project 
 * @version 2007-5-25
 * @description 
 */
public class ComponentParserFactory {

    public static XMLParser getInstance(String type) {
        if (XMLConstants.SAX_TYPE.equals(type)) {
            return new ComponentSAXParser(ContainerConstants.COMPONENT_ELEMENT, ContainerConstants.CONSTRUCTOR_ELEMENT);
        } else if (XMLConstants.DOM4J_TYPE.equals(type)) {
            return new ComponentDOM4JParser();
        } else if (XMLConstants.JDOM_TYPE.equals(type)) {
            return new ComponentJDOMParser();
        } else {
            return new ComponentSAXParser(ContainerConstants.COMPONENT_ELEMENT, ContainerConstants.CONSTRUCTOR_ELEMENT);
        }
    }
}
