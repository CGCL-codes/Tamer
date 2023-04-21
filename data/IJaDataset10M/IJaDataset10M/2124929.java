package net.sourceforge.javacavemaps.control;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author IBM
 *
 */
public class CommandPanel extends JPanel {

    JButton calculateButton = new JButton("Calculate");

    JTextField azmithField = new JTextField();

    JTextField distanceField = new JTextField();

    JTextField verticalAngleField = new JTextField();

    JLabel azmithLabel = new JLabel("Azmith");

    JLabel distanceLabel = new JLabel("Distance");

    JLabel verticalAngleLabel = new JLabel("Vertical Angle");

    CommandPanel() {
        this.add(calculateButton);
        this.add(azmithLabel);
        this.add(azmithField);
        this.add(distanceLabel);
        this.add(distanceField);
        this.add(verticalAngleLabel);
        this.add(verticalAngleField);
    }

    public double getAzmith() {
        return getDoubleValue(azmithField.getText());
    }

    public double getDistance() {
        return getDoubleValue(distanceField.getText());
    }

    public double getVerticalAngle() {
        return getDoubleValue(verticalAngleField.getText());
    }

    public double getDoubleValue(String aValue) {
        try {
            return Double.parseDouble(aValue);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return 0.0;
    }
}
