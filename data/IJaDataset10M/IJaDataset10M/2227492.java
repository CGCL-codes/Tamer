package fr.vtt.gattieres.gcs.gui;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.io.File;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import fr.vtt.gattieres.gcs.gui.common.CommonDialog;
import fr.vtt.gattieres.gcs.gui.common.ContentEvent;
import fr.vtt.gattieres.gcs.gui.common.ContentListener;
import fr.vtt.gattieres.gcs.gui.common.ExtentionFileFilter;
import fr.vtt.gattieres.gcs.resources.Resource;
import fr.vtt.gattieres.gcs.resources.ResourceManager;
import fr.vtt.gattieres.gcs.resources.UserPreferencesResource;

public class PrintDialog extends CommonDialog {

    private PrintPanel mailPanel;

    protected static ResourceManager s_resMgr = ResourceManager.getInstance();

    protected static Class s_ResCl = Resource.class;

    private JFileChooser fChooser;

    private File saveDestinatonFile;

    public PrintDialog(Frame owner) throws HeadlessException {
        super(owner, s_resMgr.getString(s_ResCl, Resource.KEY_ACTION_PRINT_LABEL), true);
        initUI();
    }

    private void initUI() {
        Preferences pref = resMgr.getLocalUserPreferences();
        String storedPath = pref.get(UserPreferencesResource.EXPORT_PATH_PREFERENCE, null);
        fChooser = new JFileChooser();
        if (storedPath != null) {
            fChooser.setCurrentDirectory(new File(storedPath));
        }
        mailPanel = new PrintPanel();
        mailPanel.addContentListener(new ContentListener() {

            public void contentChanged(ContentEvent evt) {
                PrintDialog.super.fireContentChanged(evt.getSource());
            }
        });
        this.add(mailPanel);
        this.pack();
        this.setResizable(false);
        super.fireContentChanged(this);
    }

    public String getSelectedDestination() {
        return this.mailPanel.getSelection();
    }

    @Override
    protected boolean validationPerform() {
        if (Resource.KEY_LABEL_PRINT_HTML_DESTINATION_MSG.equals(this.getSelectedDestination())) {
            String extention = resMgr.getString(s_ResCl, Resource.KEY_PRINT_HTML_EXTENTION_LABEL);
            fChooser.setDialogTitle(resMgr.getString(s_ResCl, Resource.KEY_PRINT_HTML_CHALLENGER_TITLE_LABEL));
            fChooser.setFileFilter(new ExtentionFileFilter(extention, resMgr.getString(s_ResCl, Resource.KEY_PRINT_HTML_DESCRIPTION_LABEL)));
            int returnVal = fChooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                this.saveDestinatonFile = fChooser.getSelectedFile();
                if (!this.saveDestinatonFile.getName().endsWith(extention)) {
                    this.saveDestinatonFile = new File(this.saveDestinatonFile.getParentFile(), this.saveDestinatonFile.getName() + "." + extention);
                }
            } else {
                this.saveDestinatonFile = null;
                return false;
            }
        } else if (Resource.KEY_LABEL_PRINT_CSV_DESTINATION_MSG.equals(this.getSelectedDestination())) {
            String extention = resMgr.getString(s_ResCl, Resource.KEY_PRINT_CSV_EXTENTION_LABEL);
            fChooser.setDialogTitle(resMgr.getString(s_ResCl, Resource.KEY_PRINT_CSV_CHALLENGER_TITLE_LABEL));
            fChooser.setFileFilter(new ExtentionFileFilter(extention, resMgr.getString(s_ResCl, Resource.KEY_PRINT_CSV_DESCRIPTION_LABEL)));
            int returnVal = fChooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                this.saveDestinatonFile = fChooser.getSelectedFile();
                if (!this.saveDestinatonFile.getName().endsWith(extention)) {
                    this.saveDestinatonFile = new File(this.saveDestinatonFile.getParentFile(), this.saveDestinatonFile.getName() + "." + extention);
                }
            } else {
                this.saveDestinatonFile = null;
                return false;
            }
        } else {
            this.saveDestinatonFile = null;
        }
        if (this.saveDestinatonFile != null) {
            Preferences pref = resMgr.getLocalUserPreferences();
            pref.put(UserPreferencesResource.EXPORT_PATH_PREFERENCE, this.saveDestinatonFile.getPath());
        }
        return true;
    }

    @Override
    public boolean isValid() {
        return mailPanel.isDataValid();
    }

    @Override
    public void clean() {
        mailPanel.clean();
    }

    public File getSaveDestinationFile() {
        return saveDestinatonFile;
    }
}
