package de.d3web.we.flow.type;

import de.knowwe.kdom.xml.AbstractXMLType;

/**
 * 
 * @author hatko Created on: 08.10.2009
 */
public class TargetType extends AbstractXMLType {

    private static TargetType instance;

    private TargetType() {
        super("target");
    }

    public static TargetType getInstance() {
        if (instance == null) instance = new TargetType();
        return instance;
    }
}
