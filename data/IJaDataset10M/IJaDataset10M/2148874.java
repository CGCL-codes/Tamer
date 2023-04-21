package edu.rice.cs.drjava.config;

/** Class defining all configuration options with values of type Integer.
  * @version $Id: IntegerOption.java 5175 2010-01-20 08:46:32Z mgricken $
  */
public class IntegerOption extends Option<Integer> {

    /** @param key The name of this Option.
    * @param def The default value for this option
    */
    public IntegerOption(String key, Integer def) {
        super(key, def);
    }

    /** @param s The String to be parsed.
    * @return The Integer object represented by "s".
    * @exception IllegalArgumentException if "s" does not represent an integer value: only Strings that can be generated
    *            by the method Integer.toString() are supported.
    */
    public Integer parse(String s) {
        try {
            return Integer.valueOf(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            throw new OptionParseException(name, s, "Must be a valid integer value.");
        }
    }
}
