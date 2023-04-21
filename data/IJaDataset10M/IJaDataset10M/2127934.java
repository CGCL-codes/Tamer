package org.jaffa.applications.test.modules.security.components.test1.ui;

import org.jaffa.presentation.portlet.ActionBase;
import org.jaffa.security.SecurityManager;
import org.jaffa.presentation.portlet.FormKey;
import java.security.PrivilegedAction;
import org.jaffa.presentation.portlet.session.UserSession;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;
import java.security.AccessControlException;
import javax.servlet.http.HttpSession;
import org.jaffa.presentation.portlet.component.ComponentManager;
import org.jaffa.presentation.portlet.component.IComponent;
import org.jaffa.presentation.portlet.component.ComponentCreationRuntimeException;

/**
 * @author PaulE
 * @version 1.0
 */
public class Page1Action extends ActionBase {

    /** This button is protected by 'Function1' */
    public FormKey do_Button1_Clicked() {
        System.out.println("Trying to do something protected by Function1");
        SecurityManager.runFunction("Function1", new PrivilegedAction() {

            public Object run() {
                String msg = "Doing Something Protected By Function1";
                ((Page1Form) form).setMessage(msg);
                System.out.println(msg);
                return null;
            }
        });
        return new FormKey(Page1Form.NAME, component != null ? component.getComponentId() : null);
    }

    /** This button is protected by 'Function1', it throws an exception from the secured method */
    public FormKey do_Button1b_Clicked() {
        System.out.println("Trying to do something protected by Function1");
        try {
            SecurityManager.runFunction("Function1", new PrivilegedExceptionAction() {

                public Object run() {
                    throw new UnsupportedOperationException("Test Exception");
                }
            });
        } catch (AccessControlException e) {
            ((Page1Form) form).setMessage("You were not allowed access to Function1");
        } catch (PrivilegedActionException e) {
            ((Page1Form) form).setMessage("Caught Exception From Secured Function : " + e.getException().getClass().getName() + " - " + e.getException().getMessage());
        }
        return new FormKey(Page1Form.NAME, component != null ? component.getComponentId() : null);
    }

    /** This handles the fact that security access is denied. */
    public FormKey do_Button2_Clicked() {
        System.out.println("Trying to do something protected by Function2");
        try {
            SecurityManager.runFunction("Function2", new PrivilegedAction() {

                public Object run() {
                    String msg = "Doing Something Protected By Function2";
                    ((Page1Form) form).setMessage(msg);
                    System.out.println(msg);
                    return null;
                }
            });
        } catch (AccessControlException e) {
            String msg = "You Were Not Allowed Access To Function 2";
            ((Page1Form) form).setMessage(msg);
            System.out.println(msg);
        }
        return new FormKey(Page1Form.NAME, component != null ? component.getComponentId() : null);
    }

    /** This tries to run a component that the user may, or may not have access to  */
    public FormKey do_Button3_Clicked() {
        System.out.println("Trying to run component Test.Security.Test2");
        try {
            IComponent c = ComponentManager.run("Test.Security.Test2", UserSession.getUserSession(request));
            String msg = "Running Component : Create Succeeded";
            ((Page1Form) form).setMessage(msg);
            System.out.println(msg);
        } catch (AccessControlException e) {
            String msg = "No Access To Component";
            ((Page1Form) form).setMessage(msg);
            System.out.println(msg);
        } catch (ComponentCreationRuntimeException e) {
            String msg = "Running Component : Create Failed";
            ((Page1Form) form).setMessage(msg);
            System.out.println(msg);
        }
        return new FormKey(Page1Form.NAME, component != null ? component.getComponentId() : null);
    }

    /** Log this user out. */
    public FormKey do_Logout_Clicked() {
        return null;
    }
}
