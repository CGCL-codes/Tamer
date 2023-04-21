package jworktime.js;

public class OutputObject extends JSObject {

    public OutputObject() {
        super();
    }

    public OutputObject(String name, String type) {
        super();
        this.name = name;
        this.type = type;
    }

    public static final OutputObject DEFAULT_OUTPUT = new OutputObject("Return", "String");
}
