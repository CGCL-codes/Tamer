package ru.itbrains.jicard.soap12;

public class Operation {

    private String soapAction;

    private String style;

    public Operation() {
    }

    public Operation(String soapAction, String style) {
        this.style = style;
        this.soapAction = soapAction;
    }

    public String getSoapAction() {
        return soapAction;
    }

    public void setSoapAction(String soapAction) {
        this.soapAction = soapAction;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String toString() {
        return "Operation{" + "soapAction='" + soapAction + '\'' + ", style='" + style + '\'' + '}';
    }
}
