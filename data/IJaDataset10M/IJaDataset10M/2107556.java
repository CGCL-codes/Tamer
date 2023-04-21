package au.jSummit.GUI;

import au.jSummit.Core.*;
import au.jSummit.Core.*;
import java.util.*;

public class Profile extends JSModule {

    private Vector vAvailableSummits;

    private ProfileFrame jProfiles;

    public Profile(Vector vSummitInformation) {
        vAvailableSummits = vSummitInformation;
        jProfiles = new ProfileFrame(vAvailableSummits, this);
    }

    public void run() {
        jProfiles.setVisible(true);
    }

    public void receivePacket(JSPacket modulePacket) {
    }

    public void updateSummitInfo(SummitInfo newSummitInfo) {
    }

    public static String getModuleName() {
        return new String("Profile/Summit Loader");
    }
}

;
