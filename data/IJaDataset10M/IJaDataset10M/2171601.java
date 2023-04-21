package ie.lawlor.amvc.example.explorer.login;

import ie.lawlor.amvc.event.Event;
import ie.lawlor.amvc.event.ModelUpdatePayload;
import ie.lawlor.amvc.patterns.standard.StandardModel;

public class LoginModel extends StandardModel {

    public static final int USERNAME_FIELD = 1;

    public static final int PASSWORD_FIELD = 2;

    String org;

    String username = "";

    String password = "";

    String message = "Enter username and password and click OK";

    /** Creates a new instance of LoginModel */
    public LoginModel(String name) {
        super(name);
    }

    public void doLogin(Event e) {
        this.org = (String) e.getPayload();
        username = "";
        password = "";
        message = "Enter username and password and click OK";
        fireUpdateView();
        fireShowView();
    }

    public void doOk(Event event) {
        logger.debug("Entering");
        System.out.println("Logging in with " + username + "/" + password);
        fireHideView();
        fire(new Event(getController().getEvent("loggedInLoginEvent"), this, "UserDetails"));
    }

    public void doUpdateModel(Event e) {
        ModelUpdatePayload modelUpdate = (ModelUpdatePayload) e.getPayload();
        switch(modelUpdate.fieldId) {
            case USERNAME_FIELD:
                doSetUsername((String) modelUpdate.value);
                break;
            case PASSWORD_FIELD:
                doSetPassword((String) modelUpdate.value);
                break;
            default:
                logger.warn("LoginModel could not handle update of field with id " + modelUpdate.fieldId);
        }
    }

    public void doSetUsername(String username) {
        this.username = username;
    }

    public void doSetPassword(String password) {
        this.password = password;
    }

    public void doCancel(Event e) {
        fireHideView();
        fire(new Event(getController().getEvent("cancelledLoginEvent"), this));
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMessage() {
        return message;
    }
}
