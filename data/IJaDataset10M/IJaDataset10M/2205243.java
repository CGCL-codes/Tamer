package fiswidgets.fisdesktop;

import fiswidgets.fisgui.*;
import fiswidgets.fisutils.*;
import fiswidgets.fisremote.*;

public class SetLogName extends FisBase {

    FisTextField logfile;

    public SetLogName() {
        super();
        try {
            FisProperties.loadFisProperties(FisProperties.FISPROPERTIES_SYSTEM_OR_USER);
        } catch (Exception ex) {
            Dialogs.ShowErrorDialog(this, "Error loading properties file: " + ex.getMessage());
            deactivateRunButton();
        }
        try {
            activateHelpButton("SetLogName.html");
        } catch (Exception ex) {
            Dialogs.ShowErrorDialog(this, ex.getMessage());
        }
        addToAbout("\n The SetLogName widget was create by Eugene Tseytlin (University of Pittsburgh)");
        setClientServerCapable(true);
        activateRunButton("fiswidgets.fisdesktop.SetLogNameRun", this, true);
        setTitle("SetLogName");
        setLogging(false);
        String log = "";
        if (FisDesktop.getRunningDesk()) log = FisDesktop.getLogFile();
        logfile = new FisTextField("Log filename", log, this);
        (new FisFileBrowser(this)).attachTo(logfile);
    }

    public void postRun() {
        if (FisDesktop.getRunningDesk()) FisDesktop.setLogFile(logfile.getText());
    }

    public FisParameters getGUIdata() {
        SetLogNameParameters params = new SetLogNameParameters();
        params.logfile = logfile.getText();
        return params;
    }

    public static void main(String[] args) {
        (new SetLogName()).display();
    }
}
