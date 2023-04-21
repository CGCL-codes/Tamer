package de.outofbounds.kinderactive.gui.learn.money;

import de.outofbounds.kinderactive.audio.NumberSpeaker;
import de.outofbounds.kinderactive.gui.CategoryTab;
import java.awt.GridLayout;
import javax.swing.JPanel;

/**
 *
 * @author root
 */
public class LearnMoneyBillsTab extends CategoryTab {

    public LearnMoneyBillsTab() {
        super("intro-bills", "ask-drueckauf");
    }

    protected JPanel buildButtonPanel() {
        String[][] values = { { "euro", "500" }, { "euro", "200" }, { "euro", "100" }, { "euro", "50" }, { "euro", "20" }, { "euro", "10" }, { "euro", "5" } };
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3));
        NumberSpeaker speaker = new NumberSpeaker(true);
        for (int i = 0; i < values.length; i++) {
            String amount = values[i][1];
            String unit = values[i][0];
            MoneyButton button = new MoneyButton(amount, unit, speaker, this);
            panel.add(button);
        }
        return panel;
    }
}
