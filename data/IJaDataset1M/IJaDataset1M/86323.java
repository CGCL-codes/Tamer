package thing;

import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import thinlet.Thinlet;
import utils.AWTUtils;
import thing.spi.Settings;

/**
 * An instance of this class maintains the properties of the components
 * contained in a given Thinlet.
 *
 * @author Dirk Moebius
 */
public class PropertyManager {

    private static final Logger log = Logger.getLogger("thing");

    private static final boolean debug() {
        return log.isLoggable(Level.FINE);
    }

    private Thinlet thinlet;

    private Settings settings;

    private PropertyMap map = new PropertyMap();

    public PropertyManager(Thinlet thinlet, Settings settings) {
        this.thinlet = thinlet;
        this.settings = settings;
    }

    public Object getValue(Object component, ThinletDTD.Property property) {
        String key = property.getName();
        switch(property.getType()) {
            case ThinletDTD.Property.STRING:
                return thinlet.getString(component, key);
            case ThinletDTD.Property.BOOLEAN:
                if (property == ThinletDTD.getProperty("dialog", "modal")) {
                    Object value = map.getValue(component, property);
                    return value != null ? value : Boolean.FALSE;
                } else if (property == ThinletDTD.getProperty("dialog", "closable")) return property.getDefaultValue(); else return new Boolean(thinlet.getBoolean(component, key));
            case ThinletDTD.Property.INTEGER:
                return new Integer(thinlet.getInteger(component, key));
            case ThinletDTD.Property.CHOICE:
                return thinlet.getChoice(component, key);
            case ThinletDTD.Property.COLOR:
                return ThinletWorkarounds.getColor(component, key);
            case ThinletDTD.Property.ICON:
                return thinlet.getIcon(component, key);
            case ThinletDTD.Property.FONT:
                Font font = ThinletWorkarounds.getFont(component, key);
                return font != null ? AWTUtils.getFontString(font, thinlet.getFont()) : "";
            case ThinletDTD.Property.KEYSTROKE:
                AWTKeyStroke keystroke = ThinletWorkarounds.getKeystroke(component, key);
                return keystroke != null ? AWTUtils.getAWTKeyStrokeDescription(keystroke) : null;
            case ThinletDTD.Property.PROPERTY:
                StringBuffer buf = new StringBuffer();
                Enumeration enumer = ThinletWorkarounds.getPropertyKeys(component);
                while (enumer.hasMoreElements()) {
                    String propkey = (String) enumer.nextElement();
                    String value = thinlet.getProperty(component, propkey).toString();
                    buf.append(propkey).append('=').append(value);
                    if (enumer.hasMoreElements()) buf.append(';');
                }
                return buf.toString();
            case ThinletDTD.Property.METHOD:
            case ThinletDTD.Property.COMPONENT:
                return map.getValueString(component, property);
            case ThinletDTD.Property.BEAN:
                return "";
            default:
                throw new IllegalArgumentException("unknown property type: " + property.getType());
        }
    }

    public Object getValue(Object component, String propName) {
        return getValue(component, getProperty(component, propName));
    }

    public String getValueString(Object component, ThinletDTD.Property property) {
        if (property.getType() == ThinletDTD.Property.ICON) return map.getValueString(component, property); else {
            Object obj = getValue(component, property);
            return value2string(obj);
        }
    }

    public String getValueString(Object component, String propName) {
        return getValueString(component, getProperty(component, propName));
    }

    public void setValue(Object component, String propName, String value) {
        setValue(component, getProperty(component, propName), value);
    }

    /**
     * Set a property value for the specified component.
     *
     * <p>
     * <b>Note:</b> The implementation of this method is similar to the
     * private method <code>Thinlet.addAttribute()</code>.
     *
     * @throws IllegalArgumentException  if the string value cannot be cast into
     *   a property value of appropiate type.
     */
    public void setValue(Object component, ThinletDTD.Property property, String value) {
        String key = property.getName();
        switch(property.getType()) {
            case ThinletDTD.Property.STRING:
                thinlet.setString(component, key, value);
                break;
            case ThinletDTD.Property.BOOLEAN:
                boolean b = false;
                if ("true".equals(value)) b = true; else if ("false".equals(value)) b = false; else throw new IllegalArgumentException("value not allowed for boolean property " + key + ": " + value);
                if (property == ThinletDTD.getProperty("dialog", "modal")) {
                    map.put(component, property, new Boolean(b), value);
                    b = false;
                }
                if (property != ThinletDTD.getProperty("dialog", "closable")) thinlet.setBoolean(component, key, b);
                break;
            case ThinletDTD.Property.INTEGER:
                thinlet.setInteger(component, key, Integer.parseInt(value));
                break;
            case ThinletDTD.Property.CHOICE:
                thinlet.setChoice(component, key, value);
                break;
            case ThinletDTD.Property.COLOR:
                thinlet.setColor(component, key, ThinletWorkarounds.string2color(value));
                break;
            case ThinletDTD.Property.ICON:
                Image icon = loadIcon(value);
                thinlet.setIcon(component, key, icon);
                map.put(component, property, icon, value);
                break;
            case ThinletDTD.Property.FONT:
                thinlet.setFont(component, key, ThinletWorkarounds.string2font(thinlet, value));
                break;
            case ThinletDTD.Property.KEYSTROKE:
                thinlet.setKeystroke(component, key, value);
                break;
            case ThinletDTD.Property.PROPERTY:
                StringTokenizer st = new StringTokenizer(value, ";");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    int equals = token.indexOf('=');
                    if (equals == -1) throw new IllegalArgumentException("property " + token + ": no '=' char");
                    thinlet.putProperty(component, token.substring(0, equals), token.substring(equals + 1));
                }
                break;
            case ThinletDTD.Property.METHOD:
            case ThinletDTD.Property.COMPONENT:
                map.put(component, property, value, value);
                break;
            case ThinletDTD.Property.BEAN:
                break;
            default:
                throw new IllegalArgumentException("unknown property type: " + property.getType());
        }
    }

    private Image loadIcon(String path) {
        Image image = null;
        try {
            image = Toolkit.getDefaultToolkit().getImage(new URL(path));
        } catch (MalformedURLException e) {
            String iconDir = settings.getIconDir();
            File file = new File(iconDir, path);
            if (file.exists()) {
                try {
                    FileInputStream is = new FileInputStream(file);
                    byte[] data = new byte[is.available()];
                    is.read(data, 0, data.length);
                    image = thinlet.getToolkit().createImage(data);
                    is.close();
                } catch (FileNotFoundException ignore) {
                    if (debug()) log.fine("icon file not found: " + file);
                } catch (IOException ioex) {
                    log.log(Level.SEVERE, "IOException during load of " + file, ioex);
                }
            } else if (debug()) log.fine("icon file not found: " + file);
        }
        if (image != null) {
            MediaTracker mediatracker = new MediaTracker(thinlet);
            mediatracker.addImage(image, 1);
            try {
                mediatracker.waitForID(1, 5000);
            } catch (InterruptedException ie) {
                log.warning("loading of image " + path + " has been interrupted!");
            }
        }
        return image;
    }

    public void removeComponent(Object component) {
        map.removeAll(component);
    }

    public static String getDefaultValueString(ThinletDTD.Property property) {
        Object obj = property.getDefaultValue();
        return value2string(obj);
    }

    public static ThinletDTD.Property getProperty(Object component, String propName) {
        String classname = Thinlet.getClass(component);
        ThinletDTD.Property property = ThinletDTD.getProperty(classname, propName);
        return property;
    }

    private static String value2string(Object obj) {
        if (obj == null) return ""; else if (obj instanceof String) return (String) obj; else if (obj instanceof Boolean) return ((Boolean) obj).toString(); else if (obj instanceof Integer) return ((Integer) obj).toString(); else if (obj instanceof Color) return AWTUtils.getColorString((Color) obj); else if (obj instanceof Image) return ""; else if (obj instanceof Font) return AWTUtils.getFontString((Font) obj); else throw new IllegalArgumentException("unexpected property type class: " + obj.getClass().getName());
    }

    private static class PropertyMap {

        private static class Key {

            Object component;

            ThinletDTD.Property property;

            public Key(Object component, ThinletDTD.Property property) {
                this.component = component;
                this.property = property;
            }

            public boolean equals(Object other) {
                Key otherkey = (Key) other;
                return (this.component == otherkey.component) && (this.property == otherkey.property);
            }

            public int hashCode() {
                return component.hashCode() + 3 * property.hashCode() + 7;
            }
        }

        private static class Value {

            Object value;

            String valueString;

            public Value(Object value, String valueString) {
                this.value = value;
                this.valueString = valueString;
            }
        }

        private HashMap map = new HashMap();

        public PropertyMap() {
        }

        public void put(Object component, ThinletDTD.Property property, Object value, String valueString) {
            map.put(new Key(component, property), new Value(value, valueString));
        }

        public Object getValue(Object component, ThinletDTD.Property property) {
            Value value = (Value) map.get(new Key(component, property));
            return value != null ? value.value : null;
        }

        public String getValueString(Object component, ThinletDTD.Property property) {
            Value value = (Value) map.get(new Key(component, property));
            return value != null ? value.valueString : null;
        }

        public void remove(Object component, ThinletDTD.Property property) {
            map.remove(new Key(component, property));
        }

        public void removeAll(Object component) {
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                Key key = (Key) it.next();
                if (key.component == component) it.remove();
            }
        }
    }
}
