package com.sun.xacml.ctx;

import com.sun.xacml.ParsingException;
import com.sun.xacml.attr.AttributeDesignator;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Represents a context Resource.
 *
 * @since 2.0
 * @author Pascal S. de Kloe
 */
public class Resource {

    private final Node resourceContent;

    private final Set<Attribute> ATTRIBUTES;

    public Resource(Set<Attribute> attributes) {
        this(attributes, (Node) null);
    }

    /**
     * @param content the {@code ResourceContent} element or {@code null} if
     *                unspecified.
     */
    public Resource(Set<Attribute> attributes, Node content) {
        try {
            Set<Attribute> copy = new HashSet<Attribute>(attributes);
            copy.remove(null);
            ATTRIBUTES = Collections.unmodifiableSet(copy);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("attributes is null");
        }
        if (content == null) resourceContent = null; else {
            resourceContent = content.cloneNode(true);
            if (!Context.isElement(resourceContent, "ResourceContent")) throw new IllegalArgumentException("Not a ResourceContent element");
        }
    }

    Resource(Node node) throws ParsingException {
        assert Context.isElement(node, "Resource");
        assert Context.isSupportedNamespace(node.getNamespaceURI());
        node = node.getFirstChild();
        if (node != null && Context.isElement(node, "ResourceContent")) {
            resourceContent = node;
            node = node.getNextSibling();
        } else resourceContent = null;
        Set<Attribute> attributes = new HashSet<Attribute>();
        while (node != null) {
            attributes.add(Attribute.getInstance(node));
            node = node.getNextSibling();
        }
        ATTRIBUTES = Collections.unmodifiableSet(attributes);
    }

    /**
     * Gets a immutable set of all {@code Attribute} definitions.
     */
    public Set<Attribute> getAttributes() {
        return ATTRIBUTES;
    }

    /**
     * Gets a copy of the {@code ResourceContent} element or {@code null} if
     * unspecified.
     */
    public Node getResourceContent() {
        return resourceContent == null ? null : resourceContent.cloneNode(true);
    }

    Node toXML(Document context, String namespace) {
        Element resource = context.createElementNS(namespace, "Resource");
        if (resourceContent != null) resource.appendChild(resourceContent.cloneNode(true));
        for (Attribute x : ATTRIBUTES) resource.appendChild(x.toXML(context, namespace));
        return resource;
    }
}
