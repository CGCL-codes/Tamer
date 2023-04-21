package org.epo.gui.generic.bean;

/**
 * Bean describing a property to be displayed in the Properties JSP.
 * Creation date: 04/2001
 * @author: INFOTEL
 */
public class Filter extends org.epo.gui.jphoenix.generic.BeanObject {

    private java.lang.String name;

    private java.lang.String label;

    private String[] possibleValues = null;

    /**
 * Property constructor comment.
 */
    public Filter() {
        super();
    }

    /**
 * Insert the method's description here.
 * Creation date: (11/04/01 16:52:34)
 * @param theName java.lang.String
 * @param theValue java.lang.String
 * @param theLabel java.lang.String
 * @param theEditFlag boolean
 * @param theMinValue int
 * @param theMaxValue int
 */
    public Filter(String theName, String theLabel, String[] thePossibleValues) {
        setName(theName);
        setLabel(theLabel);
        setPossibleValues(thePossibleValues);
    }

    /**
 * Insert the method's description here.
 * Creation date: (19/11/01 10:52:44)
 * @param theValue java.lang.String
 */
    public static String formatHTMLString(String theValue) {
        String myReturnString = "";
        char myChar;
        for (int i = 0; i < theValue.length(); i++) {
            myChar = theValue.charAt(i);
            myReturnString += (myChar == '"') ? "&#34" : myChar + "";
        }
        return myReturnString;
    }

    /**
 * Insert the method's description here.
 * Creation date: (11/04/01 11:37:28)
 * @return java.lang.String
 */
    public java.lang.String getLabel() {
        return label;
    }

    /**
 * Insert the method's description here.
 * Creation date: (11/04/01 11:36:18)
 * @return java.lang.String
 */
    public java.lang.String getName() {
        return name;
    }

    /**
 * Insert the method's description here.
 * Creation date: (06/06/01 16:42:57)
 * @return java.lang.String[]
 */
    public java.lang.String[] getPossibleValues() {
        return possibleValues;
    }

    /**
 * Insert the method's description here.
 * Creation date: (11/04/01 11:37:28)
 * @param newLabel java.lang.String
 */
    private void setLabel(java.lang.String newLabel) {
        label = newLabel;
    }

    /**
 * Insert the method's description here.
 * Creation date: (11/04/01 11:36:18)
 * @param newName java.lang.String
 */
    private void setName(java.lang.String newName) {
        name = newName;
    }

    /**
 * Insert the method's description here.
 * Creation date: (06/06/01 16:42:57)
 * @param newPossibleValues java.lang.String[]
 */
    private void setPossibleValues(java.lang.String[] newPossibleValues) {
        possibleValues = newPossibleValues;
    }
}
