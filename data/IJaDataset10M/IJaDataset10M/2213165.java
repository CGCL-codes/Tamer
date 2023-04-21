package org.ximtec.igesture.geco.xml;

import org.jdom.Element;

/**
 * XML support for the <minimize> element.
 * 
 * @version 0.9, Jan 15, 2008
 * @author Michele Croci, mcroci@gmail.com
 */
public class JdomMinimizeElement extends Element {

    private static final String COMMAND = "minimize";

    public JdomMinimizeElement(boolean minimize) {
        this(COMMAND, minimize);
    }

    public JdomMinimizeElement(String name, boolean minimize) {
        super(name);
        this.addContent(String.valueOf(minimize));
    }
}
