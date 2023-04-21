package se.waltersson.wowarmory.page;

import org.w3c.dom.Node;

/**
 * @author Joakim Waltersson
 * @version $Revision: 13 $
 */
public class StringNode implements ParsedNode {

    private String value;

    public StringNode(String string) {
        this.value = string;
    }

    public String toString() {
        return value;
    }
}
