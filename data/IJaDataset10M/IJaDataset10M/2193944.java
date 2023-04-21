package org.ji18n.core.content;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Makes {@link Content} available via {@link java.util.ResourceBundle#getObject(String)}.
 * This implementation is preferred over extending {@link java.util.ListResourceBundle} and
 * exposing {@link Content} objects via {@link java.util.ListResourceBundle#getContents()}
 * since the latter would cache (potentially a lot of binary) data statically in memory.
 * 
 * <pre>
 * public class MyResources extends ContentResourceBundle {
 *     protected String[][] getContents() {
 *         return new String[][] {
 *             new String[] {"img.cat", "META-INF/img/cat.jpg"},
 *             new String[] {"img.dog", "http://www.dogs.com/images/dog.gif"}
 *         };
 *     }
 * }
 * public class MyResources_es extends ContentResourceBundle {
 *     protected String[][] getContents() {
 *         return new String[][] {
 *             new String[] {"img.cat", "META-INF/img/es/gato.jpg"},
 *             new String[] {"img.dog", "file:///project/images/es/perro.gif"}
 *         };
 *     }
 * }
 * </pre>
 * 
 * @version $Id: ContentResourceBundle.java 159 2008-07-03 01:28:51Z david_ward2 $
 * @author david at ji18n.org
 */
public abstract class ContentResourceBundle extends ResourceBundle {

    private Map<String, String[]> lookup = null;

    @Override
    public final Enumeration<String> getKeys() {
        if (lookup == null) loadLookup();
        ResourceBundle parent = this.parent;
        return new ResourceBundleEnumeration(lookup.keySet(), (parent != null) ? parent.getKeys() : null);
    }

    @Override
    protected final Object handleGetObject(String key) {
        if (lookup == null) loadLookup();
        if (key == null) throw new NullPointerException();
        Content content = null;
        String[] c = lookup.get(key);
        if (c != null) {
            try {
                URL url = new URL(c[0]);
                content = new DataSourceContent(url, c[1]);
            } catch (MalformedURLException mue) {
                content = new DataSourceContent(c[0], c[1]);
            }
        }
        return content;
    }

    protected abstract String[][] getContents();

    private synchronized void loadLookup() {
        if (lookup != null) return;
        String[][] c = getContents();
        Map<String, String[]> tmp = new HashMap<String, String[]>();
        for (int i = 0; i < c.length; ++i) {
            String code = c[i][0];
            String resource = c[i][1];
            String encoding = (c.length > 2) ? c[i][2] : null;
            tmp.put(code, new String[] { resource, encoding });
        }
        lookup = tmp;
    }

    private static class ResourceBundleEnumeration implements Enumeration<String> {

        Set<String> set;

        Iterator<String> iterator;

        Enumeration<String> enumeration;

        ResourceBundleEnumeration(Set<String> set, Enumeration<String> enumeration) {
            this.set = set;
            this.iterator = set.iterator();
            this.enumeration = enumeration;
        }

        String next = null;

        public boolean hasMoreElements() {
            if (next == null) {
                if (iterator.hasNext()) {
                    next = iterator.next();
                } else if (enumeration != null) {
                    while (next == null && enumeration.hasMoreElements()) {
                        next = enumeration.nextElement();
                        if (set.contains(next)) {
                            next = null;
                        }
                    }
                }
            }
            return next != null;
        }

        public String nextElement() {
            if (hasMoreElements()) {
                String result = next;
                next = null;
                return result;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
