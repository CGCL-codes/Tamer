package townhall;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 *
 * @author Bartosz Tuszyński
 */
public class Utils {

    public static void log(FacesContext facesContext, String message) {
        facesContext.getExternalContext().log(message);
    }

    public static void log(FacesContext facesContext, String message, Exception exception) {
        facesContext.getExternalContext().log(message, exception);
    }

    public static void log(ServletContext servletContext, String message) {
        servletContext.log(message);
    }

    public static void reportError(FacesContext facesContext, String message, String detail, Exception exception) {
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, detail));
        if (exception != null) {
            facesContext.getExternalContext().log(message, exception);
        }
    }

    public static void reportError(FacesContext facesContext, String messageId, Exception exception) {
        FacesMessage message = getMessage(messageId, null, FacesMessage.SEVERITY_ERROR);
        facesContext.addMessage(null, message);
        if (exception != null) {
            facesContext.getExternalContext().log(message.getSummary(), exception);
        }
    }

    protected static ClassLoader getCurrentClassLoader(Object defaultObject) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = defaultObject.getClass().getClassLoader();
        }
        return loader;
    }

    public static FacesMessage getMessage(String messageId, Object params[], FacesMessage.Severity severity) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String bundleName = facesContext.getApplication().getMessageBundle();
        if (bundleName != null) {
            String summary = null;
            String detail = null;
            Locale locale = facesContext.getViewRoot().getLocale();
            ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, getCurrentClassLoader(params));
            try {
                summary = bundle.getString(messageId);
                detail = bundle.getString(messageId + ".detail");
            } catch (MissingResourceException e) {
            }
            if (summary != null) {
                MessageFormat mf = null;
                if (params != null) {
                    mf = new MessageFormat(summary, locale);
                    summary = mf.format(params, new StringBuffer(), null).toString();
                }
                if (detail != null && params != null) {
                    mf.applyPattern(detail);
                    detail = mf.format(params, new StringBuffer(), null).toString();
                }
                return (new FacesMessage(severity, summary, detail));
            }
        }
        return new FacesMessage(severity, "!! key " + messageId + " not found !!", null);
    }
}
