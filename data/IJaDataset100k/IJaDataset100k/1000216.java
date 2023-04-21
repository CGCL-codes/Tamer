package jprededitor.editors.java;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class JavaEditorMessages {

    private static final String RESOURCE_BUNDLE = "org.eclipse.ui.examples.javaeditor.JavaEditorMessages";

    private static ResourceBundle fgResourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE);

    private JavaEditorMessages() {
    }

    public static String getString(String key) {
        try {
            return fgResourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }
}
