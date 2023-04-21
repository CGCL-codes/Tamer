package nl.tue.win.riaca.openmath.lang;

import java.util.Enumeration;

/**
 * Models an OpenMath symbol.
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @see "The OpenMath standard 2.0, 2.1.1"
 * @version $Revision: 1.4 $
 */
public class OMSymbol extends OMObject {

    /**
     * Constructor.
     */
    public OMSymbol() {
        super();
    }

    /**
     * Constructor.
     *
     * @param cd the CD of the symbol.
     * @param name the name of the symbol.
     */
    public OMSymbol(String cd, String name) {
        super();
        attributes.put("cd", cd);
        attributes.put("name", name);
    }

    /**
     * Gets the CD for this OpenMath symbol.
     *
     * @deprecated use the getCd method instead.
     * @return the CD of the symbol.
     */
    public String getCD() {
        return getCd();
    }

    /**
     * Gets the CD for this OpenMath symbol.
     *
     * @return the CD of the symbol, or <b>null</b> if not set.
     */
    public String getCd() {
        if (attributes.get("cd") != null) {
            return (String) attributes.get("cd");
        }
        return null;
    }

    /**
     * Sets the CD for this OpenMath symbol.
     *
     * @deprecated
     * @param cd the CD of the symbol.
     */
    public void setCD(String cd) {
        setCd(cd);
    }

    /**
     * Set the CD for this OpenMath symbol.
     *
     * @param cd the CD of the symbol.
     */
    public void setCd(String cd) {
        attributes.put("cd", cd);
    }

    /**
     * Gets the name for this OpenMath symbol.
     *
     * @return the name of the symbol, or <b>null</b> if not set.
     */
    public String getName() {
        if (attributes.get("name") != null) {
            return (String) attributes.get("name");
        }
        return null;
    }

    /**
     * Sets the name for this OpenMath symbol.
     *
     * @param name the name of the symbol.
     */
    public void setName(String name) {
        attributes.put("name", name);
    }

    /**
     * Gets the type. 
     *
     * @return the type of the symbol.
     */
    public String getType() {
        return "OMS";
    }

    /**
     * Is this an atom object. 
     *
     * @return <b>true</b> if this is an atom object, <b>false</b> if it is not.
     */
    public boolean isAtom() {
        return true;
    }

    /**
     * Is this a composite object. 
     *
     * @return <b>true</b> if this is a composite object,
     *         <b>false</b> if it is not.
     */
    public boolean isComposite() {
        return false;
    }

    /**
     * Returns a string representation of the object. 
     *
     * @return the string representation of the object.
     */
    public String toString() {
        return "<OMS cd=\"" + attributes.get("cd") + "\" name=\"" + attributes.get("name") + "\"/>";
    }

    /**
     * Clones the object (shallow copy).
     *
     * @return the cloned object.
     */
    public Object clone() {
        OMSymbol symbol = new OMSymbol();
        symbol.attributes = attributes;
        return symbol;
    }

    /**
     * Copies the object (full copy).
     *
     * @return the copied object.
     */
    public Object copy() {
        OMSymbol symbol = new OMSymbol();
        Enumeration keys = attributes.keys();
        Enumeration values = attributes.elements();
        while (keys.hasMoreElements()) {
            String key = new String((String) keys.nextElement());
            String value = new String((String) values.nextElement());
            symbol.setAttribute(key, value);
        }
        return symbol;
    }

    /**
     * Determines if this is the same object.
     *
     * <p>
     *  Note: this object is deemed to be semantically the same when both the
     *  'cd' and the 'name' are equals to their counter parts.
     * </p>
     *
     * @param object the object to test against.
     * @return <b>true</b> if this is semantically the same object,
     *         <b>false</b> if it is not.
     */
    public boolean isSame(OMObject object) {
        if (object instanceof OMSymbol) {
            OMSymbol symbol = (OMSymbol) object;
            if (getCd().equals(symbol.getCd()) && getName().equals(symbol.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if this object is valid. 
     *
     * @return <b>true</b> if this is a valid object,
     *         <b>false</b> if it is not.
     */
    public boolean isValid() {
        if (getCd() != null && getName() != null) {
            return true;
        }
        return false;
    }

    /**
     * Set the cdbase for this OMSymbol.
     *
     * @param cdbase the CDBase to set.
     */
    public void setCdbase(String cdbase) {
        attributes.put("cdbase", cdbase);
    }

    /**
     * Get the cdbase for this OMSymbol.
     *
     * @return the cdbase of the symbol, or <b>null</b> if not set.
     */
    public String getCdbase() {
        if (attributes.get("cdbase") != null) {
            return (String) attributes.get("cdbase");
        }
        return null;
    }
}
