package org.powerfolder.workflow.model.script.v1.core;

import org.powerfolder.utils.misc.MiscHelper;
import org.powerfolder.workflow.model.script.CoreScriptTag;
import org.powerfolder.workflow.model.script.InitializeScriptTagContext;
import org.powerfolder.workflow.model.script.InitializeScriptTagContextBean;
import org.powerfolder.workflow.model.script.StaticScriptTagConstraint;
import org.powerfolder.workflow.model.script.StudioScriptTagInstanceInformationContext;
import org.powerfolder.workflow.model.script.StudioScriptTagTypeInformationContext;
import org.powerfolder.workflow.model.script.ScriptTagCharacteristic;
import org.powerfolder.workflow.model.script.ScriptTagCharacteristicFactory;
import org.powerfolder.workflow.model.script.ScriptTagHelper;
import org.powerfolder.workflow.model.script.ScriptTagInformationContext;
import org.powerfolder.workflow.model.script.ScriptTagInitializer;
import org.powerfolder.workflow.model.script.ScriptTagInitializerFactory;

public class StopScriptTag implements CoreScriptTag {

    private ScriptTagCharacteristic nameTc = null;

    private ScriptTagCharacteristic xCordTc = null;

    private ScriptTagCharacteristic yCordTc = null;

    private static final String NAME_TC = "name";

    private static final String NAME_TITLE = "Name";

    private static final String NAME_SHORT_DESCRIPTION = "Name of this start.";

    private static final String X_CORD_TC = "x-cord";

    private static final String X_CORD_TITLE = "X Coordinate";

    private static final String X_CORD_SHORT_DESCRIPTION = "Distance (in pixels) from the left side " + "of the workflow-template graph.";

    private static final String Y_CORD_TC = "y-cord";

    private static final String Y_CORD_TITLE = "Y Coordinate";

    private static final String Y_CORD_SHORT_DESCRIPTION = "Distance (in pixels) from the top side " + "of the workflow-template graph.";

    public StopScriptTag() {
        this.nameTc = ScriptTagCharacteristicFactory.newInstance();
        this.xCordTc = ScriptTagCharacteristicFactory.newInstance();
        this.yCordTc = ScriptTagCharacteristicFactory.newInstance();
    }

    public void initializeScriptTag(InitializeScriptTagContext inItc) {
        ScriptTagInitializer ti = ScriptTagInitializerFactory.newInstance(inItc);
        StaticScriptTagConstraint stc1 = StaticScriptTagConstraint.newInstance(this.NAME_TC, this.nameTc, ti);
        stc1.setTitle(this.NAME_TITLE);
        stc1.setShortDescription(this.NAME_SHORT_DESCRIPTION);
        stc1.setDefaultValueAsString("Stop");
        stc1.setRegEx(MiscHelper.REG_EX_JAVA_NAME);
        ((InitializeScriptTagContextBean) inItc).registerStop(this.nameTc);
        StaticScriptTagConstraint stc2 = StaticScriptTagConstraint.newInstance(this.X_CORD_TC, this.xCordTc, ti);
        stc2.setTitle(this.X_CORD_TITLE);
        stc2.setShortDescription(this.X_CORD_SHORT_DESCRIPTION);
        stc2.setDefaultValueAsString(ScriptTagHelper.RESET);
        stc2.setRegEx(MiscHelper.REG_EX_POSITIVE_INTEGER);
        ((InitializeScriptTagContextBean) inItc).registerXCord(this.xCordTc);
        StaticScriptTagConstraint stc3 = StaticScriptTagConstraint.newInstance(this.Y_CORD_TC, this.yCordTc, ti);
        stc3.setTitle(this.Y_CORD_TITLE);
        stc3.setShortDescription(this.Y_CORD_SHORT_DESCRIPTION);
        stc3.setDefaultValueAsString(ScriptTagHelper.RESET);
        stc3.setRegEx(MiscHelper.REG_EX_POSITIVE_INTEGER);
        ((InitializeScriptTagContextBean) inItc).registerYCord(this.yCordTc);
        ti.initialize();
    }

    public void getScriptTagInformation(ScriptTagInformationContext inTic) {
        if (inTic instanceof StudioScriptTagInstanceInformationContext) {
            StudioScriptTagInstanceInformationContext stiic = (StudioScriptTagInstanceInformationContext) inTic;
            stiic.setScriptTagInstanceTitle(this.nameTc.getValueAsString());
            stiic.setScriptTagInstanceDescription("Potential end point for a workflow-instance.");
            stiic.setScriptTagInstanceIcon("stop");
        } else if (inTic instanceof StudioScriptTagTypeInformationContext) {
            StudioScriptTagTypeInformationContext sttic = (StudioScriptTagTypeInformationContext) inTic;
            sttic.setScriptTagTypeTitle("Stop");
            sttic.setScriptTagTypeDescription("Potential end point for a workflow-instance.");
        }
    }

    public String getName() {
        return this.nameTc.getValueAsString();
    }

    public boolean isXCordValid() {
        return isCordValid(this.xCordTc);
    }

    public boolean isYCordValid() {
        return isCordValid(this.yCordTc);
    }

    private boolean isCordValid(ScriptTagCharacteristic inTc) {
        boolean outValue = false;
        try {
            if (inTc.getValueLength() > 0 && inTc.isStatic()) {
                int value = Integer.parseInt(inTc.getValueAsString());
                outValue = (value > 1);
            }
        } catch (NumberFormatException nfe) {
        }
        return outValue;
    }

    public int getXCord() {
        return Integer.parseInt(this.xCordTc.getValueAsString());
    }

    public int getYCord() {
        return Integer.parseInt(this.yCordTc.getValueAsString());
    }

    public void setXCord(String inCord) {
        this.xCordTc.setValue(0, inCord, null);
    }

    public void setYCord(String inCord) {
        this.yCordTc.setValue(0, inCord, null);
    }
}
