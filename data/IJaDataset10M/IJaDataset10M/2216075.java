package net.sf.fallfair.view.utils;

import java.util.ResourceBundle;

/**
 *
 * @author nathanj
 */
public class ResourceBundleWrapper {

    public ResourceBundleWrapper(ResourceBundle rb) {
        this.setResourceBundle(rb);
    }

    public String getString(String key) {
        String result;
        try {
            result = getResourceBundle().getString(key);
        } catch (Exception e) {
            result = "?" + key + "?";
        }
        return result;
    }

    public ResourceBundle getResourceBundle() {
        return this.resourceBundle;
    }

    public void setResourceBundle(ResourceBundle rb) {
        this.resourceBundle = rb;
    }

    private ResourceBundle resourceBundle;
}
