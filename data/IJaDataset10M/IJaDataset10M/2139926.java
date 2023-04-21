package org.radeox.regex;

public interface Pattern {

    /**
   * Return a string representation of the regular expression
   *
   * @return String representation of the regular expression
   */
    public String getRegex();

    /**
   * Return whether the pattern is multiline or not
   *
   * @return Ture if the pattern is multiline
   */
    public boolean getMultiline();
}
