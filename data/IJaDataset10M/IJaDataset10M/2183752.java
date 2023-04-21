package net.sf.poormans.gui.dialog.pojo;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

/**
 * ModifyListener to clear the error messages of the pojo dialog.
 *
 * @see DialogCreator#clearErrorMessage()
 * @version $Id: ModifyListenerClearErrorMessages.java 2017 2010-08-30 16:33:49Z th-schwarz $
 * @author <a href="mailto:th-schwarz@users.sourceforge.net">Thilo Schwarz</a>
 */
public class ModifyListenerClearErrorMessages implements ModifyListener {

    private DialogCreator dialogCreator;

    protected ModifyListenerClearErrorMessages(DialogCreator dialogCreator) {
        this.dialogCreator = dialogCreator;
    }

    @Override
    public void modifyText(ModifyEvent e) {
        dialogCreator.clearErrorMessage();
    }
}
