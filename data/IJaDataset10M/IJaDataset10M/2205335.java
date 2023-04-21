package gov.nasa.gsfc.visbard.gui;

import gov.nasa.gsfc.visbard.model.RangeControllerModel;
import gov.nasa.gsfc.visbard.model.Universe;
import gov.nasa.gsfc.visbard.model.VisbardMain;

public class OptionsDialog extends VisbardDialog {

    private static final String sTitle = "Options";

    private static final int sWidth = 350;

    private static final int sHeight = 500;

    /**
     * constructor
    **/
    public OptionsDialog(Universe uni, RangeControllerModel rm) {
        super(false);
        initGUI(uni, rm);
    }

    /**
     * intialize the dialog
    **/
    protected void initGUI(Universe uni, RangeControllerModel rm) {
        getContentPane().add(new OptionsPanel(uni, rm));
        setTitle(sTitle);
        setSize(sWidth, sHeight);
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
