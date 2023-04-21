package org.powerfolder.workflow.model.script.v1.returnable;

import java.math.BigDecimal;
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
import org.powerfolder.workflow.model.script.ScriptTag;
import org.powerfolder.workflow.model.script.ScriptTagCharacteristic;
import org.powerfolder.workflow.model.script.ScriptTagCharacteristicFactory;
import org.powerfolder.workflow.model.script.ScriptTagHelper;
import org.powerfolder.workflow.model.script.ScriptTagInformationContext;
import org.powerfolder.workflow.model.script.ScriptTagInitializer;
import org.powerfolder.workflow.model.script.ScriptTagInitializerFactory;
import org.powerfolder.workflow.model.script.WorkflowComponentsForReturnableScriptTag;

public class GreaterThanScriptTag implements ReturnableScriptTag {

    private ScriptTagCharacteristic operandTc = null;

    private static final String OPERAND_TC = "operand";

    private static final String OPERAND_TITLE = "Operands";

    private static final String OPERAND_SHORT_DESCRIPTION = "Number values to compare. " + "The result is true only if the first operand is greater" + " than the second, false otherwise.";

    public GreaterThanScriptTag() {
        this.operandTc = ScriptTagCharacteristicFactory.newInstance();
    }

    public void initializeScriptTag(InitializeScriptTagContext inItc) {
        ScriptTagInitializer ti = ScriptTagInitializerFactory.newInstance(inItc);
        MultiStaticOrDynamicScriptTagConstraint msodtc1 = MultiStaticOrDynamicScriptTagConstraint.newInstance(this.OPERAND_TC, this.operandTc, ti);
        msodtc1.setDefaultLength(2);
        msodtc1.setMinimumLength(2);
        msodtc1.setMaximumLength(2);
        msodtc1.setTitle(this.OPERAND_TITLE);
        msodtc1.setShortDescription(this.OPERAND_SHORT_DESCRIPTION);
        msodtc1.setRegEx(MiscHelper.REG_EX_DECIMAL);
        msodtc1.setReturnClassRestrictions(MiscHelper.getDecimalClasses());
        msodtc1.setDefaultValueAsString("0");
        ti.initialize();
    }

    public void getScriptTagInformation(ScriptTagInformationContext inTic) {
        if (inTic instanceof StudioScriptTagInstanceInformationContext) {
            StudioScriptTagInstanceInformationContext stiic = (StudioScriptTagInstanceInformationContext) inTic;
            if (this.operandTc.getValueLength() == 2) {
                String operandTitle1 = "";
                String operandDesc1 = "";
                if (this.operandTc.isStatic(0)) {
                    operandTitle1 = this.operandTc.getValueAsString(0);
                    operandDesc1 = this.operandTc.getValueAsString(0);
                } else if (this.operandTc.isDynamic(0)) {
                    ScriptTag nextScriptTag = this.operandTc.getValueAsScriptTag(0);
                    StudioScriptTagInstanceInformationContext nextStiic = StudioScriptTagInstanceInformationContext.newInstance();
                    nextScriptTag.getScriptTagInformation(nextStiic);
                    operandTitle1 = nextStiic.getScriptTagInstanceTitle();
                    operandDesc1 = nextStiic.getScriptTagInstanceDescription();
                }
                String operandTitle2 = "";
                String operandDesc2 = "";
                if (this.operandTc.isStatic(1)) {
                    operandTitle2 = this.operandTc.getValueAsString(1);
                    operandDesc2 = this.operandTc.getValueAsString(1);
                } else if (this.operandTc.isDynamic(1)) {
                    ScriptTag nextScriptTag = this.operandTc.getValueAsScriptTag(1);
                    StudioScriptTagInstanceInformationContext nextStiic = StudioScriptTagInstanceInformationContext.newInstance();
                    nextScriptTag.getScriptTagInformation(nextStiic);
                    operandTitle2 = nextStiic.getScriptTagInstanceTitle();
                    operandDesc2 = nextStiic.getScriptTagInstanceDescription();
                }
                stiic.setScriptTagInstanceTitle("(" + operandTitle1 + " > " + operandTitle2 + ")");
                stiic.setScriptTagInstanceDescription("(" + operandDesc1 + " > " + operandDesc2 + ")");
            } else {
                stiic.setScriptTagInstanceTitle("(Error)");
                stiic.setScriptTagInstanceDescription("(Error)");
            }
        } else if (inTic instanceof StudioScriptTagTypeInformationContext) {
            StudioScriptTagTypeInformationContext sttic = (StudioScriptTagTypeInformationContext) inTic;
            sttic.setScriptTagTypeTitle("Greater Than");
            sttic.setScriptTagTypeDescription("Determines if one number is greater than another.");
        }
    }

    public ValueAndClass returnValueAndClass(ReturnValueAndClassContext inRvacc) throws PFException {
        WorkflowComponentsForReturnableScriptTag wcfrt = ScriptTagHelper.createWorkflowComponentsForReturnableScriptTag(inRvacc);
        boolean result = true;
        BigDecimal nextOperand = null;
        if (this.operandTc.isStatic(0)) {
            nextOperand = new BigDecimal(this.operandTc.getValueAsString(0));
        } else {
            ValueAndClass vac = this.operandTc.returnValueAndClassFromScriptTag(0, inRvacc);
            nextOperand = MiscHelper.convertNumberToBigDecimal(vac.getValue());
        }
        if (this.operandTc.isStatic(1)) {
            BigDecimal bd = new BigDecimal(this.operandTc.getValueAsString(1));
            result = result & (nextOperand.compareTo(bd) > 0);
        } else {
            ValueAndClass vac = this.operandTc.returnValueAndClassFromScriptTag(1, inRvacc);
            BigDecimal bd = MiscHelper.convertNumberToBigDecimal(vac.getValue());
            result = result & (nextOperand.compareTo(bd) > 0);
        }
        return ValueAndClassFactory.newValueAndClass(new Boolean(result), Boolean.TYPE);
    }

    public Class getReturnClass(ReturnClassContext inRcc) {
        return Boolean.TYPE;
    }
}
