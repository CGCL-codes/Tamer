package nl.utwente.ewi.portunes.gui.view.panels;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import nl.utwente.ewi.portunes.gui.controllers.actions.ActionController;
import nl.utwente.ewi.portunes.gui.controllers.actions.AttackAction;
import nl.utwente.ewi.portunes.gui.controllers.managers.PortunesController;
import nl.utwente.ewi.portunes.gui.controllers.utilities.Message;
import nl.utwente.ewi.portunes.gui.controllers.utilities.Observer;
import nl.utwente.ewi.portunes.model.edges.Edge;

/**
 * Panel for showing the attack senario in text format.
 */
public class TextPanel extends JPanel implements Observer {

    private PortunesController portunesController;

    private List<Edge> attack;

    private int currentAction;

    private AttackAction attackAction;

    private String currentLog;

    /**
     * Default constructor.
     */
    public TextPanel(PortunesController portunesController) {
        this.portunesController = portunesController;
        this.portunesController.registerObserver(this);
        this.attackAction = new AttackAction(portunesController);
        this.currentLog = "";
        this.createUpdateAndShowPanel();
    }

    /**
     * Receives update messages.
     * @param message
     */
    @Override
    public void update(Message message) {
        if (message == Message.ATTACK_SELECTED) {
            this.attack = this.portunesController.getAlgorithmController().getCurrentAttack();
            this.currentAction = this.portunesController.getAlgorithmController().getCurrentAction();
            this.currentLog = this.portunesController.getAlgorithmController().getCurrentActionLog();
            refresh();
        } else if (message == Message.ACTION_CHANGED) {
            this.currentAction = this.portunesController.getAlgorithmController().getCurrentAction();
            this.currentLog = this.portunesController.getAlgorithmController().getCurrentActionLog();
            refresh();
        } else if (message == Message.ATTACK_CLEARED) {
            this.attack = null;
            this.currentLog = "";
            refresh();
        }
    }

    /**
     * Refresh the panel.
     */
    public void refresh() {
        this.removeAll();
        this.createUpdateAndShowPanel();
        this.repaint();
        this.revalidate();
    }

    /**
     * Draw the actual panel.
     */
    private void createUpdateAndShowPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        if (attack != null && !attack.isEmpty()) {
            JPanel panel = new JPanel();
            JButton beginning = new JButton("<<");
            ActionController.registerActionListner(beginning, this.attackAction, AttackAction.FIRST_ACTION);
            JButton back = new JButton("<");
            ActionController.registerActionListner(back, this.attackAction, AttackAction.PREVIOUS_ACTION);
            JButton forward = new JButton(">");
            ActionController.registerActionListner(forward, this.attackAction, AttackAction.NEXT_ACTION);
            JButton end = new JButton(">>");
            ActionController.registerActionListner(end, this.attackAction, AttackAction.LAST_ACTION);
            panel.add(beginning);
            panel.add(back);
            panel.add(forward);
            panel.add(end);
            panel.add(new JLabel("Action " + (currentAction + 1) + " of " + attack.size()));
            this.add(panel);
            JTextField action = new JTextField(attack.get(currentAction).toString());
            action.setHorizontalAlignment(JTextField.CENTER);
            this.add(action);
            JTextField log1 = new JTextField(currentLog);
            log1.setHorizontalAlignment(JTextField.CENTER);
            this.add(log1);
        }
    }
}
