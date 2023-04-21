package admin.view.poker.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import admin.controller.PokerController;

public class PokerActionListener implements ActionListener {

    private PokerController controller;

    public PokerActionListener(PokerController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("Fold") || command.equals("Muck")) {
            controller.fold();
        } else if (command.equals("Raise") || command.equals("Bet")) {
            controller.raise();
        } else if (command.equals("Call") || command.equals("Show")) {
            controller.call();
        } else if (command.equals("Check")) {
            controller.check();
        } else if (command.equals("All-In")) {
            controller.allIn();
        } else if (command.equals("Connect To Server")) {
            controller.promtServerDetails();
        } else if (command.equals("Leave Table")) {
            controller.tryLeaveTable();
        } else {
            JComboBox cb = (JComboBox) event.getSource();
            String preAction = (String) cb.getSelectedItem();
            checkPreAction(preAction);
        }
    }

    private void checkPreAction(String preAction) {
        if ("Automatically Fold".equalsIgnoreCase(preAction)) {
            controller.preFold();
        } else if ("Automatically Check/Fold".equalsIgnoreCase(preAction)) {
            controller.preCheckFold();
        } else if ("Automatically Call".equalsIgnoreCase(preAction)) {
            controller.preCall();
        } else {
            controller.clearPreAction();
        }
    }
}
