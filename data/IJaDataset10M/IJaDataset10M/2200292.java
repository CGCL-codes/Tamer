package org.ejc.struts.updateReg;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.ejc.api.registration.RegistrationAPIUtil;
import org.ejc.api.transaction.TransactionAPIUtil;
import org.ejc.datatype.registrant.RegistrantSearchCriteria;
import org.ejc.persist.model.AccomodationType;
import org.ejc.persist.model.Address;
import org.ejc.persist.model.ConventionRegistrant;
import org.ejc.persist.model.CountryCode;
import org.ejc.persist.model.Registrant;
import org.ejc.persist.model.RegistrantStatusType;
import org.ejc.persist.model.RegistrantType;
import org.ejc.persist.model.Transaction;
import org.ejc.session.EJCSession;
import org.ejc.struts.address.AddressForm;
import org.ejc.struts.registration.RegistrationForm;
import org.ejc.tech.combo.ComboConstants;
import org.ejc.tech.combo.ComboMgr;

/**
 * @struts.action path="/struts/register/registrationUpdateAction" scope="request"
 *                parameter="methodToCall" name="registrationForm"
 *                validate="false"
 *                input="/struts/register/updateRegistrant.jsp"
 * 
 * @struts.action-forward name="preUpdateSuccess" path="/struts/register/updateRegistrant.jsp"
 * @struts.action-forward name="updateSuccess" path="/struts/register/registrationUpdateAction.do?methodToCall=preUpdate"
 * @struts.action-forward name="selectRegFailure" path="/struts/search/searchRegistrantsAction.do?methodToCall=preSearch"
 * 
 * 
 * @author Brian Boyle
 *
 */
public class RegistrantUpdateAction extends DispatchAction implements ComboConstants {

    private static Logger log = Logger.getLogger(RegistrantUpdateAction.class);

    /**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public final ActionForward preUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        EJCSession ejcSession = (EJCSession) session.getAttribute("ejcSession");
        ejcSession.setCurrentPrice(null);
        ejcSession.setTransaction(null);
        ejcSession.setConventionRegistrant(null);
        RegistrationForm regForm = (RegistrationForm) form;
        Integer registrantId = regForm.getRegistrantId();
        ConventionRegistrant conventionReg = RegistrationAPIUtil.getLocalHome().create().selectRegistrantById(registrantId);
        log.debug("About to setup the Registrant details");
        setupRegistrantDetails(conventionReg, regForm);
        log.debug("Setting Registrant objects in the session");
        ejcSession.setCurrentAction("Update Registrant");
        ejcSession.setConventionRegistrant(conventionReg);
        Transaction transaction = getRegistrantPaymentDetails(conventionReg.getRegistrant());
        if (transaction != null) {
            ejcSession.setTransaction(transaction);
        }
        request.setAttribute("countries", getCountryListing(request.getLocale()));
        request.setAttribute("registrantTypes", getRegistrantTypesListing(request.getLocale()));
        request.setAttribute("accomodationTypes", getAccomodationTypesListing(request.getLocale()));
        return mapping.findForward("preUpdateSuccess");
    }

    /**
	 * Select a guest using the preReg code
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public final ActionForward preRegCodeEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        EJCSession ejcSession = (EJCSession) session.getAttribute("ejcSession");
        ejcSession.setCurrentPrice(null);
        ejcSession.setTransaction(null);
        ejcSession.setConventionRegistrant(null);
        RegistrationForm regForm = (RegistrationForm) form;
        if (regForm.getPreRegInput().trim().length() == 0) {
            ActionMessages messages = new ActionMessages();
            ActionMessage msg = new ActionMessage("info.preReg.noMatch", "");
            messages.add("message1", msg);
            saveMessages(request, messages);
            return mapping.findForward("selectRegFailure");
        }
        RegistrantSearchCriteria searchCriteria = new RegistrantSearchCriteria();
        searchCriteria.setPreRegCode(regForm.getPreRegInput());
        searchCriteria.setLastName("");
        List regs = RegistrationAPIUtil.getLocalHome().create().searchRegistrants(searchCriteria, 0, 0);
        log.debug("About to setup the Registrant details");
        if (regs == null || regs.size() == 0) {
            ActionMessages messages = new ActionMessages();
            ActionMessage msg = new ActionMessage("info.preReg.noMatch", searchCriteria.getPreRegCode());
            messages.add("message1", msg);
            saveMessages(request, messages);
            return mapping.findForward("selectRegFailure");
        }
        ConventionRegistrant conventionReg = (ConventionRegistrant) regs.get(0);
        setupRegistrantDetails(conventionReg, regForm);
        log.debug("Setting Registrant objects in the session");
        ejcSession.setCurrentAction("Update Registrant");
        ejcSession.setConventionRegistrant(conventionReg);
        Transaction transaction = getRegistrantPaymentDetails(conventionReg.getRegistrant());
        if (transaction != null) {
            ejcSession.setTransaction(transaction);
        }
        request.setAttribute("countries", getCountryListing(request.getLocale()));
        request.setAttribute("registrantTypes", getRegistrantTypesListing(request.getLocale()));
        request.setAttribute("accomodationTypes", getAccomodationTypesListing(request.getLocale()));
        return mapping.findForward("preUpdateSuccess");
    }

    /**
	 * 
	 * @param conventionReg
	 * @param regForm
	 */
    private void setupRegistrantDetails(ConventionRegistrant conventionReg, RegistrationForm regForm) {
        Registrant registrant = conventionReg.getRegistrant();
        RegistrantType regType = conventionReg.getRegistrantType();
        AccomodationType accomType = conventionReg.getAccomodationType();
        Address regAddress = registrant.getAddress();
        log.debug("Setting the basic Registrant details");
        regForm.setFirstName(registrant.getFirstName());
        regForm.setLastName(registrant.getLastName());
        regForm.setEmail(registrant.getEmail());
        regForm.setNotes(registrant.getNotes());
        regForm.setContactNumber(registrant.getContactNumber());
        regForm.setLengthOfStay(conventionReg.getLengthOfStay());
        regForm.setAgeBracket(registrant.getAgeBracket());
        if (conventionReg.getPreReg() != null) {
            regForm.setPreRegistration(true);
            if (conventionReg.getRegistrantStatusType().getId() == 1) {
                regForm.setGuestArrived(true);
            }
        }
        if (regType != null) {
            regForm.setRegistrantTypeId(regType.getRegistrantTypeId());
        }
        if (accomType != null) {
            regForm.setAccomodationTypeId(accomType.getAccomodationTypeId());
        }
        if (regAddress != null) {
            log.debug("Setting the address deatils");
            AddressForm addressForm = new AddressForm();
            addressForm.setAddressId(regAddress.getAddressId());
            addressForm.setAddressLine1(regAddress.getAddressLine1());
            addressForm.setAddressLine2(regAddress.getAddressLine2());
            addressForm.setCityTown(regAddress.getCityTown());
            CountryCode countryCode = regAddress.getCountryCode();
            if (countryCode != null) {
                addressForm.setCountryCodeCountryId(countryCode.getCountryId());
            }
            regForm.setAddressForm(addressForm);
        }
    }

    public final ActionForward updateRegistrant(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages errors = form.validate(mapping, request);
        if (errors != null && !errors.isEmpty()) {
            log.fatal("There are errors");
            saveErrors(request, errors);
            request.setAttribute("countries", getCountryListing(request.getLocale()));
            request.setAttribute("registrantTypes", getRegistrantTypesListing(request.getLocale()));
            request.setAttribute("accomodationTypes", getAccomodationTypesListing(request.getLocale()));
            return (mapping.findForward("preUpdateSuccess"));
        }
        HttpSession session = request.getSession();
        EJCSession ejcSession = (EJCSession) session.getAttribute("ejcSession");
        RegistrationForm regForm = (RegistrationForm) form;
        ConventionRegistrant conventionReg = updateRegistrantFromForm(regForm, ejcSession.getConventionRegistrant());
        RegistrationAPIUtil.getLocalHome().create().updateRegistrant(conventionReg);
        log.debug("Reloading the update page with the new Registrant Details");
        ActionForward forward = mapping.findForward("updateSuccess");
        StringBuffer path = new StringBuffer(forward.getPath());
        path.append("&registrantId=");
        path.append(conventionReg.getRegistrantId());
        log.debug("Redirection URL " + path.toString());
        ActionMessages messages = new ActionMessages();
        ActionMessage msg = new ActionMessage("info.register.updateSuccessful");
        messages.add("message1", msg);
        saveMessages(request, messages);
        return new ActionForward(path.toString());
    }

    private ConventionRegistrant updateRegistrantFromForm(RegistrationForm form, ConventionRegistrant conventionRegistrant) {
        Registrant registrant = conventionRegistrant.getRegistrant();
        registrant.setEmail(form.getEmail());
        registrant.setFirstName(form.getFirstName());
        registrant.setLastName(form.getLastName());
        registrant.setNotes(form.getNotes());
        registrant.setContactNumber(form.getContactNumber());
        registrant.setAgeBracket(form.getAgeBracket());
        Address registrantAddress = registrant.getAddress();
        registrantAddress.setAddressLine1(form.getAddressLine1());
        registrantAddress.setAddressLine2(form.getAddressLine2());
        registrantAddress.setCityTown(form.getCityTown());
        CountryCode countryCode = registrantAddress.getCountryCode();
        countryCode.setCountryId(form.getCountryCodeCountryId());
        registrantAddress.setCountryCode(countryCode);
        AccomodationType accomType = conventionRegistrant.getAccomodationType();
        if (form.getAccomodationTypeId() == 0) {
            accomType.setAccomodationTypeId(5);
        } else {
            accomType.setAccomodationTypeId(form.getAccomodationTypeId());
        }
        RegistrantType regType = conventionRegistrant.getRegistrantType();
        regType.setRegistrantTypeId(form.getRegistrantTypeId());
        conventionRegistrant.setLengthOfStay(form.getLengthOfStay());
        if (form.getGuestArrived()) {
            RegistrantStatusType regStatus = new RegistrantStatusType();
            regStatus.setId(1);
            conventionRegistrant.setRegistrantStatusType(regStatus);
        }
        return conventionRegistrant;
    }

    /**
	 * 
	 * @param registrant
	 * @return
	 */
    private Transaction getRegistrantPaymentDetails(Registrant registrant) {
        Transaction transaction = null;
        try {
            transaction = TransactionAPIUtil.getLocalHome().create().getTransactionByRegistrantId(registrant);
        } catch (CreateException e) {
            log.fatal("Error creating an instance of TransactionLocalHome Interface");
            e.printStackTrace();
        } catch (NamingException e) {
            log.fatal("NamingException: Error getting an instance of TransactionLocalHome Interface");
            e.printStackTrace();
        }
        return transaction;
    }

    /**
	 * Retrieve a list of the countries
	 * @param locale The user locale
	 * @return List of countries
	 */
    private Collection getCountryListing(Locale locale) {
        return ComboMgr.getInstance().getComboContents(COUNTRY, locale);
    }

    /**
	 * Retrieve a list of the RegistrantTypes
	 * @param locale The user locale
	 * @return List of RegistrantTypes
	 */
    private Collection getRegistrantTypesListing(Locale locale) {
        return ComboMgr.getInstance().getComboContents(REGISTRANT_TYPE, locale);
    }

    /**
	 * Retrieve a list of the AccomodationTypes
	 * @param locale The user locale
	 * @return List of AccomodationTypes
	 */
    private Collection getAccomodationTypesListing(Locale locale) {
        return ComboMgr.getInstance().getComboContents(ACCOMODATION_TYPE, locale);
    }
}
