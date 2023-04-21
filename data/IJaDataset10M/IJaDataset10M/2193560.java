package org.jmlspecs.models;

public class JMLString implements JMLComparable {

    protected String str_;

    public JMLString() {
        str_ = "";
    }

    public JMLString(String s) throws IllegalArgumentException {
        if (s != null) {
            str_ = s;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /** The empty JMLString.
     * @see #JMLString()
     */
    public static final JMLString EMPTY = new JMLString();

    public Object clone() {
        return this;
    }

    public int compareTo(Object op2) throws ClassCastException, NullPointerException {
        return str_.compareTo(((JMLString) op2).str_);
    }

    public int compareTo(JMLString another) {
        return str_.compareTo(another.str_);
    }

    public boolean equals(Object s) {
        if (s != null && s instanceof JMLString) {
            return (str_.equals(((JMLString) s).str_));
        } else {
            return (false);
        }
    }

    public boolean equalsIgnoreCase(JMLString another) {
        return str_.equalsIgnoreCase(another.str_);
    }

    public boolean equalsIgnoreCase(String another) {
        return str_.equalsIgnoreCase(another);
    }

    public int hashCode() {
        return (str_.hashCode());
    }

    public String toString() {
        return (str_);
    }

    /** Produce a new JMLString that is the concatentation of two JMLStrings.
     */
    public JMLString concat(JMLString s) {
        return new JMLString(str_ + s.str_);
    }

    /** Produce a new JMLString that is the concatentation of the JMLString
	and a String.
     */
    public JMLString concat(String s) {
        return new JMLString(str_ + s);
    }

    /** Produce a new JMLString that is the concatentation of the JMLString
	and a char.
     */
    public JMLString concat(char c) {
        return new JMLString(str_ + c);
    }
}
