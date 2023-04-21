package org.likken.web.actions;

import org.likken.web.*;
import org.likken.web.states.*;
import org.likken.core.LikkenContext;

/**
 * @author Stephane Boisson
 * @version $Revision: 1.3 $ $Date: 2001/09/20 11:28:14 $
 */
public class SubmitLogin extends AbstractAction {

    LikkenContext context = new LikkenContext();

    private String userId;

    private String password;

    public SubmitLogin() {
        super();
    }

    public void perform(LoginState theState) throws StateException {
        if ((userId == null) || (password == null)) {
        } else {
            context.setUser(userId, password);
        }
        if (context.isValid()) {
            theState.getController().setLikkenContext(context);
            throw new ActionFiredException(new Browse());
        }
        theState.setMessage("Login failed");
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String anUserId) {
        userId = anUserId;
    }

    public void setPassword(final String aPassword) {
        password = aPassword;
    }

    public void setServerName(final String aServerName) {
        context.setServer(aServerName);
    }

    public void setSuffix(final String aSuffix) {
        context.setSuffix(aSuffix);
    }

    public String toString() {
        return "SubmitLogin-" + context;
    }
}
