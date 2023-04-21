package de.tum.in.botl.metamodel.implementation;

/**
 * <p>�berschrift: Systementwicklungsprojekt</p>
 * <p>Beschreibung: A term can be a constant or a variable</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Organisation: </p>
 * @author Georgi Todorov
 * @version 1.0
 */
public class Term {

    private String value;

    /**
   * Returns a term
   */
    public Term() {
        this.value = "";
    }

    /**
   * Returns a term with the specified value
   * @param value
   */
    public Term(String value) {
        this.value = value;
    }

    /**
   * Must be extended! 
   * @param value
   * @return
   */
    public static Term parse(String value) {
        if (value.startsWith("$")) {
            return new Variable(value.substring(1));
        }
        if (value.startsWith("#")) {
            return new Diamond();
        }
        return new Constant(value.substring(1));
    }

    /**
   * Must be extended! 
   * @return
   */
    public static String unparse(Term t) {
        if (t instanceof Variable) {
            return "$" + t.getValue();
        }
        if (t instanceof Diamond) {
            return "#";
        }
        return t.getValue();
    }

    /**
   * Returns the value of this term
   * @return
   */
    public String getValue() {
        return value;
    }

    /**
   * Sets the value of this term
   * @param value
   */
    public void setValue(String value) {
        this.value = value;
    }
}
