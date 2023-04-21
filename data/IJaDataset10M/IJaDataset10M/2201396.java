package se.infact.controller.tree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import se.infact.controller.ControllerUtil;
import se.infact.manager.InfactManager;

@Controller
@Secured({ "ROLE_EDITOR", "ROLE_WRITER" })
@RequestMapping(value = "/tree/node/moveto/{id}*")
public class MoveToController extends ControllerUtil {

    private InfactManager infactManager;

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView move(@PathVariable long id, @RequestParam long source, @RequestParam long destination) {
        infactManager.moveNode(source, destination);
        return new ModelAndView("redirect:/presentation/edit/" + id + ".html");
    }

    @RequestMapping(value = "/slim.html", method = RequestMethod.POST)
    public ModelAndView moveSlim(@PathVariable long id, @RequestParam long source, @RequestParam long destination) {
        infactManager.moveNode(source, destination);
        return new ModelAndView("redirect:/presentation/edit/" + id + "/slim.html");
    }

    @Autowired
    public void setInfactManager(InfactManager infactManager) {
        this.infactManager = infactManager;
    }
}
