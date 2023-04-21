package nl.gridshore.samples.training.web.controllers.training;

import nl.gridshore.samples.training.business.TrainingService;
import nl.gridshore.samples.training.domain.Training;
import nl.gridshore.samples.training.web.controllers.ControllerConstants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA.
 * User: jettro
 * Date: Jan 24, 2008
 * Time: 2:24:07 PM
 * Super class for all update controllers of trainings
 */
public abstract class UpdateableTrainingController {

    protected static final String REDIRECT_TRAININGS_VIEW = "redirect:trainings.view";

    protected static final String TRAINING_FORM = "trainingForm";

    private static final String SESSION_OBJECT_NAME = "training";

    protected TrainingService trainingService;

    protected Validator validator;

    public UpdateableTrainingController(TrainingService trainingService, Validator validator) {
        this.trainingService = trainingService;
        this.validator = validator;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processFormSubmit(@RequestParam(value = ControllerConstants.REQUEST_PARAM_CANCEL, required = false) String cancel, @ModelAttribute(SESSION_OBJECT_NAME) Training training, BindingResult bindingResult, SessionStatus status) {
        String returnValue;
        if (ControllerConstants.REQUEST_PARAM_CANCEL_VALUE.equals(cancel)) {
            returnValue = REDIRECT_TRAININGS_VIEW;
        } else {
            validator.validate(training, bindingResult);
            if (bindingResult.hasErrors()) {
                returnValue = TRAINING_FORM;
            } else {
                trainingService.storeTraining(training);
                status.setComplete();
                returnValue = REDIRECT_TRAININGS_VIEW;
            }
        }
        return returnValue;
    }
}
