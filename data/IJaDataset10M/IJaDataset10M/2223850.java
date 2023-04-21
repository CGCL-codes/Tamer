package org.magicbox.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.magicbox.controller.util.WebUtils;
import org.magicbox.domain.Gruppo;
import org.magicbox.domain.GruppoImpl;
import org.magicbox.service.GruppiService;
import org.magicbox.util.Constant;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * Controller per creazione e modifica gruppi
 * 
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since jdk 1.6.0
 * @version 3.0
 */
public class GruppoFormController extends SimpleFormController {

    public GruppoFormController() {
        setSessionForm(true);
        setCommandClass(GruppoImpl.class);
        setCommandName(Constant.GRUPPO);
        setFormView("gruppi/gruppoForm");
        setSuccessView(Constant.REDIRECT_ELENCO_GRUPPI);
    }

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        StringTrimmerEditor trimmerEditor = new StringTrimmerEditor(false);
        binder.registerCustomEditor(String.class, trimmerEditor);
    }

    protected Object formBackingObject(HttpServletRequest req) throws Exception {
        return gruppiService.getGruppo(ServletRequestUtils.getLongParameter(req, Constant.ID, 0), WebUtils.getIdCentro(req));
    }

    public ModelAndView onSubmit(HttpServletRequest req, HttpServletResponse res, Object command, BindException errors) throws Exception {
        gruppiService.saveGruppo((Gruppo) command, WebUtils.getIdCentro(req));
        return new ModelAndView(getSuccessView());
    }

    public void setGruppiService(GruppiService gruppiService) {
        this.gruppiService = gruppiService;
    }

    private GruppiService gruppiService;
}
