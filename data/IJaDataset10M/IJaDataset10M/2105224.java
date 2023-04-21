package org.patientos.portal.business;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.patientos.portal.actions.BaseAction;
import com.patientis.framework.web.controls.Services;
import com.patientis.business.security.Authentication;
import com.patientis.business.security.InvalidUserLoginException;
import com.patientis.business.security.InvalidUserPasswordException;
import com.patientis.framework.logging.Log;
import com.patientis.model.patient.PortalUserModel;
import com.patientis.model.patient.ViewPatientModel;

/**
 * @author gcaulton
 *
 */
public class PatientAuthentication {

    public static void authenticatePatient(String username, String password, HttpServletRequest request, BaseAction action) throws Exception {
        PortalUserModel portalUser = null;
        try {
            portalUser = Services.getSecurityBean().authenticatePortalUser(username, Authentication.hashPassword(password.getBytes()), action.getServiceCall());
        } catch (InvalidUserLoginException l1) {
            try {
                username = username.toUpperCase();
                portalUser = Services.getSecurityBean().authenticatePortalUser(username, Authentication.hashPassword(password.getBytes()), action.getServiceCall());
            } catch (InvalidUserLoginException l2) {
                username = username.toLowerCase();
                portalUser = Services.getSecurityBean().authenticatePortalUser(username, Authentication.hashPassword(password.getBytes()), action.getServiceCall());
            } catch (InvalidUserPasswordException p1) {
                try {
                    portalUser = Services.getSecurityBean().authenticatePortalUser(username, Authentication.hashPassword(password.toUpperCase().getBytes()), action.getServiceCall());
                } catch (InvalidUserPasswordException p2) {
                    portalUser = Services.getSecurityBean().authenticatePortalUser(username, Authentication.hashPassword(password.toLowerCase().getBytes()), action.getServiceCall());
                }
            }
        } catch (InvalidUserPasswordException p1) {
            try {
                portalUser = Services.getSecurityBean().authenticatePortalUser(username, Authentication.hashPassword(password.toUpperCase().getBytes()), action.getServiceCall());
            } catch (InvalidUserPasswordException p2) {
                portalUser = Services.getSecurityBean().authenticatePortalUser(username, Authentication.hashPassword(password.toLowerCase().getBytes()), action.getServiceCall());
            }
        }
        action.getWebUser(request.getSession()).setPortalUser(portalUser);
        List<ViewPatientModel> patients = Services.getPatientBean().getPatientsForPortalUser(portalUser.getId(), action.getServiceCall());
        action.getWebUser(request.getSession()).setPortalPatientIds(patients);
        if (patients.size() > 0) {
            action.getWebUser(request.getSession()).setCurrentPatient(patients.get(0));
        }
        if (patients.size() == 0) {
            Log.warn("user has no patients ");
            throw new Exception("user has no patients");
        } else {
            if (patients.size() > 1) {
                Log.warn("Multiple patients not supported yet");
            }
            action.getWebUser(request.getSession()).setPortalPatientId(patients.get(0).getId());
        }
    }
}
