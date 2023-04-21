package com.centraview.customer.profile;

import java.io.IOException;
import java.util.ArrayList;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.DynaActionForm;
import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.administration.emailsettings.EmailSettings;
import com.centraview.administration.emailsettings.EmailSettingsHome;
import com.centraview.administration.emailsettings.EmailTemplateForm;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.mail.MailMessageVO;
import com.centraview.settings.Settings;

/**
 * Handles the request when the user clicks the "Request Changes"
 * button on the Customer Profile screen in Customer View. This
 * Action class will send an email to the address defined in the
 * systememailsettings table, with the details the user entered
 * into the form.
 */
public class UpdateProfileHandler extends Action {

    private static Logger logger = Logger.getLogger(UpdateProfileHandler.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException {
        String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        String forward = ".view.customer.profile_confirm";
        HttpSession session = request.getSession();
        UserObject userObject = (UserObject) session.getAttribute("userobject");
        int individualID = userObject.getIndividualID();
        int entityID = userObject.getEntityId();
        ActionErrors allErrors = new ActionErrors();
        DynaActionForm profileForm = (DynaActionForm) form;
        EmailSettingsHome emailSettingsHome = (EmailSettingsHome) CVUtility.getHomeObject("com.centraview.administration.emailsettings.EmailSettingsHome", "EmailSettings");
        try {
            EmailSettings emailSettingsRemote = (EmailSettings) emailSettingsHome.create();
            emailSettingsRemote.setDataSource(dataSource);
            EmailTemplateForm custProfileTemplateForm = emailSettingsRemote.getEmailTemplate(AdministrationConstantKeys.EMAIL_TEMPLATE_CUST_PROFILE);
            String toAddress = custProfileTemplateForm.getToAddress();
            String fromAddress = custProfileTemplateForm.getFromAddress();
            String replyTo = custProfileTemplateForm.getReplyTo();
            String emailSubject = custProfileTemplateForm.getSubject();
            String emailBody = custProfileTemplateForm.getBody();
            MailMessageVO mailMessage = new MailMessageVO();
            ArrayList toList = new ArrayList();
            toList.add(toAddress);
            mailMessage.setToList(toList);
            mailMessage.setHeaders("Change Of Profile Information");
            mailMessage.setContentType("text/plain");
            mailMessage.setFromAddress(fromAddress);
            mailMessage.setReplyTo(replyTo);
            mailMessage.setSubject(emailSubject);
            StringBuffer body = new StringBuffer("");
            body.append(emailBody);
            body.append("\n\nCustomer Profile Change Request:\n\n");
            DynaActionForm dynaForm = (DynaActionForm) form;
            String entityName = (String) dynaForm.get("entityName");
            body.append("Entity Name: " + entityName + "\n");
            String website = (String) dynaForm.get("website");
            body.append("Website: " + website + "\n");
            String street1 = (String) dynaForm.get("street1");
            body.append("Street1: " + street1 + "\n");
            String street2 = (String) dynaForm.get("street2");
            body.append("Street2: " + street2 + "\n");
            String city = (String) dynaForm.get("city");
            body.append("City: " + city + "\n");
            String state = (String) dynaForm.get("state");
            body.append("State: " + state + "\n");
            String zipCode = (String) dynaForm.get("zipCode");
            body.append("Zip Code: " + zipCode + "\n");
            String country = (String) dynaForm.get("country");
            body.append("Country: " + country + "\n");
            String mocContent = "";
            String mocTypeName = "";
            if (dynaForm.get("email") != null) {
                mocContent = (dynaForm.get("email") == null) ? "" : (String) dynaForm.get("email");
                body.append("Email: " + mocContent + "\n");
            }
            for (int i = 1; i < 4; i++) {
                mocContent = CVUtility.getMOCContent(dynaForm, i + "");
                int mocType = Integer.parseInt((String) dynaForm.get("mocType" + i));
                mocTypeName = CVUtility.getSyncAs(mocType);
                body.append(mocTypeName + ": " + mocContent + "\n");
            }
            body.append("\n\nEnd CentraView Web Request\n");
            mailMessage.setBody(body.toString());
            boolean messageSent = emailSettingsRemote.simpleMessage(individualID, mailMessage);
            if (!messageSent) {
                allErrors.add("error.customer.profile.emailSendFailure", new ActionMessage("error.customer.profile.emailSendFailure"));
            }
        } catch (Exception e) {
            logger.error("[Exception] UpdateProfileHandler.Execute Handler ", e);
            allErrors.add("error.unknownError", new ActionMessage("error.unknownError"));
            forward = ".view.customer.profile";
        }
        if (!allErrors.isEmpty()) {
            saveErrors(request, allErrors);
        }
        return (mapping.findForward(forward));
    }
}
