package be.vds.jtbdive.client.core.filters.inspector;

import be.vds.jtbdive.core.core.Dive;

public class DiveSiteInspector implements FilterInspector {

    public Object getFilterParameter(Dive dive) {
        return dive.getDiveSite();
    }
}
