package fighter.gui;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import fighter.algorithm.FighterDescription;

@SuppressWarnings("serial")
public class FighterStatus extends JPanel {

    private JLabel health;

    private JLabel name;

    public FighterStatus(final FighterDescription fighterDescription) {
        setLayout(new GridLayout(2, 1));
        this.name = new JLabel(fighterDescription.getFighter().getName());
        add(name);
        this.health = new JLabel() {

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                this.setText(String.valueOf(fighterDescription.getHealth()));
            }
        };
        add(health);
    }
}
