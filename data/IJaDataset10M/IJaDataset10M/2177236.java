package lib.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * This class allows the user to enter an double value. Only keys 
 * 0 to 9 may be entered.
 * The min and max values for the number to be entered may be set.
 * The effect of setting either of these is that when the user enters 
 * a value outside the valid range the background of the field will 
 * change to a different color indicating that the current value 
 * displayed is incorrect. The isValid method may also be used to 
 * check for a valid input value.
 * 
 * @author Paul Austen
 */
public class DoubleNumberField extends JTextField implements KeyListener, FocusListener {

    private String saveText = "";

    private double highLimit = Double.MAX_VALUE;

    private double lowLimit = Double.MIN_VALUE;

    private Color withinLimitsColor;

    private Color outsideLimitsColor;

    /**
   * Constructor
   * 
   * @param lowLimit The minimum value that may be enterd.
   * @param highLimit The maximum value that may be enterd.
   */
    public DoubleNumberField(double lowLimit, double highLimit) {
        this.addKeyListener(this);
        this.setColumns(10);
        this.addFocusListener(this);
        this.setText("0");
        withinLimitsColor = Color.WHITE;
        outsideLimitsColor = Color.RED;
        int rgb = outsideLimitsColor.getRGB();
        outsideLimitsColor = Color.getHSBColor((float) rgb, 0.25F, 1F);
        setToolTipText("Enter any value you wish here.");
        setMin(lowLimit);
        setMax(highLimit);
    }

    /**
   * Constructor with no limits on the number to be entered.
   */
    public DoubleNumberField() {
        this(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    /**
   * Set the tooltip for the LongNumberField.
   */
    private void setToolTip() {
        setToolTipText("Enter a decimal value, from " + lowLimit + " to " + highLimit + ".");
    }

    /**
   * Set the max value that the user may enter into the LongNumberField.
   * 
   * @param highLimit the max value that may be entered.
   */
    public void setMax(double highLimit) {
        this.highLimit = highLimit;
        setBackGroundColour();
        setToolTip();
    }

    /**
	 * Set the min value that the user may enter into the LongNumberField.
	 * 
	 * @param lowLimit the mow value that may be entered.
	 */
    public void setMin(double lowLimit) {
        this.lowLimit = lowLimit;
        setBackGroundColour();
        setToolTipText("Max value = " + highLimit + ", Min value = " + lowLimit);
        setToolTip();
    }

    /**
	 * Set the background color of the LongNumberField to be displayed when
	 * the number displayed is within the defined limits.
	 * 
	 * @param withinLimitsColor The Color object.
	 */
    public void setWithinLimitsColor(Color withinLimitsColor) {
        this.withinLimitsColor = withinLimitsColor;
        setBackGroundColour();
    }

    /**
	 * Set the background color of the LongNumberField to be displayed when
	 * the number displayed is outside the defined limits.
	 * 
	 * @param outsideLimitsColor The Color object.
	 */
    public void setOutsideLimitsColor(Color outsideLimitsColor) {
        this.outsideLimitsColor = outsideLimitsColor;
        setBackGroundColour();
    }

    /**
	 * Get the maximum value that may be entered into the LongNumberField.
	 * 
	 * @return The maximum value.
	 */
    public double getMax() {
        return highLimit;
    }

    /**
   * Get the minimum value that may be entered into the LongNumberField.
   * 
   * @return The minimum value.
   */
    public double getMin() {
        return lowLimit;
    }

    /**
   * Get the background color of the LongNumberField to be displayed 
   * when the number shown is within the defined limits.
   * 
   * @return The color object.
   */
    public Color getWithinLimitsColor() {
        return withinLimitsColor;
    }

    /**
	 * Get the background color of the LongNumberField to be displayed 
	 * when the number shown is outside the defined limits.
	 * 
	 * @return The color object.
	 */
    public Color getOutsideLimitsColor() {
        return outsideLimitsColor;
    }

    /**
	 * Sets the background color of the LongNumberField based upon
	 * wether the number it holds is within the defined limits.
	 */
    private void setBackGroundColour() {
        if (getText().toLowerCase().endsWith("e") || (getText().startsWith("-") && getText().length() == 1)) {
            this.setBackground(withinLimitsColor);
        } else {
            double number = getNumber();
            if (number > highLimit | number < lowLimit) {
                this.setBackground(outsideLimitsColor);
            } else {
                this.setBackground(withinLimitsColor);
            }
        }
    }

    /**
	 * The doLayout method of the superclass is called from this method 
	 * along with the setBackground color method in order to set the correct 
	 * initial background color if the setText method is called to set the 
	 * field contents. The setText method is called when the PersistentConfig
	 * class is used to restore values to LongNumberField fields.
	 */
    public void doLayout() {
        setBackGroundColour();
        super.doLayout();
    }

    /**
	 * if focus lost from the LongNumberField object then check low
	 * limit and high limit.
	 * 
	 * @param e A FocusEvent object.
	 */
    public synchronized void focusLost(FocusEvent e) {
        setBackGroundColour();
    }

    /**
	 * Place holder method. No actions are taken within this method.
	 */
    public void focusGained(FocusEvent e) {
    }

    /**
	 * Convert the text entered to a number and return the number.
	 */
    public double getNumber() {
        double number = 0;
        try {
            number = Double.parseDouble(this.getText());
        } catch (Exception e) {
            if (saveText != null && saveText.length() > 0 && this.getText() != null && this.getText().length() != 0) {
                this.setText(saveText);
            }
        }
        return number;
    }

    /**
	 * Set number displayed. The number must be within the current limits.
	 * 
	 * @param num The number that is to be displayed in the LongNumberField.
	 */
    public void setNumber(double num) {
        if (num < lowLimit) {
            setNumber(lowLimit);
        } else {
            if (num > highLimit) {
                setNumber(highLimit);
            } else {
                String str = Double.toString(num);
                setText(str);
            }
        }
        setBackGroundColour();
    }

    /**
	 * Called when a user key is relesed in the LongNumberField object.
	 * 
	 * @param e The KeyEvent object
	 */
    public synchronized void keyReleased(KeyEvent e) {
        try {
            if (!getText().toLowerCase().endsWith("e") && !(getText().startsWith("-") && getText().length() == 1)) {
                if (this.getText().length() > 0 && this.getText().compareTo("-") != 0) {
                    getNumber();
                }
            }
        } catch (NumberFormatException ex) {
            this.setText(saveText);
        }
        saveText = this.getText();
        setBackGroundColour();
    }

    /**
   * Place holder method. No actions are taken within this method.
   */
    public void keyTyped(KeyEvent e) {
    }

    /**
   * Place holder method. No actions are taken within this method.
   */
    public void keyPressed(KeyEvent e) {
    }

    /**
   * Check the number entered. Return true if its between lowLimit 
   * and highLimit (inclusive).
   */
    public boolean isValid() {
        double number = getNumber();
        if (number < lowLimit | number > highLimit) {
            return false;
        } else {
            return true;
        }
    }

    /**
	 * Check the number entered (using the isValid method) and 
	 * throw an exception with the defined message if the number
	 * is out of bounds.
	 */
    public void exceptionCheck(String errorMessage) throws Exception {
        if (!isValid()) {
            throw new Exception(errorMessage);
        }
    }
}
