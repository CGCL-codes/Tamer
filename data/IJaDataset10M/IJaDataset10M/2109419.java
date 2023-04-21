package net.sourceforge.solexatools.webapp.controller;

import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import net.sourceforge.solexatools.Debug;
import net.sourceforge.solexatools.Security;
import net.sourceforge.solexatools.model.Registration;
import net.sourceforge.solexatools.model.RegistrationDTO;
import net.sourceforge.solexatools.business.RegistrationService;

public class RegistrationController extends MultiActionController {

    private RegistrationService registrationService;

    private Validator validator;

    /**
	 * Handles the user's request to submit a new registration.
	 *
	 * @param command RegistrationDTO command object
	 */
    public ModelAndView handleSubmit(HttpServletRequest request, HttpServletResponse response, RegistrationDTO command) throws Exception {
        Debug.put("");
        Registration registration = Security.getRegistration(request);
        ModelAndView modelAndView;
        BindingResult errors = this.validateRegistration(request, command);
        if (errors.hasErrors()) {
            Map model = errors.getModel();
            modelAndView = new ModelAndView("Registration", model);
        } else if (registration != null) {
            if (registration.getRegistrationId() == command.getRegistrationId() || registration.getEmailAddress().equalsIgnoreCase(command.getEmailAddress())) {
                Registration domainObject;
                try {
                    domainObject = new Registration(registration);
                    command.setDomainObject(domainObject);
                } catch (Exception x) {
                    x.printStackTrace();
                }
                getRegistrationService().update(command);
            } else {
                Debug.put("EXISTING USER CREATING NEW ACCOUNT");
                getRegistrationService().insert(command);
            }
            modelAndView = new ModelAndView("redirect:/experimentList.htm");
        } else {
            getRegistrationService().insert(command);
            modelAndView = new ModelAndView("redirect:/login.htm");
        }
        return modelAndView;
    }

    /**
	 * Handles the user's request to reset the registration page during a new or
	 * update registration.
	 *
	 * @param command RegistrationDTO command object
	 */
    public ModelAndView handleReset(HttpServletRequest request, HttpServletResponse response, RegistrationDTO command) throws Exception {
        Debug.put("");
        ModelAndView modelAndView = null;
        RegistrationDTO registration = getCurrentRegistration(request);
        if (registration.getUpdateTimestamp() == null) {
            modelAndView = new ModelAndView("Registration");
        } else {
            modelAndView = new ModelAndView("RegistrationUpdate");
        }
        request.setAttribute(getCommandName(command), registration);
        return modelAndView;
    }

    /**
	 * Handles the user's request to cancel the registration
	 * or the registration update page.
	 *
	 * @param command RegistrationDTO command object
	 */
    public ModelAndView handleCancel(HttpServletRequest request, HttpServletResponse response, RegistrationDTO command) throws Exception {
        Debug.put("");
        return new ModelAndView("redirect:/experimentList.htm");
    }

    /**
	 * Handles the user's request to update their registration.
	 *
	 * @param command RegistrationDTO command object
	 */
    public ModelAndView handleUpdate(HttpServletRequest request, HttpServletResponse response, RegistrationDTO command) throws Exception {
        Debug.put("");
        ModelAndView modelAndView = null;
        BindingResult errors = this.validateRegistration(request, command);
        if (errors.hasErrors()) {
            Map model = errors.getModel();
            modelAndView = new ModelAndView("RegistrationUpdate", model);
        } else {
            RegistrationDTO registration = getCurrentRegistration(request);
            if (registration.getUpdateTimestamp() != null) {
                RegistrationDTO updatedRegistration = command;
                Debug.put(": updatedRegistration = " + updatedRegistration);
                getRegistrationService().update(updatedRegistration);
                Debug.put(": updatedRegistration = " + updatedRegistration);
                modelAndView = new ModelAndView("redirect:/experimentList.htm");
            } else {
                modelAndView = new ModelAndView("redirect:/Error.htm");
            }
        }
        return modelAndView;
    }

    public ModelAndView handleUnknownMethod(HttpServletRequest request, HttpServletResponse response, RegistrationDTO command) throws Exception {
        Debug.put("");
        return new ModelAndView("RegistrationUpdate");
    }

    /**
	 * Validates a registration.
	 * @return BindingResult validation errors
	 */
    private BindingResult validateRegistration(HttpServletRequest request, Object command) {
        BindingResult errors = new BindException(command, getCommandName(command));
        ValidationUtils.invokeValidator(getValidator(), command, errors);
        if (!errors.hasErrors()) {
            RegistrationDTO registration = (RegistrationDTO) command;
            if (getRegistrationService().hasEmailAddressBeenUsed(getEmailAddressFromSession(request), registration.getEmailAddress())) {
                errors.reject("error.registration.email.used");
            }
        }
        return errors;
    }

    /**
	 * Gets the emailAddress from the registration in the session.
	 * @return the emailAddress from the registration in the session, or null if
	 * there is no registration in the session
	 */
    private String getEmailAddressFromSession(HttpServletRequest request) {
        return getCurrentRegistration(request).getEmailAddress();
    }

    /**
	 * Gets the registration from the session.
	 * @return instance of RegistrationDTO from the session, or a new instance
	 * if the registration is not in the session (e.g. the user is not logged in)
	 */
    private RegistrationDTO getCurrentRegistration(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object registration = session.getAttribute("registration");
            if (registration != null) {
                return (RegistrationDTO) registration;
            }
        }
        return new RegistrationDTO();
    }

    public RegistrationController() {
        super();
    }

    public RegistrationController(Object delegate) {
        super(delegate);
    }

    public RegistrationService getRegistrationService() {
        return registrationService;
    }

    public void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
}
