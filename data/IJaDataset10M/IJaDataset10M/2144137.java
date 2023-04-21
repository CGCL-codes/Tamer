package org.powerfolder.workflow.model.script;

import java.util.ArrayList;

public class CompoundScriptTagConstraintTemplate extends BaseScriptTagConstraintTemplate implements SingleScriptTagConstraintOrTemplate, CompoundScriptTagConstraintOrTemplate {

    private ArrayList tagConstraintTemplates = null;

    private CompoundScriptTagConstraintTemplate(String inName, CompoundScriptTagConstraintOrTemplate inCstcot) {
        super(inName, inCstcot);
        this.tagConstraintTemplates = new ArrayList();
    }

    public static final CompoundScriptTagConstraintTemplate newInstance(String inName, CompoundScriptTagConstraintOrTemplate inCstcot) {
        return new CompoundScriptTagConstraintTemplate(inName, inCstcot);
    }

    public void registerScriptTagConstraintTemplate(ScriptTagConstraintTemplate inStct) {
        this.tagConstraintTemplates.add(inStct);
    }

    public ScriptTagConstraint newScriptTagConstraint(ScriptTagConstraintHolder inStch) {
        CompoundScriptTagConstraint outValue = null;
        ScriptTagCharacteristic stc = ScriptTagCharacteristicFactory.newInstance();
        outValue = CompoundScriptTagConstraint.newInstance(getName(), stc, inStch);
        initializeBaseMembers(outValue);
        for (int i = 0; i < this.tagConstraintTemplates.size(); i++) {
            Object nextStct = this.tagConstraintTemplates.get(i);
            outValue.registerScriptTagConstraintTemplate((ScriptTagConstraintTemplate) nextStct);
        }
        return outValue;
    }
}
