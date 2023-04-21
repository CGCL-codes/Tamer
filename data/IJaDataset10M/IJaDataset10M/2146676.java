package org.snipsnap.util.collection;

/**
 * Filters objects from a List.
 * Original version by <a href="mailto:mgrosze@web.de">Michael Gro&szlig;e</a>
 *
 * @author    stephan
 * @version   $Id: Filterator.java 648 2003-01-09 09:49:12Z stephan $
 */
public interface Filterator {

    /**
   * Determines, whether the object belongs to the sort of filtered objects or
   * not
   *
   * @param obj  object to filter
   * @return     true, if the object should be filtered.
   */
    public boolean filter(Object obj);
}
