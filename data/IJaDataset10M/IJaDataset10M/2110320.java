package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author wouter debecker
 */
public class SettingsControleerPanel extends TopPanel implements ActionListener {

    private JButton terugkeren;

    protected void addHoofdingWidgets() {
        JLabel l1 = new JLabel("SETTINGS");
        getHoofding().add(l1);
    }

    protected void addInhoudWidgets() {
    }

    protected void addEindeWidgets() {
        JPanel knoppen2 = new JPanel();
        terugkeren = new JButton("Terug");
        terugkeren.setActionCommand("terug");
        terugkeren.addActionListener(this);
        knoppen2.add(terugkeren);
        getEinde().add(knoppen2);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("terug")) {
            WindowManager.Instantie().showControleerPanel();
        }
    }
}
