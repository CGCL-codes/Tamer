package org.jskat.gui.iss;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.jskat.gui.LayoutFactory;
import org.jskat.gui.action.JSkatAction;

/**
 * Context panel for starting a skat series on ISS
 */
class StartContextPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public StartContextPanel(ActionMap actions) {
        initPanel(actions);
    }

    public void initPanel(ActionMap actions) {
        this.setLayout(LayoutFactory.getMigLayout("fill", "fill", "fill"));
        JPanel panel = new JPanel(LayoutFactory.getMigLayout("fill"));
        panel.add(new JButton(actions.get(JSkatAction.INVITE_ISS_PLAYER)), "center");
        panel.add(new JButton(actions.get(JSkatAction.READY_TO_PLAY)), "center");
        panel.add(new JButton(actions.get(JSkatAction.TALK_ENABLED)), "center, wrap");
        panel.add(new JButton(actions.get(JSkatAction.LEAVE_ISS_TABLE)), "center");
        panel.setOpaque(false);
        this.add(panel, "center");
        setOpaque(false);
    }
}
