package net.sourceforge.kwaai.samples.form;

import net.sourceforge.markup.extensions.feedback.Feedback;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/form")
public class FormController {

    @RequestMapping
    public final String view(final HttpServletRequest request, final Model model) {
        model.addAttribute("form", new SimpleForm(request.getContextPath() + "/form", "hello", 20));
        return "/net/sourceforge/kwaai/samples/form/formView.html";
    }

    @RequestMapping(method = RequestMethod.POST)
    public final String submit(final HttpServletRequest request, @ModelAttribute(value = "form") final SimpleForm form, final Model model) {
        Feedback feedback = new Feedback();
        model.addAttribute("feedback", feedback);
        feedback.addSuccessMessage("Value: " + form.getValue());
        feedback.addSuccessMessage("Number: " + form.getNumber());
        return "/net/sourceforge/kwaai/samples/form/formView.html";
    }
}
