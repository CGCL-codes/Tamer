package org.vardb.web.spring;

import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.vardb.resources.dao.CHelp;

@Controller
public class CHelpController extends CAbstractController {

    @RequestMapping("/help/index.html")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
        List<CHelp> list = m_vardbService.getResourceService().getDao().getHelp();
        model.addAttribute("list", list);
        return "help-index";
    }

    @RequestMapping("/help/tutorial.html")
    public String tutorial(Model model, HttpServletRequest request, HttpServletResponse response) {
        return "help-tutorial";
    }

    @RequestMapping("/help/help.html")
    public String help(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam("identifier") String identifier) {
        CHelp help = m_vardbService.getResourceService().getDao().getHelp(identifier);
        model.addAttribute("help", help);
        return "help-help";
    }

    @RequestMapping("/ajax/help.html")
    public String helpAjax(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam("identifier") String identifier) {
        CHelp help = m_vardbService.getResourceService().getDao().getHelp(identifier);
        model.addAttribute("help", help);
        return "ajax-help";
    }
}
