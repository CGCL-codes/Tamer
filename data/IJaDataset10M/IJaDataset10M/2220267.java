package eu.future.earth.gwt.client.date.week.staend;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import com.allen_sauer.gwt.dnd.client.AbstractDragController;
import com.google.gwt.user.client.ui.Widget;
import eu.future.earth.gwt.client.date.DateUtils;
import eu.future.earth.gwt.client.date.DateEvent.DateEventActions;
import eu.future.earth.gwt.client.date.week.staend.DayField.DirectionConstant;

public final class ResizeDragController<T> extends AbstractDragController {

    public ResizeDragController(DayPanel<T> boundaryPanel) {
        super(boundaryPanel.getBody());
        super.setBehaviorDragStartSensitivity(1);
    }

    private DayField<T> resizePanel = null;

    private DayPanel<T> parent = null;

    private HashMap<Widget, DirectionConstant> directionMap = new HashMap<Widget, DirectionConstant>();

    public void dragStart() {
        super.dragStart();
        resizePanel = getDayField(context.draggable);
        parent = getDayPanel(resizePanel);
    }

    @SuppressWarnings("unchecked")
    private DayField<T> getDayField(Widget current) {
        Widget parent = current.getParent();
        if (parent instanceof DayField<?>) {
            return (DayField<T>) parent;
        } else {
            if (parent != null) {
                return getDayField(parent);
            } else {
                return null;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private DayPanel<T> getDayPanel(Widget current) {
        Widget parent = current.getParent();
        if (parent instanceof DayPanel<?>) {
            return (DayPanel<T>) parent;
        } else {
            if (parent != null) {
                return getDayPanel(parent);
            } else {
                return null;
            }
        }
    }

    private Calendar helper = new GregorianCalendar();

    protected DirectionConstant getDirection(Widget draggable) {
        return (DirectionConstant) directionMap.get(draggable);
    }

    @Override
    public void makeNotDraggable(Widget draggable) {
        directionMap.remove(draggable);
        super.makeNotDraggable(draggable);
    }

    public void makeDraggable(Widget widget, eu.future.earth.gwt.client.date.week.staend.DayField.DirectionConstant direction) {
        super.makeDraggable(widget);
        directionMap.put(widget, direction);
    }

    @SuppressWarnings("unchecked")
    public void dragMove() {
        int direction = ((ResizeDragController<T>) context.dragController).getDirection(context.draggable).directionBits;
        final int he = resizePanel.getContentHeight();
        if ((direction & DayField.DIRECTION_SOUTH) != 0) {
            int delta = context.desiredDraggableY - context.draggable.getAbsoluteTop();
            if (delta != 0) {
                final int newHeight = he + delta;
                helper.setTime(resizePanel.getStart());
                helper.set(Calendar.SECOND, 0);
                helper.set(Calendar.MILLISECOND, 0);
                int minute = parent.calculateIntervalSnapForY(newHeight);
                helper.add(Calendar.MINUTE, minute * (60 / parent.getRenderer().getIntervalsPerHour()));
                Date oldTime = resizePanel.getStart();
                Date newTime = helper.getTime();
                if (DateUtils.isSameDayOrNextDayZeroMinutes(oldTime, newTime)) {
                    if (DateUtils.nextDayZeroMinutes(oldTime, newTime)) {
                        helper.add(Calendar.MINUTE, -1);
                        newTime = helper.getTime();
                    }
                }
                resizePanel.setEndTime(newTime);
                resizePanel.repaintTime();
                resizePanel.setContentHeight(newHeight);
            }
        }
    }

    public void dragEnd() {
        super.dragEnd();
        final DayPanel<T> parent = getDayPanel(resizePanel);
        parent.notifyParentOfUpdate(DateEventActions.UPDATE, resizePanel.getValue());
    }
}
