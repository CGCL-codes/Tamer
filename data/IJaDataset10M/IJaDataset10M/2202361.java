package org.javacoding.upupa.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.service.GenericManager;
import org.javacoding.upupa.model.entity.NestCard;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class NestCardController implements Controller {

    private final Log log = LogFactory.getLog(NestCardController.class);

    private GenericManager<NestCard, Long> nestCardManager = null;

    public void setNestCardManager(final GenericManager<NestCard, Long> nestCardManager) {
        this.nestCardManager = nestCardManager;
    }

    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        log.debug("entering 'handleRequest' method...");
        return new ModelAndView().addObject(nestCardManager.getAll());
    }
}
