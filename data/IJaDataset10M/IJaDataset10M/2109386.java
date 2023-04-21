package sears.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import sears.tools.SearsResourceBundle;
import sears.tools.Trace;

/**
 * Class JDialogNormalizeAction.
 * This dialog permits to define the parameters of a normalizeDuration action.
 * i.e the min duration and the max duration of the ST.
 */
public class JDialogNormalizeAction extends SearsJDialog {

    /** (<b>long</b>) serialVersionUID: The serialVersionUID */
    private static final long serialVersionUID = -6105540923502362579L;

    private JPanel jContentPane;

    private JPanel jPanelCenter;

    protected static final int MIN = 0;

    protected static final int MAX = 1;

    private static final int NB_TIMES = 2;

    private static final String[] MIN_MAX_SUFFIXES = new String[] { "min", "max" };

    private static final String[] DEFAULT_MIN_MAX = new String[] { "0.5", "5" };

    private JPanel[] jPanelTime = null;

    private JLabel[] jLabelTime = null;

    private JTextField[] jTextFieldTime = null;

    private JSlider[] jSliderTime = null;

    /**
     * This is the default constructor
     */
    public JDialogNormalizeAction() {
        super(SearsResourceBundle.getResource("normalizeDuration_title"));
        jPanelTime = new JPanel[NB_TIMES];
        jTextFieldTime = new JTextField[NB_TIMES];
        jSliderTime = new JSlider[NB_TIMES];
        jLabelTime = new JLabel[NB_TIMES];
        setContentPane(getJContentPane());
        configureSize();
    }

    /**
     * Method getJContentPane.
     * <br><b>Summary:</b><br>
     * return the contentPane
     * @return  (<b>JPanel</b>)   A JPanel.
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setBorder(super.createEmptyBorder());
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJPanelCenter(), java.awt.BorderLayout.CENTER);
            jContentPane.add(getJPanelButtons(), java.awt.BorderLayout.SOUTH);
        }
        return jContentPane;
    }

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelCenter() {
        if (jPanelCenter == null) {
            jPanelCenter = new JPanel();
            jPanelCenter.setLayout(new GridLayout(NB_TIMES, 1));
            jPanelCenter.add(getJPanelTime(MIN));
            jPanelCenter.add(getJPanelTime(MAX));
        }
        return jPanelCenter;
    }

    /**
     * Method getTimePanel.
     * <br><b>Summary:</b><br>
     * Return a penl to define a time, that use a slider, and a textField.
     * @param minMax	MIN or MAX.
     * @return  (<b>JPanel</b>)   A JPanel.
     */
    private JPanel getJPanelTime(int minMax) {
        if (jPanelTime[minMax] == null) {
            jLabelTime[minMax] = new JLabel();
            jLabelTime[minMax].setText(SearsResourceBundle.getResource("normalizeDuration_" + MIN_MAX_SUFFIXES[minMax] + "_label"));
            jPanelTime[minMax] = new JPanel();
            jPanelTime[minMax].setLayout(new GridBagLayout());
            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.gridx = 0;
            gbc1.gridy = 0;
            jPanelTime[minMax].add(jLabelTime[minMax], gbc1);
            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridx = 1;
            gbc2.gridy = 0;
            gbc2.fill = GridBagConstraints.HORIZONTAL;
            gbc2.weightx = 1;
            jPanelTime[minMax].add(getJTextFieldTime(minMax), gbc2);
            GridBagConstraints gbc3 = new GridBagConstraints();
            gbc3.gridx = 0;
            gbc3.gridy = 1;
            gbc3.gridwidth = 2;
            gbc3.fill = GridBagConstraints.HORIZONTAL;
            gbc3.weightx = 1;
            jPanelTime[minMax].add(getJSliderTime(minMax), gbc3);
        }
        return jPanelTime[minMax];
    }

    /**
	 * Method getJSliderTime.
	 * <br><b>Summary:</b><br>
	 * return the slider that correspond to the given min or max.
	 * @param minMax	MIN or MAX.
	 * @return  (<b>JSlider</b>)   A JSlider.
	 */
    private JSlider getJSliderTime(int minMax) {
        if (jSliderTime[minMax] == null) {
            jSliderTime[minMax] = new JSlider(0, 100);
            jSliderTime[minMax].setValue((int) Double.parseDouble(DEFAULT_MIN_MAX[minMax]) * 10);
            final int minMaxFinal = minMax;
            jSliderTime[minMax].addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    valueChanged(e.getSource(), minMaxFinal);
                }
            });
        }
        return jSliderTime[minMax];
    }

    /**
	 * Method valueChanged.
	 * <br><b>Summary:</b><br>
	 * this method is called when a slider changed or value.
	 * @param source		The slider that generates the event.
	 * @param minMax		To know if it is min or max that changed.
	 */
    private void valueChanged(Object source, int minMax) {
        if (source instanceof JSlider) {
            boolean updateTextField = false;
            double sliderDelayValue = ((double) jSliderTime[minMax].getValue()) / 10;
            updateTextField = true;
            try {
                double delay = getDoubleTextDelayValue(minMax);
                if (delay != sliderDelayValue) {
                    updateTextField = true;
                }
            } catch (NumberFormatException e) {
                updateTextField = true;
            }
            if (updateTextField) {
                jTextFieldTime[minMax].setText("" + sliderDelayValue);
            }
        } else if (source instanceof JTextField) {
            try {
                double delay = getDoubleTextDelayValue(minMax);
                double sliderDelayValue = ((double) jSliderTime[minMax].getValue()) / 10;
                if (delay != sliderDelayValue) {
                    int newSliderValue = (int) (delay * 10);
                    if (Math.abs(newSliderValue) > jSliderTime[minMax].getMaximum()) {
                        jSliderTime[minMax].setMaximum(Math.abs(newSliderValue));
                    }
                    jSliderTime[minMax].setValue(newSliderValue);
                }
            } catch (NumberFormatException e) {
            }
        }
    }

    /**
     * Method getDoubleTextDelayValue.
     * <br><b>Summary:</b><br>
     * This method returns the double value of the delay in the textfield for given minMax
     * @param minMax	To select the MIN or the MAX
     * @return double   The double value of the delay in the textfield.
     * @throws NumberFormatException
     */
    public double getDoubleTextDelayValue(int minMax) throws NumberFormatException {
        double result = 0;
        String delayString = jTextFieldTime[minMax].getText();
        if (delayString != null && !delayString.equals("")) {
            result = Double.parseDouble(delayString);
        } else {
            result = 0;
        }
        return result;
    }

    /**
	 * Method getJTextFieldTime.
	 * <br><b>Summary:</b><br>
	 * return the textField that correspond to the given min or max.
	 * @param minMax	MIN or MAX
	 * @return  (<b>JTextField</b>)   A JTextField.
	 */
    private JTextField getJTextFieldTime(int minMax) {
        if (jTextFieldTime[minMax] == null) {
            jTextFieldTime[minMax] = new JTextField(5);
            jTextFieldTime[minMax].setToolTipText(SearsResourceBundle.getResource("delay_tip"));
            jTextFieldTime[minMax].setText(DEFAULT_MIN_MAX[minMax]);
            jTextFieldTime[minMax].addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    okAction();
                }
            });
            final int minMaxFinal = minMax;
            jTextFieldTime[minMax].addFocusListener(new FocusListener() {

                public void focusLost(FocusEvent e) {
                    valueChanged(e.getSource(), minMaxFinal);
                }

                public void focusGained(FocusEvent e) {
                }
            });
        }
        return jTextFieldTime[minMax];
    }

    /**
     * Method okAction.
     * <br><b>Summary:</b><br>
     * This method is called when user validate the dialog.
     */
    protected void okAction() {
        String error = checkParameters();
        if (error != null && !error.equals("")) {
            JOptionPane.showMessageDialog(this, error, SearsResourceBundle.getResource("error_delayConfigurationError"), JOptionPane.ERROR_MESSAGE);
            Trace.trace("Error in Delay parameters:" + error, Trace.WARNING_PRIORITY);
        } else {
            validationStatus = true;
            dispose();
        }
    }

    /**
     * Method checkParameters.
     * <br><b>Summary:</b><br>
     * This method checks the parameters.
     * @return  <b>String</b>      "" if there is no error, or the error message.
     */
    private String checkParameters() {
        String errorMessage = "";
        for (int i = 0; i < NB_TIMES; i++) {
            String delay = jTextFieldTime[i].getText();
            if (delay != null && !delay.equals("")) {
                try {
                    Double.parseDouble(delay);
                } catch (NumberFormatException e) {
                    errorMessage += "[" + MIN_MAX_SUFFIXES[i] + "]" + SearsResourceBundle.getResource("error_delayNotValid") + "\n";
                }
            } else {
                errorMessage += "[" + MIN_MAX_SUFFIXES[i] + "]" + SearsResourceBundle.getResource("error_delayNull") + "\n";
            }
        }
        return errorMessage;
    }

    /**
     * Method getTimes.
     * <br><b>Summary:</b><br>
     * return the times configured by the user.
     * chack that user validates the dialog.
     * @return  (<b>double[]</b>)   The result of the normalize dialog, double[]=[MIN, MAX].
     */
    public double[] getTimes() {
        double[] result = new double[NB_TIMES];
        for (int i = 0; i < NB_TIMES; i++) {
            result[i] = Double.parseDouble(jTextFieldTime[i].getText());
        }
        return result;
    }

    protected String getDialogName() {
        return "normalizeDuration";
    }
}
