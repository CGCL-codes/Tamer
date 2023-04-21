package org.mitre.scap.xccdf.check;

import gov.nist.checklists.xccdf.x11.CheckContentRefType;
import java.util.List;

class UnsupportedCheck extends Check {

    public UnsupportedCheck(final CheckSystem system, final List<CheckContentRefType> checkContent) {
        super(system, checkContent);
    }
}
