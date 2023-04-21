package com.pbxworkbench.campaign.ui.netbeans.actions;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import junit.framework.TestCase;
import com.pbxworkbench.commons.SwingHelper;
import com.pbxworkbench.commons.ui.AliasingActionWrapper;

public class NewCampaignToolbarActionTest extends TestCase {

    public void testIcon() throws Exception {
        NewCampaignToolbarAction action = new NewCampaignToolbarAction();
        Icon icon = (Icon) action.getValue(Action.SMALL_ICON);
        assertNotNull(icon);
        AliasingActionWrapper aliasedAction = new AliasingActionWrapper(action);
        aliasedAction.setOverrideValue(Action.NAME, "Create Campaign");
        JButton btn = new JButton(aliasedAction);
        SwingHelper.openComponent(btn);
        Thread.sleep(10000);
    }
}
