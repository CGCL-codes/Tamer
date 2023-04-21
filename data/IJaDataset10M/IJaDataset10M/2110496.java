package org.plazmaforge.bsolution.base.client.swt.actions;

import org.plazmaforge.bsolution.base.client.swt.dialogs.TransferTypeDialog;
import org.plazmaforge.framework.client.swt.action.SWTProcessAction;
import org.plazmaforge.framework.client.swt.dialogs.FormatSelectionDialog;
import org.plazmaforge.framework.core.FileFormat;
import org.plazmaforge.framework.core.FileFormatFactory;
import org.plazmaforge.framework.core.exception.ApplicationException;
import org.plazmaforge.framework.datawarehouse.TransferEnvironment;
import org.plazmaforge.framework.datawarehouse.ITransferService;

/**
 * 
 * @author ohapon
 * $Id: AbstractConvertAction.java,v 1.1 2010/05/19 13:52:32 ohapon Exp $
 */
public abstract class AbstractConvertAction extends SWTProcessAction {

    /**
     * True if custom mode
     */
    private boolean customMode;

    /**
     * Return format
     * @return
     */
    protected abstract String getFormat();

    /**
     * Return true if the action supports custom mode
     * @return
     */
    protected boolean isSupportCustomMode() {
        return true;
    }

    /**
     * Return custom <code>ITransferService</code>
     * @return
     * @throws ApplicationException
     */
    protected ITransferService getCustomTransferService() throws ApplicationException {
        return TransferEnvironment.getCustomTransferService();
    }

    /**
     * Return selection format
     * @param formats
     * @return
     */
    protected String getSelectionFormat(String[] formats) {
        FileFormat[] formatObjects = FileFormatFactory.getFormats(formats);
        if (formatObjects == null | formatObjects.length == 0) {
            return null;
        }
        FormatSelectionDialog dialog = new FormatSelectionDialog(getShell());
        dialog.setElements(formatObjects);
        dialog.open();
        Object result[] = dialog.getResult();
        if (result == null || result.length == 0) {
            return null;
        }
        String format = ((FileFormat) result[0]).getId();
        if (format == null) {
            return null;
        }
        return getNormalizeFormat(format);
    }

    protected String getNormalizeFormat(String format) {
        return format;
    }

    /**
     * Return true if selection custom mode
     * @return
     */
    protected boolean getSelectionCustomMode(int type) {
        return type == TransferTypeDialog.CUSTOM_TYPE;
    }

    protected int getSelectionTransferType() {
        TransferTypeDialog dialog = new TransferTypeDialog(getShell());
        dialog.open();
        return dialog.getSelectionType();
    }

    public boolean isCustomMode() {
        return customMode;
    }

    public void setCustomMode(boolean customMode) {
        this.customMode = customMode;
    }
}
