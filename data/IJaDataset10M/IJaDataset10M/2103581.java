package org.pojosoft.lms.content.scorm2004.datatypes;

import java.io.Serializable;

/**
 * Encapsulation of information required for processing a data model request.
 * <br><br>
 * <p/>
 * <strong>Filename:</strong> DMDelimiterDescriptor.java<br><br>
 * <p/>
 * <strong>Description:</strong><br>
 * Encapsulation of all information required to describe one dot-notation bound
 * delimiter.  This information will be used to create instances of delimiters
 * assocaited with data model elements.<br><br>
 * <p/>
 * <strong>Design Issues:</strong><br><br>
 * <p/>
 * <strong>Implementation Issues:</strong><br>
 * All fields are purposefully public to allow immediate access to known data
 * elements.<br><br>
 * <p/>
 * <strong>Known Problems:</strong><br><br>
 * <p/>
 * <strong>Side Effects:</strong><br><br>
 * <p/>
 * <strong>References:</strong><br>
 * <ul>
 * <li>SCORM 2004
 * </ul>
 *
 * @author ADL Technical Team
 */
public class DMDelimiterDescriptor implements Serializable {

    /**
   * Describes the name of this delimiter
   */
    public String mName = null;

    /**
   * Describes if the default value of this delimiter
   */
    public String mDefault = null;

    /**
   * Describes the SPM for the value.
   */
    public int mValueSPM = -1;

    /**
   * Describes the method used to validate the value of this delimiter.
   */
    public DMTypeValidator mValidator = null;

    public DMDelimiterDescriptor(String iName, String iDefault, DMTypeValidator iValidator) {
        mName = iName;
        mDefault = iDefault;
        mValidator = iValidator;
    }

    public DMDelimiterDescriptor(String iName, String iDefault, int iValueSPM, DMTypeValidator iValidator) {
        mName = iName;
        mDefault = iDefault;
        mValueSPM = iValueSPM;
        mValidator = iValidator;
    }
}
