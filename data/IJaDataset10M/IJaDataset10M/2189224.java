package edu.isi.div2.metadesk.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.prefs.*;
import java.io.*;
import edu.isi.div2.metadesk.util.*;

/**
 * Graphical component allows editing of user preferences related to the
 * metadesk system, and programmatic read-only access to these preferences.
 * 
 * @author Sameer Maggon (maggon@isi.edu)
 * @version $Id: SystemPreferencesEditor.java,v 1.1 2004/09/13 06:47:44 maggon
 *          Exp $
 *  
 */
public class SystemPreferencesEditor extends Observable implements PreferencesEditor, MetaDeskConstants {

    SystemPreferencesEditor() {
    }

    public JComponent getUI() {
        JPanel content = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        content.setLayout(gridbag);
        addPersonalURIField(content);
        addModelFileField(content);
        addAutoSaveTimeField(content);
        matchGuiToStoredPrefs();
        return content;
    }

    public void matchGuiToDefaultPreferences() {
    }

    public String getTitle() {
        return title;
    }

    public int getMnemonic() {
        return mnemonic;
    }

    public void savePreferences() {
        String personalNamespace = txtPersonalNamespace.getText();
        ModelManager.getModelManager().getMetaDeskConfiguration().setPersonalNamespace(personalNamespace);
        String modelFile = txtModel.getText();
        if (modelFile.toLowerCase().startsWith("http:")) {
            ModelManager.getModelManager().getMetaDeskConfiguration().setModelURL(modelFile);
            ModelManager.getModelManager().getMetaDeskConfiguration().setModelFileName(null);
            ModelManager.getModelManager().getMetaDeskConfiguration().setIsModelWebDAV(true);
        } else {
            ModelManager.getModelManager().getMetaDeskConfiguration().setModelURL("file:///" + modelFile);
            ModelManager.getModelManager().getMetaDeskConfiguration().setModelFileName(modelFile);
            ModelManager.getModelManager().getMetaDeskConfiguration().setIsModelWebDAV(false);
        }
        int autoSaveTime;
        try {
            autoSaveTime = Integer.parseInt(txtAutoSave.getText());
        } catch (NumberFormatException nfe) {
            autoSaveTime = 0;
        }
        ModelManager.getModelManager().getMetaDeskConfiguration().setAutoSaveTime(autoSaveTime);
        ModelManager.getModelManager().getMetaDeskConfiguration().saveConfiguration();
        setChanged();
        notifyObservers();
    }

    private static final String title = "System";

    private static final int mnemonic = KeyEvent.VK_L;

    private static final String SYSTEM_NODE_NAME = "metadesk/gui/prefs/System";

    private Preferences preferences = Preferences.userRoot().node(SYSTEM_NODE_NAME);

    private JLabel lblPersonalNamespace;

    private JTextField txtPersonalNamespace;

    private JLabel lblModel;

    private JTextField txtModel;

    private JButton btnBrowse;

    private JLabel lblAutoSave;

    private JTextField txtAutoSave;

    private void addPersonalURIField(JPanel aContent) {
        lblPersonalNamespace = new JLabel("Personal Namespace:");
        lblPersonalNamespace.setDisplayedMnemonic(KeyEvent.VK_P);
        lblPersonalNamespace.setToolTipText("Personal Namespace (e.g. http://www.isi.edu/~maggon/metadesk#)");
        lblPersonalNamespace.setFont(lblPersonalNamespace.getFont().deriveFont((float) ModelManager.getModelManager().getGeneralLookPreferencesEditor().getFontSize()));
        aContent.add(lblPersonalNamespace, getConstraints(0, 0, 1, 1));
        txtPersonalNamespace = new JTextField(20);
        txtPersonalNamespace.setFont(txtPersonalNamespace.getFont().deriveFont((float) ModelManager.getModelManager().getGeneralLookPreferencesEditor().getFontSize()));
        lblPersonalNamespace.setLabelFor(txtPersonalNamespace);
        txtPersonalNamespace.setText(ModelManager.getModelManager().getMetaDeskConfiguration().getPersonalNamespace());
        aContent.add(txtPersonalNamespace, getConstraints(0, 1, 2, 1));
    }

    private void addModelFileField(JPanel content) {
        lblModel = new JLabel("Model File (or URL)");
        lblModel.setToolTipText("Model File (e.g. c:\\model.rdf or http://bigbear.isi.edu/webdav/model.rdf)");
        lblModel.setFont(lblModel.getFont().deriveFont((float) ModelManager.getModelManager().getGeneralLookPreferencesEditor().getFontSize()));
        content.add(lblModel, getConstraints(1, 0, 2, 1));
        txtModel = new JTextField(20);
        txtModel.setFont(txtModel.getFont().deriveFont((float) ModelManager.getModelManager().getGeneralLookPreferencesEditor().getFontSize()));
        String modelFileName = ModelManager.getModelManager().getMetaDeskConfiguration().getModelFileName();
        if (modelFileName == null) modelFileName = ModelManager.getModelManager().getMetaDeskConfiguration().getModelURL();
        txtModel.setText(modelFileName);
        lblModel.setLabelFor(txtModel);
        content.add(txtModel, getConstraints(2, 0, 2, 1));
        btnBrowse = new JButton();
        btnBrowse.setFont(btnBrowse.getFont().deriveFont((float) ModelManager.getModelManager().getGeneralLookPreferencesEditor().getFontSize()));
        btnBrowse.setMnemonic('B');
        btnBrowse.setText("Browse");
        btnBrowse.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                browseForModelFile();
            }
        });
        content.add(btnBrowse, getConstraints(2, 2, 1, 1));
    }

    private void addAutoSaveTimeField(JPanel content) {
        lblAutoSave = new JLabel("Auto Save Time (minutes)");
        lblAutoSave.setToolTipText("Interval after which, model should be saved");
        lblAutoSave.setFont(lblAutoSave.getFont().deriveFont((float) ModelManager.getModelManager().getGeneralLookPreferencesEditor().getFontSize()));
        content.add(lblAutoSave, getConstraints(3, 0, 1, 1));
        txtAutoSave = new JTextField(5);
        txtAutoSave.setFont(txtAutoSave.getFont().deriveFont((float) ModelManager.getModelManager().getGeneralLookPreferencesEditor().getFontSize()));
        String autoSaveTime = String.valueOf(ModelManager.getModelManager().getMetaDeskConfiguration().getAutoSaveTime());
        if (autoSaveTime == null) autoSaveTime = "0";
        txtAutoSave.setText(autoSaveTime);
        lblAutoSave.setLabelFor(txtAutoSave);
        content.add(txtAutoSave, getConstraints(3, 1, 1, 1));
    }

    private void browseForModelFile() {
        JFileChooser fc = SwingUtil.getFileChooser();
        ExampleFileFilter filter = new ExampleFileFilter();
        filter.addExtension("rdf");
        filter.setDescription("RDF File(s)");
        fc.setFileFilter(filter);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String filename = file.getAbsolutePath();
            if (!filename.equals(txtModel.getText())) {
                txtModel.setText(filename);
            }
        }
    }

    private void matchGuiToStoredPrefs() {
    }

    private GridBagConstraints getConstraints(int aY, int aX) {
        GridBagConstraints result = SwingUtil.getConstraints(aY, aX);
        addBottom(result);
        return result;
    }

    private GridBagConstraints getConstraints(int aY, int aX, int aWidth, int aHeight) {
        GridBagConstraints result = SwingUtil.getConstraints(aY, aX, aWidth, aHeight);
        addBottom(result);
        return result;
    }

    private void addBottom(GridBagConstraints aConstraints) {
        aConstraints.insets = new Insets(0, 0, SwingUtil.ONE_SPACE, SwingUtil.ONE_SPACE);
    }
}
