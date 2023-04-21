package gov.sns.xal.smf.impl;

import gov.sns.xal.smf.impl.qualify.*;

/** 
 * <h1>VerticalKicker</h1>
 * <p>
 * The extraction kicker represents a pulsed magnet for 
 * extracting the beam vertically from the ring.
 * </p> 
 *
 *  @author Tom Pelaia 
 *  @since 1/9/07 
 *
 */
public class ExtractionKicker extends Dipole {

    /** node type */
    public static final String s_strType = "EKick";

    static {
        registerType();
    }

    /** Constructor */
    public ExtractionKicker(final String strId) {
        super(strId);
    }

    /** Register type for qualification */
    private static void registerType() {
        ElementTypeManager typeManager = ElementTypeManager.defaultManager();
        typeManager.registerType(ExtractionKicker.class, s_strType);
        typeManager.registerType(ExtractionKicker.class, "kicker");
        typeManager.registerType(ExtractionKicker.class, "vertkicker");
        typeManager.registerType(ExtractionKicker.class, "extractionkicker");
    }

    /** Override to provide type signature */
    public String getType() {
        return s_strType;
    }

    ;

    /**
	 * Get the orientation of the magnet as defined by MagnetType.  The orientation of all vertical correctors is VERTICAL.
     * @return VERTICAL
     */
    public int getOrientation() {
        return VERTICAL;
    }

    /**
	 * Determine whether this magnet is a corrector.
     * @return false     
	 */
    public boolean isCorrector() {
        return false;
    }
}
