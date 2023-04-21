package org.ms150hams.trackem.ui.wizards.setupwizard;

import org.ms150hams.trackem.prefs.GpsPrefs;
import org.ms150hams.trackem.ui.wizards.*;

public class SetupGPSDescriptor extends WizardPanelDescriptor {

    public static final String IDENTIFIER = "GPS_PANEL";

    public SetupGPSDescriptor(GpsPrefs prefs) {
        panel = new SetupGPS(prefs);
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(panel);
    }

    public Object getNextPanelDescriptor() {
        return WizardPanelDescriptor.FINISH;
    }

    public Object getBackPanelDescriptor() {
        return SetupAx25Descriptor.IDENTIFIER;
    }

    SetupGPS panel;
}
