package mekhangar.design.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import mekhangar.design.gui.models.IntegerModel;

public class IntegerButton extends JButton implements ActionListener {

    private IntegerModel model;

    private boolean increase;

    public IntegerButton(IntegerModel model, boolean increase) {
        this.model = model;
        this.increase = increase;
        if (increase) {
            setText("+");
        } else {
            setText("-");
        }
        addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (increase) {
            model.setValue(model.getValue() + 1);
        } else {
            model.setValue(model.getValue() - 1);
        }
    }
}
