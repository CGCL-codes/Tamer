package edu.upmc.opi.caBIG.caTIES.client.vr.desktop.toolbars;

import javax.swing.JButton;
import javax.swing.JToolBar;
import edu.upmc.opi.caBIG.caTIES.client.vr.desktop.CaTIES_Aesthetics;
import edu.upmc.opi.caBIG.caTIES.client.vr.desktop.actions.ShowHonestBrokerOrdersTabAction;
import edu.upmc.opi.caBIG.caTIES.client.vr.desktop.actions.TagPatientsAction;

/**
 * Administrator Toolbar.
 */
public class CaTIES_ManageOrdersToolbar extends JToolBar {

    private boolean taggingVisible;

    /**
     * The Constructor.
     */
    public CaTIES_ManageOrdersToolbar(boolean taggingVisible) {
        super();
        this.taggingVisible = taggingVisible;
        init();
        addButtons();
    }

    /**
     * Adds the buttons.
     */
    private void addButtons() {
        JButton button = new JButton("Manage Orders");
        button.setToolTipText("Show Manage Orders Tab");
        button.setIcon(CaTIES_Aesthetics.ICON_CASESET_16);
        button.setFocusPainted(false);
        CaTIES_CommonToolbar.doToolBarButtonSetup(button);
        button.addActionListener(new ShowHonestBrokerOrdersTabAction());
        this.add(button);
        if (taggingVisible) {
            button = new JButton("Tag Patients");
            button.setToolTipText("Tag patients for searching later");
            button.setIcon(CaTIES_Aesthetics.ICON_REDTAG);
            button.setFocusPainted(false);
            CaTIES_CommonToolbar.doToolBarButtonSetup(button);
            button.addActionListener(new TagPatientsAction());
            this.add(button);
        }
    }

    /**
     * Init.
     */
    private void init() {
        this.setName("Manage Orders Toolbar");
        this.setRollover(true);
    }
}
