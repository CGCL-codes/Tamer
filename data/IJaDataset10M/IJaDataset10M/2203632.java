package de.huxhorn.sulky.resources;

import java.util.Stack;

/**
 * DOCUMENT: <code>CyclicLinkException</code>
 */
public class CyclicLinkException extends Exception {

    private static final long serialVersionUID = 5197383554254357523L;

    private Stack<String> linkStack;

    private String cycleCause;

    public CyclicLinkException(Stack<String> linkStack, String cycleCause) {
        this.linkStack = linkStack;
        this.cycleCause = cycleCause;
    }

    public String getCycleCause() {
        return cycleCause;
    }

    public String getLinkStackString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < linkStack.size(); i++) {
            if (i != 0) {
                result.append(" => ");
            }
            result.append("\"");
            result.append(linkStack.elementAt(i));
            result.append("\"");
        }
        return result.toString();
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("CyclicLinkException: ");
        result.append("Link-Stack: {");
        result.append(getLinkStackString());
        result.append(" => \"");
        result.append(cycleCause);
        result.append("\"}");
        return result.toString();
    }
}
