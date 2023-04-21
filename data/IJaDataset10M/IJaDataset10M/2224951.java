package org.apache.shindig.gadgets.templates.tags;

import org.apache.shindig.gadgets.parse.HtmlSerialization;
import org.apache.shindig.gadgets.templates.TemplateProcessor;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.io.IOException;

/**
 * Abstract implementation of TagHandler, provides convenience methods
 * for resolving values in context.
 */
public abstract class AbstractTagHandler implements TagHandler {

    private final String tagName;

    private final String namespaceUri;

    /**
   * Create the tag handler instance.
   * @param namespaceUri the namespace of element this tag parses.
   * @param tagName the local name of the element this tag parses.
   */
    public AbstractTagHandler(String namespaceUri, String tagName) {
        this.tagName = tagName;
        this.namespaceUri = namespaceUri;
    }

    public String getTagName() {
        return tagName;
    }

    public String getNamespaceUri() {
        return namespaceUri;
    }

    /**
   * Returns the value of a tag attribute, evaluating any contained EL
   * expressions if necessary.
   * 
   * @param <T> the type of the value
   * @param tag the element
   * @param name the attribute name
   * @param processor the template processor
   * @param type the type of the value
   * @return the value of the attribute, or null if the attribute is not
   *     present.
   */
    protected final <T> T getValueFromTag(Element tag, String name, TemplateProcessor processor, Class<T> type) {
        if (tag.hasAttribute(name)) {
            return processor.evaluate(tag.getAttribute(name), type, null);
        } else {
            return null;
        }
    }

    protected final DocumentFragment processChildren(Element tag, TemplateProcessor processor) {
        DocumentFragment fragment = tag.getOwnerDocument().createDocumentFragment();
        processor.processChildNodes(fragment, tag);
        return fragment;
    }

    /**
   * Create a text node with proper escaping.
   */
    protected final void appendTextNode(Node parent, String text) {
        if (text == null || "".equals(text)) {
            return;
        }
        try {
            StringBuilder sb = new StringBuilder(text.length());
            HtmlSerialization.printEscapedText(text, sb);
            parent.appendChild(parent.getOwnerDocument().createTextNode(sb.toString()));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
