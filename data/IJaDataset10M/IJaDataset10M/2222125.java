package neon.tools;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import neon.tools.help.HelpLabels;
import java.awt.event.*;
import java.awt.*;

public class InfoEditor implements ActionListener {

    private JDialog frame;

    private JTextField titleField, bigField, smallField;

    public InfoEditor(JFrame parent) {
        frame = new JDialog(parent, "Game Info Editor");
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        frame.setContentPane(content);
        JPanel edit = new JPanel();
        edit.setBorder(new TitledBorder("Properties"));
        GroupLayout layout = new GroupLayout(edit);
        edit.setLayout(layout);
        layout.setAutoCreateGaps(true);
        JLabel titleLabel = new JLabel("Title: ");
        JLabel bigLabel = new JLabel("Big coins: ");
        JLabel smallLabel = new JLabel("Small coins: ");
        titleField = new JTextField(20);
        bigField = new JTextField(20);
        bigField.setText("€");
        smallField = new JTextField(20);
        smallField.setText("c");
        JLabel titleHelpLabel = HelpLabels.getTitleHelpLabel();
        JLabel bigHelpLabel = HelpLabels.getBigHelpLabel();
        JLabel smallHelpLabel = HelpLabels.getSmallHelpLabel();
        layout.setVerticalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(titleLabel).addComponent(titleField).addComponent(titleHelpLabel)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(bigLabel).addComponent(bigField).addComponent(bigHelpLabel)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(smallLabel).addComponent(smallField).addComponent(smallHelpLabel)).addGap(10));
        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(titleLabel).addComponent(bigLabel).addComponent(smallLabel)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(titleField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(bigField).addComponent(smallField)).addGap(10).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(titleHelpLabel).addComponent(bigHelpLabel).addComponent(smallHelpLabel)));
        content.add(edit, BorderLayout.PAGE_START);
        JPanel buttons = new JPanel();
        content.add(buttons, BorderLayout.PAGE_END);
        JButton ok = new JButton("Ok");
        ok.addActionListener(this);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        JButton apply = new JButton("Apply");
        apply.addActionListener(this);
        buttons.add(ok);
        buttons.add(cancel);
        buttons.add(apply);
    }

    public void show() {
        Mod data = Editor.getStore().getActive();
        titleField.setText(data.get("title"));
        if (data.get("big") != null) {
            bigField.setText(data.get("big"));
        }
        if (data.get("small") != null) {
            smallField.setText(data.get("small"));
        }
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void save() {
        Mod data = Editor.getStore().getActive();
        if (!bigField.getText().equals(smallField.getText())) {
            if (!bigField.getText().isEmpty() && !bigField.getText().equals("€")) {
                data.set("big", bigField.getText());
            }
            if (!smallField.getText().isEmpty() && !bigField.getText().equals("c")) {
                data.set("small", smallField.getText());
            }
        }
        if (!Editor.getStore().getActive().isExtension() || !(titleField == null && titleField.getText().isEmpty())) {
            data.set("title", titleField.getText());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if ("Ok".equals(e.getActionCommand())) {
            save();
            frame.dispose();
        } else if ("Cancel".equals(e.getActionCommand())) {
            frame.dispose();
        } else if ("Apply".equals(e.getActionCommand())) {
            save();
        }
    }
}
