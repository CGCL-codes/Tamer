package neon.tools.objects;

import java.awt.event.*;
import javax.swing.*;
import org.jdom.Element;
import java.awt.*;
import java.text.NumberFormat;
import javax.swing.border.*;
import javax.swing.text.MaskFormatter;
import neon.tools.ColorCellRenderer;
import neon.util.ColorFactory;

public class MoneyEditor implements ObjectEditor {

    private JDialog frame;

    private JPanel itemProps;

    private JTextField nameField;

    private JFormattedTextField costField, charField;

    private JComboBox colorBox;

    private Element data;

    public MoneyEditor(JFrame parent, Element data) {
        frame = new JDialog(parent, "Money Editor: " + data.getAttributeValue("id"));
        JPanel content = new JPanel(new BorderLayout());
        frame.setContentPane(content);
        this.data = data;
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
        itemProps = new JPanel();
        GroupLayout layout = new GroupLayout(itemProps);
        itemProps.setLayout(layout);
        layout.setAutoCreateGaps(true);
        JLabel nameLabel = new JLabel("Name: ");
        JLabel costLabel = new JLabel("Cost: ");
        JLabel colorLabel = new JLabel("Color: ");
        JLabel charLabel = new JLabel("Character: ");
        nameField = new JTextField(10);
        costField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        colorBox = new JComboBox(ColorFactory.getColorNames().toArray());
        colorBox.setBackground(Color.black);
        colorBox.setRenderer(new ColorCellRenderer());
        colorBox.addActionListener(this);
        charField = new JFormattedTextField(getMaskFormatter("*"));
        layout.setVerticalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nameLabel).addComponent(nameField)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(costLabel).addComponent(costField)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(colorLabel).addComponent(colorBox)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(charLabel).addComponent(charField)));
        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(nameLabel).addComponent(costLabel).addComponent(colorLabel).addComponent(charLabel)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(costField).addComponent(colorBox).addComponent(charField)));
        JScrollPane propScroller = new JScrollPane(itemProps);
        propScroller.setBorder(new TitledBorder("Properties"));
        content.add(propScroller, BorderLayout.CENTER);
        initProps();
    }

    public void show() {
        frame.pack();
        frame.setVisible(true);
    }

    private void initProps() {
        if (data.getName() == "newt") {
            createDefault();
        }
        nameField.setText(data.getAttributeValue("name"));
        costField.setValue(Integer.parseInt(data.getAttributeValue("cost")));
        colorBox.setSelectedItem(data.getAttributeValue("color"));
        charField.setValue(data.getAttributeValue("char"));
    }

    private void createDefault() {
        data.setName("coin");
        data.setAttribute("color", "white");
        data.setAttribute("cost", "0");
        data.setAttribute("char", "€");
    }

    private void save() {
        if (nameField.getText() != null && !nameField.getText().isEmpty()) {
            data.setAttribute("name", nameField.getText());
        }
        if (costField.getValue().toString() != "0") {
            data.setAttribute("cost", costField.getValue().toString());
        }
        data.setAttribute("color", (String) colorBox.getSelectedItem());
        data.setAttribute("char", charField.getValue().toString());
    }

    private MaskFormatter getMaskFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException e) {
        }
        return formatter;
    }

    public void actionPerformed(ActionEvent e) {
        if ("Ok".equals(e.getActionCommand())) {
            save();
            frame.dispose();
        } else if ("Cancel".equals(e.getActionCommand())) {
            frame.dispose();
        } else if ("Apply".equals(e.getActionCommand())) {
            save();
        } else {
            colorBox.setForeground(neon.util.ColorFactory.getColor((String) colorBox.getSelectedItem()));
        }
    }
}
