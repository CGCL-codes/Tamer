package grog.gui;

import grog.core.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Marco Brambilla
 */
public class CasketPanel extends JPanel implements ActionListener {

    protected enum ITEM_KIND {

        CONTINGENT, SECTION, UNIT, MODEL, OPTION, CHOICE
    }

    ;

    private Army army;

    private ItemCasket caskets[] = new ItemCasket[ITEM_KIND.values().length];

    private JScrollPane scrolls[] = new JScrollPane[ITEM_KIND.values().length];

    private JButton cmdEdit = new JButton("Edit");

    private JButton cmdSave = new JButton("Save");

    private JButton cmdAdd = new JButton("+");

    private JButton cmdRemove = new JButton("-");

    /** Creates a new instance of CasketPanel */
    public CasketPanel(Army army) {
        this.army = army;
        caskets[0] = new ItemCasket(army, new ArrayList<Contingent>());
        caskets[1] = new ItemCasket(army, new ArrayList<Section>());
        caskets[2] = new ItemCasket(army, new ArrayList<Unit>());
        caskets[3] = new ItemCasket(army, new ArrayList<Model>());
        caskets[4] = new ItemCasket(army, new ArrayList<Option>());
        caskets[5] = new ItemCasket(army, new ArrayList<Choice>());
        for (int i = 0; i < 6; i++) {
            caskets[i].setVisibleRowCount(5);
            scrolls[i] = new JScrollPane(caskets[i]);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(cmdEdit.getActionCommand())) {
            for (int i = 0; i < ITEM_KIND.values().length; i++) {
                if (caskets[i].hasFocus()) caskets[i].editItem();
            }
        } else if (e.getActionCommand().equals(cmdAdd.getActionCommand())) {
        } else if (e.getActionCommand().equals(cmdRemove.getActionCommand())) {
        }
    }
}
