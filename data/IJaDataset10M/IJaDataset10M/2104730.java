package org.springframework.samples.petclinic.web;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Owner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.WebDataBinder;

/**
 * JavaBean Form controller that is used to search for <code>Owner</code>s by
 * last name.
 *
 * @author Juergen Hoeller
 * @author Ken Krebs
 */
@Controller
@RequestMapping("/findOwners.do")
public class FindOwnersForm {

    private final Clinic clinic;

    @Autowired
    public FindOwnersForm(Clinic clinic) {
        this.clinic = clinic;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields(new String[] { "id" });
    }

    @RequestMapping(method = RequestMethod.GET)
    public String setupForm(Model model) {
        model.addAttribute("owner", new Owner());
        return "findOwners";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(Owner owner, BindingResult result, Model model) {
        Collection<Owner> results = this.clinic.findOwners(owner.getLastName());
        if (results.size() < 1) {
            result.rejectValue("lastName", "notFound", "not found");
            return "findOwners";
        }
        if (results.size() > 1) {
            model.addAttribute("selections", results);
            return "owners";
        } else {
            owner = results.iterator().next();
            return "redirect:owner.do?ownerId=" + owner.getId();
        }
    }
}
