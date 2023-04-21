package se.infact.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import se.infact.controller.ControllerUtil;
import se.infact.manager.InfactManager;

@Controller
@Secured({ "ROLE_ADMIN" })
@RequestMapping(value = "/admin/group/deleteconfirm/{id}.html")
public class DeleteGroupConfirmController extends ControllerUtil {

    private InfactManager infactManager;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getNodeView(@PathVariable long id) {
        ModelAndView modelAndView = getModelAndView("admin/group/deleteconfirm", "admin/user/menu");
        modelAndView.addObject("group", infactManager.loadGroup(id));
        return modelAndView;
    }

    @Autowired
    public void setInfactManager(InfactManager infactManager) {
        this.infactManager = infactManager;
    }
}
