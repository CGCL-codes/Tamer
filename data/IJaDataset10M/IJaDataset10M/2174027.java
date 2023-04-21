package neon.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.*;
import neon.core.Engine;
import neon.magic.Effect;
import neon.objects.entities.Creature;
import neon.objects.entities.Player;
import neon.objects.resources.RSpell;

public class SpellMakerDialog {

    private JDialog frame;

    private JFrame parent;

    private JPanel panel, options;

    private Player player;

    private JComboBox<Effect> effectBox;

    private JSpinner sizeSpinner, rangeSpinner, durationSpinner;

    private JTextField nameField;

    public SpellMakerDialog(JFrame parent) {
        this.parent = parent;
        frame = new JDialog(parent, true);
        frame.setPreferredSize(new Dimension(parent.getWidth() - 100, parent.getHeight() - 100));
        frame.setUndecorated(true);
        panel = new JPanel(new BorderLayout());
        panel.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.RAISED), new EmptyBorder(10, 10, 10, 10)));
        JLabel instructions = new JLabel("Use tab to switch between the available options, press enter to purchase spell, esc to cancel.");
        instructions.setBorder(new CompoundBorder(new TitledBorder("Instructions"), new EmptyBorder(0, 5, 10, 5)));
        panel.add(instructions, BorderLayout.PAGE_END);
        options = new JPanel();
        options.setBorder(new TitledBorder("Make your spell"));
        GroupLayout layout = new GroupLayout(options);
        options.setLayout(layout);
        layout.setAutoCreateGaps(true);
        JLabel nameLabel = new JLabel("Name: ");
        JLabel targetLabel = new JLabel("Target: ");
        JLabel effectLabel = new JLabel("Effect: ");
        JLabel sizeLabel = new JLabel("Size: ");
        JLabel rangeLabel = new JLabel("Range: ");
        JLabel durationLabel = new JLabel("Duration: ");
        nameField = new JTextField(20);
        effectBox = new JComboBox<Effect>(neon.magic.Effect.values());
        sizeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        rangeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        durationSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        layout.setVerticalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nameLabel).addComponent(nameField)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(targetLabel)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(effectLabel).addComponent(effectBox)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(sizeLabel).addComponent(sizeSpinner)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(rangeLabel).addComponent(rangeSpinner)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(durationLabel).addComponent(durationSpinner)));
        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(nameLabel).addComponent(targetLabel).addComponent(effectLabel).addComponent(sizeLabel).addComponent(rangeLabel).addComponent(durationLabel)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(nameField).addComponent(effectBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(sizeSpinner).addComponent(rangeSpinner).addComponent(durationSpinner)));
        panel.add(options, BorderLayout.CENTER);
        Action ok = new OkAction();
        Action cancel = new CancelAction();
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "enter");
        panel.getActionMap().put("enter", ok);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "esc");
        panel.getActionMap().put("esc", cancel);
        frame.setContentPane(panel);
        try {
            frame.setOpacity(0.9f);
        } catch (UnsupportedOperationException e) {
            System.out.println("setOpacity() not supported.");
        }
    }

    public void show(Player player, Creature enchanter) {
        this.player = player;
        frame.pack();
        frame.setLocationRelativeTo(parent);
        frame.setVisible(true);
        nameField.requestFocus();
    }

    private RSpell createSpell() {
        return new RSpell(nameField.getText(), (Integer) rangeSpinner.getValue(), (Integer) durationSpinner.getValue(), effectBox.getSelectedItem().toString(), (Integer) sizeSpinner.getValue(), (Integer) sizeSpinner.getValue(), "spell");
    }

    private boolean isValid() {
        return (!nameField.getText().equals(null) && !nameField.getText().equals(""));
    }

    @SuppressWarnings("serial")
    private class OkAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            if (isValid()) {
                player.addSpell(createSpell());
                frame.dispose();
            } else {
                Engine.getUI().showMessage("Please fill in all required fields.", 2);
            }
        }
    }

    @SuppressWarnings("serial")
    private class CancelAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            frame.dispose();
        }
    }
}
