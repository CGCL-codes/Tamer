package org.aubit4gl.remote_client.connection.command;

import org.w3c.dom.Element;

/**
 * Options 4gl statement sended from the 4gl server program to the UI client
 * 
 * @author S�rgio Ferreira
 *
 */
public class Option {

    String type;

    String value;

    /**
	 * Default constructor.
	 */
    public Option() {
    }

    /**
	 * Initialize the UI command from the corresponding element on the
	 * DOM.
	 * 
	 * @param dom The Document Object Model that contains the information
	 */
    public void initFromDom(Element dom) {
        type = dom.getAttribute("TYPE");
        value = dom.getAttribute("VALUE");
    }

    /**
	 * TODO : Implement it. This should work with events
	 */
    public void execute() {
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
