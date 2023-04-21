package eu.redseeds.sc.current.ui.validation.validators;

import eu.redseeds.sc.current.ui.validation.IValidate;
import eu.redseeds.sc.current.ui.validation.ValidationResult;
import eu.redseeds.scl.model.rsl.rslrequirementrepresentations.descriptiverequirementrepresentations.ConstrainedLanguageScenarioDTO;
import eu.redseeds.scl.model.rsl.rslrequirementrepresentationsentences.controlsentences.ConditionSentenceDTO;
import eu.redseeds.scl.model.rsl.rslrequirements.requirementsspecifications.UseCaseDTO;

public class ConditionSentenceValidator implements IValidate {

    @Override
    public int getComplexness() {
        return 1;
    }

    @Override
    public String getLabel() {
        return "Validating Condtition Sentence";
    }

    @Override
    public ValidationResult[] validate(Object element) {
        if (!(element instanceof ConditionSentenceDTO)) {
            ValidationResult vr = new ValidationResult();
            vr.setProblemID(IValidate.ID_WRONG_TYPE);
            vr.setMessage(MSG_WRONG_TYPE);
            vr.setSclElement(element);
            return new ValidationResult[] { vr };
        }
        ConditionSentenceDTO sent = (ConditionSentenceDTO) element;
        if (!validateCondition(sent)) {
            ValidationResult vr = new ValidationResult();
            vr.setProblemID(IValidate.ID_SENT_COND_EMPTY);
            vr.setMessage(MSG_SENT_COND_EMPTY);
            if (!sent.getOwningScenarios().isEmpty()) {
                ConstrainedLanguageScenarioDTO scen = sent.getOwningScenarios().get(0);
                UseCaseDTO uc = scen.getParent();
                vr.setPath(uc.getSpecificationPath() + "\\" + uc.getName() + "\\" + scen.getName());
            }
            vr.setSclElement(element);
            vr.setSeverity(IValidate.SEVERITY_WARNING);
            return new ValidationResult[] { vr };
        }
        return new ValidationResult[0];
    }

    @Override
    public ValidationResult[] validateRecursively(Object element) {
        return validate(element);
    }

    private boolean validateCondition(ConditionSentenceDTO cond) {
        if (cond.getSentenceText() != null) if (!cond.getSentenceText().equals("")) return true;
        return false;
    }
}
