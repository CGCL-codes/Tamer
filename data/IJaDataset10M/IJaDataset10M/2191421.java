package edu.vt.middleware.gator.web;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import edu.vt.middleware.gator.AppenderConfig;
import edu.vt.middleware.gator.ProjectConfig;
import edu.vt.middleware.gator.web.validation.AppenderCopyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Handles copying an existing appender to a new one with a different name.
 *
 * @author  Middleware Services
 * @version  $Revision: 1421 $
 */
@Controller
@RequestMapping("/secure")
@SessionAttributes({ "spec", "project", "appenders" })
public class AppenderCopyFormController extends AbstractFormController {

    public static final String VIEW_NAME = "appenderCopy";

    @Autowired
    @NotNull
    private AppenderCopyValidator validator;

    @InitBinder
    public void initValidator(final WebDataBinder binder) {
        if (binder.getTarget() != null && validator.supports(binder.getTarget().getClass())) {
            binder.setValidator(validator);
        }
    }

    @RequestMapping(value = "/project/{projectName}/appender/copy.html", method = RequestMethod.GET)
    public String getAppenders(@PathVariable("projectName") final String projectName, final Model model) {
        final ProjectConfig project = getProject(projectName);
        model.addAttribute("project", project);
        model.addAttribute("appenders", project.getAppenders());
        model.addAttribute("spec", new CopySpec(AppenderConfig.class));
        return VIEW_NAME;
    }

    @RequestMapping(value = "/project/{projectName}/appender/copy.html", method = RequestMethod.POST)
    public String copy(@Valid @ModelAttribute("spec") final CopySpec spec, final BindingResult result) {
        if (result.hasErrors()) {
            return VIEW_NAME;
        }
        final AppenderConfig source = configManager.find(AppenderConfig.class, spec.getSourceId());
        final ProjectConfig project = source.getProject();
        final AppenderConfig newAppender = ControllerHelper.cloneAppender(source);
        newAppender.setName(spec.getName());
        newAppender.setProject(project);
        project.addAppender(newAppender);
        configManager.save(project);
        return String.format("redirect:/secure/project/%s/edit.html#appender", project.getName());
    }
}
