package org.wsmostudio.choreography.editors;

import org.eclipse.swt.widgets.Composite;
import org.wsmo.service.choreography.rule.*;
import org.wsmo.service.rule.*;
import org.wsmostudio.choreography.editors.model.ChoreographyModel;

public abstract class RuleFormEditor {

    protected ChoreographyRule rule;

    protected ChoreographyModel uiModel;

    public RuleFormEditor(ChoreographyRule rule, ChoreographyModel model) {
        this.rule = rule;
        this.uiModel = model;
    }

    public abstract Composite initUI(Composite parent);

    public abstract void updateRule() throws Exception;

    public abstract void dispose();

    protected String labelForRule() {
        if (rule instanceof Add) {
            return "add( ... )";
        } else if (rule instanceof Delete) {
            return "delete( ... )";
        } else if (rule instanceof Update) {
            return "update( ... )";
        } else if (rule instanceof ChoreographyIfThen) {
            return "if ( ... ) then ";
        } else if (rule instanceof ChoreographyChoose) {
            return "choose { ... } with ( ... ) do ";
        } else {
            return "forall { ... } with ( ... ) do ";
        }
    }

    public Rule getRule() {
        return this.rule;
    }
}
