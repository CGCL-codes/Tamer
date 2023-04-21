package net.jforerunning.gui.components;

import java.util.Calendar;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import net.jforerunning.XDate;

/**
 * A JSpinner for selecting months.
 * 
 * @author jens
 *
 */
public class MonthSpinner extends JSpinner {

    /**
	 * Create a month spinner initialized with the value of initDate
	 * @param initDate Initial date for the spinner
	 */
    public MonthSpinner(XDate initDate) {
        super(new SpinnerDateModel(initDate.toDate(), new XDate(initDate, Calendar.YEAR, -100).toDate(), new XDate(initDate, Calendar.YEAR, 100).toDate(), Calendar.MONTH));
        setEditor(new JSpinner.DateEditor(this, "MM/yyyy"));
    }
}
