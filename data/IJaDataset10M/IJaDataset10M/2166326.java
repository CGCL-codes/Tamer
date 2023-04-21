package org.paccman.ui.common;

import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author joao
 */
public class MyDateChooser extends JDateChooser {

    /**
     * 
     */
    public MyDateChooser() {
    }

    @Override
    public void setEnabled(boolean enabled) {
        for (Component comp : getComponents()) {
            comp.setEnabled(enabled);
        }
        super.setEnabled(enabled);
    }

    /**
     * 
     * @return
     */
    public Calendar getCalendarDate() {
        Calendar retVal = new GregorianCalendar();
        retVal.setTime(getDate());
        retVal.set(Calendar.HOUR_OF_DAY, 0);
        retVal.set(Calendar.MINUTE, 0);
        retVal.set(Calendar.SECOND, 0);
        retVal.set(Calendar.MILLISECOND, 0);
        return retVal;
    }
}
