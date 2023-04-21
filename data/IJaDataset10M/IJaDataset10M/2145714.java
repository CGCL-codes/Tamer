package com.velociti.ikarus.ui.widget;

import com.vaadin.data.Property;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.TextField;
import com.velociti.ikarus.widget.client.ui.VIkarusTextField;

/** ######IkarusTextField#####
 * -there are two additional properties that are used during  validating 
 *  the input characters at client side.
 * 
 * -IkarusTextField aims to communicate with server minimum.
 *  
 * -textType property can be PLAIN,ALPHANUMERIC,DIGIT,LETTER.
 *  If text type is set to PLAIN it will behave same as com.vaadin.ui.TextField.
 *  If text type is set to DIGIT, text field will only accept digits.
 *  etc...
 * 
 * -exceptionalChars property is being used for allowing additional 
 *  characters to be input other than text type
 * 
 *  For example an IkarusTextField to be used as an Email input
 *  set TextType to ALPHANUMERIC and set exceptionalChars to "@._#"
 *  
 * @author Alper Turkyilmaz - VelocitiSoftware - 2011
 * @version 1.0.1
 * 
 */
@ClientWidget(VIkarusTextField.class)
public class IkarusTextField extends TextField {

    public static final String TEXTTYPE_PLAIN = "PLAIN";

    public static final String TEXTTYPE_ALPHANUMERIC = "ALPHANUMERIC";

    public static final String TEXTTYPE_DIGIT = "DIGIT";

    public static final String TEXTTYPE_LETTER = "LETTER";

    private String textType = TEXTTYPE_PLAIN;

    private String exceptionalChars = "";

    public IkarusTextField() {
    }

    public IkarusTextField(Property dataSource) {
        super(dataSource);
    }

    public IkarusTextField(String caption, Property dataSource) {
        super(caption, dataSource);
    }

    public IkarusTextField(String caption, String value) {
        super(caption, value);
    }

    public IkarusTextField(String caption) {
        super(caption);
    }

    public String getTextType() {
        return textType;
    }

    public void setTextType(String textType) {
        if (textType.equals(TEXTTYPE_ALPHANUMERIC) || textType.equals(TEXTTYPE_DIGIT) || textType.equals(TEXTTYPE_LETTER) || textType.equals(TEXTTYPE_PLAIN)) {
            this.textType = textType;
        } else {
            this.textType = TEXTTYPE_PLAIN;
        }
    }

    public String getExceptionalChars() {
        return exceptionalChars;
    }

    public void setExceptionalChars(String exceptionalChars) {
        if (exceptionalChars == null) {
            this.exceptionalChars = "";
        } else {
            this.exceptionalChars = exceptionalChars;
        }
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        target.addAttribute("textType", getTextType());
        target.addAttribute("exceptionalChars", getExceptionalChars());
    }
}
