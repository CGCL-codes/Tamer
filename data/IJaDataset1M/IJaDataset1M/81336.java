package com.jchapman.jempire.actions;

import java.awt.event.ActionEvent;
import com.jchapman.jempire.events.AttackEvent;
import com.jchapman.jempire.game.AttackModel;
import com.jchapman.jempire.gui.MainPanel;
import com.jchapman.jempire.utils.JEmpireConstants;
import org.yasl.arch.application.YASLSwingApplication;
import org.yasl.arch.impl.action.YASLSwingAbstractAction;

/**
 *
 * @author Jeff Chapman
 * @version 1.0
 */
public class AttackAction extends YASLSwingAbstractAction {

    public AttackAction(YASLSwingApplication yaslSwingApplication) {
        super("attack", true, yaslSwingApplication);
    }

    protected void performAction(ActionEvent actionEvent) throws Exception {
        YASLSwingApplication swingApp = getYASLSwingApplication();
        AttackModel attackModel = (AttackModel) swingApp.getSingleton(JEmpireConstants.APPKEY_ATTACK_MODEL);
        attackModel.handleAttack((AttackEvent) actionEvent);
        MainPanel mainPanel = (MainPanel) swingApp.getSingleton(JEmpireConstants.APPKEY_MAIN_PANEL_GUI);
        mainPanel.updateMap();
    }
}
