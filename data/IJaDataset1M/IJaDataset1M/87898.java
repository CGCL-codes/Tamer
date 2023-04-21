package fr.gedeon.telnetservice.syntaxtree.ast.impl;

public class TypeNotSupportedException extends Exception {

    private static final long serialVersionUID = -2234159570557901605L;

    private String type;

    private int index = -1;

    public TypeNotSupportedException(Class type) {
        super();
        this.type = type.getName();
    }

    public void setParameterIndex(int index) {
        this.index = index;
    }

    public String getMessage() {
        if (this.index >= 0) {
            return "Type " + this.type + " for parameter " + this.index + " not supported";
        } else {
            return "Type " + this.type + " not supported";
        }
    }
}
