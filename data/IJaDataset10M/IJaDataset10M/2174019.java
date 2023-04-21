package com.pcmsolutions.device.EMU.E4.zcommands.preset;

import com.pcmsolutions.device.EMU.E4.preset.ContextEditablePreset;
import com.pcmsolutions.gui.FixedLengthTextField;
import com.pcmsolutions.gui.zcommand.AbstractZCommandField;
import com.pcmsolutions.gui.zcommand.ZCommandDialog;
import com.pcmsolutions.gui.zcommand.ZCommandField;
import com.pcmsolutions.system.CommandFailedException;
import com.pcmsolutions.system.IntPool;
import com.pcmsolutions.system.ZCommandTargetsNotSpecifiedException;
import com.pcmsolutions.system.ZMTCommand;
import javax.swing.*;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: pmeehan
 * Date: 22-Mar-2003
 * Time: 14:36:45
 * To change this template use Options | File Templates.
 */
public class OffsetPresetSamplesZMTC extends AbstractContextEditablePresetZMTCommand {

    private boolean userMode;

    private static final AbstractZCommandField<FixedLengthTextField, String> numericField = new AbstractZCommandField<FixedLengthTextField, String>(new FixedLengthTextField(6), "Offset", "") {

        public String getValue() {
            return getComponent().getText();
        }
    };

    private static final ZCommandDialog cmdDlg = new ZCommandDialog();

    static {
        cmdDlg.init("", new ZCommandField[] { numericField });
    }

    public OffsetPresetSamplesZMTC() {
        this(true);
    }

    private OffsetPresetSamplesZMTC(boolean userMode) {
        this.userMode = userMode;
    }

    public ZMTCommand getNextMode() {
        if (userMode == true) return new OffsetPresetSamplesZMTC(false);
        return null;
    }

    public String getPresentationString() {
        return (userMode ? "Offset user samples" : "Offset ROM samples");
    }

    public String getDescriptiveString() {
        return (userMode ? "Offset user sample indexes in preset" : "Offset rom sample indexes in preset");
    }

    public String getMenuPathString() {
        return ";Utility";
    }

    public boolean handleTarget(ContextEditablePreset contextEditablePreset, int total, int curr) throws Exception {
        numericField.getComponent().setToolTipText((userMode ? "Offset to be applied to user sample indexes" : "Offset to be applied to ROM sample indexes"));
        cmdDlg.setTitle(getPresentationString());
        cmdDlg.run(new ZCommandDialog.Executable() {

            public void execute() throws Exception {
                Integer offset = IntPool.get(Integer.parseInt(numericField.getValue()));
                for (Iterator<ContextEditablePreset> i = getTargets().iterator(); i.hasNext(); ) i.next().offsetSampleIndexes(offset, userMode);
            }
        });
        return false;
    }
}
