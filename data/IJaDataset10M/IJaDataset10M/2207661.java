package se.entitymanager.presentation;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Action handling for quiting the application
 */
public class InfoAction extends AbstractAction {

    /**
     * The icon to use for the info dialog.<p>
     * 
     * @uml.property name="icon"
     * @uml.associationEnd 
     * @uml.property name="icon" multiplicity="(1 1)"
     */
    private Icon icon;

    public InfoAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
        super(text, icon);
        this.icon = icon;
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
    }

    public void actionPerformed(ActionEvent e) {
        createInfoDialog();
    }

    private void createInfoDialog() {
        JPanel propertiesPanel = new JPanel();
        propertiesPanel.setLayout(new GridLayout(0, 2));
        propertiesPanel.add(new JLabel("SE Fallstudie:   "));
        propertiesPanel.add(new JLabel("Swing File Manager"));
        propertiesPanel.add(new JLabel());
        propertiesPanel.add(new JLabel());
        propertiesPanel.add(new JLabel("Autor:   "));
        propertiesPanel.add(new JLabel("Stefan Gudenkauf"));
        propertiesPanel.add(new JLabel("Version:   "));
        propertiesPanel.add(new JLabel("1.0"));
        propertiesPanel.add(new JLabel("Datum:   "));
        propertiesPanel.add(new JLabel("24. August 2004"));
        propertiesPanel.add(new JLabel("Organisation:   "));
        propertiesPanel.add(new JLabel("Carl von Ossietzky"));
        propertiesPanel.add(new JLabel());
        propertiesPanel.add(new JLabel("Universit�t Oldenburg"));
        propertiesPanel.add(new JLabel());
        propertiesPanel.add(new JLabel());
        propertiesPanel.add(new JLabel("E-Mail:   "));
        propertiesPanel.add(new JLabel("stefan@gudenkauf.de"));
        JOptionPane.showMessageDialog(new JFrame(), propertiesPanel, "Info...", JOptionPane.INFORMATION_MESSAGE, icon);
    }
}
