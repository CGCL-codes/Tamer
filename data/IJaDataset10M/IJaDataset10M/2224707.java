package joelib.math.symmetry;

/**
 * PointGroup.
 *
 * @author     Serguei Patchkovskii
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.4 $, $Date: 2004/07/25 20:43:23 $
 */
public class PointGroup {

    /**
     * Canonical group name.
     */
    private String groupName;

    /**
     * Group symmetry code.
     */
    private String symmetryCode;

    /**
     *Additional verification routine, not used.
     */
    private boolean check;

    /**
     *  Constructor for the PointGroup object
     *
     */
    public PointGroup(String _groupName, String _symmetryCode, boolean _check) {
        groupName = _groupName;
        symmetryCode = _symmetryCode;
        check = _check;
    }

    public boolean getCheck() {
        return check;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getSymmetryCode() {
        return symmetryCode;
    }
}
