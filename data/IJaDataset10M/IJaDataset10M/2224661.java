package proteomics.plugins;

import pedro.plugins.*;
import pedro.model.*;
import pedro.view.*;
import javax.swing.*;
import java.net.URL;
import java.io.File;

/**
*
* Copyright (c) 2003 University of Mancester.
* @author Kevin Garwood (garwood@cs.man.ac.uk)
* @version 1.0
*
*/
public class PeakListExporter implements DataExportPlugin {

    public String getDisplayName() {
        return "Peak List Data Export";
    }

    public String getDescription() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("exports peaks");
        return buffer.toString();
    }

    public boolean isSuitableForRecordModel(String modelStamp, String recordClassName) {
        if (recordClassName.equals("PeakList") == true) {
            return true;
        }
        return false;
    }

    public void process(RecordModelFactory recordModelFactory, NavigationTree navigationTree, NavigationTreeNode node) {
        JOptionPane.showConfirmDialog(null, "Peak List Export Routine...", "Export Routine", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null);
    }

    public URL getHelpLink() {
        URL url = null;
        try {
            File file = new File("C://plugins_out//MassSpecExperimentLoader.html");
            url = file.toURL();
        } catch (Exception err) {
        }
        return url;
    }
}
