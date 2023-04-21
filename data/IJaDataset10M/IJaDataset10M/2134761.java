package net.larsbehnke.petclinicplus.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import net.larsbehnke.petclinicplus.model.Owner;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * JavaBean Form controller that is used to edit an existing <code>Owner</code>.
 * @author Ken Krebs
 */
public class EditOwnerForm extends AbstractClinicForm {

    /**
     * Creates and initializes the form. The command name is set. This name must
     * be used in the form (JSP, Freemarker page etc.) when binding the command
     * object.
     */
    public EditOwnerForm() {
        setCommandName("owner");
        setSessionForm(true);
        setBindOnNewForm(true);
    }

    /**
     * Method forms a copy of an existing Owner for editing. This method returns
     * the command object that is populated with request parameters / input
     * field values. An alternative (yet less flexible) way to register an
     * command is calling <code>setCommandClass()</code> from the controller's
     * constructor.
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        return getClinic().loadOwner(ServletRequestUtils.getRequiredIntParameter(request, "ownerId"));
    }

    /** Method updates an existing Owner. */
    protected ModelAndView onSubmit(Object command) throws ServletException {
        Owner owner = (Owner) command;
        owner = getClinic().storeOwner(owner);
        return new ModelAndView(getSuccessView(), "ownerId", owner.getId());
    }
}
