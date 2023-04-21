package org.jaffa.components.audit.components.audittransactionviewer.ui;

import java.util.*;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.jaffa.presentation.portlet.ActionBase;
import org.jaffa.presentation.portlet.FormKey;
import org.jaffa.presentation.portlet.HistoryNav;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.ApplicationException;
import org.jaffa.presentation.portlet.widgets.model.GridModel;
import org.jaffa.presentation.portlet.widgets.model.GridModelRow;
import org.jaffa.components.audit.components.audittransactionviewer.dto.AuditTransactionViewerOutDto;

/** The Action class for handling events related to the View screen.
 */
public class AuditTransactionViewerAction extends ActionBase {

    private static final Logger log = Logger.getLogger(AuditTransactionViewerAction.class);

    /** Invokes the do_RelatedAuditTransactionObject_View_Clicked() method.
     * @param rowNum The selected row on the Grid.
     * @return The FormKey for the View screen of the AuditTransactionObject object.
     */
    public FormKey do_RelatedAuditTransactionObject_Clicked(String rowNum) {
        return do_RelatedAuditTransactionObject_View_Clicked(rowNum);
    }

    /** Invokes the viewAuditTransactionObject() method on the component.
     * @param rowNum The selected row on the Grid.
     * @return The FormKey for the View screen of the AuditTransactionObject object.
     */
    public FormKey do_RelatedAuditTransactionObject_View_Clicked(String rowNum) {
        FormKey fk = null;
        AuditTransactionViewerForm myForm = (AuditTransactionViewerForm) form;
        AuditTransactionViewerComponent myComp = (AuditTransactionViewerComponent) myForm.getComponent();
        GridModel model = (GridModel) myForm.getRelatedAuditTransactionObjectWM();
        GridModelRow selectedRow = model.getRow(Integer.parseInt(rowNum));
        if (selectedRow != null) {
            try {
                fk = myComp.viewAuditTransactionObject((java.lang.String) selectedRow.get("transactionId"), (java.lang.String) selectedRow.get("auditObjectId"));
            } catch (ApplicationExceptions e) {
                if (log.isDebugEnabled()) log.debug("Viewer Failed");
                myForm.raiseError(request, ActionMessages.GLOBAL_MESSAGE, e);
            } catch (FrameworkException e) {
                log.error(null, e);
                myForm.raiseError(request, ActionMessages.GLOBAL_MESSAGE, "error.framework.general");
            }
        }
        HistoryNav.initializeHistoryNav(request);
        if (fk == null) fk = new FormKey(myForm.NAME, myComp.getComponentId());
        return fk;
    }

    /** Quits the component and returns the FormKey for the calling screen.
     * @return The FormKey for the caling screen. A null will be returned, if no calling screen was specified.
     */
    public FormKey do_Close_Clicked() {
        return ((AuditTransactionViewerForm) form).getComponent().quitAndReturnToCallingScreen();
    }

    /** Invokes the deleteObject() method on the component.
     * @return The FormKey for the Delete screen.
     */
    public FormKey do_Delete_Clicked() {
        FormKey fk = null;
        AuditTransactionViewerForm myForm = (AuditTransactionViewerForm) form;
        AuditTransactionViewerComponent myComp = (AuditTransactionViewerComponent) myForm.getComponent();
        try {
            fk = myComp.deleteObject();
        } catch (ApplicationExceptions e) {
            if (log.isDebugEnabled()) log.debug("Delete Failed");
            myForm.raiseError(request, ActionMessages.GLOBAL_MESSAGE, e);
        } catch (FrameworkException e) {
            log.error(null, e);
            myForm.raiseError(request, ActionMessages.GLOBAL_MESSAGE, "error.framework.general");
        }
        if (fk == null) fk = myComp.getViewerFormKey();
        return fk;
    }
}
