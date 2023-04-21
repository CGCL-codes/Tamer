package de.ufinke.cubaja.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a DOM element.
 * @author Uwe Finke
 */
public class DOMElement extends DOMContent {

    private String name;

    private Map<String, String> attributes;

    private List<DOMContent> content;

    /**
   * Constructor.
   */
    public DOMElement() {
        super(DOMType.ELEMENT);
        attributes = new HashMap<String, String>();
        content = new ArrayList<DOMContent>();
    }

    /**
   * Constructor with the element's name.
   * @param name
   */
    public DOMElement(String name) {
        this();
        setName(name);
    }

    /**
   * Returns the element's name.
   * @return name
   */
    public String getName() {
        return name;
    }

    /**
   * Sets the element's name.
   * @param name
   */
    public void setName(String name) {
        this.name = name;
    }

    /**
   * Returns the element's attributes.
   * @return map with attribute names (keys) and values
   */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
   * Sets the element's attributes.
   * @param attributes
   */
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    /**
   * Returns the element's subnodes.
   * @return child elements and text
   */
    public List<DOMContent> getContent() {
        return content;
    }

    /**
   * Adds a content.
   * @param content
   */
    public void addContent(DOMContent content) {
        this.content.add(content);
    }

    /**
   * Returns an attribute value.
   * May be <tt>null</tt> when there is no attribute with the given name.
   * @param name
   * @return value
   */
    public String getAttribute(String name) {
        return attributes.get(name);
    }

    /**
   * Returns an attribute value or a default if attribute doesn't exist.
   * @param name
   * @param defaultValue
   * @return value
   */
    public String getAttribute(String name, String defaultValue) {
        String result = getAttribute(name);
        return (result == null) ? defaultValue : result;
    }

    /**
   * Sets an attribute.
   * @param name
   * @param value
   */
    public void setAttribute(String name, String value) {
        attributes.put(name, value);
    }
}
