package org.qfirst.batavia.actions.file;

import org.qfirst.batavia.actions.*;
import javax.swing.*;
import java.awt.event.*;
import org.qfirst.batavia.*;
import org.qfirst.batavia.ui.*;
import org.qfirst.batavia.model.*;
import org.qfirst.vfs.*;
import org.qfirst.batavia.utils.*;
import org.qfirst.i18n.*;
import java.io.*;
import org.qfirst.batavia.misc.*;
import org.qfirst.options.*;

/**
 *  Change directory action. Changes to the directory under the bar. If the
 *  AbstractFile is neither a directory nor (supported) archived file, nothing
 *  happens.
 *
 * @author     francisdobi
 * @created    May 27, 2004
 */
public class ChangeDir extends BataviaAction {

    /**
	 *  The instance of this singleton.
	 */
    private static final BataviaAction instance = new ChangeDir();

    /**
	* Returns the value of instance.
	* @return the instance value.
	*/
    public static BataviaAction getInstance() {
        return instance;
    }

    /**
	 *  Constructor for the ChangeDir.
	 */
    private ChangeDir() {
    }

    /**
	 *  called by super's actionPerformed method.
	 *
	 * @param  evt  ActionEvent object
	 */
    public final void perform(ActionEvent evt) {
        DirectoryPane pane = Batavia.getCurrentCommanderWindow().getCurrentDirectoryPane();
        CommonDirectoryViewModel cmodel = pane.getModel();
        if (!(cmodel instanceof FileTableModel)) {
            return;
        }
        FileTableModel model = (FileTableModel) pane.getModel();
        int pos = model.getBarPosition();
        AbstractFile file = model.getFileAt(pos);
        try {
            if (!file.isDirectory() && !VFS.isSupportedArchive(file)) {
                exec(pane, model, file);
            } else {
                enter(model, pos, file);
            }
        } catch (FileSystemException ex) {
            UIUtils.showErrorDialog(Batavia.getCurrentCommanderWindow(), ex, Message.getInstance().getMessage(Batavia.getLocale(), "dialog.error", "Error"));
        }
    }

    /**
	 *  change directory
	 *
	 * @param  model  the model
	 * @param  path   the path to change the model to
	 */
    public void execute(FileTableModel model, String path) {
        try {
        } catch (Exception ex) {
            logger.error(ex, ex);
            UIUtils.showErrorDialog(Batavia.getCurrentCommanderWindow(), ex, Message.getInstance().getMessage(Batavia.getLocale(), "dialog.cannot_change_dir", "Cannot change directory to {0}", new Object[] { path }));
        }
    }

    /**
	 *  Description of the Method
	 *
	 * @exception  HeadlessException
	 * @exception  MalformedPathException
	 */
    private void exec(DirectoryPane pane, FileTableModel model, AbstractFile file) throws MalformedPathException {
        if (model.getCurrentDirectory().isLocal()) {
            try {
                Batavia.launchFile(file);
            } catch (Exception ioe) {
                UIUtils.showErrorDialog(pane, ioe, "Cannot execute");
            }
        } else {
            JOptionPane.showMessageDialog(pane, "Cannot exec on non local filesystems");
        }
    }

    /**
	 *  Description of the Method
	 *
	 */
    private void enter(FileTableModel model, int pos, AbstractFile file) {
        try {
            String oldName = model.getCurrentDirectory().getName();
            AbstractFile old = model.getCurrentDirectory();
            if (!file.isDirectory()) {
                if (pos != 0) {
                    file = VFS.enterArchive(file);
                } else {
                    file = file.getParent();
                }
            }
            model.setCurrentDirectory(file);
            if (pos == 0) {
                if (oldName.length() == 0 && old.getContainer() != null) {
                    oldName = old.getContainer().getName();
                }
                model.moveBarTo(oldName);
            }
        } catch (FileSystemException ex) {
            UIUtils.showErrorDialog(Batavia.getCurrentCommanderWindow(), ex, Message.getInstance().getMessage(Batavia.getLocale(), "dialog.cannot_change_dir", "Cannot change directory to {0}", new Object[] { file.getPath() }));
        }
    }

    protected Option getShortcutOption() {
        return Batavia.getOptionManager().getOption(Constants.OPT_KEY_CHDIR);
    }
}
