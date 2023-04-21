package net.jwpa.model.exposed;

import net.jwpa.config.Permission;
import net.jwpa.model.CustomizableProperty;

public class SecureCustomizableProperty extends SecureObject {

    private CustomizableProperty prop;

    public SecureCustomizableProperty(CustomizableProperty p) {
        prop = p;
    }

    public String getDefaultValue() {
        check(Permission.PICS_ORGANIZE);
        return prop.getDefaultValue();
    }

    public String getName() {
        check(Permission.PICS_VIEW_REGULAR);
        return prop.getName();
    }

    public String getValue() {
        check(Permission.PICS_VIEW_REGULAR);
        return prop.getValue();
    }

    public boolean isLocalizable() {
        check(Permission.PICS_VIEW_REGULAR);
        return prop.isLocalizable();
    }

    public static SecureCustomizableProperty[] getCP(CustomizableProperty[] p) {
        SecureCustomizableProperty[] res = new SecureCustomizableProperty[p.length];
        for (int i = 0; i < p.length; i++) {
            res[i] = new SecureCustomizableProperty(p[i]);
        }
        return res;
    }
}
