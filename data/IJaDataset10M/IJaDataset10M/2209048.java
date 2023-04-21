package org.jaffa.applications.jaffa.modules.user.components.requestpassword.ui;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.jaffa.presentation.portlet.ActionBase;
import org.jaffa.presentation.portlet.FormKey;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.presentation.portlet.EventHandlerMissingRuntimeException;

/** The Action class for handling events.
 */
public class RequestPasswordAction extends ActionBase {

    private static final Logger log = Logger.getLogger(RequestPasswordAction.class);

    /** Clicked event handler for the field Finish.
     * @return The FormKey.
     */
    public FormKey do_Finish_Clicked() {
        FormKey fk = null;
        RequestPasswordForm myForm = (RequestPasswordForm) form;
        RequestPasswordComponent myComp = (RequestPasswordComponent) myForm.getComponent();
        if (invokeDoValidateForScreen(0)) {
            myComp.sendEmail();
        }
        if (fk == null) fk = myComp.determineFormKey();
        return fk;
    }

    /** Clicked event handler for the field Clear.
     * @return The FormKey.
     */
    public FormKey do_Clear_Clicked() {
        FormKey fk = null;
        RequestPasswordForm myForm = (RequestPasswordForm) form;
        RequestPasswordComponent myComp = (RequestPasswordComponent) myForm.getComponent();
        if (fk == null) fk = myComp.determineFormKey();
        return fk;
    }

    /** Determines the currentFormKey
     * This event is invoked when a user changes the settings of the UserGrid in the Results screen.
     * @return The FormKey for the Results screen.
     */
    public FormKey do_refresh() {
        RequestPasswordForm myForm = (RequestPasswordForm) form;
        RequestPasswordComponent myComp = (RequestPasswordComponent) myForm.getComponent();
        return myComp.determineFormKey();
    }

    /** Quits the component and returns the FormKey for the calling screen.
     * @return The FormKey for the calling screen. A null will be returned, if no calling screen was specified.
     */
    public FormKey do_Cancel_Clicked() {
        return component.quitAndReturnToCallingScreen();
    }

    /** This will invoke the doValidate() method for the current screen.
     * It then moves to the next screen
     * @return The FormKey for the next screen.
     */
    public FormKey do_Next_Clicked() {
        RequestPasswordForm myForm = (RequestPasswordForm) form;
        RequestPasswordComponent myComp = (RequestPasswordComponent) myForm.getComponent();
        if (invokeDoValidateForScreen(myComp.getCurrentScreenCounter())) {
            myComp.determineAndSetNextScreen();
        }
        return myComp.determineFormKey();
    }

    /** It moves to the previous screen
     * @return The FormKey for the previous screen.
     */
    public FormKey do_Previous_Clicked() {
        RequestPasswordForm myForm = (RequestPasswordForm) form;
        RequestPasswordComponent myComp = (RequestPasswordComponent) myForm.getComponent();
        myComp.determineAndSetPreviousScreen();
        return myComp.determineFormKey();
    }

    private boolean invokeDoValidateForScreen(int screenCounter) {
        String methodName = "doValidate" + screenCounter;
        Method method = null;
        try {
            method = form.getClass().getMethod(methodName, new Class[] { HttpServletRequest.class });
            if (method.getReturnType() != Boolean.TYPE) throw new NoSuchMethodException();
        } catch (NoSuchMethodException e) {
            String str = "The method 'public boolean doValidate" + screenCounter + "(HttpServletRequest request) needs to be defined in the class " + form.getClass().getName();
            log.error(str, e);
            throw new EventHandlerMissingRuntimeException(str);
        }
        try {
            Boolean output = (Boolean) method.invoke(form, new Object[] { request });
            return output.booleanValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
