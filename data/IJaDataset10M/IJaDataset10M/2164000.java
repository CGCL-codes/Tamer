package eu.future.earth.gwt.client.date.horizontal;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbstractPositioningDropController;
import com.google.gwt.user.client.ui.Widget;

/**
 * DropController which allows a widget to be dropped on a SimplePanel drop target when the drop target does not yet
 * have a child widget.
 */
public class HorizontalViewPanelDropController<T, M> extends AbstractPositioningDropController {

    private Calendar helper = new GregorianCalendar();

    private HorizontalViewPanelNoDays<T, M> week = null;

    public HorizontalViewPanelDropController(HorizontalItemRow<T, M> newDropTarget, HorizontalViewPanelNoDays<T, M> newWeek) {
        super(newDropTarget);
        week = newWeek;
    }

    @SuppressWarnings("unchecked")
    public void onDrop(DragContext context) {
        final Widget drp = getDropTarget();
        final Widget drag = context.draggable;
        if (drp instanceof HorizontalItemRow<?, ?> && drag instanceof HorizontalDayField<?, ?>) {
            final HorizontalItemRow<T, M> parent = (HorizontalItemRow<T, M>) drp;
            final HorizontalDayField<T, M> data = (HorizontalDayField<T, M>) drag;
            int x = context.draggable.getAbsoluteLeft() - week.getLeftScrollable();
            if (x < 0) {
                x = 0;
            }
            x = x + (parent.getRenderer().getStartHour() * parent.getRenderer().getIntervalsPerHour() * parent.getRenderer().getIntervalWidth());
            long duration = data.getEnd().getTime() - data.getStart().getTime();
            helper.setTime(data.getStart());
            helper.set(Calendar.HOUR_OF_DAY, 0);
            helper.set(Calendar.MINUTE, 0);
            helper.set(Calendar.SECOND, 0);
            helper.set(Calendar.MILLISECOND, 0);
            helper.add(Calendar.MINUTE, (parent.calculateIntervalSnapForX(x)) * (60 / parent.getRenderer().getIntervalsPerHour()));
            data.setStart(helper.getTime());
            if (data.getEnd() != null) {
                data.setEndTime(new Date(data.getStart().getTime() + duration));
            }
            parent.addEventByDrop(data);
        }
    }
}
