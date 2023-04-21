package org.chargecar.sensorboard;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import edu.cmu.ri.createlab.userinterface.GUIConstants;
import edu.cmu.ri.createlab.userinterface.util.SwingUtils;
import org.jdesktop.layout.GroupLayout;

/**
 * @author Chris Bartley (bartley@cmu.edu)
 */
final class MinMaxGauge<T> extends JPanel {

    private static final String UNKNOWN_VALUE = "?";

    private final JLabel valueLabel;

    private final JLabel minValueLabel;

    private final JLabel maxValueLabel;

    private final String stringFormat;

    MinMaxGauge(final String labelText, final String stringFormat) {
        this.stringFormat = stringFormat;
        final JLabel label = SwingUtils.createLabel(labelText, GUIConstants.FONT_NORMAL);
        this.valueLabel = SwingUtils.createLabel("", GUIConstants.FONT_MEDIUM_LARGE);
        this.minValueLabel = SwingUtils.createLabel("", GUIConstants.FONT_TINY);
        this.maxValueLabel = SwingUtils.createLabel("", GUIConstants.FONT_TINY);
        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        this.setBackground(Color.WHITE);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.CENTER).add(maxValueLabel).add(valueLabel).add(minValueLabel).add(label));
        layout.setVerticalGroup(layout.createSequentialGroup().add(maxValueLabel).add(valueLabel).add(minValueLabel).add(label));
    }

    /**
    * Sets the gauge's value using the string format given to the constructor.  Assumes it is being called in the GUI
    * thread.
    */
    void setValues(final T value, final T min, final T max) {
        setValue(value, valueLabel);
        setValue(min, minValueLabel);
        setValue(max, maxValueLabel);
    }

    private void setValue(final T value, final JLabel label) {
        if (value != null) {
            label.setForeground(Color.BLACK);
            label.setText(String.format(stringFormat, value));
        } else {
            label.setForeground(Color.RED);
            label.setText(UNKNOWN_VALUE);
        }
    }
}
