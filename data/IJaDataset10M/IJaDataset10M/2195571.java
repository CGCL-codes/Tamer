package org.knosoft.gui.window;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class ConfigWindow extends JDialog {

    Frame parent;

    Dimension minimunDimension = new Dimension(400, 150);

    public ConfigWindow(Frame frame) {
        super(frame);
        this.parent = frame;
        this.setTitle("Nueva Conexion");
        this.init();
    }

    private void init() {
        this.setLocationRelativeTo(this.parent);
        Container container = this.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        this.add(createStepOne());
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(minimunDimension);
    }

    private JPanel createStepOne() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("Nombre"), constraints);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(new JTextField(30), constraints);
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(new JButton("Hola"), constraints);
        Border compound = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());
        panel.setBorder(compound);
        return panel;
    }
}
