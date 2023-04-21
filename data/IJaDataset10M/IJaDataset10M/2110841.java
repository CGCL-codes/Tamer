package org.magicbox.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.magicbox.controller.util.WebUtils;
import org.magicbox.domain.AmministratoreImpl;
import org.magicbox.service.AmministratoriCentriManager;
import org.magicbox.util.Constant;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * Controller per inserimento aggiornamento dati coordinatore centro
 * 
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since jdk 1.6.0
 * @version 3.0
 */
public class ResponsabileCentroFormController extends SimpleFormController {

    public ResponsabileCentroFormController() {
        setCommandClass(AmministratoreImpl.class);
        setCommandName(Constant.ADMIN_CENTRO);
        setFormView("centro/adminCentroForm");
        setSuccessView(Constant.REDIRECT_INFO_ADMIN_CENTRO);
    }

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        StringTrimmerEditor trimmerEditor = new StringTrimmerEditor(false);
        binder.registerCustomEditor(String.class, trimmerEditor);
    }

    protected Object formBackingObject(HttpServletRequest req) throws Exception {
        if (ServletRequestUtils.getLongParameter(req, Constant.ID, 0) != 0) {
            return amministratoriManager.getAmministratoreCentro(WebUtils.getIdCentro(req));
        } else {
            return new AmministratoreImpl();
        }
    }

    public ModelAndView onSubmit(HttpServletRequest req, HttpServletResponse res, Object command, BindException errors) throws Exception {
        AmministratoreImpl admin = (AmministratoreImpl) command;
        admin.setIdCentro(WebUtils.getIdCentro(req));
        amministratoriManager.updateAmministratoreCentro(admin);
        return new ModelAndView(getSuccessView());
    }

    public void setAmministratoriCentriManager(AmministratoriCentriManager amministratoriManager) {
        this.amministratoriManager = amministratoriManager;
    }

    private AmministratoriCentriManager amministratoriManager;
}
