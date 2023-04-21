package findgoshow.Users;

import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.PasswordField;
import com.sun.webui.jsf.component.TextField;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import Helpers.Strings;
import java.util.List;
import java.util.regex.Pattern;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import findgoshow.ApplicationBean1;
import findgoshow.RequestBean1;
import findgoshow.SessionBean1;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @version Register.java
 * @version Created on 2009-05-02, 19:36:06
 * @author roger
 */
public class Register extends AbstractPageBean {

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }

    private TextField login = new TextField();

    public TextField getLogin() {
        return login;
    }

    public void setLogin(TextField tf) {
        this.login = tf;
    }

    private TextField fname = new TextField();

    public TextField getFname() {
        return fname;
    }

    public void setFname(TextField tf) {
        this.fname = tf;
    }

    private TextField lname = new TextField();

    public TextField getLname() {
        return lname;
    }

    public void setLname(TextField tf) {
        this.lname = tf;
    }

    private TextField email = new TextField();

    public TextField getEmail() {
        return email;
    }

    public void setEmail(TextField tf) {
        this.email = tf;
    }

    private PasswordField password = new PasswordField();

    public PasswordField getPassword() {
        return password;
    }

    public void setPassword(PasswordField pf) {
        this.password = pf;
    }

    private PasswordField retypepassword = new PasswordField();

    public PasswordField getRetypepassword() {
        return retypepassword;
    }

    public void setRetypepassword(PasswordField pf) {
        this.retypepassword = pf;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public Register() {
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
            log("Register Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
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
    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
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

    public String register_action() {
        findgoshow.controller.User um;
        EntityManager em;
        EntityManagerFactory emf;
        String passwordString = (String) this.password.getText();
        if (!passwordString.equals((String) this.retypepassword.getText())) {
            error("Passwords must match one another.");
            return null;
        }
        String passwordMD5 = Strings.encode(passwordString, "MD5");
        String passwordSHA1 = Strings.encode(passwordString, "SHA-1");
        if (passwordMD5 == null || passwordSHA1 == null) {
            error("Error while encoding password!");
            return null;
        }
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = req.getRemoteAddr();
        findgoshow.entity.User user = new findgoshow.entity.User();
        user.prepareToRegistration();
        user.setLogin((String) this.login.getText());
        user.setFirstName((String) fname.getText());
        user.setLastName((String) lname.getText());
        user.setPasswordMD5(passwordMD5);
        user.setPasswordSHA1(passwordSHA1);
        user.setEmail((String) this.email.getText());
        user.setAuthorizationToken(user.generateAuthorizationToken());
        user.setIp(ipAddress);
        user.setLanguage(0L);
        user.setStatus("unauthorized");
        emf = Persistence.createEntityManagerFactory("findgoshowPU");
        em = emf.createEntityManager();
        um = new findgoshow.controller.User(emf);
        um.createUser(user);
        um.close();
        em.close();
        emf.close();
        findgoshow.controller.GalleryUser gum;
        findgoshow.entity.GalleryUser guser = new findgoshow.entity.GalleryUser();
        guser.setAdditionalPhotos(100);
        guser.setPhotoCommentsTree(true);
        guser.setShowPhotoComments("public");
        guser.setShowPhotos("public");
        guser.setWantsPhotoComments("fromall");
        emf = Persistence.createEntityManagerFactory("findgoshowPU");
        em = emf.createEntityManager();
        gum = new findgoshow.controller.GalleryUser(emf);
        gum.createUser(guser);
        gum.close();
        em.close();
        emf.close();
        return null;
    }

    @SuppressWarnings("unchecked")
    public void login_validate(FacesContext context, UIComponent component, Object value) {
        String loginValue = (String) value;
        boolean wrong = false;
        if (Pattern.matches("A-Za-z0-9", loginValue) == false) {
        }
        findgoshow.controller.User um;
        EntityManager em;
        EntityManagerFactory emf;
        emf = Persistence.createEntityManagerFactory("findgoshowPU");
        em = emf.createEntityManager();
        um = new findgoshow.controller.User(emf);
        Query query = em.createNamedQuery("User.findByLogin");
        query.setParameter("login", loginValue);
        List<findgoshow.entity.User> users;
        users = query.getResultList();
        if (users != null && users.size() != 0) {
            wrong = true;
        }
        um.close();
        em.close();
        emf.close();
        if (wrong) {
            throw new ValidatorException(new FacesMessage("User " + loginValue + " already exists."));
        }
    }

    public void fname_validate(FacesContext context, UIComponent component, Object value) {
        if (true) {
        }
    }

    public void lname_validate(FacesContext context, UIComponent component, Object value) {
        if (true) {
        }
    }

    public void email_validate(FacesContext context, UIComponent component, Object value) {
        if (true) {
        }
    }

    public void password_validate(FacesContext context, UIComponent component, Object value) {
        if (true) {
        }
    }

    public void retypepassword_validate(FacesContext context, UIComponent component, Object value) {
        if (true) {
        }
    }
}
