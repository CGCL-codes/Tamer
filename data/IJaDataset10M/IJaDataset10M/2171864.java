package fr.fg.client.openjwt.ui;

import fr.fg.client.openjwt.OpenJWT;

public class JSPasswordField extends JSTextField {

    public static final String UI_CLASS_ID = "PasswordField";

    static {
        setDefaultProperty(UI_CLASS_ID, OpenJWT.ELEMENT, "div");
        setDefaultProperty(UI_CLASS_ID, OpenJWT.INNER_HTML, "<input id=\"${text}\" type=\"password\" value=\"\"/>");
        setDefaultProperty(UI_CLASS_ID, OpenJWT.DEFAULT_WIDTH, "-1");
        setDefaultProperty(UI_CLASS_ID, OpenJWT.DEFAULT_HEIGHT, "-1");
        setDefaultProperty(UI_CLASS_ID, OpenJWT.LINE_HEIGHT, "false");
        setDefaultProperty(UI_CLASS_ID, OpenJWT.HORIZONTAL_MARGIN, "0");
        setDefaultProperty(UI_CLASS_ID, OpenJWT.VERTICAL_MARGIN, "0");
        setDefaultProperty(UI_CLASS_ID, OpenJWT.CSS_CLASS, "textfield password");
    }

    public JSPasswordField() {
        this("");
    }

    public JSPasswordField(String text) {
        super(text, UI_CLASS_ID);
    }
}
