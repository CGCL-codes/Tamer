package ch.elca.el4ant.model;

import org.apache.tools.ant.types.DataType;

/**
 * Mapping declaration between execution units.
 *
 * <script type="text/javascript">printFileStatus
 *   ("$URL: http://el4ant.svn.sourceforge.net/svnroot/el4ant/trunk/buildsystem/core/java/ch/elca/el4ant/model/Mapping.java $",
 *   "$Revision: 266 $", "$Date: 2005-10-24 04:26:50 -0400 (Mon, 24 Oct 2005) $", "$Author: yma $"
 * );</script>
 *
 * @author Yves Martin (YMA)
 * @version $Revision: 266 $
 */
public class Mapping extends DataType {

    /** Current module execution unit. */
    private String eu;

    /** Dependency target execution unit. */
    private String target;

    /**
     * Get the Eu value.
     * @return the Eu value.
     */
    public String getEu() {
        return eu;
    }

    /**
     * Set the Eu value.
     * @param newEu The new Eu value.
     */
    public void setEu(String newEu) {
        this.eu = newEu;
    }

    /**
     * Get the Target value.
     * @return the Target value.
     */
    public String getTarget() {
        return target;
    }

    /**
     * Set the Target value.
     * @param newTarget The new Target value.
     */
    public void setTarget(String newTarget) {
        this.target = newTarget;
    }
}
