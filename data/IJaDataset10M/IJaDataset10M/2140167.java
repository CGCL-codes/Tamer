package org.jaffa.modules.printing.components.printerdefinitionlookup.ui;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.jaffa.presentation.portlet.ActionBase;
import org.jaffa.presentation.portlet.FormKey;
import org.jaffa.presentation.portlet.HistoryNav;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.ApplicationException;
import org.jaffa.components.finder.*;
import org.jaffa.components.lookup.*;
import org.jaffa.presentation.portlet.widgets.model.GridModel;
import org.jaffa.presentation.portlet.widgets.model.GridModelRow;
import org.jaffa.modules.printing.components.printerdefinitionlookup.dto.PrinterDefinitionLookupInDto;

/** The Action class for handling events related to PrinterDefinitionLookup.
 */
public class PrinterDefinitionLookupAction extends LookupAction {

    private static final Logger log = Logger.getLogger(PrinterDefinitionLookupAction.class);

    /** Invokes the createFromCriteria() method on the component.
     * @return The FormKey for the Create screen.
     */
    public FormKey do_CreateFromCriteria_Clicked() {
        FormKey fk = null;
        PrinterDefinitionLookupForm myForm = (PrinterDefinitionLookupForm) form;
        PrinterDefinitionLookupComponent myComp = (PrinterDefinitionLookupComponent) myForm.getComponent();
        if (myForm.doValidate(request)) {
            try {
                fk = myComp.createFromCriteria();
            } catch (ApplicationExceptions e) {
                if (log.isDebugEnabled()) log.debug("Create Failed");
                myForm.raiseError(request, ActionMessages.GLOBAL_MESSAGE, e);
            } catch (FrameworkException e) {
                log.error(null, e);
                myForm.raiseError(request, ActionMessages.GLOBAL_MESSAGE, "error.framework.general");
            }
        }
        if (fk == null) fk = myComp.getCriteriaFormKey();
        return fk;
    }

    /** Invokes the createFromResults() method on the component.
     * @return The FormKey for the Create screen.
     */
    public FormKey do_CreateFromResults_Clicked() {
        FormKey fk = null;
        PrinterDefinitionLookupForm myForm = (PrinterDefinitionLookupForm) form;
        PrinterDefinitionLookupComponent myComp = (PrinterDefinitionLookupComponent) myForm.getComponent();
        try {
            fk = myComp.createFromResults();
        } catch (ApplicationExceptions e) {
            if (log.isDebugEnabled()) log.debug("Create Failed");
            myForm.raiseError(request, ActionMessages.GLOBAL_MESSAGE, e);
        } catch (FrameworkException e) {
            log.error(null, e);
            myForm.raiseError(request, ActionMessages.GLOBAL_MESSAGE, "error.framework.general");
        }
        if (fk == null) fk = myComp.getResultsFormKey();
        return fk;
    }

    /** Invokes the viewObject() method on the component.
     * @param rowNum The selected row on the Results screen.
     * @return The FormKey for the View screen.
     */
    public FormKey do_Rows_View_Clicked(String rowNum) {
        FormKey fk = null;
        PrinterDefinitionLookupForm myForm = (PrinterDefinitionLookupForm) form;
        PrinterDefinitionLookupComponent myComp = (PrinterDefinitionLookupComponent) myForm.getComponent();
        GridModel model = (GridModel) myForm.getRowsWM();
        GridModelRow selectedRow = model.getRow(Integer.parseInt(rowNum));
        if (selectedRow != null) {
            try {
                fk = myComp.viewObject((java.lang.String) selectedRow.get("printerId"));
            } catch (ApplicationExceptions e) {
                if (log.isDebugEnabled()) log.debug("Viewer Failed");
                myForm.raiseError(request, ActionMessages.GLOBAL_MESSAGE, e);
            } catch (FrameworkException e) {
                log.error(null, e);
                myForm.raiseError(request, ActionMessages.GLOBAL_MESSAGE, "error.framework.general");
            }
        }
        HistoryNav.initializeHistoryNav(request);
        if (fk == null) fk = myComp.getResultsFormKey();
        return fk;
    }

    /** Invokes the updateObject() method on the component.
     * @param rowNum The selected row on the Results screen.
     * @return The FormKey for the Update screen.
     */
    public FormKey do_Rows_Update_Clicked(String rowNum) {
        FormKey fk = null;
        PrinterDefinitionLookupForm myForm = (PrinterDefinitionLookupForm) form;
        PrinterDefinitionLookupComponent myComp = (PrinterDefinitionLookupComponent) myForm.getComponent();
        GridModel model = (GridModel) myForm.getRowsWM();
        GridModelRow selectedRow = model.getRow(Integer.parseInt(rowNum));
        if (selectedRow != null) {
            try {
                fk = myComp.updateObject((java.lang.String) selectedRow.get("printerId"));
            } catch (ApplicationExceptions e) {
                if (log.isDebugEnabled()) log.debug("Update Failed");
                myForm.raiseError(request, ActionMessages.GLOBAL_MESSAGE, e);
            } catch (FrameworkException e) {
                log.error(null, e);
                myForm.raiseError(request, ActionMessages.GLOBAL_MESSAGE, "error.framework.general");
            }
        }
        if (fk == null) fk = myComp.getResultsFormKey();
        return fk;
    }

    /** Invokes the deleteObject() method on the component.
     * @param rowNum The selected row on the Results screen.
     * @return The FormKey for the Delete screen.
     */
    public FormKey do_Rows_Delete_Clicked(String rowNum) {
        FormKey fk = null;
        PrinterDefinitionLookupForm myForm = (PrinterDefinitionLookupForm) form;
        PrinterDefinitionLookupComponent myComp = (PrinterDefinitionLookupComponent) myForm.getComponent();
        try {
            performTokenValidation(request);
            GridModel model = (GridModel) myForm.getRowsWM();
            GridModelRow selectedRow = model.getRow(Integer.parseInt(rowNum));
            if (selectedRow != null) {
                fk = myComp.deleteObject((java.lang.String) selectedRow.get("printerId"));
            }
        } catch (ApplicationExceptions e) {
            if (log.isDebugEnabled()) log.debug("Delete Failed");
            myForm.raiseError(request, ActionMessages.GLOBAL_MESSAGE, e);
        } catch (FrameworkException e) {
            log.error(null, e);
            myForm.raiseError(request, ActionMessages.GLOBAL_MESSAGE, "error.framework.general");
        }
        if (fk == null) fk = myComp.getResultsFormKey();
        return fk;
    }
}
