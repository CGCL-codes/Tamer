package gov.nasa.gsfc.visbard.gui.categoryview;

import gov.nasa.gsfc.visbard.gui.*;
import gov.nasa.gsfc.visbard.model.*;
import javax.swing.*;

public class ScalingDialog extends VisbardDialog {

    private static final String sTitle = "Scaling Panel";

    private static final int sMinWidth = 370;

    private static final int sMinHeight = 350;

    public ScalingDialog() {
        super(false);
        initGUI();
    }

    protected void initGUI() {
        JComponent pane = new ScalingPanel();
        getContentPane().add(pane);
        setTitle(sTitle);
        setMinimumDimensions(sMinWidth, sMinHeight);
        setResizable(true);
        VisbardMain.getSettingsManager().registerSettingsHolder(this);
    }

    /**
     * Returns a string which identifies this object uniquely. Any string is
     * valid as long as it does not contain the underscore character.
     */
    public String getHolderID() {
        return sTitle;
    }
}
