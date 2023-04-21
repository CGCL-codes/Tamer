package si.mk.k3.gui;

import javax.swing.JComponent;

public class StringEditor extends ScalarEditor {

    public final int DEFAULT_STR_CHARS = 32;

    public StringEditor(String text, String paramName) {
        super(text, paramName);
    }

    public JComponent createVisible() {
        return super.createVisible(DEFAULT_STR_CHARS);
    }
}
