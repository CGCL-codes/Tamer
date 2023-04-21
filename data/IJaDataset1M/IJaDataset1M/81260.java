package allensoft.util;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.net.URL;

/** Contains useful static methods for loading locale resources.
 @author Nicholas Allen. */
public class ResourceLoader {

    /** Map from package names to resource bundles. */
    private static Map g_Bundles = new HashMap();

    private String m_sClassName;

    private ResourceBundle m_Bundle;

    private ResourceLoader(Class c, ResourceBundle bundle) {
        m_sClassName = MiscUtilities.getShortClassName(c);
        m_Bundle = bundle;
    }

    /** Gets a ResourceLoader for the supplied class by loading a resource bundle
	 named "Resources" in the package of the supplied class. This could be useful
	 from a static method or initializer. The getString method of the resource loader
	 will look for a key classname.key. */
    public static synchronized ResourceLoader getLoader(Class c) {
        String sPackageName = c.getPackage().getName();
        ResourceBundle bundle = (ResourceBundle) g_Bundles.get(sPackageName);
        if (bundle == null) {
            StringBuffer buffer = new StringBuffer(sPackageName);
            for (int i = 0; i < buffer.length(); i++) {
                if (buffer.charAt(i) == '.') buffer.setCharAt(i, '/');
            }
            buffer.append("/Resources");
            bundle = ResourceBundle.getBundle(buffer.toString(), Locale.getDefault(), c.getClassLoader());
            g_Bundles.put(sPackageName, bundle);
        }
        return new ResourceLoader(c, bundle);
    }

    /** Gets a string for the given key. This looks for a key called classname.key
	 in the resource bundle for the class. */
    public String getString(String key) {
        return m_Bundle.getString(m_sClassName + "." + key);
    }

    /** Gets a resource string from a ResourceBundle for the supplied object.
	    This is done by looking for a string with the key classname.sKey, where
	    classname is the class name of object. If a string is found under this key
	    it is returned, otherwise the process is repeated for the super class. If
	    it gets to the suplied baseClass and it still hasn't found a string value it
	    throws a MissingResourceException. This allows for the easy overriding of resource
	 strings without overriding any methods in the base classes.
	 @throws MissingResourceException if resource is not found */
    public static String getString(Object object, Class baseClass, String sKey) {
        Class c = object.getClass();
        String s = null;
        MissingResourceException ex = null;
        if (!baseClass.isAssignableFrom(c)) throw new IllegalArgumentException("object must be of type baseClass or a sub class of it");
        while (c != null) {
            try {
                s = getLoader(c).getString(sKey);
            } catch (MissingResourceException e) {
                if (ex == null) ex = e;
                if (c == baseClass) throw ex;
            }
            if (s != null) break;
            c = c.getSuperclass();
        }
        return s;
    }

    public static Image getImage(Class c, String sImageName) {
        URL url = null;
        Locale locale = Locale.getDefault();
        String sBase = "images/" + MiscUtilities.getShortClassName(c) + "_" + sImageName;
        for (int i = 0; i < 4; i++) {
            String s = "";
            if (i == 0) s = locale.getLanguage() + "_" + locale.getCountry() + "_" + locale.getVariant(); else if (i == 1) s = locale.getLanguage() + "_" + locale.getCountry(); else if (i == 2) locale.getLanguage();
            url = c.getResource(sBase + ".gif");
            if (url == null) {
                url = c.getResource(sBase + ".jpg");
                if (url == null) url = c.getResource(sBase + ".png");
            }
        }
        if (url == null) return null;
        return Toolkit.getDefaultToolkit().getImage(url);
    }

    public static Image getImage(Object object, Class baseClass, String sIconName) {
        Class c = object.getClass();
        Image image = null;
        while (c != null) {
            image = getImage(c, sIconName);
            if (image != null) break;
            if (c == baseClass) break;
            c = c.getSuperclass();
        }
        return image;
    }

    public static Icon getIcon(Class c, String sImageName) {
        Image image = getImage(c, sImageName);
        if (image != null) return new ImageIcon(image);
        return null;
    }

    public static Icon getIcon(Object object, Class baseClass, String sIconName) {
        Image image = getImage(object, baseClass, sIconName);
        if (image != null) return new ImageIcon(image);
        return null;
    }
}
