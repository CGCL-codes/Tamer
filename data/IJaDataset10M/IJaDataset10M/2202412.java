package ch.odi.justblog.api;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a blog entry as featured by JustBlog. Blog APIs can extend this entry
 * by adding extensions (see Extension Object pattern).
 *
 * @author oglueck
 */
public class Entry {

    private String text;

    private Map extensions = new HashMap();

    /**
     * Gets the extension that implements a certain interface.
     * @param clazz meta object of an interface.
     * @return an object implementing <code>clazz</code> or <code>null</code>.
     */
    public Object getExtension(Class clazz) {
        if (!clazz.isInterface()) throw new IllegalArgumentException("clazz must be an interface");
        return extensions.get(clazz);
    }

    /**
     * Registers an extension under all the interfaces
     * the extension implements.
     * @param extension
     */
    public void addExtension(Object extension) {
        Class c = extension.getClass();
        Class[] ifs = c.getInterfaces();
        for (int i = 0; i < ifs.length; i++) {
            extensions.put(ifs[i], extension);
        }
    }

    /**
     * @return Returns the text.
     */
    public String getText() {
        return text;
    }

    /**
     * @param text The text to set.
     */
    public void setText(String text) {
        this.text = text;
    }
}
