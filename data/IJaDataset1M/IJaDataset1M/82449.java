package confregwar.Registration;

import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Form;
import com.sun.webui.jsf.model.Option;
import confreg.ejb.controls.ConfigurationControlLocal;
import confreg.ejb.controls.PaymentControlLocal;
import confreg.ejb.domain.BankCardType;
import confreg.ejb.domain.PaymentType;
import confreg.ejb.domain.ConferencePackStatus;
import confreg.ejb.dto.PaymentMethodDTO;
import confreg.ejb.exceptions.NonExistingUserException;
import confreg.ejb.exceptions.SecurityException;
import confreg.ejb.exceptions.ServiceException;
import confreg.ejb.exceptions.ValidationException;
import javax.faces.FacesException;
import confregwar.ApplicationBean1;
import confregwar.RequestBean1;
import confregwar.SessionBean1;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @author Rolee
 */
public class PaymentConfirmation extends AbstractPageBean {

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }

    private Form form1 = new Form();

    public Form getForm1() {
        return form1;
    }

    public void setForm1(Form f) {
        this.form1 = f;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public PaymentConfirmation() {
    }

    @EJB
    PaymentControlLocal paymentControlLocal;

    confreg.ejb.dto.ConferencePackSummary confPackSum;

    confreg.ejb.domain.ParticipantData participantData;

    public String getBasicPackage() {
        if (getConferencePackSummary() != null && getConferencePackSummary().getConferencePack() != null) {
            switch(getConferencePackSummary().getConferencePack().getBasicPackage()) {
                case CONFERENCESANDSATELLITE:
                    return "Conference and satellite events";
                case SATELLITEEVENTSONLY:
                    return "Satellite events only";
                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    public confreg.ejb.dto.ConferencePackSummary getConferencePackSummary() {
        if (confPackSum == null) {
            try {
                confPackSum = paymentControlLocal.getConferencePackWithSummary();
                return confPackSum;
            } catch (Exception e) {
            }
        }
        return confPackSum;
    }

    public confreg.ejb.domain.ParticipantData getParticipantData() {
        if (participantData == null) {
            try {
                participantData = paymentControlLocal.getParticipantData();
                return participantData;
            } catch (Exception e) {
            }
        }
        return participantData;
    }

    public boolean isExistsLunchOrder() {
        return getLunchOrder().length > 0;
    }

    public boolean isExistsProceedingOrder() {
        return getProceedingOrder().length > 0;
    }

    public boolean isExistsTicket() {
        return getTickets().length > 0;
    }

    public boolean isExistsWorkshop() {
        return getWorkshops().length > 0;
    }

    public boolean isExistsTutorial() {
        return getTutorials().length > 0;
    }

    public confreg.ejb.domain.LunchOrder[] getLunchOrder() {
        List<confreg.ejb.domain.LunchOrder> lunchList = new ArrayList<confreg.ejb.domain.LunchOrder>();
        if (getConferencePackSummary() == null) {
            return new confreg.ejb.domain.LunchOrder[0];
        }
        lunchList = getConferencePackSummary().getConferencePack().getLunchOrders();
        if (lunchList != null) {
            confreg.ejb.domain.LunchOrder[] lunchOrder = lunchList.toArray(new confreg.ejb.domain.LunchOrder[0]);
            return lunchOrder;
        } else {
            return new confreg.ejb.domain.LunchOrder[0];
        }
    }

    public confreg.ejb.domain.ProceedingOrder[] getProceedingOrder() {
        List<confreg.ejb.domain.ProceedingOrder> procList = new ArrayList<confreg.ejb.domain.ProceedingOrder>();
        if (getConferencePackSummary() == null) {
            return new confreg.ejb.domain.ProceedingOrder[0];
        }
        procList = getConferencePackSummary().getConferencePack().getProceedingOrders();
        if (procList != null) {
            confreg.ejb.domain.ProceedingOrder[] proceedingOrder = procList.toArray(new confreg.ejb.domain.ProceedingOrder[0]);
            return proceedingOrder;
        } else {
            return new confreg.ejb.domain.ProceedingOrder[0];
        }
    }

    public confreg.ejb.domain.Ticket[] getTickets() {
        List<confreg.ejb.domain.Ticket> ticketList = new ArrayList<confreg.ejb.domain.Ticket>();
        if (getConferencePackSummary() == null) {
            return new confreg.ejb.domain.Ticket[0];
        }
        ticketList = getConferencePackSummary().getConferencePack().getTickets();
        if (ticketList != null) {
            confreg.ejb.domain.Ticket[] tickets = ticketList.toArray(new confreg.ejb.domain.Ticket[0]);
            return tickets;
        } else {
            return new confreg.ejb.domain.Ticket[0];
        }
    }

    public confreg.ejb.domain.Tutorial[] getTutorials() {
        List<confreg.ejb.domain.Tutorial> tutorialList = new ArrayList<confreg.ejb.domain.Tutorial>();
        if (getConferencePackSummary() == null) {
            return new confreg.ejb.domain.Tutorial[0];
        }
        tutorialList = getConferencePackSummary().getConferencePack().getTutorials();
        if (tutorialList != null) {
            confreg.ejb.domain.Tutorial[] tutorials = tutorialList.toArray(new confreg.ejb.domain.Tutorial[0]);
            return tutorials;
        } else {
            return new confreg.ejb.domain.Tutorial[0];
        }
    }

    public confreg.ejb.domain.Workshop[] getWorkshops() {
        List<confreg.ejb.domain.Workshop> workshopList = new ArrayList<confreg.ejb.domain.Workshop>();
        if (getConferencePackSummary() == null) {
            return new confreg.ejb.domain.Workshop[0];
        }
        workshopList = getConferencePackSummary().getConferencePack().getWorkshops();
        if (workshopList != null) {
            confreg.ejb.domain.Workshop[] workshops = workshopList.toArray(new confreg.ejb.domain.Workshop[0]);
            return workshops;
        } else {
            return new confreg.ejb.domain.Workshop[0];
        }
    }

    protected confreg.util.AlertUtilBean getAlertUtilBean() {
        return (confreg.util.AlertUtilBean) getBean("AlertUtilBean");
    }

    @EJB
    ConfigurationControlLocal confControl;

    public confreg.ejb.domain.RegistrationStage registrationStage;

    public confreg.ejb.domain.RegistrationStage getRegistrationStage() {
        if (registrationStage == null) {
            try {
                registrationStage = confControl.getRegistrationStageAtDate(Calendar.getInstance().getTime());
            } catch (ServiceException ex) {
                getAlertUtilBean().setAlert("Service error!", ex.getMessage(), "error");
            }
        }
        return registrationStage;
    }

    public boolean isEarlyStage() {
        return getRegistrationStage() == confreg.ejb.domain.RegistrationStage.Early;
    }

    public boolean isNormalStage() {
        return getRegistrationStage() == confreg.ejb.domain.RegistrationStage.Normal;
    }

    public boolean isLateStage() {
        return getRegistrationStage() == confreg.ejb.domain.RegistrationStage.Late;
    }

    private confreg.ejb.dto.PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();

    public confreg.ejb.dto.PaymentMethodDTO getPaymentMethodDTO() {
        return paymentMethodDTO;
    }

    public void setPaymentMethodDTO(confreg.ejb.dto.PaymentMethodDTO paymentMethodDTO) {
        this.paymentMethodDTO = paymentMethodDTO;
    }

    @SuppressWarnings("empty-statement")
    public Option[] getCardTypeOptions() {
        return new Option[] { new Option(null, "---- Select Credit Card type ----"), new Option(BankCardType.AMEX, "Amex"), new Option(BankCardType.MAESTRO, "Maestro"), new Option(BankCardType.VISA, "Visa") };
    }

    public Option[] getPaymentTypeOptions() {
        return new Option[] { new Option(null, "---- Select payment type ----"), new Option(PaymentType.BANKTRANSFER, "Bank transfer"), new Option(PaymentType.CREDITCARDFAX, "Fax credit card info"), new Option(PaymentType.ONLINETRANSACTION, "Online Transaction") };
    }

    public boolean getSelectedPaymentMethod() {
        return getPaymentMethodDTO().getPaymentType() == PaymentType.ONLINETRANSACTION;
    }

    public boolean getConferencePackStatus() {
        if (getConferencePackSummary() != null) {
            return getConferencePackSummary().getConferencePack().getStatus() == ConferencePackStatus.MODIFYING;
        }
        return false;
    }

    /**
     * <p>Callback method that is called whenever a page is navigated to,
     * either directly via a URL, or indirectly via page navigation.
     * Customize this method to acquire resources that will be needed
     * for event handlers and lifecycle methods, whether or not this
     * page is performing post back processing.</p>
     * 
     * <p>Note that, if the current request is a postback, the property
     * values of the components do <strong>not</strong> represent any
     * values submitted with this request.  Instead, they represent the
     * property values that were saved for this view when it was rendered.</p>
     */
    @Override
    public void init() {
        super.init();
        try {
            _init();
        } catch (Exception e) {
            log("PaymentConfirmation Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }
        getConferencePackSummary();
        getParticipantData();
        if (getConferencePackSummary() != null) {
            if (getConferencePackSummary().getConferencePack().getStatus() == ConferencePackStatus.PAYING) {
                getAlertUtilBean().setAlert("Payment process has already started.", null, "information");
            }
            if (getConferencePackSummary().getConferencePack().getStatus() == ConferencePackStatus.CONFIRMED) {
                getAlertUtilBean().setAlert("Payment process has already completed", null, "information");
            }
        }
    }

    /**
     * <p>Callback method that is called after the component tree has been
     * restored, but before any event processing takes place.  This method
     * will <strong>only</strong> be called on a postback request that
     * is processing a form submit.  Customize this method to allocate
     * resources that will be required in your event handlers.</p>
     */
    @Override
    public void preprocess() {
    }

    /**
     * <p>Callback method that is called just before rendering takes place.
     * This method will <strong>only</strong> be called for the page that
     * will actually be rendered (and not, for example, on a page that
     * handled a postback and then navigated to a different page).  Customize
     * this method to allocate resources that will be required for rendering
     * this page.</p>
     */
    @Override
    public void prerender() {
    }

    /**
     * <p>Callback method that is called after rendering is completed for
     * this request, if <code>init()</code> was called (regardless of whether
     * or not this was the page that was actually rendered).  Customize this
     * method to release resources acquired in the <code>init()</code>,
     * <code>preprocess()</code>, or <code>prerender()</code> methods (or
     * acquired during execution of an event handler).</p>
     */
    @Override
    public void destroy() {
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1) getBean("ApplicationBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected RequestBean1 getRequestBean1() {
        return (RequestBean1) getBean("RequestBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
    }

    public String submitAction() {
        try {
            System.out.println(getPaymentMethodDTO().getCardNumber());
            System.out.println(getPaymentMethodDTO().getPaymentType());
            System.out.println(getPaymentMethodDTO().getCardType());
            System.out.println(getPaymentMethodDTO().getExpireDate());
            ConferencePackStatus status = paymentControlLocal.pay(getPaymentMethodDTO());
            if (status == ConferencePackStatus.PAYING) {
                getAlertUtilBean().setAlert("Payment process has started.", null, "information");
            } else if (status == ConferencePackStatus.CONFIRMED) {
                getAlertUtilBean().setAlert("Payment process has completed", null, "information");
            } else if (status == ConferencePackStatus.MODIFYING) {
                getAlertUtilBean().setAlert("Payment retracted.", null, "error");
            }
        } catch (ValidationException ex) {
            getAlertUtilBean().setAlert("Validation error!", ex.getMessage(), "error");
        } catch (ServiceException ex) {
            getAlertUtilBean().setAlert("Service error!", ex.getMessage(), "error");
        } catch (NonExistingUserException ex) {
            getAlertUtilBean().setAlert("User not exisits!", ex.getMessage(), "error");
        } catch (SecurityException ex) {
            getAlertUtilBean().setAlert("Security error!", ex.getMessage(), "error");
        }
        return null;
    }

    public void cardNumberValidate(FacesContext fc, UIComponent uic, Object o) {
        int length = o.toString().length();
        if (length < 13 || length > 16) {
            FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary("Credit card number must be at least 13 character, max 16 character!");
            message.setDetail("You gave only " + length + " characters!");
            throw new ValidatorException(message);
        }
        try {
            long value = Long.parseLong(o.toString());
        } catch (NumberFormatException ex) {
            FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary("Max. participant number is invalid!");
            throw new ValidatorException(message);
        }
    }
}
