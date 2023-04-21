package org.jscience.biology.lsystems.fixed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.Vector;

/**
 * This class represents an LSystem as we know it in text form. It can read
 * values from a file or stream and store them in this class. After a system has
 * been read and validated you can make the derivations and obtain the final
 * string representing the figure. With this string the turtle is able to build
 * the system and make it visible to the user.<br>
 * The file or stram must respect some definitions: All codewords listed in the
 * Def class must stand at the beginning of the line and written in the right
 * case. As separator to the value only the specified symbol may be used. The
 * order of the lines does not matter. Lines which do not begin with a codeword
 * are ignored. White spaces may be used or not. <p/> Example:<br>
 *
 * <pre>
 * depth = 5
 * angle = 90
 * &lt;p/&gt;
 * axiom = F-F-F-F
 * rule = F -&gt; F-F+F+FF-F-F+F
 * </pre>
 */
public class LSystem {

    /**
     * The axiom of the LSystem
     */
    private String m_strAxiom;

    /**
     * A vector containing all the rules for the LSystem
     */
    private Vector m_vRules;

    /**
     * The recursion depth of the LSystem
     */
    private int m_iDepth;

    /**
     * The angle for the turtle to turn in radians.
     */
    private float m_fAngle;

    /**
     * The thickness of the branch on the actual position.
     */
    private float m_fThickness;

    /**
     * The length of a piece of branch.
     */
    private float m_fLength;

    /**
     * The step to decrement the thickness.
     */
    private float m_fDecrement;

    /**
     * The generated LSystem after derivating it.
     */
    private String m_strSystem;

    /**
     * Constructor. Initializes the member variables.
     */
    public LSystem() {
        m_strAxiom = "no axiom set yet";
        m_vRules = new Vector();
        m_iDepth = 0;
        m_fAngle = 0.0f;
        m_strSystem = "no system generated yet";
    }

    /**
     * Reads the values for the LSystem from a file and stores them in the
     * member fields. Note that the values have to be declared in a certain
     * manner defined in this class.
     *
     * @param fileValues
     *            The file to extract and store.
     */
    public void build(File fileValues) throws Exception {
        if (fileValues.canRead() == false) {
            return;
        }
        FileReader frValues = new FileReader(fileValues);
        BufferedReader brValues = new BufferedReader(frValues);
        String strLine;
        while ((strLine = brValues.readLine()) != null) {
            strLine.trim();
            if (strLine.startsWith(FixedPlantsDefinitions.STR_DEPTH)) {
                m_iDepth = readInt(strLine);
            } else if (strLine.startsWith(FixedPlantsDefinitions.STR_ANGLE)) {
                m_fAngle = (float) Math.toRadians(readFloat(strLine));
            } else if (strLine.startsWith(FixedPlantsDefinitions.STR_LENGTH)) {
                m_fLength = readFloat(strLine);
            } else if (strLine.startsWith(FixedPlantsDefinitions.STR_THICKNESS)) {
                m_fThickness = readFloat(strLine);
            } else if (strLine.startsWith(FixedPlantsDefinitions.STR_DECREMENT)) {
                m_fDecrement = readFloat(strLine);
            } else if (strLine.startsWith(FixedPlantsDefinitions.STR_AXIOM)) {
                readAxiom(strLine);
            } else if (strLine.startsWith(FixedPlantsDefinitions.STR_RULE)) {
                addRule(strLine);
            }
        }
        brValues.close();
        frValues.close();
    }

    /**
     * Extracts the value for the depth from the string, converts it to an
     * integer and stores the value in the member field.
     *
     * @param strDepth
     *            The line with the depth value.
     * @return The integer value found behing the separator.
     * @throws Exception
     *             An exception is thrown if the number can not be parsed.
     */
    private int readInt(String strLine) throws Exception {
        String strNumber = getStringBehindSeparator(strLine);
        return (Integer.valueOf(strNumber).intValue());
    }

    /**
     * Extracts the float value from a string where the number is behind the
     * separator sign and returns it.
     *
     * @param strLine
     *            The string with the float value behind the separator.
     * @return The float value found behind the separator.
     * @throws Exception
     *             An exception is thrown if the number can not be parsed.
     */
    private float readFloat(String strLine) throws Exception {
        String strNumber = getStringBehindSeparator(strLine);
        double dValue = Float.valueOf(strNumber).floatValue();
        return (float) dValue;
    }

    /**
     * Checks if the given string is a valid axiom and stores it in the member
     * field.
     *
     * @param strAxiom
     *            The string containing the axiom.
     * @throws Exception
     *             Is thown if the string contains invalid signs.
     */
    private void readAxiom(String strAxiom) throws Exception {
        strAxiom = getStringBehindSeparator(strAxiom);
        validateSuccessor(strAxiom);
        setAxiom(strAxiom);
    }

    /**
     * Checks if the given string contains a valid rule, stores it in a new rule
     * instance and adds it in the member vector for all rules.
     *
     * @param strRule
     *            The string containing the rule.
     * @throws Exception
     *             Is thrown if the rule is not in the right form or contains
     *             invalid signs.
     */
    private void addRule(String strRule) throws Exception {
        strRule = getStringBehindSeparator(strRule);
        int iSignPos;
        if ((iSignPos = strRule.indexOf(FixedPlantsDefinitions.STR_RULESIGN)) == -1) {
        }
        String strPred = strRule.substring(0, iSignPos).trim();
        if (strPred.length() > 1 || Character.isLetter(strPred.charAt(0)) == false) {
        }
        String strSucc = strRule.substring(iSignPos + FixedPlantsDefinitions.STR_RULESIGN.length()).trim();
        validateSuccessor(strSucc);
        Rule ruleNew = new Rule(strPred.charAt(0), strSucc);
        m_vRules.add(ruleNew);
    }

    /**
     * Gets the string behind the separator. This is the string from the
     * separator to the end of the line.
     *
     * @param strLine
     *            The string containing the separator.
     * @throws Exception
     *             An exception in thrown if no separator can be found.
     */
    private String getStringBehindSeparator(String strLine) throws Exception {
        return strLine.substring(strLine.indexOf(FixedPlantsDefinitions.STR_SEPARATOR) + FixedPlantsDefinitions.STR_SEPARATOR.length()).trim();
    }

    /**
     * Checks if the axiom or the successor contain invalid signs. If so an
     * exception is thrown. Else the method returns normally. All valid signs
     * must be listed in the Def class.
     *
     * @throws Exception
     *             If the string contains invalid signs.
     * @param strSucc
     *            The successor resp. axiom to check.
     * @see FixedPlantsDefinitions
     */
    private void validateSuccessor(String strSucc) throws Exception {
        for (int i = 0; i < strSucc.length(); i++) {
            if (Character.isLetter(strSucc.charAt(i)) == false) {
                if (FixedPlantsDefinitions.STR_SIGNS.indexOf(strSucc.charAt(i)) == -1) {
                }
            }
        }
    }

    /**
     * This method derivates the LSystem. Once all values as depth, axiom and
     * all rules are stored in this class a string representing the derivated
     * LSystem can be generated. Beginning with the axiom the letters will be
     * replaced using the rules. This is repeated as many times as indicated in
     * the depth.
     *
     * @throws Exception
     *             If the derivation process fails.
     */
    public void derivate() throws Exception {
        m_strSystem = m_strAxiom;
        for (int i = 1; i <= m_iDepth; i++) {
            m_strSystem = replace(m_strSystem);
        }
    }

    /**
     * This method replaces all letters in the passed string with the
     * corresponding rule. This is actually one iteration in the LSystem
     * derivation process. If no rule can be found the letter will be inserted.
     *
     * @param strSystem
     *            The system which shall be iterated.
     * @return The new string where all letters have been replaced by rules.
     * @throws Exception
     *             If the passed string contains invalid signs.
     */
    private String replace(String strSystem) throws Exception {
        String strNewSystem = new String();
        for (int i = 0; i < strSystem.length(); i++) {
            if (FixedPlantsDefinitions.STR_SIGNS.indexOf(strSystem.charAt(i)) != -1) {
                strNewSystem = strNewSystem.concat(strSystem.substring(i, i + 1));
            } else if (Character.isLetter(strSystem.charAt(i))) {
                strNewSystem = strNewSystem.concat(getSuccessor(strSystem.charAt(i)));
            } else {
            }
        }
        return strNewSystem;
    }

    /**
     * Searches the successor with the specified predecessor. If the rule can
     * not be found it will return the originally passed letter. This because if
     * a letter does not represent a rule it might be a sign for a step ('F' or
     * 'f') and will be left as is.
     *
     * @param chPred
     *            The letter being the predecessor of a rule.
     * @return The successor of the rule or the passed letter if the rule can
     *         not be found.
     */
    private String getSuccessor(char chPred) {
        Rule ruleActual;
        for (Enumeration e = m_vRules.elements(); e.hasMoreElements(); ) {
            ruleActual = (Rule) e.nextElement();
            if (ruleActual.getPredecessor() == chPred) {
                return ruleActual.getSuccessor();
            }
        }
        return String.valueOf(chPred);
    }

    /**
     * Sets the derivation depth for the LSystem.
     *
     * @param iDepth
     *            The depth to set.
     */
    public void setDepth(int iDepth) {
        m_iDepth = iDepth;
    }

    /**
     * Sets the angle for the LSystem to the member field.
     *
     * @param fAngle
     *            The new value for the angle.
     */
    public void setAngle(float fAngle) {
        m_fAngle = fAngle;
    }

    /**
     * Stores the new axiom for this class.
     *
     * @param strAxiom
     *            The new axiom to set.
     */
    public void setAxiom(String strAxiom) {
        m_strAxiom = strAxiom;
    }

    /**
     * Gets the LSystem represented by a string.
     *
     * @return The system.
     */
    public String getSystem() {
        return m_strSystem;
    }

    /**
     * Gets the angle of the LSystem
     *
     * @return The angle of the system
     */
    public float getAngle() {
        return m_fAngle;
    }

    /**
     * Get the thickness of the branch at beginning.
     *
     * @return The thickness of the branch.
     */
    public float getThickness() {
        return m_fThickness;
    }

    /**
     * Gets the length of the branch at beginning.
     *
     * @return The length of the branches.
     */
    public float getLength() {
        return m_fLength;
    }

    /**
     * Gets the value of decrenentation of the thickness of a branch.
     *
     * @return The decrementation value.
     */
    public float getDecrement() {
        return m_fDecrement;
    }
}
