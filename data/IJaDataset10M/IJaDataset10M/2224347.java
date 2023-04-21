package org.jaffa.modules.printing.components.formtemplateviewer.ui;

import java.util.*;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.jaffa.presentation.portlet.ActionBase;
import org.jaffa.presentation.portlet.FormKey;
import org.jaffa.presentation.portlet.HistoryNav;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.ApplicationException;
import org.jaffa.modules.printing.components.formtemplateviewer.dto.FormTemplateViewerOutDto;

/** The Action class for handling events related to the View screen.
 */
public class FormTemplateViewerAction extends ActionBase {

    private static final Logger log = Logger.getLogger(FormTemplateViewerAction.class);

    /** Quits the component and returns the FormKey for the calling screen.
     * @return The FormKey for the caling screen. A null will be returned, if no calling screen was specified.
     */
    public FormKey do_Close_Clicked() {
        return ((FormTemplateViewerForm) form).getComponent().quitAndReturnToCallingScreen();
    }

    /** Invokes the updateObject() method on the component.
     * @return The FormKey for the Update screen.
     */
    public FormKey do_Update_Clicked() {
        FormKey fk = null;
        FormTemplateViewerForm myForm = (FormTemplateViewerForm) form;
        FormTemplateViewerComponent myComp = (FormTemplateViewerComponent) myForm.getComponent();
        try {
            fk = myComp.updateObject();
        } catch (ApplicationExceptions e) {
            if (log.isDebugEnabled()) log.debug("Update Failed");
            myForm.raiseError(request, ActionMessages.GLOBAL_MESSAGE, e);
        } catch (FrameworkException e) {
            log.error(null, e);
            myForm.raiseError(request, ActionMessages.GLOBAL_MESSAGE, "error.framework.general");
        }
        if (fk == null) fk = myComp.getViewerFormKey();
        return fk;
    }

    /** Invokes the deleteObject() method on the component.
     * @return The FormKey for the Delete screen.
     */
    public FormKey do_Delete_Clicked() {
        FormKey fk = null;
        FormTemplateViewerForm myForm = (FormTemplateViewerForm) form;
        FormTemplateViewerComponent myComp = (FormTemplateViewerComponent) myForm.getComponent();
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
