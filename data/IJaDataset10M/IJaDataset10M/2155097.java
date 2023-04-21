package org.opennms.netmgt.vulnscand;

import org.opennms.netmgt.EventConstants;

/**
 * Class that holds the return values when parsing a description field from
 * Nessus.
 */
public class DescrValues {

    String descr;

    String cveEntry;

    int severity;

    public DescrValues() {
        descr = null;
        cveEntry = null;
        severity = -1;
    }

    public void useDefaults() {
        descr = "";
        severity = EventConstants.SEV_INDETERMINATE;
    }

    public boolean isValid() {
        if ((descr != null) && (severity > 0)) return true; else return false;
    }

    public String toString() {
        return ("descr: " + descr + "\ncveEntry: " + cveEntry + "\nseverity: " + severity + "\n");
    }
}
