package edu.upmc.opi.caBIG.caTIES.client.vr.query.dialogs;

import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import org.apache.log4j.Logger;
import edu.upmc.opi.caBIG.caTIES.common.GeneralUtilities;
import edu.upmc.opi.caBIG.caTIES.middletier.CaTIES_DocumentImpl;

public class CaTIES_ReportExporter extends JFileChooser {

    private static File lastUsedDir = null;

    /**
     * Field logger.
     */
    private static Logger logger = Logger.getLogger(CaTIES_ReportExporter.class);

    CaTIES_DocumentImpl document;

    Frame parent;

    JTextField filename;

    CaTIES_ReportParameterPanel params = new CaTIES_ReportParameterPanel();

    public CaTIES_ReportExporter(Frame f, CaTIES_DocumentImpl document) {
        super(lastUsedDir);
        parent = f;
        this.document = document;
        initGUI();
    }

    private void initGUI() {
        params.setBorder(BorderFactory.createTitledBorder("Include"));
        this.setAccessory(params);
        this.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return "txt".equals(getExtension(f));
            }

            @Override
            public String getDescription() {
                return "Text Files(*.txt)";
            }

            public String getExtension(File f) {
                String ext = null;
                String s = f.getName();
                int i = s.lastIndexOf('.');
                if (i > 0 && i < s.length() - 1) {
                    ext = s.substring(i + 1).toLowerCase();
                }
                return ext;
            }
        });
        int result = this.showDialog(parent, "Export");
        if (result == JFileChooser.CANCEL_OPTION) return;
        File f = null;
        lastUsedDir = this.getSelectedFile().getParentFile();
        if (this.getSelectedFile().exists()) {
            int opt = JOptionPane.showConfirmDialog(parent, "Do you want to overwrite the file?", "File exists", JOptionPane.YES_NO_CANCEL_OPTION);
            if (opt == JOptionPane.YES_OPTION) f = this.getSelectedFile(); else return;
        } else {
            try {
                URL file;
                URL tmp = this.getSelectedFile().toURL();
                if (tmp.toString().toLowerCase().indexOf(".txt") < 0) file = new URL(tmp.toString() + ".txt"); else file = tmp;
                f = new File(file.getPath());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        try {
            f.createNewFile();
            printToFile(f);
            JOptionPane.showMessageDialog(parent, "Exported successfully to \n" + f.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printToFile(File f) {
        String buf = "";
        if (params.exportReportSynoptics()) {
            buf += "Synoptics" + "\n";
            buf += "---------" + "\n";
            buf += "De-Identified Accession No: ";
            buf += document.obj.getUuid() + "\n";
            buf += "De-Identified Patient ID: ";
            buf += document.obj.getPatient().getId() + "\n";
            buf += "Organization: ";
            buf += document.obj.getPatient().getOrganization().getName() + "\n";
            buf += "Age: ";
            buf += document.obj.getPatientAgeAtCollection() + "\n";
            buf += "Gender: ";
            buf += document.obj.getPatient().getGender() + "\n";
            buf += "Race: ";
            buf += document.obj.getPatient().getEthnicity() + "\n";
        }
        if (params.exportReportText()) {
            buf += "\n\n";
            buf += "Report" + "\n";
            buf += "------" + "\n";
            buf += document.obj.getDocumentData().getDocumentText() + "\n";
        }
        buf += "==========================================";
        try {
            GeneralUtilities.setContents(f, buf);
        } catch (FileNotFoundException e) {
            logger.fatal("file not found when saving results");
        } catch (IOException e) {
            logger.fatal("Error occured when writing results to file");
        }
    }
}
