package com.prefabware.jmodel.code.hash;

import com.prefabware.jmodel.code.JCodeOptions;
import com.prefabware.jmodel.expression.JExpression;
import com.prefabware.jmodel.expression.JVariable;

public class HashCodelong extends HashCode {

    public HashCodelong(JVariable variable) {
        super(variable);
    }

    public JExpression getHashCode() {
        JExpression expression = new JExpression() {

            @Override
            public String asCode(JCodeOptions options) {
                String field = getVariable().asCode(JCodeOptions.SIMPLE_NAMES);
                return "(int) (" + field + " ^ (" + field + " >>> 32))";
            }
        };
        return expression;
    }
}
