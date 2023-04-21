package de.mpiwg.vspace.digilib.preferences;

import org.eclipse.jface.preference.IPreferencePage;
import de.mpiwg.vspace.extension.interfaces.IVSpacePreferencePage;

public class DigilibVSpacePreferencePage implements IVSpacePreferencePage {

    public IPreferencePage getPreferencePage() {
        return new DigilibPreferencePage();
    }

    public String getPreferencePageId() {
        return "digilibVSpacePreferencePage";
    }

    public String getPreferencePagePath() {
        return "";
    }

    public void setPreferencePage(IPreferencePage page) {
    }
}
