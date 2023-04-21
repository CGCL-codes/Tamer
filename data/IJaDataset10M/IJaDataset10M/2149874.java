package org.databene.commons;

import java.util.Comparator;

/**
 * Compares to {@link Named} objects by their name.<br/><br/>
 * Created: 12.08.2010 09:26:04
 * @since 0.5.4
 * @author Volker Bergmann
 */
public class NameComparator implements Comparator<Named> {

    public int compare(Named named1, Named named2) {
        return named1.getName().compareTo(named2.getName());
    }
}
