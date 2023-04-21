package lug.serenity.npc.gui.controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import lug.serenity.npc.model.event.StatChangeEvent;
import lug.serenity.npc.model.event.StatChangeListener;
import lug.serenity.npc.model.stats.NamedStat;
import lug.gui.CachedImageLoader;

/**
 * @author Luggy
 *
 */
public class StatControl extends JPanel implements StatChangeListener {

    public static final String MINUS_BUTTON = "minus.button";

    public static final String VALUE_FIELD = "value.field";

    public static final String PLUS_BUTTON = "plus.button";

    public static final String NAME_LABEL = "name.label";

    private static final long serialVersionUID = 1L;

    private static final Icon PLUS_ICON = CachedImageLoader.getCachedIcon("images/plus_button.png");

    private static final Icon MINUS_ICON = CachedImageLoader.getCachedIcon("images/minus_button.png");

    private NamedStat namedStat;

    private JLabel nameLabel;

    private JTextField valueField;

    private Action plusAction;

    private Action minusAction;

    private JButton plusButton;

    private JButton minusButton;

    private int valueMax = 16;

    private int valueMin = 4;

    private Font bold = this.getFont().deriveFont(Font.BOLD);

    public StatControl(NamedStat orgStat) {
        createGUI();
        setNamedStat(orgStat);
    }

    private void createGUI() {
        setLayout(new BorderLayout());
        nameLabel = new JLabel("Name");
        valueField = new JTextField(7);
        plusAction = new AbstractAction("", PLUS_ICON) {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent ae) {
                doPlus();
            }
        };
        minusAction = new AbstractAction("", MINUS_ICON) {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent ae) {
                doMinus();
            }
        };
        plusButton = new JButton(plusAction);
        minusButton = new JButton(minusAction);
        nameLabel.setName(NAME_LABEL);
        plusButton.setName(PLUS_BUTTON);
        valueField.setName(VALUE_FIELD);
        minusButton.setName(MINUS_BUTTON);
        JPanel spinPanel = new JPanel(new BorderLayout());
        spinPanel.add(minusButton, BorderLayout.WEST);
        spinPanel.add(valueField, BorderLayout.CENTER);
        spinPanel.add(plusButton, BorderLayout.EAST);
        add(spinPanel, BorderLayout.EAST);
        add(nameLabel, BorderLayout.CENTER);
        plusButton.setMargin(new Insets(0, 0, 0, 0));
        minusButton.setMargin(new Insets(0, 0, 0, 0));
        nameLabel.setBorder(new EmptyBorder(0, 2, 0, 2));
        valueField.setHorizontalAlignment(SwingConstants.CENTER);
        valueField.setFont(bold);
        valueField.setEditable(false);
        Dimension nameSize = new Dimension(72, nameLabel.getHeight());
        nameLabel.setPreferredSize(nameSize);
    }

    /**
	 * Increase the value (if allowed)
	 */
    protected void doPlus() {
        if (namedStat.getValue() >= valueMax) {
            return;
        }
        namedStat.setValue(namedStat.getValue() + 2);
    }

    /**
	 * Decrease the value (if allowed)
	 */
    protected void doMinus() {
        if (namedStat.getValue() <= valueMin) {
            return;
        }
        namedStat.setValue(namedStat.getValue() - 2);
    }

    public static void main(String[] args) {
        try {
            JFrame win = new JFrame("Stat control test");
            NamedStat stat = new NamedStat(14, "Intelligence", "INT");
            StatControl control = new StatControl(stat);
            win.add(control);
            win.pack();
            win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            win.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * @return Returns the namedStat.
	 */
    public NamedStat getNamedStat() {
        return namedStat;
    }

    /**
	 * @param nStat The namedStat to populate this gui with
	 */
    public void setNamedStat(NamedStat nStat) {
        if (namedStat != null) {
            namedStat.removeStatChangeListener(this);
        }
        this.namedStat = nStat;
        namedStat.addStatChangeListener(this);
        nameLabel.setText(namedStat.getName());
        valueField.setText(namedStat.getDisplay());
        checkActions();
    }

    /**
	 * Enable or disable the up and down actions as required for the current stat and
	 * the current maximum value.
	 */
    private void checkActions() {
        plusAction.setEnabled(namedStat.getValue() < valueMax);
        minusAction.setEnabled(namedStat.getValue() > valueMin);
    }

    /**
	 * Tells this control how many points are available to spend.  This will
	 * disable the maximum value (and thus the up button) if not enough points exist to
	 * raise the current stat value.
	 * @param pts
	 */
    public void setPointsAvailable(int pts) {
        int highest = namedStat.getValue() + pts;
        valueMax = (highest > 16 ? 16 : highest);
        checkActions();
    }

    public void statChanged(StatChangeEvent sce) {
        nameLabel.setText(namedStat.getName());
        valueField.setText(namedStat.getDisplay());
        checkActions();
    }

    @Override
    public void setBackground(Color c) {
        super.setBackground(c);
        if (valueField != null) {
            valueField.setBackground(c);
        }
    }
}
