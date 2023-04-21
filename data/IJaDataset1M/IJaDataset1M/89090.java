package raja.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.Border;

/**
 * Class defining the dialog for the ray tracing parameters.
 */
class ComputeParametersDialog extends JDialog {

    private NumberTextField xResolTextField, yResolTextField, depthTextField;

    private JRadioButton defaultSamplingRadioButton, antialiasSamplingRadioButton;

    private JRadioButton t2bPixelSchedulerRadioButton, b2tPixelSchedulerRadioButton;

    private JRadioButton l2rPixelSchedulerRadioButton, r2lPixelSchedulerRadioButton;

    private JRadioButton dyadicPixelSchedulerRadioButton, spiralPixelSchedulerRadioButton;

    private JRadioButton defaultThreadingRadioButton, multiThreadingRadioButton;

    private JComboBox antialiasLevelComboBox, nbThreadsComboBox;

    private JCheckBox exactCheckBox;

    private String antialiasLevelString, nbThreadsString;

    int xResol, yResol, depth;

    int antialiasLevel, pixelSchedulerType, nbThreads;

    boolean antialiasing, exact, multiThreading;

    ComputeParametersDialog(Frame owner) {
        super(owner, "Ray tracing parameters", true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                restoreParameters();
                hide();
            }
        });
        xResolTextField = new NumberTextField(256);
        yResolTextField = new NumberTextField(192);
        depthTextField = new NumberTextField(5);
        defaultSamplingRadioButton = new JRadioButton("Default");
        antialiasSamplingRadioButton = new JRadioButton("Antialias");
        ButtonGroup samplingGroup = new ButtonGroup();
        samplingGroup.add(defaultSamplingRadioButton);
        samplingGroup.add(antialiasSamplingRadioButton);
        defaultSamplingRadioButton.setSelected(true);
        dyadicPixelSchedulerRadioButton = new JRadioButton("Dyadic");
        spiralPixelSchedulerRadioButton = new JRadioButton("Spiral");
        t2bPixelSchedulerRadioButton = new JRadioButton("Top to bottom");
        b2tPixelSchedulerRadioButton = new JRadioButton("Bottom to top");
        l2rPixelSchedulerRadioButton = new JRadioButton("Left to right");
        r2lPixelSchedulerRadioButton = new JRadioButton("Right to left");
        ButtonGroup pixelSchedulerGroup = new ButtonGroup();
        pixelSchedulerGroup.add(dyadicPixelSchedulerRadioButton);
        pixelSchedulerGroup.add(spiralPixelSchedulerRadioButton);
        pixelSchedulerGroup.add(t2bPixelSchedulerRadioButton);
        pixelSchedulerGroup.add(b2tPixelSchedulerRadioButton);
        pixelSchedulerGroup.add(l2rPixelSchedulerRadioButton);
        pixelSchedulerGroup.add(r2lPixelSchedulerRadioButton);
        dyadicPixelSchedulerRadioButton.setSelected(true);
        defaultThreadingRadioButton = new JRadioButton("Default");
        multiThreadingRadioButton = new JRadioButton("Multi threading");
        ButtonGroup threadingGroup = new ButtonGroup();
        threadingGroup.add(defaultThreadingRadioButton);
        threadingGroup.add(multiThreadingRadioButton);
        defaultThreadingRadioButton.setSelected(true);
        String[] antialiasLevelValues = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16" };
        antialiasLevelComboBox = new JComboBox(antialiasLevelValues);
        antialiasLevelComboBox.setSelectedIndex(1);
        String[] nbThreadsValues = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" };
        nbThreadsComboBox = new JComboBox(nbThreadsValues);
        nbThreadsComboBox.setSelectedIndex(1);
        exactCheckBox = new JCheckBox("Exact");
        exactCheckBox.setSelected(false);
        saveParameters();
        antialiasLevelComboBox.setEnabled(false);
        antialiasSamplingRadioButton.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                antialiasLevelComboBox.setEnabled(antialiasSamplingRadioButton.isSelected());
            }
        });
        nbThreadsComboBox.setEnabled(false);
        multiThreadingRadioButton.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                nbThreadsComboBox.setEnabled(multiThreadingRadioButton.isSelected());
            }
        });
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String emptyTxtField = null;
                if (xResolTextField.getText().equals("")) {
                    emptyTxtField = "X Resolution";
                } else if (yResolTextField.getText().equals("")) {
                    emptyTxtField = "Y Resolution";
                } else if (depthTextField.getText().equals("")) {
                    emptyTxtField = "Depth";
                }
                if (emptyTxtField != null) {
                    JOptionPane.showMessageDialog(ComputeParametersDialog.this, emptyTxtField + " field cannot be empty.", "Empty Field Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                saveParameters();
                hide();
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                restoreParameters();
                hide();
            }
        });
        JPanel pane = new JPanel();
        setContentPane(pane);
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        JPanel paneResolution = new JPanel();
        paneResolution.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Resolution"), emptyBorder));
        paneResolution.setLayout(new GridLayout(2, 2, 5, 5));
        paneResolution.add(new JLabel("X resolution"));
        paneResolution.add(xResolTextField);
        paneResolution.add(new JLabel("Y resolution"));
        paneResolution.add(yResolTextField);
        pane.add(paneResolution);
        pane.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel paneSampling = new JPanel();
        paneSampling.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Sampling"), emptyBorder));
        paneSampling.setLayout(new GridLayout(2, 1, 5, 5));
        paneSampling.add(defaultSamplingRadioButton);
        Box antialiasBox = new Box(BoxLayout.X_AXIS);
        antialiasBox.add(antialiasSamplingRadioButton);
        antialiasBox.add(antialiasLevelComboBox);
        antialiasBox.add(Box.createHorizontalGlue());
        paneSampling.add(antialiasBox);
        pane.add(paneSampling);
        pane.add(Box.createRigidArea(new Dimension(0, 15)));
        JPanel panePixelScheduling = new JPanel();
        panePixelScheduling.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Pixel scheduling"), emptyBorder));
        panePixelScheduling.setLayout(new GridLayout(3, 2, 5, 5));
        panePixelScheduling.add(t2bPixelSchedulerRadioButton);
        panePixelScheduling.add(l2rPixelSchedulerRadioButton);
        panePixelScheduling.add(b2tPixelSchedulerRadioButton);
        panePixelScheduling.add(r2lPixelSchedulerRadioButton);
        panePixelScheduling.add(dyadicPixelSchedulerRadioButton);
        panePixelScheduling.add(spiralPixelSchedulerRadioButton);
        pane.add(panePixelScheduling);
        pane.add(Box.createRigidArea(new Dimension(0, 15)));
        JPanel paneRecursivity = new JPanel();
        paneRecursivity.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Recursivity"), emptyBorder));
        paneRecursivity.setLayout(new GridLayout(2, 2, 5, 5));
        paneRecursivity.add(new JLabel("Depth"));
        paneRecursivity.add(depthTextField);
        paneRecursivity.add(exactCheckBox);
        pane.add(paneRecursivity);
        pane.add(Box.createRigidArea(new Dimension(0, 15)));
        JPanel paneRenderingThreads = new JPanel();
        paneRenderingThreads.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Rendering threads"), emptyBorder));
        paneRenderingThreads.setLayout(new GridLayout(2, 1, 5, 5));
        paneRenderingThreads.add(defaultThreadingRadioButton);
        Box multiThreadingBox = new Box(BoxLayout.X_AXIS);
        multiThreadingBox.add(multiThreadingRadioButton);
        multiThreadingBox.add(nbThreadsComboBox);
        multiThreadingBox.add(Box.createHorizontalGlue());
        paneRenderingThreads.add(multiThreadingBox);
        pane.add(paneRenderingThreads);
        pane.add(Box.createRigidArea(new Dimension(0, 15)));
        JPanel paneButtons = new JPanel();
        paneButtons.setLayout(new BoxLayout(paneButtons, BoxLayout.X_AXIS));
        paneButtons.add(Box.createHorizontalGlue());
        paneButtons.add(okButton);
        paneButtons.add(Box.createRigidArea(new Dimension(10, 0)));
        paneButtons.add(cancelButton);
        pane.add(paneButtons);
    }

    /**
     * Saves the parameters in the package accessible fields.
     */
    private void saveParameters() {
        xResol = xResolTextField.getValue();
        yResol = yResolTextField.getValue();
        depth = depthTextField.getValue();
        antialiasing = antialiasSamplingRadioButton.isSelected();
        antialiasLevelString = (String) antialiasLevelComboBox.getSelectedItem();
        antialiasLevel = antialiasing ? Integer.parseInt(antialiasLevelString) : 0;
        if (dyadicPixelSchedulerRadioButton.isSelected()) {
            pixelSchedulerType = Util.PIXEL_SCHEDULER_TYPE_DYADIC;
        } else if (spiralPixelSchedulerRadioButton.isSelected()) {
            pixelSchedulerType = Util.PIXEL_SCHEDULER_TYPE_SPIRAL;
        } else if (t2bPixelSchedulerRadioButton.isSelected()) {
            pixelSchedulerType = Util.PIXEL_SCHEDULER_TYPE_TOP2BOTTOM;
        } else if (b2tPixelSchedulerRadioButton.isSelected()) {
            pixelSchedulerType = Util.PIXEL_SCHEDULER_TYPE_BOTTOM2TOP;
        } else if (l2rPixelSchedulerRadioButton.isSelected()) {
            pixelSchedulerType = Util.PIXEL_SCHEDULER_TYPE_LEFT2RIGHT;
        } else if (r2lPixelSchedulerRadioButton.isSelected()) {
            pixelSchedulerType = Util.PIXEL_SCHEDULER_TYPE_RIGHT2LEFT;
        }
        exact = exactCheckBox.isSelected();
        multiThreading = multiThreadingRadioButton.isSelected();
        nbThreadsString = (String) nbThreadsComboBox.getSelectedItem();
        nbThreads = multiThreading ? Integer.parseInt(nbThreadsString) : 0;
    }

    /**
     * Restores the parameters from the package accessible fields.
     */
    private void restoreParameters() {
        xResolTextField.setValue(xResol);
        yResolTextField.setValue(yResol);
        depthTextField.setValue(depth);
        antialiasLevelComboBox.setSelectedItem(antialiasLevelString);
        if (antialiasing) {
            antialiasSamplingRadioButton.setSelected(true);
        } else {
            defaultSamplingRadioButton.setSelected(true);
        }
        if (pixelSchedulerType == Util.PIXEL_SCHEDULER_TYPE_DYADIC) {
            dyadicPixelSchedulerRadioButton.setSelected(true);
        } else if (pixelSchedulerType == Util.PIXEL_SCHEDULER_TYPE_SPIRAL) {
            spiralPixelSchedulerRadioButton.setSelected(true);
        } else if (pixelSchedulerType == Util.PIXEL_SCHEDULER_TYPE_TOP2BOTTOM) {
            t2bPixelSchedulerRadioButton.setSelected(true);
        } else if (pixelSchedulerType == Util.PIXEL_SCHEDULER_TYPE_BOTTOM2TOP) {
            b2tPixelSchedulerRadioButton.setSelected(true);
        } else if (pixelSchedulerType == Util.PIXEL_SCHEDULER_TYPE_LEFT2RIGHT) {
            l2rPixelSchedulerRadioButton.setSelected(true);
        } else if (pixelSchedulerType == Util.PIXEL_SCHEDULER_TYPE_RIGHT2LEFT) {
            r2lPixelSchedulerRadioButton.setSelected(true);
        }
        exactCheckBox.setSelected(exact);
        nbThreadsComboBox.setSelectedItem(nbThreadsString);
        if (multiThreading) {
            multiThreadingRadioButton.setSelected(true);
        } else {
            defaultThreadingRadioButton.setSelected(true);
        }
    }
}

/**
 * Class defining a text field accepting only digit input characters.
 */
class NumberTextField extends JTextField {

    private Toolkit toolkit;

    NumberTextField(int value) {
        super(String.valueOf(value));
        toolkit = Toolkit.getDefaultToolkit();
    }

    int getValue() {
        int retVal;
        retVal = Integer.parseInt(getText());
        return retVal;
    }

    void setValue(int value) {
        setText(String.valueOf(value));
    }

    protected Document createDefaultModel() {
        return new WholeNumberDocument();
    }

    class WholeNumberDocument extends PlainDocument {

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            for (int i = 0; i < str.length(); i++) {
                if (!Character.isDigit(str.charAt(i))) {
                    toolkit.beep();
                    return;
                }
            }
            super.insertString(offs, str, a);
        }
    }
}
