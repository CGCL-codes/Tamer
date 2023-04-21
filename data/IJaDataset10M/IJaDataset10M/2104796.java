package eu.redseeds.sc.current.ui.validation.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import eu.redseeds.sc.current.ui.validation.IValidate;
import eu.redseeds.sc.current.ui.validation.ValidationAdapter;
import eu.redseeds.sc.current.ui.validation.ValidationResult;
import eu.redseeds.scl.model.rsl.rslrequirements.requirementsspecifications.RequirementsPackageDTO;
import eu.redseeds.scl.model.rsl.rslrequirements.requirementsspecifications.RequirementsSpecificationDTO;

public class RequirementsSpecificationValidator implements IValidate {

    @Override
    public int getComplexness() {
        return 0;
    }

    @Override
    public String getLabel() {
        return "Validating requirements specification";
    }

    @Override
    public ValidationResult[] validate(Object element) {
        if (!(element instanceof RequirementsSpecificationDTO)) {
            ValidationResult vr = new ValidationResult();
            vr.setProblemID(IValidate.ID_WRONG_TYPE);
            vr.setMessage(MSG_WRONG_TYPE);
            vr.setSclElement(element);
            return new ValidationResult[] { vr };
        }
        return new ValidationResult[0];
    }

    @Override
    public ValidationResult[] validateRecursively(Object element) {
        List<ValidationResult> results = new ArrayList<ValidationResult>();
        results.addAll(Arrays.asList(validate(element)));
        if (element instanceof RequirementsSpecificationDTO) {
            RequirementsSpecificationDTO rs = (RequirementsSpecificationDTO) element;
            if (rs.getRequirementsPackagesDTOList().size() > 0) {
                IValidate validatorRP = ValidationAdapter.getValidator(rs.getRequirementsPackagesDTOList().get(0));
                for (RequirementsPackageDTO reqpack : rs.getRequirementsPackagesDTOList()) {
                    results.addAll(Arrays.asList(validatorRP.validateRecursively(reqpack)));
                }
            }
            IValidate validatorDom = ValidationAdapter.getValidator(rs.getDomainSpecificationDTO());
            results.addAll(Arrays.asList(validatorDom.validateRecursively(rs.getDomainSpecificationDTO())));
        }
        return (ValidationResult[]) results.toArray(new ValidationResult[results.size()]);
    }
}
