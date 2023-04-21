package org.dcm4chee.xero.display;

import java.util.Map;
import javax.xml.namespace.QName;
import org.dcm4chee.xero.search.study.Macro;

/** The navigate macro allows a patient/study/series to have a defined "start" position for the next level.
 * This is a simple navigation scheme that allows navigation at one level at a time.  More complex navigation
 * schemes use other mechanisms to define positions.
 * @author bwallace
 *
 */
public class NavigateMacro implements Macro {

    public static QName Q_VIEW_START = new QName("viewStart");

    String viewStart;

    /** Setup an object to navigate to the given start position. */
    public NavigateMacro(String viewStart) {
        if (viewStart == null || viewStart.equals("")) throw new IllegalArgumentException("Navigate must be provided with a start position.");
        this.viewStart = viewStart;
    }

    /** Add to the array of extra attributes */
    public int updateAny(Map<QName, String> attrs) {
        attrs.put(Q_VIEW_START, viewStart);
        return 1;
    }

    public String toString() {
        return "nav(" + viewStart + ")";
    }
}
