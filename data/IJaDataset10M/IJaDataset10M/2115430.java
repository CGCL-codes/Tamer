package org.redclasp.components.jbpm.context.matcher;

import org.jbpm.context.exe.JbpmTypeMatcher;
import org.redclasp.components.jbpm.context.VariableDomainEntity;

/**
 * @author sshwsfc
 */
public class DomainMatcher implements JbpmTypeMatcher {

    private static final long serialVersionUID = 1L;

    public boolean matches(Class valueClass) {
        return VariableDomainEntity.class.isAssignableFrom(valueClass);
    }

    public boolean matches(Object arg0) {
        return false;
    }
}
