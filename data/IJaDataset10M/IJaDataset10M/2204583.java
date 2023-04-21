package org.jaffa.applications.mylife.admin.components.userlookup.ui;

import java.util.*;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.jaffa.presentation.portlet.ActionBase;
import org.jaffa.presentation.portlet.FormKey;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.ApplicationException;
import org.jaffa.components.finder.*;
import org.jaffa.applications.mylife.admin.components.userlookup.dto.UserLookupInDto;

/** The Action class for handling events related to the Criteria screen.
 */
public class UserLookupCriteriaAction extends ActionBase {

    private static final Logger log = Logger.getLogger(UserLookupCriteriaAction.class);

    /** Creates a criteria object and invokes the displayResults() method on the component.
     * @return The FormKey for the Results screen.
     */
    public FormKey do_Search_Clicked() {
        FormKey fk = null;
        UserLookupCriteriaForm myForm = (UserLookupCriteriaForm) form;
        UserLookupComponent myComp = (UserLookupComponent) myForm.getComponent();
        if (myForm.doValidate(request)) {
            try {
                fk = myComp.displayResults();
            } catch (ApplicationExceptions e) {
                if (log.isDebugEnabled()) log.debug("Search Failed");
                myForm.raiseError(request, ActionErrors.GLOBAL_ERROR, e);
            } catch (FrameworkException e) {
                log.error(null, e);
                myForm.raiseError(request, ActionErrors.GLOBAL_ERROR, "error.framework.general");
            }
        }
        if (fk == null) fk = new FormKey(myForm.NAME, myComp.getComponentId());
        return fk;
    }

    /** Invokes the createFromCriteria() method on the component.
     * @return The FormKey for the Create screen.
     */
    public FormKey do_Create_Clicked() {
        FormKey fk = null;
        UserLookupCriteriaForm myForm = (UserLookupCriteriaForm) form;
        UserLookupComponent myComp = (UserLookupComponent) myForm.getComponent();
        if (myForm.doValidate(request)) {
            try {
                fk = myComp.createFromCriteria();
            } catch (ApplicationExceptions e) {
                if (log.isDebugEnabled()) log.debug("Create Failed");
                myForm.raiseError(request, ActionErrors.GLOBAL_ERROR, e);
            } catch (FrameworkException e) {
                log.error(null, e);
                myForm.raiseError(request, ActionErrors.GLOBAL_ERROR, "error.framework.general");
            }
        }
        if (fk == null) fk = new FormKey(myForm.NAME, myComp.getComponentId());
        return fk;
    }

    /** Quits the component and closes the browser window.
     * @return a FormKey object for the generic lookup jsp.
     */
    public FormKey do_Close_Clicked() {
        UserLookupCriteriaForm myForm = (UserLookupCriteriaForm) form;
        UserLookupComponent myComp = (UserLookupComponent) myForm.getComponent();
        return myComp.quitLookup(request);
    }
}
