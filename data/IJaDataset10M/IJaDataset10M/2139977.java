package net.sourceforge.solexatools.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import net.sourceforge.solexatools.business.StudyService;
import net.sourceforge.solexatools.model.Study;

public class StudyValidator extends LoginValidator {

    private StudyService studyService;

    public StudyValidator() {
        super();
    }

    /**
	 * Validates the specified Object.
	 *
	 * @param obj the Object to validate
	 * @param errors Errors object for validation errors
	 */
    public void validate(Object obj, Errors errors) {
        Study study = (Study) obj;
        this.validateTitle(study.getTitle(), errors);
        ValidationUtils.rejectIfEmpty(errors, "title", "study.required.title");
    }

    /**
	 * Determines if the experiment's email address and confirm
	 * email address match.
	 *
	 * @param errors Errors object for validation errors
	 */
    public void validateTitle(String title, Errors errors) {
        if (errors.getFieldError("title") == null) {
            if (this.getStudyService().findByTitle(title) != null) {
                errors.reject("error.study.match.title");
            }
        }
    }

    public StudyService getStudyService() {
        return studyService;
    }

    public void setStudyService(StudyService studyService) {
        this.studyService = studyService;
    }
}
