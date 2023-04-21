package net.sf.smbt.blinkm.cmd.actions;

import net.sf.smbt.blinkm.cmd.injection.BlinkMUtil;
import net.sf.smbt.commands.GetCurrentRGBColorCmd;
import net.sf.xqz.engine.ui.queues.actions.AbstractSmartIDEAction;
import net.sf.xqz.engine.utils.EngineUtil;
import net.sf.xqz.model.engine.CmdEngine;
import net.sf.xqz.model.engine.EngineApplication;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

public class BlinkMGetRGBColor0x03Action extends AbstractSmartIDEAction {

    public BlinkMGetRGBColor0x03Action() {
        super();
    }

    @Override
    public void proceed(EngineApplication app) {
        CmdEngine engine = app.getEngine().get(0);
        EngineUtil.INSTANCE.sendCmd(engine.getTx(), BlinkMUtil.INSTANCE.createStopScriptCmd("0x00"));
        GetCurrentRGBColorCmd cmd = EngineUtil.INSTANCE.sendCmd(engine.getTx(), BlinkMUtil.INSTANCE.createGetCurrentRGBColorCmd("0x03"));
        MessageBox msgBox = new MessageBox(Display.getDefault().getActiveShell());
        msgBox.setMessage("0x03 Current Color");
        msgBox.setText("[" + (int) cmd.getRetValues()[0] + "][" + (int) cmd.getRetValues()[1] + "][" + (int) cmd.getRetValues()[2] + "]");
        msgBox.open();
    }
}
