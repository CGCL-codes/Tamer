package temmental;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Wrapper to ResourceBundle or Properties. It is used for internationalization. Instances are created on Template constructors calls.
 */
public class TemplateMessages {

    private List<Object> messages;

    private Locale locale;

    TemplateMessages(Locale locale, Object... resourcesContainers) throws FileNotFoundException, TemplateException, IOException {
        this.locale = locale;
        this.messages = new ArrayList<Object>();
        for (Object rs : resourcesContainers) {
            if (rs instanceof Properties || rs instanceof ResourceBundle) {
                messages.add(rs);
            } else if (rs instanceof String) {
                messages.add(readResource((String) rs, locale));
            } else {
                throw new TemplateException("Invalid object type. Accepts only Properties, ResourceBundle or String (file: or classpath:)");
            }
        }
    }

    TemplateMessages(String resourcePath, Locale locale) throws TemplateException, FileNotFoundException, IOException {
        this.locale = locale;
        this.messages = Arrays.asList(readResource(resourcePath, locale));
    }

    TemplateMessages(Properties properties, Locale locale) {
        this.messages = Arrays.asList((Object) properties);
        this.locale = locale;
    }

    TemplateMessages(ResourceBundle bundle) {
        this.messages = Arrays.asList((Object) bundle);
        this.locale = bundle.getLocale();
    }

    private void populateResourcesPossibilities(List<String> tab, String resourcePath, Locale locale) {
        String suffix = resourcePath.substring(resourcePath.lastIndexOf("."));
        String prefix = resourcePath.substring(0, resourcePath.lastIndexOf("."));
        String language = locale.getLanguage();
        String country = locale.getCountry();
        if (!language.equals("")) {
            if (!country.equals("")) tab.add(prefix + "_" + language + "_" + country + suffix);
            tab.add(prefix + "_" + language + suffix);
        }
    }

    /**
     * Tests if the specified key is known in messages. 
     * @param key possible key
     * @return <code>true</code> if and only if the specified object is a key in this messages; <code>false</code> otherwise. 
     */
    public boolean containsKey(String key) {
        for (int i = messages.size() - 1; i >= 0; i--) {
            Object messageContainer = messages.get(i);
            if (messageContainer instanceof Properties && ((Properties) messageContainer).containsKey(key)) return true;
            if (messageContainer instanceof ResourceBundle && containsKey((ResourceBundle) messageContainer, key)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsKey(ResourceBundle messageContainer, String key) {
        try {
            if (((ResourceBundle) messageContainer).getString(key) != null) return true;
        } catch (MissingResourceException e) {
        }
        return false;
    }

    /**
     * Searches the message with the specified key in messages. If the key is not found, returns <code>null</code>.
     * @param key the message key
     * @return the value in the messages with the specified key value.
     */
    public String getString(String key) {
        for (int i = messages.size() - 1; i >= 0; i--) {
            Object messageContainer = messages.get(i);
            if (messageContainer instanceof Properties && ((Properties) messageContainer).containsKey(key)) return ((Properties) messageContainer).getProperty(key);
            if (messageContainer instanceof ResourceBundle && containsKey((ResourceBundle) messageContainer, key)) return ((ResourceBundle) messageContainer).getString(key);
        }
        return null;
    }

    /**
     * Format the message with the specified key in messages with the specified parameters
     * @param key the message key
     * @param parameters the parameters
     * @return the formatted message
     * @see java.lang.String#format(String, Object...)
     */
    public String format(String key, List<? extends Object> parameters) {
        return (new MessageFormat(getString(key), locale)).format(parameters.toArray(), new StringBuffer(), null).toString();
    }

    private Object readResource(String resourcePath, Locale locale) throws TemplateException, FileNotFoundException, IOException {
        String protocol = resourcePath.substring(0, resourcePath.indexOf(":"));
        resourcePath = resourcePath.substring(resourcePath.indexOf(":") + 1);
        if (protocol.equals("classpath")) {
            try {
                return ResourceBundle.getBundle(resourcePath, locale);
            } catch (MissingResourceException e) {
                throw new TemplateException("Can't find bundle for %s, locale %s", resourcePath, locale.toString());
            }
        } else if (protocol.equals("file")) {
            List<String> tab = new ArrayList<String>();
            populateResourcesPossibilities(tab, resourcePath, locale);
            populateResourcesPossibilities(tab, resourcePath, Locale.getDefault());
            tab.add(resourcePath);
            String found = null;
            for (String path : tab) {
                File f = new File(path);
                if (f.exists()) {
                    found = path;
                    break;
                }
            }
            if (found == null) throw new TemplateException("Can't find properties file for %s, locale %s", resourcePath, locale.toString());
            return TemplateUtils.readProperties(found);
        } else {
            throw new TemplateException("Protocol '%s' not supported! (%s)", protocol, resourcePath);
        }
    }
}
