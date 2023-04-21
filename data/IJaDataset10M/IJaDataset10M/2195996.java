package org.hsnr.didac.modules.actions;

import org.apache.turbine.modules.actions.VelocityAction;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import org.apache.turbine.util.parser.ParameterParser;
import org.apache.turbine.services.intake.IntakeTool;
import org.apache.turbine.services.intake.model.Group;
import java.util.List;
import java.util.LinkedList;

public class DidacAction extends VelocityAction {

    protected ParameterParser params;

    protected IntakeTool intake;

    public void doPerform(RunData data, Context context) {
        params = data.getParameters();
        intake = (IntakeTool) context.get("intake");
    }

    protected List getFormErrors(String groupName, String key, RunData data) {
        List formErrors = new LinkedList();
        try {
            Group group = intake.get(groupName, key);
            String[] fieldNames = group.getFieldNames();
            for (int i = 0; i < fieldNames.length; i++) {
                if (!group.get(fieldNames[i]).isValid()) {
                    formErrors.add(group.get(fieldNames[i]).getMessage());
                }
            }
        } catch (Exception ex) {
            data.setMessage(ex.toString());
        }
        return formErrors;
    }
}
