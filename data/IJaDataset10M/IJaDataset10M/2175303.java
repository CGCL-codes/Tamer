package org.icefaces.application.showcase.util;

import javax.faces.context.FacesContext;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <p>Utility class which loads the applications message resource bundle.  This
 * class can be used to internationalize string values that are located
 * in Beans.</p>
 * <p>This bean can be access statically or setup as a Application scoped bean
 * in the faces_config.xml </p>
 * <p>This Bean should be scoped in JSF as an Application Bean.  And as a result
 * can be passed into other beans via chaining or by loading via the
 * FacesContext.  Loading an Application scoped bean "messageLoader" via the
 * FacesContext would look like the following:</p>
 * <p>JSF 1.1:</p>
 * <pre>
 * Application application =
 * FacesContext.getCurrentInstance().getApplication();
 * MessageBundleLoader messageLoader =
 * ((MessageBundleLoader) application.createValueBinding("#{messageLoader}").
 * getValue(FacesContext.getCurrentInstance()));
 * </pre>
 * <p>JSF 1.2:</p>
 * <pre>
 * FacesContext fc = FacesContext.getCurrentInstance();
 * ELContext elc = fc.getELContext();
 * ExpressionFactory ef = fc.getApplication().getExpressionFactory();
 * ValueExpression ve = ef.createValueExpression(elc, expr, Object.class);
 * </pre>
 *
 * @since 1.7
 */
public class MessageBundleLoader {

    public static final String MESSAGE_PATH = "org.icefaces.application.showcase.view.resources.messages";

    private static ResourceBundle messages;

    /**
     * Initialize internationalization.
     */
    private static void init() {
        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        if (locale == null) {
            locale = Locale.ENGLISH;
        }
        messages = ResourceBundle.getBundle(MESSAGE_PATH, locale);
    }

    /**
     * Gets a string for the given key from this resource bundle or one of its
     * parents.
     *
     * @param key the key for the desired string
     * @return the string for the given key.  If the key string value is not
     *         found the key itself is returned.
     */
    public static String getMessage(String key) {
        try {
            if (messages == null) {
                init();
            }
            return messages.getString(key);
        } catch (Exception e) {
            return key;
        }
    }
}
