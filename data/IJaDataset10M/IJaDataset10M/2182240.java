package com.bbn.feupdater.gui;

import jfireeagle.FireEagleClient;
import jfireeagle.LocationParameters;

public class LookupForm extends AbstractForm {

    private LocationParametersPanel locPanel = new LocationParametersPanel();

    public LookupForm(FireEagleClient c) {
        super(c, 1);
        add(locPanel);
    }

    public Object onSubmit() {
        LocationParameters loc = locPanel.getLocationParameters();
        return this.getClient().lookup(loc);
    }
}
