package de.fu_berlin.inf.gmanda.gui.preferences;

import java.awt.Color;
import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.preferences.ColorPreferenceItem;

public class SeenColorProperty extends ColorPreferenceItem {

    public SeenColorProperty(Configuration configuration) {
        super(configuration, "PrimaryDocumentTreeSeenColor", new Color(0xF0F0F0));
    }
}
