package news_rack.user_interface;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * Form bean for the register page with the following fields
 * <ul>
 * <li><b>name</b> - Entered real name
 * <li><b>username</b> - Entered username
 * <li><b>password</b> - Entered password
 * <li><b>passwordConfirm</b> - Password confirmation value
 * <li><b>emailid</b> - Entered Email ID
 * </ul>
 *
 * @author Subramanya Sastry
 */
public final class RegisterForm extends ValidatorForm {

    /** The real name.  */
    private String name = null;

    /** The username.  */
    private String username = null;

    /** The password.  */
    private String password = null;

    /** The passwordConfirm.  */
    private String passwordConfirm = null;

    /** The email id.  */
    private String emailid = null;

    /** Trick to prevent automated registration by spam bots */
    private String humanSumValue = null;

    /** Trick to prevent automated registration by spam bots */
    private String humanSumResponse = null;

    /** Return the real name.  */
    public String getName() {
        return (this.name);
    }

    /**
    * Set the real name.
    *
    * @param name The real name of the user
    */
    public void setName(String name) {
        this.name = name;
    }

    /** Return the username.  */
    public String getUsername() {
        return (this.username);
    }

    /**
    * Set the username.
    *
    * @param username The new username
    */
    public void setUsername(String username) {
        this.username = username;
    }

    /** Return the password.  */
    public String getPassword() {
        return (this.password);
    }

    /**
    * Set the password.
    *
    * @param password The new password
    */
    public void setPassword(String password) {
        this.password = password;
    }

    /** Return the confirmation password.  */
    public String getPasswordConfirm() {
        return (this.passwordConfirm);
    }

    /**
    * Set the confirmation password.
    *
    * @param pc The password (repeated) entered
    */
    public void setPasswordConfirm(String pc) {
        this.passwordConfirm = pc;
    }

    /** Return the emaild.  */
    public String getEmailid() {
        return (this.emailid);
    }

    /**
    * Set the emailid.
    *
    * @param emailid Email ID of the user
    */
    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    /** Return the human sum value.  */
    public String getHumanSumValue() {
        return (this.humanSumValue);
    }

    /** Set the human sum value.  */
    public void setHumanSumValue(String s) {
        this.humanSumValue = s;
    }

    /** Return the human sum response.  */
    public String getHumanSumResponse() {
        return (this.humanSumResponse);
    }

    /** Set the human sum response */
    public void setHumanSumResponse(String s) {
        this.humanSumResponse = s;
    }

    /**
    * Reset all properties to their default values.
    *
    * @param mapping The mapping used to select this instance
    * @param request The servlet request we are processing
    */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        setName(null);
        setUsername(null);
        setPassword(null);
        setPasswordConfirm(null);
        setEmailid(null);
        setHumanSumValue(null);
        setHumanSumResponse(null);
    }

    private String ValidateUserId(String uid) {
        int n = uid.length();
        char[] cs = uid.toCharArray();
        for (int i = 0; i < n; i++) {
            char c = cs[i];
            if (Character.isWhitespace(c)) {
                return ("'" + uid + "' encountered as user name.  White-space character '" + c + "' at position " + i + " cannot be used in a user name!");
            }
            if (((i == 0) && !Character.isUnicodeIdentifierStart(c)) || ((i > 0) && !Character.isUnicodeIdentifierPart(c) && (c != '_'))) {
                return (uid + " encountered as user name. Character '" + c + "' at position " + i + " cannot be used in a user name!  They have to start with letters/numbers and have either letters, numbers, or _ as its other characters!");
            }
        }
        return null;
    }

    /**
     * Ensure that both fields have been input.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        System.out.println("name     -- " + name);
        System.out.println("username -- " + username);
        System.out.println("password -- " + password);
        System.out.println("pc       -- " + passwordConfirm);
        System.out.println("emailid  -- " + emailid);
        if ((humanSumResponse == null) || (Integer.parseInt(humanSumValue) != Integer.parseInt(humanSumResponse))) {
            errors.add("humansum", new ActionMessage("error.message", "If you are a human trying to register, please enter " + humanSumValue + " in the box asking for the sum."));
        }
        String emsg = ValidateUserId(username);
        if (emsg != null) errors.add("password", new ActionMessage("error.message", emsg)); else if ((passwordConfirm == null) || !passwordConfirm.equals(password)) errors.add("password", new ActionMessage("error.password.match"));
        if (this.emailid.indexOf("@mail.com") > 0) errors.add("email", new ActionMessage("message", "SPAMMER: Go away! Rejecting email id: " + emailid));
        return errors;
    }
}
