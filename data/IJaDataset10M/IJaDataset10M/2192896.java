package com.xavax.jsf.vms.rb;

import java.util.Locale;
import com.xavax.jsf.vms.DisplayText;

/**
 * RBDisplayText is a display text item loaded from a resource bundle.
 */
public class RBDisplayText extends RBObject implements DisplayText {

    /**
   * Create an RBDisplayText with the specified locale and text.
   *
   * @param locale  the locale for this object.
   * @param text    the text for this object.
   */
    public RBDisplayText(Locale locale, String text) {
        this.locale = locale;
        this.text = text;
    }

    /**
   * Returns the locale.
   *
   * @return the locale.
   */
    public Locale locale() {
        return this.locale;
    }

    /**
   * Returns the display text.
   *
   * @return the display text.
   */
    public String text() {
        return this.text;
    }

    /**
   * Compare this object with another object of the same type.
   */
    public int compareTo(DisplayText dt) {
        String s1 = locale.toString();
        String s2 = dt.locale().toString();
        int result = s1.compareTo(s2);
        return result;
    }

    /**
   * Returns a hashcode for this concept.
   *
   * @return a hashcode for this concept.
   */
    @Override
    public int hashCode() {
        return locale == null ? 0 : locale.hashCode();
    }

    /**
   * Returns a string representation of this object.
   *
   * @return a string representation of this object.
   */
    @Override
    public String toString() {
        String s = oid() + ":" + locale + ":" + text;
        return s;
    }

    private Locale locale;

    private String text;
}
