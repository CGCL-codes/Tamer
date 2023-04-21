package org.jaffa.applications.mylife.admin.components.musiccontentmaintenance.ui;

import org.apache.log4j.Logger;
import org.jaffa.presentation.portlet.ActionBase;
import org.jaffa.presentation.portlet.FormKey;
import org.jaffa.presentation.portlet.session.UserSession;
import org.jaffa.components.maint.IMaintComponent;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.apache.struts.action.ActionErrors;

/** The Action class for handling events related to the Maintenance screen.
 */
public class MusicContentMaintenanceAction extends ActionBase {

    private static final Logger log = Logger.getLogger(MusicContentMaintenanceAction.class);

    /** Clicked event handler for the field Save.
     * @return The FormKey.
     */
    public FormKey do_Save_Clicked() {
        MusicContentMaintenanceForm myForm = (MusicContentMaintenanceForm) form;
        MusicContentMaintenanceComponent comp = (MusicContentMaintenanceComponent) myForm.getComponent();
        if (myForm.doValidate(request)) try {
            if (comp.isCreateMode()) {
                comp.create();
                comp.setMode(IMaintComponent.MODE_UPDATE);
            } else {
                comp.update();
            }
        } catch (ApplicationExceptions e) {
            myForm.raiseError(request, ActionErrors.GLOBAL_ERROR, e);
        } catch (FrameworkException e) {
            e.printStackTrace();
            myForm.raiseError(request, ActionErrors.GLOBAL_ERROR, "error.framework.general");
        }
        return new FormKey(myForm.NAME, comp.getComponentId());
    }

    /** Clicked event handler for the field Delete.
     * @return The FormKey.
     */
    public FormKey do_Delete_Clicked() {
        MusicContentMaintenanceForm myForm = (MusicContentMaintenanceForm) form;
        MusicContentMaintenanceComponent comp = (MusicContentMaintenanceComponent) myForm.getComponent();
        try {
            comp.delete();
            return comp.quitAndReturnToCallingScreen();
        } catch (ApplicationExceptions e) {
            myForm.raiseError(request, ActionErrors.GLOBAL_ERROR, e);
        } catch (FrameworkException e) {
            e.printStackTrace();
            myForm.raiseError(request, ActionErrors.GLOBAL_ERROR, "error.framework.general");
        }
        return new FormKey(myForm.NAME, comp.getComponentId());
    }

    /** Quits the component and returns the FormKey for the calling screen.
     * @return The FormKey for the caling screen. A null will be returned, if no calling screen was specified.
     */
    public FormKey do_Cancel_Clicked() {
        return ((MusicContentMaintenanceForm) form).getComponent().quitAndReturnToCallingScreen();
    }
}
