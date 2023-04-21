package org.gloudy.page.controller;

import com.google.appengine.api.datastore.Key;
import org.gloudy.page.model.GoogleDatastoreKeyEditor;
import org.gloudy.page.model.Page;
import org.gloudy.page.model.PageImpl;
import org.gloudy.page.model.PagePropertyEditor;
import org.gloudy.page.model.PageValidator;
import org.gloudy.page.service.PageService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author pcorne
 */
@Controller
public class PageEditController {

    private static final Logger log = Logger.getLogger(PageEditController.class.getName());

    private PageService pageService;

    private PageValidator validator;

    @RequestMapping("/admin/view/page/edit")
    public String showPageEditor(Model model, @RequestParam(required = true, value = "id") Key id) {
        log.log(Level.WARNING, "Showing PageEditor");
        model.addAttribute("title", "Edit page");
        model.addAttribute("page", pageService.getPageById(id));
        model.addAttribute("pages", pageService.getRootPage().unfoldPageTree());
        return "page/admin/editpage";
    }

    @RequestMapping("/admin/view/page/add")
    public String showAddPageEditor(Model model) {
        log.log(Level.WARNING, "Showing AddPageEditor");
        PageImpl page = null;
        page = new PageImpl("");
        model.addAttribute("title", "Add new page");
        model.addAttribute("page", page);
        model.addAttribute("pages", pageService.getRootPage().unfoldPageTree());
        return "page/admin/editpage";
    }

    @RequestMapping(value = { "/admin/view/page/add", "/admin/view/page/edit" }, method = RequestMethod.POST)
    public String editorAction(@ModelAttribute("page") PageImpl page, BindingResult errors, Model model) {
        validator.validate(page, errors);
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add new page");
            model.addAttribute("page", page);
            model.addAttribute("pages", pageService.getRootPage().unfoldPageTree());
            log.info("NICHT VALIDE! >> " + errors.getErrorCount() + " " + errors.getAllErrors().get(0).getDefaultMessage());
            for (int i = 0; i < errors.getErrorCount(); i++) {
                log.info("NICHT VALIDE! >> " + i + " " + errors.getAllErrors().get(i).getDefaultMessage());
            }
            return "page/admin/editpage";
        }
        log.info("VALIDE!");
        log.info(ToStringBuilder.reflectionToString(page));
        pageService.save(page);
        return "redirect:/admin/page/view";
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(Page.class, new PagePropertyEditor(pageService));
        dataBinder.registerCustomEditor(Key.class, new GoogleDatastoreKeyEditor());
    }

    @Autowired
    public void setPageService(PageService pageService) {
        this.pageService = pageService;
    }

    @Autowired
    public void setValidator(PageValidator newValidator) {
        validator = newValidator;
    }
}
