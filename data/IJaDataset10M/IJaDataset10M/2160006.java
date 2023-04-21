package com.codebitches.spruce.module.bb.web.spring;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import com.codebitches.spruce.module.bb.domain.local.SearchCriteria;
import com.codebitches.spruce.module.bb.domain.local.SprucebbConfiguration;
import com.codebitches.spruce.module.bb.domain.logic.IBBService;

/**
 * @author Stuart Eccles
 */
public class SearchFormController extends SimpleFormController {

    private static Log log = LogFactory.getLog(SearchFormController.class);

    private IBBService bbService;

    private SprucebbConfiguration sprucebbConfiguration;

    /**
	 * @param sprucebbConfiguration The sprucebbConfiguration to set.
	 */
    public void setSprucebbConfiguration(SprucebbConfiguration sprucebbConfiguration) {
        this.sprucebbConfiguration = sprucebbConfiguration;
    }

    /**
	 * @param bbService The bbService to set.
	 */
    public void setBbService(IBBService bbService) {
        this.bbService = bbService;
    }

    public SearchFormController() {
        setSessionForm(false);
        setValidateOnBinding(false);
        setCommandName("searchForm");
        setFormView("search");
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        SearchCriteria form = new SearchCriteria();
        return form;
    }

    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
        log.debug(errors);
    }

    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map model = new HashMap();
        Collection categories = bbService.viewAllCategories();
        model.put("categories", categories);
        return model;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        SearchCriteria form = (SearchCriteria) command;
        Collection posts = bbService.searchForPosts(form);
        ModelAndView mdv = super.onSubmit(request, response, command, errors);
        if (mdv != null) {
            mdv.addObject("criteria", form);
            mdv.addObject("posts", posts);
        }
        return mdv;
    }
}
