package kz.simplex.photobox.email;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.Renderer;

@Stateful
@Name("registrationMailer")
@AutoCreate
@Scope(ScopeType.APPLICATION)
public class RegistrationMailer implements IRegistrationMailer {

    @In(create = true)
    private Renderer renderer;

    @Observer("passwordReset")
    public void sendPasswordResetEmail() {
        renderer.render("/email/passwordReset.xhtml");
    }

    @Remove
    @Destroy
    public void destroy() {
    }
}
