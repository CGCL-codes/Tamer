package de.icehorsetools.iceoffice.workflow.financeinvoice;

import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.ugat.dataAccess.UnitOfWork;
import org.ugat.wiser.dialog.AUnDialog.ButtonType;
import org.ugat.wiser.dialog.MessageBoxHandler.MessageBoxType;
import org.ugat.wiser.language.Lang;
import org.ugat.wiser.workflow.implementations.ADefaultMessageBoxWorkflow;
import de.icehorsetools.dataAccess.objects.Financeinvoice;

public class FinanceinvoiceDeleteWf extends ADefaultMessageBoxWorkflow {

    private static final Logger LOG = Logger.getLogger(FinanceinvoiceDeleteWf.class);

    private MessageBoxType messageBoxType = MessageBoxType.YES_NO;

    @Override
    public boolean areObjectsValidForWorkflow(List xObjects, UnitOfWork xCurrentUnitOfWork) {
        return xObjects != null && xObjects.size() == 1 && xObjects.get(0) instanceof Financeinvoice;
    }

    @Override
    public boolean work() {
        String title = Lang.get(this.getClass(), "title");
        Object o = this.getData();
        String messageFormatPattern;
        this.setTitle(title);
        messageBoxType = MessageBoxType.YES_NO;
        messageFormatPattern = Lang.get(this.getClass(), "messagePatternDelete");
        setBoxtype(messageBoxType);
        this.setMsg(MessageFormat.format(messageFormatPattern, new Object[] { 1 }));
        return true;
    }

    @Override
    public boolean work(ButtonType xButtonType) {
        boolean result;
        switch(xButtonType) {
            case BUTTON_OK:
                if (messageBoxType != MessageBoxType.OK) {
                    LOG.error("Received button result OK for messageBoxType != OK");
                }
                result = false;
                this.cancel();
                break;
            case BUTTON_NO:
            case BUTTON_YES:
                if (messageBoxType != MessageBoxType.YES_NO) {
                    LOG.error("Received button result YES/NO for messageBoxType != YES_NO");
                }
                result = (xButtonType == ButtonType.BUTTON_YES);
                break;
            default:
                LOG.warn("Ignoring unexpected button type result: " + xButtonType);
                result = false;
                break;
        }
        return result;
    }

    @Override
    public boolean preSave() {
        markDataAsDeleted();
        return true;
    }

    @Override
    public boolean preClose() {
        getOrigin().getState().refresh();
        return true;
    }
}
