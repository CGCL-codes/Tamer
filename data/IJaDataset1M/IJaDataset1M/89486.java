package client.tabs.settings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import client.GUIVariables;
import client.tabs.GUIFrame;

/**
 * Has a JList that contains the available beamer types.
 * @author Fabian
 */
public class SelectPanel extends JPanel {

    String name = null;

    String[] Liste;

    JComboBox Beamerliste;

    /**
   * Sets the name variable.
   * @param str String
   */
    public void setName(String str) {
        name = str;
    }

    /**
   * Builds the JList and adds it to the panel.
   * @param Listenstrings String[]
   */
    public SelectPanel(String[] Listenstrings) {
        int number = 0;
        Liste = Listenstrings;
        for (int i = 0; i < Liste.length; i++) {
            if (Liste[i].equals(GUIVariables.getBeamerTyp())) {
                number = i;
            }
        }
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        Beamerliste = new JComboBox(Listenstrings);
        Beamerliste.setSelectedIndex(number);
        Beamerliste.addActionListener(new SelectPanelListener(this));
        this.setBorder(BorderFactory.createTitledBorder("Beamer Type"));
        this.add(Beamerliste, c);
    }

    /**
   * Method is called when the value in the JList is changed
   * The activated beamer type is set in Variables.
   * @param e ActionEvent
   */
    public void SelectPanel_Action_Performed(ActionEvent e) {
        String bname = (String) ((JComboBox) e.getSource()).getSelectedItem();
        if (name != null && name.equalsIgnoreCase(GUIVariables.BeamerTyp)) {
            GUIVariables.setBeamerTyp(bname);
            GUIFrame g = GUIVariables.getGUIFrame();
            for (int i = 0; i < Liste.length; i++) {
                if (Liste[i].toUpperCase().contains(bname.toUpperCase())) {
                    g.allgemein.setEnabledAt(g.allgemein.indexOfTab(Liste[i]), true);
                } else {
                    g.allgemein.setEnabledAt(g.allgemein.indexOfTab(Liste[i]), false);
                }
            }
        }
    }

    /**
   * Sets the JList on the given value, if the string value
   * corresponds to a value that is in the JList.
   * @param s String
   */
    public void setPanel(String s) {
        int i;
        for (i = 0; i < Liste.length; i++) {
            if (Liste[i].equalsIgnoreCase(s)) {
                Beamerliste.setSelectedIndex(i);
            }
        }
    }
}

class SelectPanelListener implements ActionListener {

    private SelectPanel adaptee;

    SelectPanelListener(SelectPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.SelectPanel_Action_Performed(e);
    }
}
