package be.vds.jtbdive.client.view.preferences;

import javax.swing.JPanel;

public abstract class AbstractPreferencePanel extends JPanel {

    public abstract void adaptUserPreferences();

    public abstract void setUserPreferences();
}
