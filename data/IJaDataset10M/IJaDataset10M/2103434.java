package org.myrobotlab.control;

import java.awt.GridBagLayout;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import org.myrobotlab.service.interfaces.GUI;

public class JoystickServiceGUI extends ServiceGUI {

    static final long serialVersionUID = 1L;

    private JComboBox joystickIndex = null;

    private JTextField rMultiplier = null;

    private JTextField zMultiplier = null;

    private JTextField rOffset = null;

    private JTextField zOffset = null;

    public JoystickServiceGUI(final String boundServiceName, final GUI myService) {
        super(boundServiceName, myService);
    }

    public void init() {
        Vector<Integer> idx = new Vector<Integer>();
        idx.addElement(1);
        idx.addElement(2);
        idx.addElement(3);
        idx.addElement(4);
        joystickIndex = new JComboBox(idx);
        JPanel axis = new JPanel();
        axis.setLayout(new GridBagLayout());
        TitledBorder title;
        title = BorderFactory.createTitledBorder("axis multiplier/offset");
        axis.setBorder(title);
        rMultiplier = new JTextField(5);
        rMultiplier.setHorizontalAlignment(JTextField.RIGHT);
        rMultiplier.setText("90");
        zMultiplier = new JTextField(5);
        zMultiplier.setHorizontalAlignment(JTextField.RIGHT);
        zMultiplier.setText("90");
        rOffset = new JTextField(5);
        rOffset.setHorizontalAlignment(JTextField.RIGHT);
        rOffset.setText("0");
        zOffset = new JTextField(5);
        zOffset.setHorizontalAlignment(JTextField.RIGHT);
        zOffset.setText("0");
        axis.add(new JLabel("R Axis"));
        axis.add(rMultiplier);
        axis.add(rOffset);
        axis.add(new JLabel("Z Axis"));
        axis.add(zMultiplier);
        axis.add(zOffset);
        display.add(joystickIndex);
        display.add(axis);
    }

    @Override
    public void attachGUI() {
    }

    @Override
    public void detachGUI() {
    }
}
