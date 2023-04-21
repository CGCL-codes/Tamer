package org.icefaces.application.showcase.view.bean.examples.component.selectInputDate;

import org.icefaces.application.showcase.view.bean.BaseBean;
import java.util.Date;
import java.util.TimeZone;
import java.util.GregorianCalendar;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;
import javax.faces.event.ValueChangeEvent;

/**
 * <p>The SelectInputDateBean Class is used to store the selected dates from the
 * selectinputdate components.</p>
 *
 * @since 0.3
 */
public class SelectInputDateBean extends BaseBean {

    /**
     * Variables to store the selected dates.
     */
    private Date date1;

    private Date date2;

    protected Effect valueChangeEffect2;

    public SelectInputDateBean() {
        super();
        valueChangeEffect2 = new Highlight("#fda505");
        valueChangeEffect2.setFired(true);
        date2 = new GregorianCalendar(1867, 6, 1).getTime();
    }

    /**
     * Gets the first selected date.
     *
     * @return the first selected date
     */
    public Date getDate1() {
        return date1;
    }

    /**
     * Sets the first selected date.
     *
     * @param date the first selected date
     */
    public void setDate1(Date date) {
        date1 = date;
    }

    /**
     * Gets the 2nd selected date.
     *
     * @return the 2nd selected date
     */
    public Date getDate2() {
        return date2;
    }

    /**
     * Sets the 2nd selected date.
     *
     * @param date the 2nd selected date
     */
    public void setDate2(Date date) {
        date2 = date;
    }

    /**
     * Gets the default timezone of the host server.  The timezone is needed
     * by the convertDateTime for formatting the time dat values.
     *
     * @return timezone for the current JVM
     */
    public TimeZone getTimeZone() {
        return java.util.TimeZone.getDefault();
    }

    public Effect getValueChangeEffect2() {
        return valueChangeEffect2;
    }

    public void setValueChangeEffect2(Effect valueChangeEffect2) {
        this.valueChangeEffect2 = valueChangeEffect2;
    }

    /**
     * When values change event occures on date2 then we reset the effect
     * so the user can see the changed value more easily.
     *
     * @param event JSF value change event. 
     */
    public void effect2ChangeListener(ValueChangeEvent event) {
        valueChangeEffect2.setFired(false);
    }
}
