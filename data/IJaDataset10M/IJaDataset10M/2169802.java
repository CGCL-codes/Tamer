package net.sf.sail.webapp.presentation.web.controllers.offerings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.sail.webapp.dao.ObjectNotFoundException;
import net.sf.sail.webapp.domain.impl.OfferingParameters;
import net.sf.sail.webapp.service.offering.OfferingService;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * The controller for the create offering page
 * 
 * @author Hiroki Terashima
 * @version $Id: CreateOfferingController.java 941 2007-08-16 14:03:11Z laurel $
 */
public class CreateOfferingController extends SimpleFormController {

    protected OfferingService offeringService = null;

    /**
     * On submission of the createoffering form, an offering is created and saved to the data
     * store.
     * @throws Exception 
     * 
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object,
     *      org.springframework.validation.BindException)
     */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        OfferingParameters offeringParameters = (OfferingParameters) command;
        try {
            this.offeringService.createOffering(offeringParameters);
        } catch (ObjectNotFoundException e) {
            errors.rejectValue("curnitId", "error.curnit-not_found", new Object[] { offeringParameters.getCurnitId() }, "Curnit Not Found.");
            return showForm(request, response, errors);
        }
        return new ModelAndView(new RedirectView(getSuccessView()));
    }

    /**
	 * Sets the offeringService object
	 * 
	 * @param offeringService the offeringService to set
	 */
    public void setOfferingService(OfferingService offeringService) {
        this.offeringService = offeringService;
    }
}
