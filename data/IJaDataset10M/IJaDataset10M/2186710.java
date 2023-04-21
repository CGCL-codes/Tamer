package net.sourceforge.cruisecontrol;

import java.io.Serializable;
import org.jdom.Element;

/**
 * This interface defines the method required to increment
 * the label used in the MasterBuild process. This label
 * is incorporated into the log filename when a successful
 * build occurs.
 *
 * @author <a href="mailto:alden@thoughtworks.com">alden almagro</a>
 * @author <a href="mailto:pj@thoughtworks.com">Paul Julius</a>
 */
public interface LabelIncrementer extends Serializable {

    /**
     * Increments the label when a successful build occurs.
     * The oldLabel should be transformed and returned as
     * the new label.  The build log is also passed in so that some
     * more complex label incrementing can be handled.  For example, a
     * label incrementer could find the ant target that was called and increment based on that
     * information.
     *
     * @param buildLog JDOM <code>Element</code> representation of the build.
     * @param oldLabel Label from previous successful build.
     * @return Label to use for most recent successful build.
     */
    String incrementLabel(String oldLabel, Element buildLog);

    /**
     *  Some implementations of <code>LabelIncrementer</code>, such as those involving
     *  dates, are better suited to being incremented before building rather
     *  than after building.  This method determines whether to increment before
     *  building or after building.
     */
    boolean isPreBuildIncrementer();

    /**
     *  Check the validity of a user-supplied label, making sure that it can be incremented successfully by
     *  the appropriate implementation of <code>LabelIncrementer</code>
     *
     *  @param label user-supplied label
     *  @return true if it is a valid label.
     */
    boolean isValidLabel(String label);

    /**
     * Called by Project when there is no previously serialized label.
     * @return defaultLabel
     */
    String getDefaultLabel();
}
