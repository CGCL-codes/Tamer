package com.spring66.training.web.springmvc;

import com.spring66.training.entity.Owner;
import com.spring66.training.service.Clinic;
import com.spring66.training.web.validator.OwnerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

/**
 *
 * @author TwinP
 */
@Controller
@RequestMapping("/addOwner.do")
@SessionAttributes(types = Owner.class)
public class AddOwnerForm {

    private final Clinic clinic;

    @Autowired
    public AddOwnerForm(Clinic clinic) {
        this.clinic = clinic;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String setupForm(Model model) {
        Owner owner = new Owner();
        model.addAttribute(owner);
        return "ownerForm";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(@ModelAttribute Owner owner, BindingResult result, SessionStatus status) {
        new OwnerValidator().validate(owner, result);
        if (result.hasErrors()) {
            return "ownerForm";
        } else {
            this.clinic.storeOwner(owner);
            status.setComplete();
            return "redirect:owner.do?ownerId=" + owner.getId();
        }
    }
}
