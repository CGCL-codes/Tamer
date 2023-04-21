package com.unsins.web.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import com.unsins.domains.resource.Dictionary;
import com.unsins.domains.resource.IDictionaryService;

/**
 * 类说明
 * 
 * @author odpsoft
 * @create 2008 2008-11-6
 */
public class DictionaryController extends AbstractController {

    private IDictionaryService dictService;

    private static final Logger logger = Logger.getLogger(DictionaryController.class);

    public void setDictService(IDictionaryService dictService) {
        this.dictService = dictService;
    }

    public DictionaryController() {
        this.setSupportedMethods(new String[] { METHOD_GET });
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug(request.getMethod());
        }
        return new ModelAndView("test", "dictlist", dictService.findAll(Dictionary.class));
    }
}
