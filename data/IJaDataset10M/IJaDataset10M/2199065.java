package javamicroweb.html.impl;

import javamicroweb.html.HTMLAttribute;
import javamicroweb.HTMLPageWriter;

public class SimpleHTMLAttribute<C> extends SimpleHTMLNode<C> implements HTMLAttribute<C> {

    private String name;

    private String value;

    public SimpleHTMLAttribute() {
    }

    public SimpleHTMLAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public void serialize(C context, HTMLPageWriter writer) throws Exception {
        writer.print(name);
        writer.print("=\"");
        writer.encodeText(value);
        writer.print("\"");
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
