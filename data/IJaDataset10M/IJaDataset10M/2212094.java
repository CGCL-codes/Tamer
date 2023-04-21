package org.mol.web.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mol.service.MarketService;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class SupportFormController extends SimpleFormController {

    private MarketService marketService;

    private MessageSource messageSource;

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        marketService.insert(command);
        ModelAndView mav = new ModelAndView(getSuccessView());
        mav.addObject("message", getMessageSource().getMessage("insert.success", null, "Insert ok!", null));
        return mav;
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        return super.referenceData(request, command, errors);
    }

    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors, Map controlModel) throws Exception {
        return super.showForm(request, response, errors, controlModel);
    }

    public MarketService getMarketService() {
        return marketService;
    }

    public void setMarketService(MarketService marketService) {
        this.marketService = marketService;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
