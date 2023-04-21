package org.softnetwork.xml.dom.xpath;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author stephane.manciot@ebiznext.com
 *
 */
public interface XPath {

    /**
     * XPATH separator between nodes
     */
    String XPATH_SEPARATOR = "/";

    /**
     * XPATH definition of an attribute
     */
    String XPATH_ATTRIBUTE = "@";

    String XPATH_EQUALITY = "=";

    /**
     * XPATH separator representing the begin of a condition
     */
    String XPATH_COND_BEGIN = "[";

    /**
     * XPATH separator representing the begin of a negative condition definition
     */
    String XPATH_NOT_COND = "!";

    /**
     * XPATH separator representing the end of a condition
     */
    String XPATH_COND_END = "]";

    /**
     * XPATH current element
     */
    String XPATH_CURRENT = ".";

    /**
     * XPATH all child elements
     */
    String XPATH_ALL = "*";

    String XPATH_NAME_FUNCTION = "name()";

    String XPATH_TEXT_FUNCTION = "text()";

    String XPATH_POS_FUNCTION = "position()";

    String XPATH_LAST_POS_FUNCTION = "last()";

    String XPATH_COUNT_FUNCTION = "count()";

    Element[] EMPTY = new Element[0];

    Node[] EMPTY_NODES = new Node[0];
}
