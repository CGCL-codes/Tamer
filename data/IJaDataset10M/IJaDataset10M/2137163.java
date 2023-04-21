package org.openxdm.xcap.common.uri;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.openxdm.xcap.common.xml.NamespaceContext;

/**
 * A node selector selects an element, attribute or namespace bindings in a
 * element. It's defined by XCAP specs with the regular expression:
 * 
 * node-selector = element-selector ["/" terminal-selector]
 * 
 * @author Eduardo Martins
 */
public class NodeSelector implements Externalizable {

    private String elementSelector = null;

    private String elementParentSelector = null;

    private String elementSelectorWithEmptyPrefix = null;

    private String elementParentSelectorWithEmptyPrefix = null;

    private String terminalSelector = null;

    private NamespaceContext namespaceContext;

    public NodeSelector() {
    }

    /**
	 * Creates a new NodeSelector instance pointing to an element.
	 * 
	 * @param elementSelector
	 *            selects the element.
	 */
    public NodeSelector(String elementSelector, NamespaceContext namespaceContext) {
        this(elementSelector, null, namespaceContext);
    }

    /**
	 * Creates a new NodeSelector instance pointing to an attribute or namespace
	 * bindings, from the specified terminal selector, in a element, selected by
	 * an element selector.
	 * 
	 * @param elementSelector
	 *            selects the element.
	 * @param terminalSelector
	 *            an attribute or namespace selector.
	 */
    public NodeSelector(String elementSelector, String terminalSelector, NamespaceContext namespaceContext) {
        this.elementSelector = elementSelector;
        this.terminalSelector = terminalSelector;
        this.namespaceContext = namespaceContext;
    }

    /**
	 * Retreives the element selector of the node selector.
	 * 
	 * @return
	 */
    public String getElementSelector() {
        return elementSelector;
    }

    /**
	 * Retrieves the terminal selector of the node selector.
	 */
    public String getTerminalSelector() {
        return terminalSelector;
    }

    /**
	 * 
	 * @return
	 */
    public NamespaceContext getNamespaceContext() {
        if (namespaceContext == null) {
            namespaceContext = new NamespaceContext();
        }
        return namespaceContext;
    }

    /**
	 * Checks if the element selector has steps with unbinded namespaces
	 * prefixes, considering the provided map of namespace bindings.
	 * 
	 * @param namespaceBindings
	 *            the namespace bindings map.
	 * @return
	 */
    public boolean elementSelectorHasUnbindedPrefixes() {
        String[] elementSelectorParts = elementSelector.split("/");
        for (int i = 0; i < elementSelectorParts.length; i++) {
            int index = elementSelectorParts[i].indexOf(':');
            if (index >= 0) {
                String prefix = elementSelectorParts[i].substring(0, index);
                if (namespaceContext == null || !namespaceContext.getNamespaces().containsKey(prefix)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * Retrieves the element selector with empty prefixes instead of no prefix
	 * at all. This is required for XPtah queries.
	 * 
	 * @return
	 */
    public String getElementSelectorWithEmptyPrefix() {
        if (elementSelectorWithEmptyPrefix == null) {
            elementSelectorWithEmptyPrefix = getElementSelectorWithEmptyPrefix(elementSelector);
        }
        return elementSelectorWithEmptyPrefix;
    }

    public static String getElementSelectorWithEmptyPrefix(String elementSelector) {
        StringBuilder sb = new StringBuilder();
        String[] elementSelectorParts = elementSelector.split("/");
        for (int i = 0; i < elementSelectorParts.length; i++) {
            if (elementSelectorParts[i].isEmpty()) {
                continue;
            }
            if (elementSelectorParts[i].charAt(0) == '*') {
                sb.append('/').append(elementSelectorParts[i]);
            } else if (elementSelectorParts[i].indexOf(':') > -1) {
                int pos = elementSelectorParts[i].indexOf('[');
                if (pos > 0 && elementSelectorParts[i].indexOf(':') > pos) {
                    sb.append("/:").append(elementSelectorParts[i]);
                } else {
                    sb.append('/').append(elementSelectorParts[i]);
                }
            } else {
                sb.append("/:").append(elementSelectorParts[i]);
            }
        }
        return sb.toString();
    }

    /**
	 * Retreives the element parent selector, that is, the parent of the element
	 * selected by the element selector.
	 * 
	 * @return
	 */
    public String getElementParentSelector() {
        if (elementParentSelector == null) {
            int elementParentSelectorSeparator = getElementSelectorWithEmptyPrefix().lastIndexOf('/');
            if (elementParentSelectorSeparator > 0) {
                elementParentSelector = elementSelectorWithEmptyPrefix.substring(0, elementParentSelectorSeparator);
            } else {
                elementParentSelector = "/";
            }
        }
        return elementParentSelector;
    }

    /**
	 * Retrieves the element parent selector with empty prefixes instead of no
	 * prefix at all. This is required for XPtah queries.
	 * 
	 * @return
	 */
    public String getElementParentSelectorWithEmptyPrefix() {
        if (elementParentSelectorWithEmptyPrefix == null) {
            String elementSelectorWithEmptyPrefix = getElementSelectorWithEmptyPrefix();
            int elementParentSelectorSeparator = elementSelectorWithEmptyPrefix.lastIndexOf('/');
            if (elementParentSelectorSeparator > 0) {
                elementParentSelectorWithEmptyPrefix = elementSelectorWithEmptyPrefix.substring(0, elementParentSelectorSeparator);
            } else {
                elementParentSelectorWithEmptyPrefix = "/";
            }
        }
        return elementParentSelectorWithEmptyPrefix;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(elementSelector);
        if (terminalSelector != null) {
            out.writeBoolean(true);
            out.writeUTF(terminalSelector);
        } else {
            out.writeBoolean(false);
        }
        out.writeObject(namespaceContext);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        elementSelector = in.readUTF();
        if (in.readBoolean()) {
            terminalSelector = in.readUTF();
        }
        namespaceContext = (NamespaceContext) in.readObject();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((elementSelector == null) ? 0 : elementSelector.hashCode());
        result = prime * result + ((terminalSelector == null) ? 0 : terminalSelector.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        NodeSelector other = (NodeSelector) obj;
        if (elementSelector == null) {
            if (other.elementSelector != null) return false;
        } else if (!elementSelector.equals(other.elementSelector)) return false;
        if (terminalSelector == null) {
            if (other.terminalSelector != null) return false;
        } else if (!terminalSelector.equals(other.terminalSelector)) return false;
        return true;
    }

    private transient String toString = null;

    private transient String toStringWithEntryPrefix = null;

    @Override
    public String toString() {
        if (toString == null) {
            StringBuilder sb = new StringBuilder(elementSelector);
            if (terminalSelector != null) {
                sb.append('/').append(terminalSelector);
            }
            toString = sb.toString();
        }
        return toString;
    }

    public String toStringWithEmptyPrefix() {
        if (toStringWithEntryPrefix == null) {
            StringBuilder sb = new StringBuilder(getElementSelectorWithEmptyPrefix());
            if (terminalSelector != null) {
                sb.append('/').append(terminalSelector);
            }
            toStringWithEntryPrefix = sb.toString();
        }
        return toStringWithEntryPrefix;
    }
}
