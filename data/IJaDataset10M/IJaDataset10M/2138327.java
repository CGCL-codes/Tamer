package abbot.swt.eclipse.util;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;

@SuppressWarnings("restriction")
public class DialogUtil {

    public static String buttonIdString(int buttonId) {
        String string = buttonIdLabel(buttonId);
        if (string != null) return "<\"" + string + "\">";
        switch(buttonId) {
            default:
                if (buttonId >= IDialogConstants.CLIENT_ID) return "<client " + buttonId + ">";
                if (buttonId >= IDialogConstants.INTERNAL_ID) return "<internal " + buttonId + ">";
                return "<" + buttonId + ">";
        }
    }

    public static String buttonIdLabel(int buttonId) {
        switch(buttonId) {
            case IDialogConstants.OK_ID:
                return IDialogConstants.OK_LABEL;
            case IDialogConstants.CANCEL_ID:
                return IDialogConstants.CANCEL_LABEL;
            case IDialogConstants.YES_ID:
                return IDialogConstants.YES_LABEL;
            case IDialogConstants.NO_ID:
                return IDialogConstants.NO_LABEL;
            case IDialogConstants.YES_TO_ALL_ID:
                return IDialogConstants.YES_TO_ALL_LABEL;
            case IDialogConstants.SKIP_ID:
                return IDialogConstants.SKIP_LABEL;
            case IDialogConstants.STOP_ID:
                return IDialogConstants.STOP_LABEL;
            case IDialogConstants.ABORT_ID:
                return IDialogConstants.ABORT_LABEL;
            case IDialogConstants.RETRY_ID:
                return IDialogConstants.RETRY_LABEL;
            case IDialogConstants.IGNORE_ID:
                return IDialogConstants.IGNORE_LABEL;
            case IDialogConstants.PROCEED_ID:
                return IDialogConstants.PROCEED_LABEL;
            case IDialogConstants.OPEN_ID:
                return IDialogConstants.OPEN_LABEL;
            case IDialogConstants.CLOSE_ID:
                return IDialogConstants.CLOSE_LABEL;
            case IDialogConstants.DETAILS_ID:
                return IDialogConstants.SHOW_DETAILS_LABEL;
            case IDialogConstants.BACK_ID:
                return IDialogConstants.BACK_LABEL;
            case IDialogConstants.NEXT_ID:
                return IDialogConstants.NEXT_LABEL;
            case IDialogConstants.FINISH_ID:
                return IDialogConstants.FINISH_LABEL;
            case IDialogConstants.HELP_ID:
                return IDialogConstants.HELP_LABEL;
            case IDialogConstants.NO_TO_ALL_ID:
                return IDialogConstants.NO_TO_ALL_LABEL;
            case IDialogConstants.SELECT_ALL_ID:
                return WorkbenchMessages.SelectionDialog_selectLabel;
            case IDialogConstants.DESELECT_ALL_ID:
                return WorkbenchMessages.SelectionDialog_deselectLabel;
            case IDialogConstants.SELECT_TYPES_ID:
                return IDEWorkbenchMessages.WizardTransferPage_selectTypes;
            default:
                return null;
        }
    }
}
