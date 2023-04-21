package org.qfirst.batavia.actions.file;

import org.qfirst.batavia.service.transfer.*;
import org.qfirst.batavia.*;
import org.qfirst.batavia.utils.*;
import org.qfirst.batavia.ui.*;
import java.util.*;
import org.qfirst.vfs.*;
import javax.swing.*;
import org.qfirst.i18n.*;

/**
 *  Description of the Class
 *
 * @author     francisdobi
 * @created    May 1, 2004
 */
class CopyTransferRunner extends TransferRunner implements TransferInputHandler {

    /**
	 *  Constructor for the CopyTransferRunner object
	 */
    public CopyTransferRunner(TransferAction ta, List sources, AbstractFile dst) {
        super(ta, sources, dst);
    }

    /**
	 *  Main processing method for the CopyTransferRunner object
	 */
    public void run() {
        processor.setInputHandler(this);
        super.run();
    }

    /**
	 *  Should overwrite the file
	 *
	 * @param  source               Description of the Parameter
	 * @param  alreadyExistingFile  Description of the Parameter
	 * @return                      Description of the Return Value
	 */
    public int shouldOverWrite(AbstractFile source, AbstractFile alreadyExistingFile) {
        Object[] options = { Message.getInstance().getMessage(Batavia.getLocale(), "button.overwrite", "Overwrite"), Message.getInstance().getMessage(Batavia.getLocale(), "button.skip", "Skip"), Message.getInstance().getMessage(Batavia.getLocale(), "button.all", "All"), Message.getInstance().getMessage(Batavia.getLocale(), "button.none", "None"), Message.getInstance().getMessage(Batavia.getLocale(), "button.if_source_newer", "If source file is newer"), Message.getInstance().getMessage(Batavia.getLocale(), "button.abort", "Abort") };
        int code[] = { TransferInputHandler.YES, TransferInputHandler.NO, TransferInputHandler.ALL, TransferInputHandler.NONE, TransferInputHandler.NEWER, TransferInputHandler.ABORT };
        int answer = JOptionPane.showOptionDialog(Batavia.getCurrentCommanderWindow(), Message.getInstance().getMessage(Batavia.getLocale(), "dialog.overwrite_this_file_with", "Overwrite this file {0} with {1}", new Object[] { alreadyExistingFile.getPath(), source.getPath() }), Message.getInstance().getMessage(Batavia.getLocale(), "title.warning", "Warning"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (answer == JOptionPane.CLOSED_OPTION) {
            return NO;
        } else {
            return code[answer];
        }
    }

    /**
	 *  Should continue the transfer if error
	 *
	 * @param  source       Description of the Parameter
	 * @param  destination  Description of the Parameter
	 * @param  t            Description of the Parameter
	 * @return              TransferInputHandler.YES or TransferInputHandler.NO
	 */
    public int shouldContinue(AbstractFile source, AbstractFile destination, Throwable t) {
        logger.error(t, t);
        Object[] options = { Message.getInstance().getMessage(Batavia.getLocale(), "button.retry", "Retry"), Message.getInstance().getMessage(Batavia.getLocale(), "button.skip", "Skip"), Message.getInstance().getMessage(Batavia.getLocale(), "button.abort", "Abort") };
        int codes[] = { RETRY, YES, NO };
        Object selectedValue = UIUtils.showErrorDialog(Batavia.getCurrentCommanderWindow(), t, Message.getInstance().getMessage(Batavia.getLocale(), "dialog.could_not_copy_file", "Could not copy this file: {0} to {1}", new Object[] { source.getPath(), destination.getPath() }), Message.getInstance().getMessage(Batavia.getLocale(), "title.error_copying", "ERROR copying files"), null, options, options[0]);
        if (selectedValue == null) {
            return YES;
        } else {
            for (int i = 0; i < options.length; i++) {
                if (options[i].equals(selectedValue)) {
                    return codes[i];
                }
            }
            return YES;
        }
    }
}
