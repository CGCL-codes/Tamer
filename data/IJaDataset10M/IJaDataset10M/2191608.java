package gui;

import javax.swing.*;
import java.io.*;
import dataImporters.ATOFMSBatchTableModel;
import dataImporters.ATOFMSDataSetImporter;
import database.InfoWarehouse;
import errorframework.DisplayException;
import errorframework.ErrorLogger;
import externalswing.SwingWorker;

/**
 * A GUI for importing several ATOFMS collections at once, using a CSV file that
 * looks just like the Import from MS-Analyze GUI.
 * An example of such a file is in the importation files directory of the source
 * tree.
 * 
 * @author smitht
 *
 */
public class ATOFMSBatchImportGUI {

    private JFrame parent;

    private ATOFMSBatchTableModel tab;

    private int parentID;

    public ATOFMSBatchImportGUI(JFrame parent) {
        super();
        this.parent = parent;
    }

    public boolean init() {
        try {
            FileDialogPicker fpick = new FileDialogPicker("Choose a dataset list to import", "csv", parent);
            if (fpick.getFileName() == null) {
                return false;
            }
            File file = new File(fpick.getFileName());
            tab = new ATOFMSBatchTableModel(file);
            int selection = JOptionPane.showConfirmDialog(parent, "Use the autocalibrator on these data sets?", "Autocalibrate peaks?", JOptionPane.YES_NO_OPTION);
            if (selection == JOptionPane.YES_OPTION) tab.setAutocal(true); else tab.setAutocal(false);
            selection = JOptionPane.showConfirmDialog(parent, "Import all datasets into one parent collection?", "Import into parent?", JOptionPane.YES_NO_OPTION);
            if (selection == JOptionPane.YES_OPTION) {
                EmptyCollectionDialog ecd = new EmptyCollectionDialog((JFrame) parent, "ATOFMS", false, MainFrame.db);
                parentID = ecd.getCollectionID();
                if (parentID == -1) {
                    return false;
                }
            } else {
                parentID = 0;
            }
            return true;
        } catch (IOException ex) {
            ErrorLogger.writeExceptionToLogAndPrompt("ATOFMSBatchImport", ex.toString());
        }
        return false;
    }

    /**
	 * Actually try to import the dataset.  Doesn't tell the caller whether
	 * it worked or not, but it does tell the user.  This seems to be the usual
	 * thing to do, don't know if it's right.  Whee!
	 * 
	 * Can take a while...
	 */
    public void go(final CollectionTree collectionPane) {
        final ProgressBarWrapper progressBar = new ProgressBarWrapper(parent, "Importing ATOFMS Datasets", 100);
        progressBar.constructThis();
        final InfoWarehouse dbRef = MainFrame.db;
        final SwingWorker worker = new SwingWorker() {

            public Object construct() {
                ATOFMSDataSetImporter dsi = new ATOFMSDataSetImporter(tab, parent, progressBar);
                dsi.collectTableInfo();
                dsi.setParentID(parentID);
                for (int i = 0; i < dsi.getNumCollections(); i++) {
                    dbRef.beginTransaction();
                    try {
                        dsi.collectRowInfo();
                        dbRef.commitTransaction();
                    } catch (InterruptedException e2) {
                        dbRef.rollbackTransaction();
                    } catch (DisplayException e1) {
                        ErrorLogger.displayException(progressBar, e1.toString());
                        dbRef.rollbackTransaction();
                    }
                }
                return null;
            }

            public void finished() {
                progressBar.disposeThis();
                collectionPane.updateTree();
                parent.validate();
            }
        };
        worker.start();
    }
}
