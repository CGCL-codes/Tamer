package org.tolven.web.vestibule;

import java.security.Principal;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.tolven.core.KeyUtility;
import org.tolven.web.security.AbstractVestibule;
import org.tolven.web.security.VestibuleException;

public class UserPrivateKeyVestibule extends AbstractVestibule {

    public static String VESTIBULE_NAME = "org.tolven.vestibule.userprivatekey";

    @Override
    public void abort(ServletRequest servletRequest) {
    }

    @Override
    public void enter(ServletRequest servletRequest) {
    }

    @Override
    public void exit(ServletRequest servletRequest) {
    }

    @Override
    public String getName() {
        return VESTIBULE_NAME;
    }

    @Override
    public String validate(ServletRequest servletRequest) throws VestibuleException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Principal principal = request.getUserPrincipal();
        String principalName = principal.getName();
        if (KeyUtility.getUserPrivateKey(principalName) == null) {
            throw new VestibuleException("No UserPrivateKey found for: " + principalName, "/vestibule/notregistered.xhtml");
        } else {
            return null;
        }
    }
}
