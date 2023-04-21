package org.japura.examples.checkcombobox;

import org.japura.gui.I18nStringKeys;
import org.japura.i18n.DefaultHandlerString;
import org.japura.i18n.I18nManager;

public class Example9 {

    public static void main(String[] args) {
        I18nManager.setHandlerString(new DefaultHandlerString() {

            @Override
            public String getString(String key) {
                if (key.equals(I18nStringKeys.SELECT_ALL.getKey())) {
                    return "[ SELECT ALL ]";
                } else if (key.equals(I18nStringKeys.DESELECT_ALL.getKey())) {
                    return "[ DESELECT ALL ]";
                } else if (key.equals(I18nStringKeys.SELECT_DESELECT_ALL.getKey())) {
                    return "[ SELECT / DESELECT ALL ]";
                }
                return super.getString(key);
            }
        });
    }
}
