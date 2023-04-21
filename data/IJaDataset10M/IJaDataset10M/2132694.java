package sts.gui.importing;

import sts.gui.*;
import sts.framework.*;
import java.util.List;
import java.util.*;
import java.util.zip.*;
import java.io.*;
import javax.swing.*;
import com.Ostermiller.util.*;
import kellinwood.meshi.form.FormUtils;

/**
 *
 * @author ken
 */
public class ExportSTZWizard extends BaseImportExportWizard implements ProgressMessageListener {

    FirstState firstState = null;

    /** Creates a new instance of ImportSTZWizard */
    public ExportSTZWizard(java.awt.Frame parent) throws Exception {
        super(parent, "Export");
        setTitle("Export STZ File");
        firstState = new FirstState(this);
        firstState.panel.setRequiredFilenameExtension(".stz");
        init();
    }

    public void progressMessage(ProgressMessageEvent evt) {
        String message = evt.getMessage();
        if (message == null || message.startsWith("Done")) return;
        appendProgressMessage("    " + message);
    }

    /** Implementation of this method performs the actual export of the 
     *  data in the 'rows' member variable. 
     */
    public void run() {
        try {
            ZipOutputStream zo = new ZipOutputStream(new FileOutputStream(getFilename()));
            StringWriter swt;
            byte[] data;
            appendProgressMessage("Exporting regatta...");
            swt = new StringWriter();
            ExportCSVRegattaWizard erw = new ExportCSVRegattaWizard(new JFrame());
            erw.addProgressMessageListener(this);
            erw.doExport(swt);
            data = swt.toString().getBytes();
            zo.putNextEntry(new ZipEntry("regatta.csv"));
            zo.write(data);
            appendProgressMessage("Exporting entries...");
            swt = new StringWriter();
            ExportCSVEntriesWizard eew = new ExportCSVEntriesWizard(new JFrame());
            eew.addProgressMessageListener(this);
            eew.doExport(swt);
            data = swt.toString().getBytes();
            zo.putNextEntry(new ZipEntry("entries.csv"));
            zo.write(data);
            appendProgressMessage("Exporting race data...");
            swt = new StringWriter();
            ExportCSVRaceDataWizard edw = new ExportCSVRaceDataWizard(new JFrame());
            edw.addProgressMessageListener(this);
            edw.doExport(swt);
            data = swt.toString().getBytes();
            zo.putNextEntry(new ZipEntry("race-data.csv"));
            zo.write(data);
            if (firstState.panel.getIncludeCourses()) {
                appendProgressMessage("Exporting courses...");
                swt = new StringWriter();
                ExportCSVCoursesWizard ecw = new ExportCSVCoursesWizard(new JFrame());
                ecw.addProgressMessageListener(this);
                ecw.doExport(swt);
                data = swt.toString().getBytes();
                zo.putNextEntry(new ZipEntry("courses.csv"));
                zo.write(data);
            }
            zo.close();
            appendProgressMessage("Done...");
            appendProgressMessage(null);
        } catch (Exception x) {
            ErrorDialog.handle(x);
        }
    }

    public void finish() {
    }

    protected WizardState getFirstState() throws Exception {
        return firstState;
    }

    protected void init() throws Exception {
        super.localInit();
    }

    static javax.swing.filechooser.FileFilter stzFileFilter = new Filter();

    public static javax.swing.filechooser.FileFilter getSTZFileFilter() {
        return stzFileFilter;
    }

    static class Filter extends javax.swing.filechooser.FileFilter {

        public boolean accept(File file) {
            return file.isDirectory() || file.getName().toLowerCase().endsWith(".stz");
        }

        public String getDescription() {
            return "STZ files";
        }
    }

    /**
     * Setter for property file.
     * @param file New value of property file.
     */
    public void setFile(File file) {
        firstState.panel.setFile(file);
        try {
            Framework.onlyInstance().getPreferences().setImportExportDirectory(file.getParentFile().getCanonicalPath());
        } catch (Exception x) {
            ErrorDialog.handle(x);
        }
    }

    public String getFilename() {
        return firstState.panel.getFilename();
    }

    class FirstState extends WizardState {

        ExportSTZFileFormatPanel panel = new ExportSTZFileFormatPanel();

        WizardState nextState = null;

        public FirstState(AbstractWizard wizard) {
            super(wizard);
        }

        public boolean advance() {
            return true;
        }

        public boolean hasNextState() {
            return true;
        }

        public WizardState getNextState() {
            if (progressState == null) progressState = new ProgressState(wizard);
            return progressState;
        }

        public javax.swing.JPanel getPanel() {
            return panel;
        }

        public Object getPanelBorderLayoutConstraint() {
            return java.awt.BorderLayout.NORTH;
        }
    }
}
