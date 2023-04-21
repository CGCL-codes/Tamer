package org.apache.myfaces.trinidaddemo;

import org.apache.myfaces.trinidad.event.DisclosureEvent;

public class ToggleBean implements java.io.Serializable {

    public void onDisclosure(DisclosureEvent event) {
        _totalCount++;
    }

    public int getTotalCount() {
        return _totalCount;
    }

    private int _totalCount;
}
