package org.databene.gui.swing;

import org.databene.commons.Period;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Component that allows for selecting a time scale.
 * <br/>
 * Created: 09.05.2007 19:20:02
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class TimescaleSelector extends JPanel {

    private static final long serialVersionUID = 4851141632147396863L;

    Period scale;

    private Period minScale;

    private Period maxScale;

    private JComponent component;

    private Listener listener;

    public TimescaleSelector(Period scale) {
        this(scale, scale, scale);
    }

    public TimescaleSelector(Period scale, Period minScale, Period maxScale) {
        super(new BorderLayout());
        listener = new Listener();
        setScale(scale);
        setScaleRange(minScale, maxScale);
    }

    public Period getScale() {
        return scale;
    }

    public void setScale(Period scale) {
        this.scale = scale;
        if (component instanceof PeriodComboBox) ((PeriodComboBox) component).setSelectedItem(scale);
    }

    public Period getMinScale() {
        return minScale;
    }

    public Period getMaxScale() {
        return maxScale;
    }

    public void setScaleRange(Period minScale, Period maxScale) {
        removeAll();
        if (component instanceof PeriodComboBox) ((PeriodComboBox) component).removeActionListener(listener);
        this.minScale = minScale;
        this.maxScale = maxScale;
        if (minScale == maxScale) component = new JLabel(minScale.getName()); else {
            PeriodComboBox comboBox = new PeriodComboBox(minScale, maxScale);
            comboBox.addActionListener(listener);
            component = comboBox;
        }
        add(component, BorderLayout.CENTER);
    }

    class Listener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Period selectedPeriod = (Period) ((JComboBox) e.getSource()).getSelectedItem();
            scale = selectedPeriod;
        }
    }
}
