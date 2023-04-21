package gov.nasa.gsfc.visbard.gui.graph;

import gov.nasa.gsfc.visbard.gui.VisbardDialog;
import gov.nasa.gsfc.visbard.model.VisbardMain;

public class CustomGraphDialog extends VisbardDialog {

    private static final String sTitle = "Graph";

    private static final int WIDTH = 390;

    private static final int HEIGHT = 200;

    /**
     * constructor
    **/
    public CustomGraphDialog() {
        super(false);
        initGUI();
    }

    /**
     * intialize the dialog
    **/
    protected void initGUI() {
        getContentPane().add(new CustomGraphPanel(this));
        setTitle(sTitle);
        setMinimumDimensions(WIDTH, HEIGHT);
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
