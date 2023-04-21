package com.xavax.jsf.vms;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Concept is the fundamental unit of information in the vocabulary service.
 * Examples would be the label for a field or button in a user interface and
 * a message displayed to the user.
 */
public interface Concept extends VmsObject, Comparable<Concept> {

    /**
   * Returns a list of comments for this concept.
   *
   * @return a list of comments for this concept.
   */
    public List<Comment> comments();

    /**
   * Returns the date this concept was created.
   *
   * @return the date this concept was created.
   */
    public Date created();

    /**
   * Returns the description of this concept.
   *
   * @return the description of this concept.
   */
    public String description();

    /**
   * Returns a map of display text objects indexed by locale.
   *
   * @return a map of display text objects indexed by locale.
   */
    public Map<Locale, DisplayText> displayItems();

    /**
   * Returns the display text for the specified locale.
   *
   * @param locale  the locale for the display text.
   * @return the display text for the specified locale.
   */
    public String displayText(Locale locale);

    /**
   * Returns the date this concept was last modified.
   *
   * @return the date this concept was last modified.
   */
    public Date modified();

    /**
   * Returns the concept name.
   *
   * @return the concept name.
   */
    public String name();
}
