package org.brandao.brutos;

import org.brandao.brutos.validator.RestrictionRules;

/**
 * Constr�i um par�metro de uma a��o.
 *
 * @author Afonso Brandao
 */
public class ParameterBuilder extends RestrictionBuilder {

    public ParameterBuilder(Configuration config) {
        super(config);
    }

    public RestrictionBuilder addRestriction(RestrictionRules ruleId, Object value) {
        return super.addRestriction(ruleId, value);
    }

    public RestrictionBuilder setMessage(String message) {
        return super.setMessage(message);
    }
}
