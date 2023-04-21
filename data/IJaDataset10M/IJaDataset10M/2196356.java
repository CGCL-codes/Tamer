package org.voxattendant.controller;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.voxattendant.manager.ApplicationManager;
import org.voxattendant.model.ApplicationBean;
import org.voxattendant.model.OperatorBean;
import org.voxattendant.model.PhoneNumberBean;
import org.voxattendant.model.PromptBean;
import org.voxattendant.util.BeanCollection;
import org.voxattendant.util.ErrorBean;
import org.voxattendant.util.FormError;

/**
 * 
 * @author Simon Tang
 * @version $Revision: 1.2 $ $Date: 2006/08/01 17:07:40 $
 */
public class SettingsController extends BaseController {

    private static final long serialVersionUID = 8194557054598632555L;

    public static final String PREFIX_NOTE = "note-";

    public static final String PREFIX_NUMBER = "number-";

    public static final String PREFIX_DELETE = "delete-";

    public static final String PARAM_PROMPTID = "promptId";

    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        doPost(arg0, arg1);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            if (!isUserSessionValid(req)) {
                getServletContext().getRequestDispatcher(LoginController.CONTROLLER).forward(req, res);
                return;
            }
            String reqState;
            if ((reqState = ((String) req.getParameter("reqState"))) == null) {
                reqState = "htmlShowSettings";
            }
            System.out.println("SettingsController ReqState is: " + reqState);
            if (reqState.equals("htmlShowSettings")) {
                showSettings(req, res);
            } else if (reqState.equals("htmlShowOperatorEdit")) {
                showOperatorEdit(req, res);
            } else if (reqState.equals("htmlProcessOperatorEdit")) {
                processOperatorEdit(req, res);
            } else if (reqState.equals("htmlProcessOperatorUpdate")) {
                processOperatorUpdate(req, res);
            } else if (reqState.equals("htmlShowApplicationEdit")) {
                showApplicationEdit(req, res);
            } else if (reqState.equals("htmlProcessApplicationEdit")) {
                processApplicationEdit(req, res);
            } else if (reqState.equals("htmlShowPrompts")) {
                showPrompts(req, res);
            } else if (reqState.equals("htmlProcessPromptDirections")) {
                processPromptDirections(req, res);
            } else if (reqState.equals("htmlProcessPromptGreetings")) {
                processPromptGreetings(req, res);
            } else if (reqState.equals("htmlShowPasscodeEdit")) {
                showPasscodeEdit(req, res);
            } else if (reqState.equals("htmlProcessPasscodeEdit")) {
                processPasscodeEdit(req, res);
            } else {
                System.out.println("SettingsController reqState: " + reqState + " does not match any known reqStates");
                showSettings(req, res);
            }
        } catch (Exception e) {
            System.err.println("Error: MainController");
            System.err.println(e.toString());
        }
    }

    /**
	 * Shows the main settings page
	 */
    protected void showSettings(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ApplicationBean appBean = retrieveApplicationBean();
        OperatorBean operatorBean = retrieveOperatorBean(req);
        if (operatorBean != null) req.setAttribute("operatorBean", operatorBean);
        req.setAttribute("appBean", appBean);
        displayHtmlJsp(req, res, "settings.jsp");
    }

    /**
	 * Shows the Edit Operator page
	 */
    protected void showOperatorEdit(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        OperatorBean operatorBean = retrieveOperatorBean(req);
        if (operatorBean != null) {
            req.setAttribute("operatorBean", operatorBean);
        }
        displayHtmlJsp(req, res, "settingsOperatorEdit.jsp");
    }

    /**
	 * Updates the Operator information
	 */
    protected void processOperatorEdit(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (req.getParameter("apply") != null) {
            updateOperatorNumbers(req);
            showOperatorEdit(req, res);
        } else if (req.getParameter("save") != null) {
            updateOperatorNumbers(req);
            showSettings(req, res);
        } else if (req.getParameter("cancel") != null) {
            showSettings(req, res);
        } else {
            System.out.println("going to handle delete");
            deleteOperatorNumber(req);
            showOperatorEdit(req, res);
        }
    }

    /**
	 * Updates the current Operator information
	 */
    protected void processOperatorUpdate(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ApplicationBean appBean = retrieveApplicationBean();
        String selectedId = (String) req.getParameter("selectedId");
        if (selectedId != null) {
            appBean.setCurrentOperatorNumberId(Integer.parseInt(selectedId));
            ApplicationManager.updateApplicationBean(appBean);
        }
        showSettings(req, res);
    }

    /**
	 * Shows the Application information page
	 */
    protected void showApplicationEdit(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ApplicationBean appBean = retrieveApplicationBean();
        req.setAttribute("appBean", appBean);
        displayHtmlJsp(req, res, "settingsApplicationEdit.jsp");
    }

    /**
	 * Updates the Application information
	 */
    protected void processApplicationEdit(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String appNumber = req.getParameter("appNumber");
        if (appNumber == null) {
            displayHtmlJsp(req, res, "error.jsp");
        }
        if (req.getParameter("save") != null) {
            ApplicationBean appBean = retrieveApplicationBean();
            appBean.setApplicationNumber(appNumber);
            PhoneNumberBean phoneNumberBean = new PhoneNumberBean();
            phoneNumberBean.setOwnerTypeId(ApplicationBean.APPLICATION_TYPE_ID);
            phoneNumberBean.setPhoneTypeId(PhoneNumberBean.PHONETYPEID_APPLICATION);
            phoneNumberBean.setNumber(appNumber);
            ApplicationManager.updatePhoneNumberByOwner(phoneNumberBean);
        }
        showSettings(req, res);
    }

    /**
	 * Shows the Greeting Prompts
	 */
    protected void showPrompts(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ApplicationBean appBean = retrieveApplicationBean();
        if (appBean.isPlayDirectionAudio()) {
            req.setAttribute("drivingDir", "on");
        }
        BeanCollection promptBeanList = retrievePromptBeanList(req);
        if (promptBeanList != null) {
            req.setAttribute("promptBeanList", promptBeanList);
        }
        displayHtmlJsp(req, res, "settingsPrompts.jsp");
    }

    /**
	 * Updates the Play Directions
	 */
    protected void processPromptDirections(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ApplicationBean appBean = retrieveApplicationBean();
        appBean.togglePlayDirectionAudio();
        ApplicationManager.updateApplicationBean(appBean);
        showPrompts(req, res);
    }

    /**
	 * Updates the Greeting Prompts
	 */
    protected void processPromptGreetings(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        BeanCollection promptBeanList = retrievePromptBeanList(req);
        if (promptBeanList != null) {
            for (int i = 0; i < promptBeanList.size(); i++) {
                ((PromptBean) promptBeanList.getItem(i)).setActive(false);
            }
            String[] promptIds = req.getParameterValues("promptId");
            for (int j = 0; j < promptIds.length; j++) {
                int promptId = Integer.parseInt(promptIds[j]);
                for (int i = 0; i < promptBeanList.size(); i++) {
                    PromptBean promptBean = (PromptBean) promptBeanList.getItem(i);
                    if (promptBean.getPromptId() == promptId) promptBean.setActive(true);
                }
            }
            ApplicationManager.updatePrompts(promptBeanList);
        }
        showPrompts(req, res);
    }

    /**
	 * 
	 */
    protected void showPasscodeEdit(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        displayHtmlJsp(req, res, "settingsPasscode.jsp");
    }

    /**
	 * 
	 */
    protected void processPasscodeEdit(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ApplicationBean appBean = retrieveApplicationBean();
        ErrorBean errorBean = new ErrorBean();
        if (req.getParameter("save") != null) {
            String oldPasscode = req.getParameter("oldPasscode");
            String newPasscode = req.getParameter("newPasscode");
            String newPasscode2 = req.getParameter("newPasscode2");
            if (oldPasscode.equals(appBean.getPasscode()) && newPasscode.equals(newPasscode2)) {
                appBean.setPasscode(newPasscode);
                ApplicationManager.updateApplicationBean(appBean);
                errorBean.addError("Passcode has been updated.", FormError.SEVERITY_STOP);
                req.setAttribute("errorBean", errorBean);
                showSettings(req, res);
            } else {
                errorBean.addError("Old Passcode does not match. Failed to change passcode.", FormError.SEVERITY_STOP);
                req.setAttribute("errorBean", errorBean);
                showPasscodeEdit(req, res);
            }
        } else {
            showSettings(req, res);
        }
    }

    /**
	 * 
	 */
    protected void updateOperatorNumbers(HttpServletRequest req) throws ServletException, IOException {
        String newNote = req.getParameter("newNote");
        if (newNote != null && newNote.trim().length() > 0) {
            String newNumber = req.getParameter("newNumber");
            System.out.println("Found new Note & Number: " + newNote + " : " + newNumber);
            PhoneNumberBean phoneNumberBean = new PhoneNumberBean();
            phoneNumberBean.setOwnerTypeId(OperatorBean.OPERATOR_TYPE_ID);
            phoneNumberBean.setPhoneTypeId(PhoneNumberBean.PHONETYPEID_WORK);
            phoneNumberBean.setNumber(newNumber);
            phoneNumberBean.setNote(newNote);
            ApplicationManager.insertPhoneNumber(phoneNumberBean);
        }
        Enumeration params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String data = (String) params.nextElement();
            if (!data.startsWith(PREFIX_NOTE)) continue;
            String strId = data.substring(PREFIX_NOTE.length());
            String number = req.getParameter(PREFIX_NUMBER + strId);
            if (number == null) continue;
            String note = req.getParameter(data);
            System.out.println("About to update: " + note + " : " + number);
            PhoneNumberBean phoneNumberBean = new PhoneNumberBean();
            phoneNumberBean.setPhoneNumberId(Integer.parseInt(strId));
            phoneNumberBean.setNumber(number);
            phoneNumberBean.setNote(note);
            ApplicationManager.updatePhoneNumber(phoneNumberBean);
        }
    }

    /**
	 * 
	 */
    protected void deleteOperatorNumber(HttpServletRequest req) throws ServletException, IOException {
        String data = req.getParameter("deletePressed");
        if (data != null && data.startsWith(PREFIX_DELETE)) {
            int id = Integer.parseInt(data.substring(PREFIX_DELETE.length()));
            PhoneNumberBean phoneNumberBean = new PhoneNumberBean();
            phoneNumberBean.setPhoneNumberId(id);
            ApplicationManager.deletePhoneNumber(phoneNumberBean);
        }
    }
}
