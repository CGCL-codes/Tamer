package gui.options;

import gui.JMFrame;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import shapes.Shape;

public class JMShapePropertyDialog extends JDialog implements ActionListener {

    private JMFrame parent;

    private Shape[] selected;

    private String origLink;

    private String target;

    private JTextField[] textFields = new JTextField[6];

    private JCheckBox[] checkBoxes = new JCheckBox[6];

    private JLabel[] labels = new JLabel[6];

    public JMShapePropertyDialog(Shape[] selected, String origLink, String target, boolean mouseOverb, String mouseOver, boolean mouseOutb, String mouseOut, boolean mouseDownb, String mouseDown, boolean mouseUpb, String mouseUp, JMFrame parent) {
        super();
        this.parent = parent;
        this.selected = selected;
        this.origLink = origLink;
        this.labels[0] = new JLabel("target (required)");
        this.add(labels[0]);
        this.checkBoxes[0] = new JCheckBox();
        this.checkBoxes[0].addActionListener(this);
        this.checkBoxes[0].setSelected(true);
        this.add(this.checkBoxes[0]);
        this.textFields[0] = new JTextField(target, 20);
        this.add(textFields[0]);
        this.labels[1] = new JLabel("href (required)");
        this.add(labels[1]);
        this.checkBoxes[1] = new JCheckBox();
        this.checkBoxes[1].addActionListener(this);
        this.checkBoxes[1].setSelected(true);
        this.add(this.checkBoxes[1]);
        this.textFields[1] = new JTextField(origLink, 20);
        this.add(textFields[1]);
        this.labels[2] = new JLabel("onMouseOver");
        this.add(labels[2]);
        this.checkBoxes[2] = new JCheckBox();
        this.checkBoxes[2].addActionListener(this);
        this.add(this.checkBoxes[2]);
        this.textFields[2] = new JTextField(mouseOver, 20);
        this.add(textFields[2]);
        this.labels[3] = new JLabel("onMouseOut");
        this.add(labels[3]);
        this.checkBoxes[3] = new JCheckBox();
        this.checkBoxes[3].addActionListener(this);
        this.add(this.checkBoxes[3]);
        this.textFields[3] = new JTextField(mouseOut, 20);
        this.add(textFields[3]);
        this.labels[4] = new JLabel("onMouseDown");
        this.add(labels[4]);
        this.checkBoxes[4] = new JCheckBox();
        this.checkBoxes[4].addActionListener(this);
        this.add(this.checkBoxes[4]);
        this.textFields[4] = new JTextField(mouseDown, 20);
        this.add(textFields[4]);
        this.labels[5] = new JLabel("onMouseUp");
        this.add(labels[5]);
        this.checkBoxes[5] = new JCheckBox();
        this.checkBoxes[5].addActionListener(this);
        this.add(this.checkBoxes[5]);
        this.textFields[5] = new JTextField(mouseUp, 20);
        this.add(textFields[5]);
        this.textFields[2].setEnabled(mouseOverb);
        this.textFields[3].setEnabled(mouseOutb);
        this.textFields[4].setEnabled(mouseDownb);
        this.textFields[5].setEnabled(mouseUpb);
        this.checkBoxes[0].setEnabled(false);
        this.checkBoxes[1].setEnabled(false);
        this.checkBoxes[2].setSelected(mouseOverb);
        this.checkBoxes[3].setSelected(mouseOutb);
        this.checkBoxes[4].setSelected(mouseDownb);
        this.checkBoxes[5].setSelected(mouseUpb);
        JButton save = new JButton("Save");
        save.setActionCommand("save");
        save.addActionListener(this);
        this.add(save);
        JButton cancel = new JButton("Cancel");
        cancel.setActionCommand("cancel");
        cancel.addActionListener(this);
        this.add(cancel);
        this.setSize(300, 220);
        this.setLayout(new FlowLayout());
        this.setLocation(parent.getX() + parent.getWidth() / 2 - this.getWidth() / 2, parent.getY() + parent.getHeight() / 2 - this.getHeight() / 2);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        for (int i = 2; i < checkBoxes.length; i++) {
            textFields[i].setEnabled(checkBoxes[i].isSelected());
        }
        if (e.getActionCommand().equals("save")) {
            save();
            this.dispose();
        }
        if (e.getActionCommand().equals("cancel")) {
            this.dispose();
        }
    }

    public void save() {
        for (int i = 0; i < selected.length; i++) {
            selected[i].target = textFields[0].getText();
            selected[i].onMouseOver = textFields[2].getText();
            selected[i].onMouseOut = textFields[3].getText();
            selected[i].onMouseDown = textFields[4].getText();
            selected[i].onMouseUp = textFields[5].getText();
            selected[i].link = textFields[1].getText();
            selected[i].onMouseOverb = checkBoxes[2].isSelected();
            selected[i].onMouseOutb = checkBoxes[3].isSelected();
            selected[i].onMouseDownb = checkBoxes[4].isSelected();
            selected[i].onMouseUpb = checkBoxes[5].isSelected();
        }
        this.parent.imagePanel.refreshSource();
    }
}
