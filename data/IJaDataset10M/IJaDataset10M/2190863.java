package it.mobistego.form;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author Pasquale
 */
public class PhoneNumberForm extends Form {

    private TextField text;

    public PhoneNumberForm() {
        super("Phone");
        text = new TextField("Num", "", 10, TextField.NUMERIC);
        append(text);
    }

    /**
     * @return the text
     */
    public TextField getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(TextField text) {
        this.text = text;
    }
}
