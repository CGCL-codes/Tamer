package com.patientis.framework.controls.override;

import java.text.DateFormat;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import com.michaelbaranov.microba.calendar.DatePicker;
import com.michaelbaranov.microba.calendar.ui.basic.BasicDatePickerUI;

/**
 * @author gcaulton2
 *
 */
public class ISBasicDatePickerUI extends BasicDatePickerUI {

    static {
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        field = new JFormattedTextField(createFormatterFactory(c));
    }

    private DefaultFormatterFactory createFormatterFactory(JComponent c) {
        DateFormat df = DateFormat.getDateInstance(0, peer.getLocale());
        df.setTimeZone(peer.getZone());
        return new DefaultFormatterFactory(new DateFormatter(df));
    }
}
