package pl.ehotelik.portal.web.controller.hotel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.ehotelik.portal.domain.JobPosition;
import pl.ehotelik.portal.exception.ServiceException;
import pl.ehotelik.portal.exception.ValidationException;
import pl.ehotelik.portal.service.JobPositionService;
import pl.ehotelik.portal.web.controller.ControllerConstants;
import pl.ehotelik.portal.web.controller.SimpleFormController;
import javax.servlet.ServletException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mkr
 * Date: Aug 18, 2010
 * Time: 7:56:32 PM
 * This is a representation of JobPositionFormController object.
 */
@Controller
@SessionAttributes("cmd")
@RequestMapping(ControllerConstants.MappingContext.JOB_POSITION_FORM_CONTROLLER)
public class JobPositionFormController extends SimpleFormController<JobPosition> {

    @Autowired
    private JobPositionService jobPositionService;

    public JobPositionFormController() {
        super(ControllerConstants.MappingContext.JOB_POSITION_FORM_CONTROLLER);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String setupForm(@RequestParam(value = "id", required = false) String id, Model model) throws ServletException {
        JobPosition jobPosition;
        long jobPositionId;
        if (logger.isDebugEnabled()) {
            logger.debug("<------ setupForm");
        }
        if (id == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("prepare to create new object");
            }
            jobPosition = new JobPosition();
            model.addAttribute(ControllerConstants.ModelAttribute.COMMAND_OBJECT, jobPosition);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("prepare to load object");
            }
            try {
                jobPositionId = Long.parseLong(id);
                jobPosition = this.jobPositionService.loadJobPosition(jobPositionId);
            } catch (NumberFormatException e) {
                logger.warn(String.format("NumberFormatException occurred during the execution of parse job position's id from String[ %s ] to Long value.", id), e);
                jobPosition = new JobPosition();
            } catch (ServiceException e) {
                logger.warn(String.format("ServiceException occurred during the execution of load job position about id[ %s ] from database.", id), e);
                jobPosition = new JobPosition();
            }
            model.addAttribute(ControllerConstants.ModelAttribute.COMMAND_OBJECT, jobPosition);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("------> setupForm");
        }
        return super.setupForm(model);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String submitFormAction(@ModelAttribute(ControllerConstants.ModelAttribute.COMMAND_OBJECT) JobPosition cmd, BindingResult result, Model model) throws ServletException {
        String view;
        Long jobPositionId;
        if (logger.isDebugEnabled()) {
            logger.debug("<------ submitFormAction");
        }
        try {
            view = super.defaultSubmitFormAction(cmd, result, model);
        } catch (ValidationException e) {
            logger.warn("ValidationException occurred.", e);
            return this.getFormView();
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(cmd.toString()));
        }
        try {
            jobPositionId = this.jobPositionService.saveJobPosition(cmd);
            if (logger.isInfoEnabled()) {
                logger.info(String.format("The new job position with id: %d was saved correctly.", jobPositionId));
            }
            view = getSuccessView();
        } catch (ServiceException e) {
            logger.error("ServiceException occurred during the execution of save new job position in database.", e);
            view = getErrorView();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("<------ submitFormAction");
        }
        return listViewRender(model);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listViewRender(Model model) throws ServletException {
        List<JobPosition> jobPositions;
        if (logger.isDebugEnabled()) {
            logger.debug("<------ listViewRender");
        }
        try {
            jobPositions = this.jobPositionService.loadAllJobPositions();
            if (logger.isInfoEnabled()) {
                logger.info(String.format("job position's list size: %d", jobPositions.size()));
            }
            model.addAttribute(ControllerConstants.ModelAttribute.JOB_POSITION_LIST, jobPositions);
        } catch (ServiceException e) {
            logger.error("ServiceException occurred during the execution of load all job positions from database.", e);
            if (logger.isDebugEnabled()) {
                logger.debug("------> listViewRender");
            }
            return getErrorView();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("------> listViewRender");
        }
        return getView();
    }

    @Override
    public String getView() {
        return ControllerConstants.MappingContext.JOB_POSITION_FORM_CONTROLLER + "/view";
    }

    @Override
    public String getFormView() {
        return ControllerConstants.MappingContext.JOB_POSITION_FORM_CONTROLLER + "/jobPositionForm";
    }

    @Override
    public String getSuccessView() {
        return ControllerConstants.MappingContext.JOB_POSITION_FORM_CONTROLLER + "/jobPositionForm";
    }

    @Override
    public String getErrorView() {
        return ControllerConstants.MappingContext.JOB_POSITION_FORM_CONTROLLER + "/view";
    }
}
