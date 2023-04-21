package org.eledge.components;

import static org.eledge.Eledge.create;
import java.util.List;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.tapestry.BaseComponent;
import org.eledge.domain.TemplateVariable;
import org.eledge.domain.TemplateVariableContainer;
import org.eledge.domain.TemplateVariableDefinition;
import org.eledge.domain.auto._TemplateVariable;

/**
 * @author robertz
 * 
 */
public abstract class TemplateVariableListing extends BaseComponent {

    public abstract TemplateVariableContainer getVariableContainer();

    public abstract List<?> getVariableList();

    public abstract TemplateVariableDefinition getCurrRow();

    public TemplateVariable findVariable(TemplateVariableDefinition vdef) {
        Expression e = ExpressionFactory.matchExp(_TemplateVariable.DEFINITION_PROPERTY + ".objectId", vdef.getObjectId());
        List<?> filtered = e.filterObjects(getVariableContainer().getTemplateVariables());
        TemplateVariable var;
        if (filtered.isEmpty()) {
            var = create(TemplateVariable.class);
            var.setDefinition(vdef);
            getVariableContainer().addToTemplateVariables(var);
            var.setValue(vdef.getDefaultValue());
        } else {
            var = (TemplateVariable) filtered.get(0);
        }
        return var;
    }

    public boolean getHasVars() {
        return getVariableList() != null && !getVariableList().isEmpty();
    }

    public boolean isCheckbox() {
        return getCurrRow().getType().equals(TemplateVariableDefinition.TYPE_CHECKBOX);
    }

    public Object getTypedValue() {
        return findVariable(getCurrRow()).getTypedValue();
    }

    public void setTypedValue(Object o) {
        findVariable(getCurrRow()).setTypedValue(o);
    }
}
