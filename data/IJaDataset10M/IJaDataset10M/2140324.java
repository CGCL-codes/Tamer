package com.seesman.application.maintenance.common.gui.utilities;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Panel extends JPanel {

    private JTextField[] fields;

    private JPanel labelPanel;

    private JPanel fieldPanel;

    public Panel(Map<? extends Object, String> map, Class<?> enumName) {
        super(new BorderLayout());
        int length = map.size();
        if (enumName.isEnum()) {
            List<?> list = Arrays.asList(enumName.getEnumConstants());
            if (length == list.size()) {
                labelPanel = new JPanel(new GridLayout(length, 1));
                fieldPanel = new JPanel(new GridLayout(length, 1));
                add(labelPanel, BorderLayout.WEST);
                add(fieldPanel, BorderLayout.CENTER);
                fields = new JTextField[length];
                for (int i = 0; i < length; i += 1) {
                    fields[i] = new JTextField();
                    fields[i].setColumns(20);
                    fields[i].setName(map.get(list.get(i)));
                    JLabel label = new JLabel("   " + map.get(list.get(i)), JLabel.LEFT);
                    label.setLabelFor(fields[i]);
                    labelPanel.add(label);
                    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    panel.add(fields[i]);
                    fieldPanel.add(panel);
                    System.out.println("DITO BA");
                }
            }
        }
    }

    public Panel(Map<? extends Object, String> map, Class<?> enumName, String str) {
        super(new BorderLayout());
        int length = map.size();
        if (enumName.isEnum()) {
            List<?> list = Arrays.asList(enumName.getEnumConstants());
            if (length == list.size()) {
                labelPanel = new JPanel(new GridLayout(length, 1));
                fieldPanel = new JPanel(new GridLayout(length, 1));
                add(labelPanel, BorderLayout.WEST);
                add(fieldPanel, BorderLayout.CENTER);
                for (int i = 0; i < (length - 1); i++) {
                    JLabel label = new JLabel("   " + map.get(list.get(i)), JLabel.LEFT);
                    labelPanel.add(label);
                    JLabel labels = new JLabel();
                    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    if (i == 4 || i == 6) {
                        labels.setText("   " + map.get(list.get(i + 1)));
                        panel.add(labels);
                        i++;
                    } else {
                        panel.add(labels);
                    }
                    fieldPanel.add(panel);
                }
            }
        }
    }
}
