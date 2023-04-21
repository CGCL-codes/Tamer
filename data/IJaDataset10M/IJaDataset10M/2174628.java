package org.powerfolder.workflow.model.script.v1.returnable;

import org.powerfolder.PFException;
import org.powerfolder.ValueAndClass;
import org.powerfolder.ValueAndClassFactory;
import org.powerfolder.utils.misc.MiscHelper;
import org.powerfolder.workflow.model.script.InitializeScriptTagContext;
import org.powerfolder.workflow.model.script.ReturnableScriptTag;
import org.powerfolder.workflow.model.script.ReturnClassContext;
import org.powerfolder.workflow.model.script.ReturnValueAndClassContext;
import org.powerfolder.workflow.model.script.StaticScriptTagConstraint;
import org.powerfolder.workflow.model.script.StudioScriptTagInstanceInformationContext;
import org.powerfolder.workflow.model.script.StudioScriptTagTypeInformationContext;
import org.powerfolder.workflow.model.script.ScriptTagCharacteristic;
import org.powerfolder.workflow.model.script.ScriptTagCharacteristicFactory;
import org.powerfolder.workflow.model.script.ScriptTagHelper;
import org.powerfolder.workflow.model.script.ScriptTagInformationContext;
import org.powerfolder.workflow.model.script.ScriptTagInitializer;
import org.powerfolder.workflow.model.script.ScriptTagInitializerFactory;
import org.powerfolder.workflow.model.script.WorkflowComponentsForReturnableScriptTag;

public class ShortScriptTag implements ReturnableScriptTag {

    private ScriptTagCharacteristic valueTc = null;

    private static final String VALUE_TC = "value";

    private static final String VALUE_TITLE = "Value";

    private static final String VALUE_SHORT_DESCRIPTION = "Value representing the integer number.";

    public ShortScriptTag() {
        this.valueTc = ScriptTagCharacteristicFactory.newInstance();
    }

    public void initializeScriptTag(InitializeScriptTagContext inItc) {
        ScriptTagInitializer ti = ScriptTagInitializerFactory.newInstance(inItc);
        StaticScriptTagConstraint stc1 = StaticScriptTagConstraint.newInstance(this.VALUE_TC, this.valueTc, ti);
        stc1.setTitle(this.VALUE_TITLE);
        stc1.setShortDescription(this.VALUE_SHORT_DESCRIPTION);
        stc1.setRegEx(MiscHelper.REG_EX_INTEGER);
        stc1.setDefaultValueAsString("0");
        ti.initialize();
    }

    public void getScriptTagInformation(ScriptTagInformationContext inTic) {
        if (inTic instanceof StudioScriptTagInstanceInformationContext) {
            StudioScriptTagInstanceInformationContext stiic = (StudioScriptTagInstanceInformationContext) inTic;
            if (this.valueTc.getValueLength() > 0 && this.valueTc.isStatic()) {
                stiic.setScriptTagInstanceTitle(this.valueTc.getValueAsString());
                stiic.setScriptTagInstanceDescription(this.valueTc.getValueAsString());
            } else {
                stiic.setScriptTagInstanceTitle("(Unknown)");
                stiic.setScriptTagInstanceDescription("Incorrect value specified.");
            }
        } else if (inTic instanceof StudioScriptTagTypeInformationContext) {
            StudioScriptTagTypeInformationContext sttic = (StudioScriptTagTypeInformationContext) inTic;
            sttic.setScriptTagTypeTitle("Short");
            sttic.setScriptTagTypeDescription("Represents a Java short value.");
        }
    }

    public ValueAndClass returnValueAndClass(ReturnValueAndClassContext inRvacc) throws PFException {
        WorkflowComponentsForReturnableScriptTag wcfrt = ScriptTagHelper.createWorkflowComponentsForReturnableScriptTag(inRvacc);
        String tcValue = this.valueTc.getValueAsString();
        Short s = new Short(tcValue);
        return ValueAndClassFactory.newValueAndClass(s, Short.TYPE);
    }

    public Class getReturnClass(ReturnClassContext inRcc) {
        return Short.TYPE;
    }
}
