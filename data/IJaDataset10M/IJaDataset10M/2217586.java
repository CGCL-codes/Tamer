package au.gov.nla.aons.repository.domain.validator;

import org.springframework.validation.Errors;
import au.gov.nla.aons.repository.domain.PandoraRepository;

public class NewPandoraRepositoryValidator extends NewRepositoryValidator {

    private CommonPandoraValidator commonValidator = new CommonPandoraValidator();

    @Override
    public boolean supports(Class clazz) {
        return PandoraRepository.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        super.validate(target, errors);
        commonValidator.validateCommon(target, errors);
    }

    public CommonPandoraValidator getCommonValidator() {
        return commonValidator;
    }

    public void setCommonValidator(CommonPandoraValidator commonValidator) {
        this.commonValidator = commonValidator;
    }
}
