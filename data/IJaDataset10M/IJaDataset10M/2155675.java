package org.exteca.language;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Markups represents a collection of Markup objects found in a document.
 * 
 * @see Markup
 * @author Llewelyn Fernandes
 * @author Mauro Talevi
 */
public class Markups {

    /** The underlying collection */
    private Map map;

    /**
	 * Creates a Markups collection
	 */
    public Markups() {
        map = new HashMap();
    }

    /**
	 * Creates a Markups collection from another Markups collection
	 * 
	 * @param markups the initial Markups
	 */
    public Markups(Markups markups) {
        this();
        add(markups);
    }

    /**
	 * Adds Markups
	 * 
	 * @param markups the Markups to add
	 */
    public void add(Markup markup) {
        if (markup == null) {
            return;
        }
        map.put(markup.getName(), markup);
    }

    /**
	 * Adds Markups
	 * 
	 * @param markups the Markups to add
	 */
    public void add(Markups markups) {
        for (Iterator i = markups.values().iterator(); i.hasNext(); ) {
            Markup markup = (Markup) i.next();
            add(markup);
        }
    }

    /**
	 * Returns Markup of a given type.  
	 * If not found, a new one is created and added to the collection
	 * 
	 * @param name the type of the Markup
	 */
    public Markup getMarkup(String name) {
        Markup markup = (Markup) map.get(name);
        if (markup == null) {
            markup = new Markup(name);
            add(markup);
        }
        return markup;
    }

    /**
	 * Clears the token spans of all the markups
	 */
    public void clearSpans() {
        for (Iterator i = map.values().iterator(); i.hasNext(); ) {
            Markup markup = (Markup) i.next();
            markup.getSpans().clear();
        }
    }

    /**
	 * Returns the Markups contained in the collection
	 * 
	 * @return The Collection of Markups
	 */
    public Collection values() {
        return map.values();
    }

    /**
	 * Formatted representation
	 * 
	 * @return The formatted representation
	 */
    public String format() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (Iterator i = values().iterator(); i.hasNext(); ) {
            Markup markup = (Markup) i.next();
            sb.append(markup);
            if (i.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
	 * String representation
	 * 
	 * @return The String representation
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (Iterator i = values().iterator(); i.hasNext(); ) {
            Markup markup = (Markup) i.next();
            sb.append(markup);
            if (i.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
