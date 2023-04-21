package ru.yandex.strictweb.scriptjava.base;

@Native
public abstract class Document extends Node {

    /**
	 * Returns a semicolon-separated list of the cookies for that document or sets a single cookie.
	 */
    public String cookie;

    /**
	 * Returns the BODY node of the current document.
	 */
    public Node body;

    /**
	 * Returns the currently focused element
	 */
    public Node activeElement;

    /**
	 * Returns an object reference to the identified element. 
	 */
    public Node getElementById(String id) {
        return null;
    }

    /**
	 * Returns a list of elements with the given name.
	 */
    public Node[] getElementsByName(String name) {
        return null;
    }

    /**
	 * Returns a list of elements with the given tag name. 
	 */
    public Node[] getElementsByTagName(String tagName) {
        return null;
    }

    /**
	 * Creates a new element with the given tag name. 
	 */
    public Node createElement(String tagName) {
        return null;
    }

    /**
	 * Creates a new Text node. 
	 */
    public Node createTextNode(Object text) {
        return null;
    }

    /**
	 * Returns Document Location
	 */
    public Location location;

    /**
	 * Returns the character set being used by the document.
	 */
    public String characterSet;

    /**
	 * Returns the domain of the current document.
	 */
    public String domain;

    /**
	 * Returns the node which is the target of the current tooltip.
	 */
    public Node tooltipNode;

    /**
	 * Returns a string containing the URL of the current document.
	 */
    public String URL;

    /**
	 * Returns the URI of the page that linked to this page.
	 */
    public String referrer;

    /** Writes HTML to the page */
    public void write(String html) {
    }
}
