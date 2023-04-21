package code2html;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

public class FilePropertyAccessor implements PropertyAccessor {

    private Properties props = new Properties();

    public FilePropertyAccessor() {
    }

    void loadProps(InputStream in) throws IOException {
        in = new BufferedInputStream(in);
        this.props.load(in);
        in.close();
    }

    /**
     * Fetches a property, returning null if it's not defined.
     * @param name The property
     */
    public final String getProperty(String name) {
        return this.props.getProperty(name);
    }

    /**
     * Fetches a property, returning the default value if it's not
     * defined.
     * @param name The property
     * @param def The default value
     */
    public final String getProperty(String name, String def) {
        return this.props.getProperty(name, def);
    }

    /**
     * Returns the property with the specified name, formatting it with
     * the <code>java.text.MessageFormat.format()</code> method.
     * @param name The property
     * @param args The positional parameters
     */
    public final String getProperty(String name, Object[] args) {
        if (name == null) {
            return null;
        }
        if (args == null) {
            return this.props.getProperty(name);
        } else {
            String value = this.props.getProperty(name);
            if (value == null) {
                return null;
            } else {
                return MessageFormat.format(value, args);
            }
        }
    }

    /**
     * Returns the value of a boolean property.
     * @param name The property
     */
    public final boolean getBooleanProperty(String name) {
        return this.getBooleanProperty(name, false);
    }

    /**
     * Returns the value of a boolean property.
     * @param name The property
     * @param def The default value
     */
    public final boolean getBooleanProperty(String name, boolean def) {
        String value = this.getProperty(name);
        if (value == null) {
            return def;
        } else if (value.equals("true") || value.equals("yes") || value.equals("on")) {
            return true;
        } else if (value.equals("false") || value.equals("no") || value.equals("off")) {
            return false;
        } else {
            return def;
        }
    }

    /**
     * Sets a property to a new value.
     * @param name The property
     * @param value The new value
     */
    public final void setProperty(String name, String value) {
        if (value == null || value.length() == 0) {
            props.remove(name);
        } else {
            props.put(name, value);
        }
    }

    /**
     * Sets a boolean property.
     * @param name The property
     * @param value The value
     */
    public final void setBooleanProperty(String name, boolean value) {
        this.setProperty(name, value ? "true" : "false");
    }
}
