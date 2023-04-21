package mbis.web.struts.action;

import com.opensymphony.xwork2.Validateable;
import mbis.entity.MailSendAck;
import mbis.entity.PriceCategory;
import mbis.entity.person.Person;
import mbis.persistence.MailSendAckDAO;
import mbis.persistence.PersonDAO;
import mbis.service.confirmation.ConfirmationAlreadyConfirmedException;
import mbis.service.confirmation.ExpiredConfirmationException;
import mbis.service.confirmation.InvalidConfirmationException;
import mbis.service.confirmation.action.RegistrationService;
import org.apache.struts2.interceptor.validation.SkipValidation;
import javax.annotation.Resource;
import java.util.Locale;

/**
 * User: tmichalec
 * Date: Nov 21, 2008
 * Time: 2:44:55 PM
 */
public class RegistrationAction extends MyActionSupport implements Validateable, RegistrationValidation {

    @Resource
    private RegistrationService registrationService;

    @Resource
    private MailSendAckDAO mailSendAckDAO;

    @Resource
    private PersonDAO personDAO;

    private String login;

    private String password;

    private String passwordRetyped;

    private String email;

    private String name;

    private String surname;

    private String tel;

    private PriceCategory priceCategory;

    private String proceed;

    private Long mailAckId;

    private long id;

    private String key;

    @SkipValidation()
    public String execute() throws Exception {
        if (priceCategory == null) {
            priceCategory = PriceCategory.BASE;
        }
        return INPUT;
    }

    public String processForm() {
        if (getText("register").equals(proceed)) {
            Person person = new Person();
            person.setLogin(login);
            person.setEmail(email);
            person.setName(name);
            person.setSurname(surname);
            person.setTel(tel);
            person.setPriceCategory(priceCategory);
            person.setLocale(new Locale("sk"));
            mailAckId = registrationService.registerNewCustomer(person, password);
            return SUCCESS;
        }
        return INPUT;
    }

    @SkipValidation()
    public String activate() {
        try {
            registrationService.activatePerson(id, key);
            addActionMessage(getText("registrationAction.activate.success"));
        } catch (InvalidConfirmationException e) {
            addActionMessage(getText("invalidConfirmation"));
        } catch (ExpiredConfirmationException e) {
            addActionMessage(getText("expiredConfirmation"));
        } catch (ConfirmationAlreadyConfirmedException e) {
            addActionMessage(getText("confirmationAlreadyConfirmed"));
        }
        return ActionResult.MESSAGE;
    }

    public MailSendAck getMailSendAck() {
        return mailSendAckDAO.findById(mailAckId);
    }

    public void validate() {
        super.validate();
        if (password != null && !password.equals(passwordRetyped)) {
            addFieldError("password", getText("registrationAction.incorrectPasswords"));
        }
        if (login != null && personDAO.existsLogin(login)) {
            addFieldError("login", getText("registrationAction.loginAlreadyInUse"));
        }
        if (email != null && personDAO.existsEmail(email)) {
            addFieldError("email", getText("registrationAction.emailAlreadyInUse"));
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRetyped() {
        return passwordRetyped;
    }

    public void setPasswordRetyped(String passwordRetyped) {
        this.passwordRetyped = passwordRetyped;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public PriceCategory getPriceCategory() {
        return priceCategory;
    }

    public void setPriceCategory(PriceCategory priceCategory) {
        this.priceCategory = priceCategory;
    }

    public String getProceed() {
        return proceed;
    }

    public void setProceed(String proceed) {
        this.proceed = proceed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
