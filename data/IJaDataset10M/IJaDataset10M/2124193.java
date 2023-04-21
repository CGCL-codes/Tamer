package edu.luc.cs.trull.demo.wristwatch;

import java.beans.PropertyChangeEvent;
import edu.luc.cs.trull.EmitComponent;

/**
 * ( emit MIN
 *     ; emit NOW(time / 60)
 *     ; loop
 *         START -> emit NOW(time = time / 3600 * 3600 + (time + 60) % ...)
 *     )
 */
class SetMinute extends EmitComponent implements EventLabels, DateConstants {

    private DateModel data;

    public void setModel(DateModel data) {
        this.data = data;
    }

    public void start(PropertyChangeEvent incoming) {
        super.start(incoming);
        scheduleEvent(MIN);
        scheduleEvent(NOW, new Integer(data.getTime() / SEC_PER_MIN));
    }

    public void propertyChange(final PropertyChangeEvent event) {
        if (START.equals(event.getPropertyName())) {
            data.setTime(data.getTime() / SEC_PER_HOUR * SEC_PER_HOUR + (data.getTime() + SEC_PER_MIN) % SEC_PER_HOUR);
            scheduleEvent(NOW, new Integer(data.getTime() / SEC_PER_MIN));
        }
    }
}
