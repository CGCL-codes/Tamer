package editor.view.action;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import base.exception.ExceptionHandler;
import editor.EditorSettings;
import editor.model.xml.TagMapper;
import editor.util.FileUtil;
import editor.util.Resources;
import editor.util.XmlFileFilter;
import editor.view.GraphicFrame;

/**
 * Saves the datasheet by the given file name from user
 */
public final class SaveSheetAsAction extends AbstractLocaleAction {

    private static final String ACTION_NAME = "action.save_sheet_as";

    private final GraphicFrame oFrame;

    public SaveSheetAsAction(final GraphicFrame frame) {
        super(ACTION_NAME);
        oFrame = frame;
        setEnabled(false);
    }

    /**
     * Open a file save dialog to save the datasheet file or shows a message dialog if an error occurs
     *
     * @param e ActionEvent
     */
    @Override
    public final void actionPerformed(final ActionEvent e) {
        try {
            JFileChooser chooser = new JFileChooser(EditorSettings.getInstance().getLastUsedDirectory()) {

                @Override
                public void approveSelection() {
                    File f = getSelectedFile();
                    if (f.getAbsolutePath().lastIndexOf(".") <= 0) {
                        f = new File(f.getAbsolutePath() + "." + Resources.getText("file_filter.xml.extension"));
                    }
                    if (f.exists() && getDialogType() == SAVE_DIALOG) {
                        int result = JOptionPane.showConfirmDialog(this, Resources.getText(ACTION_NAME + ".confirm.message"), Resources.getText("warning.dialog.title"), JOptionPane.YES_NO_CANCEL_OPTION);
                        switch(result) {
                            case JOptionPane.YES_OPTION:
                                super.approveSelection();
                                return;
                            case JOptionPane.NO_OPTION:
                                return;
                            case JOptionPane.CANCEL_OPTION:
                                super.cancelSelection();
                                return;
                        }
                    }
                    super.approveSelection();
                }
            };
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setFileFilter(new XmlFileFilter());
            int returnVal = chooser.showSaveDialog(oFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File propFile = chooser.getSelectedFile();
                if (propFile.getAbsolutePath().lastIndexOf(".") <= 0) {
                    propFile = new File(propFile.getAbsolutePath() + "." + Resources.getText("file_filter.xml.extension"));
                }
                oFrame.getModel().setCurrentFile(propFile);
                oFrame.getView().saveSheet();
                File dtd = new File(propFile.getParent() + System.getProperty("file.separator") + TagMapper.DTD_FILE);
                if (!dtd.exists()) {
                    FileUtil.copy(new File("." + System.getProperty("file.separator") + "config" + System.getProperty("file.separator") + TagMapper.DTD_FILE), dtd);
                }
            }
            EditorSettings.getInstance().setLastUsedDirectory(chooser.getCurrentDirectory().getAbsolutePath());
            EditorSettings.getInstance().save();
            oFrame.updateView();
        } catch (Exception ex) {
            ExceptionHandler.ERRORHANDLER.handleException(ex);
        }
    }
}
