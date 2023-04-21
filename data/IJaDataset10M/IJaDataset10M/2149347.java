package net.sf.orexio.jdcp.common;

import java.util.ResourceBundle;

/**
 * Custom resource bundle handling message's parameters.
 * @author alois.cochard@gmail.com
 *
 */
public class JdcpResourceBundle {

    /**
	 * The Java resource bundle.
	 */
    private ResourceBundle bundle = null;

    /**
	 * Default constructor.
	 * @param bundleName the bundle name
	 */
    JdcpResourceBundle(final String bundleName) {
        bundle = ResourceBundle.getBundle(bundleName);
    }

    /**
	 * Return the message for the specified key.
	 * @param key the key
	 * @return the message
	 */
    public final String getString(final String key) {
        return bundle.getString(key);
    }

    /**
	 * Return the formatted message with parameters for the specified key.
	 * @param key the key
	 * @param parameters the parameters
	 * @return the formatted message
	 */
    public final String getString(final String key, final String... parameters) {
        String message = getString(key);
        int parameterId = 0;
        for (String parameter : parameters) {
            String parameterMessage = "null";
            if (parameter != null) {
                parameterMessage = parameter;
            }
            String find = "{" + parameterId + "}";
            while (true) {
                int i = message.indexOf(find);
                if (i < 0) {
                    break;
                }
                String before = message.substring(0, i);
                String after = message.substring(i + find.length());
                message = before + parameterMessage + after;
            }
            parameterId++;
        }
        return message;
    }
}
