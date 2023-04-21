package no.ntnu.kpro09.client.gui;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * The settings panel is used to change the composition settings, like name and, in the future,
 * music composition:-)
 * 
 * @author Gaute Nordhaug
 * 
 */
public class SettingsPanel extends AbsolutePanel {

    private final TextBox nameBox = new TextBox();

    public SettingsPanel() {
        add(new Label("Composition title"), 5, 5);
        add(nameBox, 150, 5);
    }

    public String getTitle() {
        return nameBox.getText();
    }
}
