package net.sf.cplab.headtracker.ui.swing;

import java.text.NumberFormat;
import javax.swing.JLabel;
import net.sf.cplab.headtracker.ui.NumberLabel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jtse
 *
 */
public class NumberLabelImpl implements NumberLabel, SwingComponent<JLabel> {

    @SuppressWarnings("unused")
    private static final Log LOG = LogFactory.getLog(NumberLabelImpl.class);

    private JLabel label;

    private NumberFormat format;

    public NumberLabelImpl(JLabel label, NumberFormat format) {
        this.label = label;
        this.format = format;
        label.setText(format.format(0));
    }

    public void setNumber(double number) {
        label.setText(format.format(number));
    }

    public JLabel getComponent() {
        return label;
    }
}
