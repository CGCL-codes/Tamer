package bg.price.comparator.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import bg.price.comparator.dao.ComparatorDaoException;
import bg.price.comparator.domain.Category;
import bg.price.comparator.domain.propeditors.CategoryPropertyEditor;
import bg.price.comparator.service.CategoriesFacade;

@Controller
@RequestMapping(value = "/category")
public class CategoriesController {

    private CategoriesFacade categoriesFacade;

    IvkoLogger log = new IvkoLogger();

    @Autowired
    public CategoriesController(CategoriesFacade categoriesService) {
        this.categoriesFacade = categoriesService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showAllCategories() throws ComparatorDaoException {
        log.info("Showing all categories");
        ModelAndView mav = new ModelAndView("/category/showAll");
        List<Category> categories = categoriesFacade.listCategories();
        mav.addObject("categories", categories);
        log.info(categories.size() + " loaded and sent to the model");
        return mav;
    }

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.GET)
    public String showCategory(@PathVariable("categoryId") int id, ModelMap model) throws ComparatorDaoException {
        log.info("Requesting category with ID " + id);
        Category cat = categoriesFacade.getCategory(id);
        model.addAttribute("category", cat);
        return "/category/show";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createCategoryForm(ModelMap model) throws ComparatorDaoException {
        model.addAttribute("categories", categoriesFacade.listCategories());
        model.addAttribute("category", new Category());
        return "/category/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createCategory(Category category) throws Exception {
        categoriesFacade.addCategory(category);
        return "redirect:/category";
    }

    @ExceptionHandler(ComparatorDaoException.class)
    public ModelAndView handleException(ComparatorDaoException cde) {
        ModelAndView mav = new ModelAndView("category/error");
        mav.addObject("message", cde.getMessage());
        return mav;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(Category.class, new CategoryPropertyEditor(categoriesFacade));
    }
}
