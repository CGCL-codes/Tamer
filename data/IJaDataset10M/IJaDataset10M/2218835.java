package org.restfaces.util;

import java.beans.FeatureDescriptor;
import java.text.MessageFormat;
import java.util.Locale;
import javax.el.ELResolver;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import com.sun.faces.util.MessageFactory;

public class FacesUtil {

    /**
     * <p>If the FacesContext has a UIViewRoot, and this UIViewRoot has a Locale,
     * return it.  Otherwise return Locale.getDefault().
     */
    public static Locale getLocaleFromContextOrSystem(FacesContext context) {
        Locale result, temp = Locale.getDefault();
        UIViewRoot root = null;
        result = temp;
        if (null != context) {
            if (null != (root = context.getViewRoot())) {
                if (null == (result = root.getLocale())) {
                    result = temp;
                }
            }
        }
        return result;
    }

    public static FeatureDescriptor getFeatureDescriptor(String name, String displayName, String desc, boolean expert, boolean hidden, boolean preferred, Object type, Boolean designTime) {
        FeatureDescriptor fd = new FeatureDescriptor();
        fd.setName(name);
        fd.setDisplayName(displayName);
        fd.setShortDescription(desc);
        fd.setExpert(expert);
        fd.setHidden(hidden);
        fd.setPreferred(preferred);
        fd.setValue(ELResolver.TYPE, type);
        fd.setValue(ELResolver.RESOLVABLE_AT_DESIGN_TIME, designTime);
        return fd;
    }
}
