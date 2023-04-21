package com.prime.yui4jsf.examples;

public class CommandButtonBean {

    String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String executeCommand() {
        text = "Forca Barca From CommandButton";
        return null;
    }
}
