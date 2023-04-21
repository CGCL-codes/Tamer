package org.jcompany.control.jsf.service;

import java.util.Locale;
import org.hibernate.validator.InvalidValue;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jcompany.commons.PlcException;
import org.jcompany.control.PlcConstants;
import org.jcompany.domain.validate.service.PlcInvariantValidationService;

/**
 * Componente Seam que implementa a valida��o declarativa.
 */
@Name(PlcConstants.PlcJsfConstants.PLC_VALIDACAO_DECLARATIVA_SERVICE)
@Scope(ScopeType.APPLICATION)
public class PlcValidationDeclaratoryService extends PlcInvariantValidationService {

    /**
	 * Realiza valida��o no padr�o do Hibernate Validator, para a classe de Valida��o e mant�m caching de classes de valida�ao conforme recomendado
	 * @throws PlcException
	 */
    public InvalidValue[] validationDeclaratoryMergeMsgs(Locale locale, String principalBundle, Object validationClass) throws PlcException {
        return super.validationInvariantMergeMsgs(locale, principalBundle, validationClass);
    }
}
