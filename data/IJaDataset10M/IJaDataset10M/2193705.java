package pl.ehotelik.portal.web.controller.ajax.spa;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.ehotelik.portal.domain.spa.TreatmentPath;
import pl.ehotelik.portal.exception.ServiceException;
import pl.ehotelik.portal.service.spa.TreatmentPathService;
import pl.ehotelik.portal.web.controller.ControllerConstants;
import pl.ehotelik.portal.web.controller.SimpleViewController;

/**
 * Created by IntelliJ IDEA.
 * User: mkr
 * Date: Aug 19, 2010
 * Time: 11:32:39 PM
 * This is a representation of AddressAjaxController object.
 */
@Controller
@RequestMapping(ControllerConstants.MappingContext.SPA_TREATMENT_PATH_AJAX_CONTROLLER)
public class TreatmentPathAjaxController extends SimpleViewController {

    private static final Logger logger = Logger.getLogger(TreatmentPathAjaxController.class);

    @Autowired
    private TreatmentPathService treatmentPathService;

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public boolean deleteSelectedTreatmentPath(@RequestParam String id) {
        TreatmentPath treatmentPath;
        boolean status = false;
        long treatmentPathId;
        if (logger.isDebugEnabled()) {
            logger.debug("<------ deleteSelectedTreatmentPath");
        }
        try {
            treatmentPathId = Long.parseLong(id);
            treatmentPath = this.treatmentPathService.loadTreatmentPath(treatmentPathId);
            this.treatmentPathService.deleteTreatmentPackage(treatmentPath);
            status = true;
        } catch (NumberFormatException e) {
            logger.warn(String.format("NumberFormatException occurred during the execution of parse treatment path's id from String[ %s ] to Long value.", id), e);
            status = false;
        } catch (ServiceException e) {
            logger.warn(String.format("ServiceException occurred during the execution of load treatment path about id[ %s ] from database.", id), e);
            status = false;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("------> deleteSelectedTreatmentPath");
        }
        return status;
    }
}
