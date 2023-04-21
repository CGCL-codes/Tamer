package userinterface;

import javax.swing.*;

/**class that implements a textfield that allows only a double to be keyed in
  *
  *@author Tan Hong Cheong
  *@version 20041210
  */
public class JDoubleField extends JTextField {

    /**Constructor
      */
    public JDoubleField() {
        super();
        setDocument(new DigitDocument(0, true, true));
    }

    /**return the double value
      */
    public double getDouble() {
        return Double.parseDouble(getText());
    }

    /**set the double value
      *@param i the double value
      */
    public void setDouble(double d) {
        setText(new String((new Double(d)).toString()));
    }

    /**the serial uid
      */
    private static final long serialVersionUID = -2430457869584947134L;
}
