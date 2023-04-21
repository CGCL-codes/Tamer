package net.taylor.identity.entity.editor;

import java.io.Serializable;
import javax.persistence.EntityManager;
import net.taylor.identity.entity.Group;
import net.taylor.identity.entity.User;
import net.taylor.identity.util.Encoder;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.international.StatusMessage.Severity;

/**
 * Create a new user account.
 * 
 * @author jgilbert01
 */
@Name("registerAction")
@Scope(ScopeType.PAGE)
public class RegisterAction implements Serializable {

    @In
    private EntityManager identityEntityManager;

    private User instance = new User();

    public User getInstance() {
        return instance;
    }

    @Factory(value = "registerUser", scope = ScopeType.PAGE)
    public User initUser() {
        return getInstance();
    }

    public String register() {
        if (getInstance().getPassword() == null || getInstance().getConfirmation() == null) {
            return null;
        }
        if (!getInstance().getPassword().equals(getInstance().getConfirmation())) {
            addErrorMessage("confirmation", "Password and confirmation do not match.");
            return null;
        }
        encode();
        Group user = identityEntityManager.find(Group.class, "User");
        getInstance().addMembership(user);
        identityEntityManager.persist(getInstance());
        return "home";
    }

    private void encode() {
        try {
            String pwd = Encoder.instance().encode(getInstance().getPassword());
            getInstance().setPassword(pwd);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addErrorMessage(String id, String message, Object... params) {
        StatusMessages.instance().addToControl(id, Severity.ERROR, message, params);
    }
}
