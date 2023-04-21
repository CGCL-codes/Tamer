package org.jraptor.petclinic.web;

import org.jraptor.petclinic.model.Owner;
import org.jraptor.petclinic.validation.OwnerValidator;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

/**
 * The <code>AddOwnerForm</code> class is a controller that is used to add a new owner to the database.
 * 
 * @author <a href="mailto:goran.oberg@jraptor.org">Goran Oberg</a>
 * @version $Rev: 114 $ $Date: 2008-12-09 16:46:40 -0500 (Tue, 09 Dec 2008) $
 */
@Controller
@RequestMapping("/addOwner.do")
@SessionAttributes("owner")
@Transactional
public class AddOwnerForm {

    @RequestMapping(method = RequestMethod.GET)
    public String setupForm(Model model) {
        Owner owner = new Owner();
        model.addAttribute(owner);
        return "ownerForm";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(@ModelAttribute("owner") Owner owner, BindingResult result, SessionStatus status) {
        if (OwnerValidator.validate(owner, result).hasErrors()) {
            return "ownerForm";
        } else {
            Owner merged = owner.merge().flush();
            owner.setId(merged.getId());
            status.setComplete();
            return "redirect:owner.do?ownerId=" + owner.getId();
        }
    }
}
