package org.dcm4che2.code;

/**
 * CID 6009 Density Modifier from BI-RADS.
 *
 * @author Gunter Zeilinger <gunterze@gmail.com>
 * @version $Rev: 13502 $ $Date:: 2010-06-09#$
 * @since Jun 2, 2010
 */
public class DensityModifierFromBIRADS {

    /** (F-01752, SRT, "Equal density (isodense) lesion") */
    public static final String EqualDensityIsodenseLesion = "F-01752\\SRT";

    /** (F-01754, SRT, "Fat containing (radiolucent) lesion") */
    public static final String FatContainingRadiolucentLesion = "F-01754\\SRT";

    /** (F-01751, SRT, "High density lesion") */
    public static final String HighDensityLesion = "F-01751\\SRT";

    /** (F-01753, SRT, "Low density (not containing fat) lesion") */
    public static final String LowDensityNotContainingFatLesion = "F-01753\\SRT";
}
