package view.items;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import constants.Icons;
import controller.CompleteListener;

public class LOSPanel extends ItemPanel implements FocusListener {

    private JTextField waitField;

    public LOSPanel() {
        super();
        waitField = new JTextField("0", 3);
        JPanel row1 = new JPanel();
        JPanel row2 = new JPanel();
        addButton.setActionCommand("addLOS");
        delButton.setActionCommand("delLOS");
        addButton.setToolTipText("Adds a LOS event");
        delButton.setToolTipText("Removes the selected LOS event");
        setAddIcon(Icons.getIcon("addLOS"));
        setDelIcon(Icons.getIcon("delLOS"));
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        row1.setLayout(new BoxLayout(row1, BoxLayout.LINE_AXIS));
        row2.setLayout(new BoxLayout(row2, BoxLayout.LINE_AXIS));
        this.setBorder(BorderFactory.createTitledBorder("LOS"));
        row1.setBorder(BorderFactory.createEmptyBorder(0, 2, 3, 2));
        row2.setBorder(BorderFactory.createEmptyBorder(3, 2, 0, 2));
        row1.add(addButton);
        row1.add(Box.createHorizontalStrut(5));
        row1.add(delButton);
        row2.add(new JLabel("Check for"));
        row2.add(Box.createHorizontalStrut(5));
        row2.add(waitField);
        row2.add(Box.createHorizontalStrut(5));
        row2.add(new JLabel("sec."));
        this.add(row1);
        this.add(row2);
        this.add(Box.createVerticalGlue());
        waitField.addFocusListener(this);
        waitField.setMaximumSize(new Dimension(50, 20));
        waitField.setMinimumSize(new Dimension(20, 20));
        this.setMaximumSize(new Dimension(200, 80));
    }

    public int getWaitTime() {
        int ret = -1;
        try {
            ret = Integer.parseInt(waitField.getText());
        } catch (NumberFormatException e) {
        }
        return ret;
    }

    public void setIdle() {
        addButton.setIcon(Icons.getIcon("addLOS"));
        delButton.setIcon(Icons.getIcon("delLOS"));
    }

    public void setAddingLOS() {
        addButton.setIcon(Icons.getIcon("addingLOS"));
        delButton.setIcon(Icons.getIcon("delLOS"));
    }

    public void setDeletingLOS() {
        addButton.setIcon(Icons.getIcon("addLOS"));
        delButton.setIcon(Icons.getIcon("deletingLOS"));
    }

    @Override
    public void attachListener(CompleteListener l) {
        addButton.addActionListener(l);
        delButton.addActionListener(l);
    }

    @Override
    public void setEnabled(boolean enable) {
        addButton.setEnabled(enable);
        delButton.setEnabled(enable);
        waitField.setEnabled(enable);
    }

    public void focusGained(FocusEvent arg0) {
        waitField.selectAll();
    }

    public void focusLost(FocusEvent arg0) {
    }
}
