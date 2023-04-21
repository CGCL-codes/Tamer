package net.sourceforge.gateway.sstp.faces.beans;

import java.io.Serializable;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import net.sourceforge.gateway.sstp.databases.UserManager;
import net.sourceforge.gateway.sstp.databases.tables.UserDTO;

public class LoginBean implements Serializable {

    protected static final Logger LOG = Logger.getLogger("net.sourceforge.gateway");

    private static final long serialVersionUID = 1;

    private UserDTO user;

    private String username;

    private String password;

    public LoginBean() {
        this.setUser(null);
        this.setUsername("");
        this.setPassword("");
    }

    @SuppressWarnings("deprecation")
    public String login() {
        if (UserManager.authenticateWebClient(this.getUsername().toUpperCase(), this.getPassword())) {
            this.setUser(UserManager.getUser(this.getUsername().toUpperCase()));
            FacesContext context = FacesContext.getCurrentInstance();
            context.getApplication().createValueBinding("#{sessionScope.user}").setValue(context, this.getUser());
            return "success";
        } else {
            this.setUser(null);
            return "failure";
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public UserDTO getUser() {
        return this.user;
    }
}
