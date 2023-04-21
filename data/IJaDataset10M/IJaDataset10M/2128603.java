package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Implements a combination slider/text field for entering priorities.
 * @author Paul A. Rubin <rubin@msu.edu>
 */
public class PSlider extends JPanel implements DocumentListener, ChangeListener, Runnable {

    private JSlider slider;

    private JTextField text;

    private boolean semaphore;

    public PSlider() {
        super(new GridBagLayout());
        this.setPreferredSize(new Dimension(200, 47));
        slider = new JSlider(0, 100, 0);
        slider.addChangeListener(this);
        GridBagConstraints gs = new GridBagConstraints();
        gs.gridx = 0;
        gs.gridy = 0;
        gs.gridwidth = 1;
        gs.gridheight = 1;
        gs.fill = GridBagConstraints.HORIZONTAL;
        gs.anchor = GridBagConstraints.WEST;
        gs.weightx = 1.0;
        this.add(slider, gs);
        text = new JTextField("0");
        text.setHorizontalAlignment(JTextField.RIGHT);
        text.getDocument().addDocumentListener(this);
        text.setPreferredSize(new Dimension(28, 14));
        text.setMinimumSize(new Dimension(28, 14));
        GridBagConstraints gt = new GridBagConstraints();
        gt.gridx = 1;
        gt.gridy = 0;
        gt.gridwidth = 1;
        gt.gridheight = 1;
        gt.fill = GridBagConstraints.NONE;
        gt.anchor = GridBagConstraints.EAST;
        gt.weightx = 0;
        this.add(text, gt);
        semaphore = false;
    }

    public void insertUpdate(DocumentEvent e) {
        if (semaphore) {
            semaphore = false;
            return;
        }
        Integer i = validate(text.getText());
        if (i != null) {
            semaphore = true;
            slider.setValue(i);
        }
    }

    public void removeUpdate(DocumentEvent e) {
        if (semaphore) {
            return;
        }
        if (text.getText().length() == 0) {
            return;
        }
        Integer i = validate(text.getText());
        if (i != null) {
            semaphore = true;
            slider.setValue(i);
        }
    }

    public void changedUpdate(DocumentEvent e) {
        if (semaphore) {
            semaphore = false;
            return;
        }
        Integer i = validate(text.getText());
        if (i != null) {
            semaphore = true;
            slider.setValue(i);
        }
    }

    /**
   * Verifies that a string represents an integer between 0 and 100;
   * beeps otherwise.
   * @param s the string to validate
   * @return the integer equivalent (null if invalid)
   */
    private Integer validate(String s) {
        Integer i;
        try {
            i = new Integer(s);
        } catch (NumberFormatException e) {
            Toolkit.getDefaultToolkit().beep();
            SwingUtilities.invokeLater(this);
            return null;
        }
        if (i >= 0 && i <= 100) {
            return i;
        } else {
            Toolkit.getDefaultToolkit().beep();
            SwingUtilities.invokeLater(this);
            return null;
        }
    }

    public void stateChanged(ChangeEvent e) {
        if (semaphore) {
            semaphore = false;
            return;
        } else {
            semaphore = true;
            text.setText(((Integer) slider.getValue()).toString());
        }
    }

    /**
   * Gets the value of the control.
   * @return the current value
   */
    public int getValue() {
        return slider.getValue();
    }

    /**
   * Sets the value of the control.
   * @param v the new value
   */
    public void setValue(Integer v) {
        v = (v == null) ? 0 : v;
        slider.setValue(v);
        text.setText(v.toString());
    }

    /**
   * Fixes the text field after a bad input.
   */
    public void run() {
        semaphore = true;
        text.setText(((Integer) slider.getValue()).toString());
    }

    /**
   * Adds a change listener to the slider's state.
   * @param cl the new change listener
   */
    public void addChangeListener(ChangeListener cl) {
        slider.addChangeListener(cl);
    }

    /**
   * Sets the control enabled or disabled.
   * @param state true for enabled, false for disabled
   */
    public void setEnabled(boolean state) {
        this.slider.setEnabled(state);
        this.text.setEnabled(state);
    }
}
