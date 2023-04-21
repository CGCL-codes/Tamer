package org.paninij.runtime.buttonclickexample;

public class Form {

    Button submit;

    TextBox tb;

    public Form() {
        submit = new Button();
        tb = new TextBox();
    }

    void fill(int value) {
        tb.value = value;
    }

    void clickSubmit() {
        submit.click(tb.value);
    }
}
