package net.sf.gwtruts.utils.collections;

/**
 *
 * @author Reza Alavi
 */
public class Property {

    private String name, ref, value, text;

    public Property(org.w3c.dom.Element element) {
        name = element.getAttribute("name");
        ref = element.getAttribute("ref");
        value = element.getAttribute("value");
        text = element.getAttribute("text");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
