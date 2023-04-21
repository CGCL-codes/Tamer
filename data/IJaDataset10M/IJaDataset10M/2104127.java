package de.mogwai.common.i18n;

public class I18NInitializer {

    public static void initialize(I18NAble aSomething) {
        ResourceHelper theHelper = aSomething.getResourceHelper();
        if (theHelper != null) {
            if (aSomething.getResourceBundleID() != null) {
                try {
                    aSomething.setText(theHelper.getText(aSomething.getResourceBundleID()));
                } catch (ResourceException e) {
                }
            }
        }
    }
}
