package org.powerfolder.workflow.model.script.v1.returnable;

import org.powerfolder.PFException;
import org.powerfolder.ValueAndClass;
import org.powerfolder.ValueAndClassFactory;
import org.powerfolder.utils.misc.MiscHelper;
import org.powerfolder.workflow.model.script.InitializeScriptTagContext;
import org.powerfolder.workflow.model.script.MultiStaticOrDynamicScriptTagConstraint;
import org.powerfolder.workflow.model.script.ReturnableScriptTag;
import org.powerfolder.workflow.model.script.ReturnClassContext;
import org.powerfolder.workflow.model.script.ReturnValueAndClassContext;
import org.powerfolder.workflow.model.script.StudioScriptTagInstanceInformationContext;
import org.powerfolder.workflow.model.script.StudioScriptTagTypeInformationContext;
import org.powerfolder.workflow.model.script.ScriptTagCharacteristic;
import org.powerfolder.workflow.model.script.ScriptTagCharacteristicFactory;
import org.powerfolder.workflow.model.script.ScriptTagHelper;
import org.powerfolder.workflow.model.script.ScriptTagInformationContext;
import org.powerfolder.workflow.model.script.ScriptTagInitializer;
import org.powerfolder.workflow.model.script.ScriptTagInitializerFactory;
import org.powerfolder.workflow.model.script.WorkflowComponentsForReturnableScriptTag;

public class StringConcatenateScriptTag implements ReturnableScriptTag {

    private ScriptTagCharacteristic operandTc = null;

    private static final String OPERAND_TC = "operand";

    private static final String OPERAND_TITLE = "Operands";

    private static final String OPERAND_SHORT_DESCRIPTION = "The values to be concatenated.";

    public StringConcatenateScriptTag() {
        this.operandTc = ScriptTagCharacteristicFactory.newInstance();
    }

    public void initializeScriptTag(InitializeScriptTagContext inItc) {
        ScriptTagInitializer ti = ScriptTagInitializerFactory.newInstance(inItc);
        MultiStaticOrDynamicScriptTagConstraint msodtc1 = MultiStaticOrDynamicScriptTagConstraint.newInstance(this.OPERAND_TC, this.operandTc, ti);
        msodtc1.setDefaultLength(2);
        msodtc1.setMinimumLength(2);
        msodtc1.setLengthUnbounded(true);
        msodtc1.setTitle(this.OPERAND_TITLE);
        msodtc1.setShortDescription(this.OPERAND_SHORT_DESCRIPTION);
        msodtc1.setRegEx(MiscHelper.REG_EX_ANY_STRING);
        msodtc1.setDefaultValueAsString("SomeText");
        ti.initialize();
    }

    public void getScriptTagInformation(ScriptTagInformationContext inTic) {
        if (inTic instanceof StudioScriptTagInstanceInformationContext) {
            StudioScriptTagInstanceInformationContext stiic = (StudioScriptTagInstanceInformationContext) inTic;
            stiic.setScriptTagInstanceTitle("String Concatenate");
            stiic.setScriptTagInstanceDescription("Concatenates two or more strings.");
        } else if (inTic instanceof StudioScriptTagTypeInformationContext) {
            StudioScriptTagTypeInformationContext sttic = (StudioScriptTagTypeInformationContext) inTic;
            sttic.setScriptTagTypeTitle("String Concatenate");
            sttic.setScriptTagTypeDescription("Concatenates two or more strings.");
        }
    }

    public ValueAndClass returnValueAndClass(ReturnValueAndClassContext inRvacc) throws PFException {
        WorkflowComponentsForReturnableScriptTag wcfrt = ScriptTagHelper.createWorkflowComponentsForReturnableScriptTag(inRvacc);
        String result = "";
        for (int i = 0; i < this.operandTc.getValueLength(); i++) {
            if (this.operandTc.isStatic(i)) {
                result = result + this.operandTc.getValueAsString(i);
            } else {
                ValueAndClass vac = this.operandTc.returnValueAndClassFromScriptTag(inRvacc);
                result = result + vac.getValue();
            }
        }
        return ValueAndClassFactory.newValueAndClass(result, String.class);
    }

    public Class getReturnClass(ReturnClassContext inRcc) {
        return String.class;
    }
}
