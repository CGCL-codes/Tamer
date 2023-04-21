package edu.whitman.halfway.jigs.gui.jigspace;

import org.apache.log4j.Logger;
import java.beans.*;
import javax.swing.JOptionPane;
import javax.swing.*;
import edu.whitman.halfway.jigs.*;
import java.awt.*;

public class JSRSImageTypeDialog extends JDialog implements PropertyChangeListener {

    public JSRSImageTypeDialog(JFrame frame) {
        super(frame, true);
        JPanel p = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        p.setLayout(gridbag);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2, 2, 2, 2);
        gridAdd(p, new JLabel("Image Type", JLabel.LEFT), 0, 0, 1, c, gridbag);
        longName = new JTextField(40);
        gridAdd(p, longName, 1, 0, 3, c, gridbag);
        gridAdd(p, new JLabel("Short Name", JLabel.LEFT), 0, 1, 1, c, gridbag);
        shortName = new JTextField(40);
        gridAdd(p, shortName, 1, 1, 3, c, gridbag);
        gridAdd(p, new JLabel("Image Quality", JLabel.LEFT), 0, 2, 1, c, gridbag);
        imageQuality = new JSlider(JSlider.HORIZONTAL, 50, 100, 85);
        imageQuality.setMajorTickSpacing(10);
        imageQuality.setMinorTickSpacing(2);
        imageQuality.setPaintTicks(true);
        imageQuality.setPaintLabels(true);
        imageQuality.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        gridAdd(p, imageQuality, 1, 2, 3, c, gridbag);
        gridAdd(p, new JLabel("Max Height", JLabel.LEFT), 0, 3, 1, c, gridbag);
        maxHeight = new JTextField(10);
        gridAdd(p, maxHeight, 1, 3, 1, c, gridbag);
        gridAdd(p, new JLabel("Max Width", JLabel.LEFT), 2, 3, 1, c, gridbag);
        maxWidth = new JTextField(10);
        gridAdd(p, maxWidth, 3, 3, 1, c, gridbag);
        gridAdd(p, new JLabel("Min Height", JLabel.LEFT), 0, 4, 1, c, gridbag);
        minHeight = new JTextField(10);
        gridAdd(p, minHeight, 1, 4, 1, c, gridbag);
        gridAdd(p, new JLabel("Min Width", JLabel.LEFT), 2, 4, 1, c, gridbag);
        minWidth = new JTextField(10);
        gridAdd(p, minWidth, 3, 4, 1, c, gridbag);
        scaleUp = new JCheckBox("Enlarge to Fit");
        gridAdd(p, scaleUp, 0, 5, 2, c, gridbag);
        copy = new JCheckBox("Copy Image");
        gridAdd(p, copy, 2, 5, 2, c, gridbag);
        scaleDown = new JCheckBox("Shrink to Fit");
        gridAdd(p, scaleDown, 0, 6, 2, c, gridbag);
        useOriginal = new JCheckBox("Scale Original");
        gridAdd(p, useOriginal, 2, 6, 2, c, gridbag);
        Object[] options = { "Cancel", "OK" };
        optionPane = new JOptionPane(p, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[1]);
        optionPane.addPropertyChangeListener(this);
        setContentPane(optionPane);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.pack();
        this.setSize(new Dimension(370, 290));
    }

    public void load(ImageType imageData) {
        longName.setText(imageData.getLongName());
        shortName.setText(imageData.getShortName());
        imageQuality.setValue(imageData.quality);
        maxHeight.setText("" + imageData.maxHeight);
        minHeight.setText("" + imageData.minHeight);
        maxWidth.setText("" + imageData.maxWidth);
        minWidth.setText("" + imageData.minWidth);
        scaleUp.setSelected(imageData.scaleUp);
        scaleDown.setSelected(imageData.scaleDown);
        copy.setSelected(imageData.copy);
        show();
    }

    private void gridAdd(Container root, Component obj, int x, int y, int width, GridBagConstraints c, GridBagLayout gridbag) {
        c.weighty = 0.5;
        c.ipadx = 0;
        c.ipady = 0;
        if (obj instanceof JLabel) {
            c.weightx = 0;
            obj.setForeground(Color.black);
        } else if (obj instanceof JButton) {
            c.weightx = 0;
            c.ipadx = 2;
            c.ipady = 2;
        } else {
            c.weightx = 0.5;
        }
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = width;
        gridbag.setConstraints(obj, c);
        root.add(obj);
    }

    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (isVisible() && (e.getSource() == optionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
            Object value = optionPane.getValue();
            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                return;
            }
            optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
            if (value.equals("Cancel")) {
                imageType = null;
                setVisible(false);
            } else {
                int i;
                imageType = new ImageType();
                if (!validTextValue(longName, "Image Type")) return; else imageType.lng = longName.getText();
                if (!validTextValue(shortName, "Short Name")) return; else imageType.shrt = shortName.getText();
                if ((i = getIntValue(maxWidth, "Max Width")) < 0) return; else imageType.maxWidth = i;
                if ((i = getIntValue(minWidth, "Min Width")) < 0) return; else imageType.minWidth = i;
                if ((i = getIntValue(maxHeight, "Max Height")) < 0) return; else imageType.maxHeight = i;
                if ((i = getIntValue(minHeight, "Min Height")) < 0) return; else imageType.minHeight = i;
                imageType.quality = imageQuality.getValue();
                imageType.scaleUp = scaleUp.isSelected();
                imageType.scaleDown = scaleDown.isSelected();
                imageType.copy = copy.isSelected();
                setVisible(false);
            }
        }
    }

    public ImageType getImageType() {
        return imageType;
    }

    private boolean validTextValue(JTextField obj, String name) {
        if (obj.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Invalid Name in " + name + " Field.", "Error", JOptionPane.ERROR_MESSAGE);
            obj.selectAll();
            obj.requestFocus();
            return false;
        }
        return true;
    }

    private int getIntValue(JTextField obj, String name) {
        int value;
        try {
            value = Integer.parseInt(obj.getText());
        } catch (NumberFormatException e) {
            value = -1;
        }
        if (value < 0) {
            JOptionPane.showMessageDialog(this, "Invalid Integer Value in " + name + " Field.", "Error", JOptionPane.ERROR_MESSAGE);
            obj.selectAll();
            obj.requestFocus();
        }
        return value;
    }

    private ImageType imageType;

    private JCheckBox scaleUp;

    private JCheckBox scaleDown;

    private JCheckBox copy;

    private JCheckBox useOriginal;

    private JTextField maxHeight;

    private JTextField maxWidth;

    private JTextField minHeight;

    private JTextField minWidth;

    private JTextField shortName;

    private JTextField longName;

    private JSlider imageQuality;

    private JOptionPane optionPane;

    protected Logger log = Logger.getLogger(JSRSImageTypeDialog.class.getName());
}
