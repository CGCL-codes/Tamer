package org.aspencloud.calypso.ui.calendar.tasksCalendar;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import org.aspencloud.calypso.ui.calendar.BaseModel;
import org.aspencloud.calypso.ui.calendar.BasePart;
import org.aspencloud.calypso.ui.calendar.factories.FigureFactory;
import org.aspencloud.calypso.ui.calendar.tasks.TaskPart;
import org.aspencloud.calypso.util.CTimeSpan;
import org.aspencloud.calypso.util.TimeSpan;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.SnapToHelper;
import org.remus.infomngmnt.calendar.model.Task;

public class TasksCalendarPart extends BasePart {

    public static Calendar tmpcal = Calendar.getInstance(Locale.getDefault());

    private long snapToDateResolution = 1000 * 60 * 15;

    private final HashMap constraints = new HashMap();

    @Override
    protected IFigure createFigure() {
        TasksCalendarFigure fig = (TasksCalendarFigure) FigureFactory.createTasksCalendarFigure();
        fig.setPreferredSize(-1, 800);
        fig.setRowLabels(getTasksCalendarModel().getRowLabels());
        fig.setNumDays(getTasksCalendarModel().getNumDays());
        fig.addPropertyChangeListener(this);
        return fig;
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new TasksCalendarLayoutEditPolicy());
    }

    @Override
    public void deactivate() {
        ((TasksCalendarFigure) getFigure()).clearDayColors();
        super.deactivate();
    }

    @Override
    public Object getAdapter(final Class adapter) {
        if ((adapter == SnapToHelper.class) || (adapter == SnapTaskToDate.class)) {
            return new SnapTaskToDate(this);
        }
        return super.getAdapter(adapter);
    }

    protected TasksCalendarModel getTasksCalendarModel() {
        return (TasksCalendarModel) getModel();
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (TasksCalendarModel.PROP_DAY.equals(prop)) {
            refreshVisuals();
        } else if (BaseModel.PROP_CHILDREN.equals(prop)) {
            refreshChildren();
            setTaskConstraints();
        } else if (BaseModel.PROP_SIZE.equals(prop) || BaseModel.PROP_LOCATION.equals(prop)) {
            getFigure().setPreferredSize(getBaseModel().getSize());
        } else if (TasksCalendarModel.PROP_NUM_DAYS.equals(prop)) {
            TasksCalendarFigure fig = (TasksCalendarFigure) getFigure();
            TasksCalendarModel model = (TasksCalendarModel) getModel();
            fig.setNumDays(model.getNumDays());
        } else if (TasksCalendarModel.PROP_ROW_LABELS.equals(prop)) {
            String[] labels = ((TasksCalendarModel) getModel()).getRowLabels();
            ((TasksCalendarFigure) getFigure()).setRowLabels(labels);
            getFigure().repaint();
        } else if (TasksCalendarFigure.PROP_TASK_AREA.equals(prop) || TasksCalendarFigure.PROP_DAY_WIDTH.equals(prop)) {
            setTaskConstraints();
        }
    }

    @Override
    protected List getModelChildren() {
        return ((TasksCalendarModel) getModel()).getChildren();
    }

    @SuppressWarnings("unchecked")
    public void zSortChildren() {
        TreeSet set = new TreeSet(new Comparator() {

            public int compare(final Object arg0, final Object arg1) {
                int result = 0;
                Task t0 = (Task) ((TaskPart) arg0).getModel();
                Task t1 = (Task) ((TaskPart) arg1).getModel();
                if (t0.getStart() != null && t1.getEnd() != null) {
                    result = t0.getStart().getDate().compareTo(t1.getStart().getDate());
                    if (result == 0) {
                        if (t0.getStart() != null && t1.getEnd() != null) {
                            result = t1.getEnd().getDate().compareTo(t0.getEnd().getDate());
                            if (result == 0) {
                                Rectangle r0 = (Rectangle) constraints.get(arg0);
                                Rectangle r1 = (Rectangle) constraints.get(arg1);
                                if ((r0 != null) && (r1 != null)) {
                                    result = new Integer(r0.x).compareTo(new Integer(r1.x));
                                    if ((result == 0) && !r0.isEmpty() && !r1.isEmpty()) {
                                        System.out.println("A Task may be Hidden...");
                                        System.out.println("r0: " + r0.x + ", " + r0.y + ", " + r0.width + ", " + r0.height);
                                        System.out.println("r1: " + r1.x + ", " + r1.y + ", " + r1.width + ", " + r1.height);
                                    }
                                }
                            }
                        }
                    }
                }
                return result;
            }
        });
        set.addAll(getChildren());
        for (Iterator i = set.iterator(); i.hasNext(); ) {
            TaskPart part = (TaskPart) i.next();
            getFigure().remove(part.getFigure());
            getFigure().add(part.getFigure());
        }
    }

    private Rectangle getConstraint(final TaskPart part, final List requesterList) {
        if (constraints.containsKey(part)) {
            Object o = constraints.get(part);
            if ((o != null) && (o instanceof Rectangle)) {
                return (Rectangle) o;
            }
        }
        int offset = (int) (getDayWidth() / 7);
        Task task = (Task) part.getModel();
        TimeSpan ts1 = new CTimeSpan(task);
        if (ts1.isInstant()) {
            ts1.setStart(ts1.getStart() - getSnapToDateResolution());
            ts1.setDurationFromStart(2 * getSnapToDateResolution());
        }
        Rectangle constraint;
        Point p1 = getPointFromDate(ts1.getStartDate());
        Point p2 = getPointFromDate(ts1.getEndDate());
        if ((p1 != null) && (p2 != null)) {
            constraint = new Rectangle(p1, p2);
            constraint.x++;
            ;
            constraint.width = (int) getDayWidth() - 1;
            constraint.y++;
            ;
            constraint.height--;
        } else {
            return new Rectangle();
        }
        CTimeSpan ts2 = new CTimeSpan();
        List overlappingParts = new ArrayList();
        for (Iterator i = getChildren().iterator(); i.hasNext(); ) {
            TaskPart part2 = (TaskPart) i.next();
            Task test = (Task) part2.getModel();
            if (test.equals(task)) {
                continue;
            }
            ts2.setTimeSpan(test);
            if (ts1.overlaps(ts2)) {
                if (ts1.getStartDate().after(ts2.getStartDate()) || (ts1.getStartDate().equals(ts2.getStartDate()) && ts1.getEndDate().before(ts2.getEndDate())) || (ts1.getStartDate().equals(ts2.getStartDate()) && ts1.getEndDate().equals(ts2.getEndDate()) && !requesterList.contains(part2))) {
                    overlappingParts.add(part2);
                }
            }
        }
        int xOffset = 0;
        for (Iterator i = overlappingParts.iterator(); i.hasNext(); ) {
            TaskPart opart = (TaskPart) i.next();
            if (!requesterList.contains(part)) {
                requesterList.add(part);
            }
            Rectangle opConstraint = getConstraint(opart, requesterList);
            xOffset = Math.max(xOffset, opConstraint.x - constraint.x + offset);
        }
        constraint.x += xOffset;
        constraint.width -= xOffset;
        constraints.put(part, constraint);
        return constraint;
    }

    private void setTaskConstraints() {
        constraints.clear();
        for (Iterator i = getChildren().iterator(); i.hasNext(); ) {
            TaskPart part = (TaskPart) i.next();
            getConstraint(part, new ArrayList());
        }
        zSortChildren();
        for (Iterator i = constraints.keySet().iterator(); i.hasNext(); ) {
            TaskPart part = (TaskPart) i.next();
            setLayoutConstraint(part, part.getFigure(), constraints.get(part));
        }
    }

    public double getMillisPerPixel() {
        TasksCalendarFigure fig = (TasksCalendarFigure) getFigure();
        TasksCalendarModel model = (TasksCalendarModel) getModel();
        int height = fig.getTaskArea().height;
        long millis = (model.getDayIntervals()[3] - model.getDayIntervals()[0]);
        return (double) millis / (double) height;
    }

    public double getPixelsPerMilli() {
        TasksCalendarFigure fig = (TasksCalendarFigure) getFigure();
        TasksCalendarModel model = (TasksCalendarModel) getModel();
        int height = fig.getTaskArea().height;
        long millis = (model.getDayIntervals()[3] - model.getDayIntervals()[0]);
        return (double) height / (double) millis;
    }

    public Date getDateFromPoint(final Point point) {
        if (point == null) {
            return null;
        }
        TasksCalendarFigure fig = (TasksCalendarFigure) getFigure();
        TasksCalendarModel model = (TasksCalendarModel) getModel();
        if (fig.getTaskArea().contains(point)) {
            int day = (int) ((point.x - fig.getTaskArea().x) / getDayWidth());
            if ((day >= 0) && (day < model.getNumDays())) {
                tmpcal.set(Calendar.YEAR, model.getDay(day)[0]);
                tmpcal.set(Calendar.DAY_OF_YEAR, model.getDay(day)[1]);
                tmpcal.set(Calendar.HOUR_OF_DAY, 0);
                tmpcal.set(Calendar.MINUTE, 0);
                tmpcal.set(Calendar.SECOND, 0);
                tmpcal.set(Calendar.MILLISECOND, 0);
                double per = (double) (point.y - fig.getTaskArea().y) / (double) (fig.getTaskArea().height - 1);
                long timeOffset = (long) (per * (model.getDayIntervals()[3] - model.getDayIntervals()[0]));
                tmpcal.setTimeInMillis(tmpcal.getTimeInMillis() + timeOffset + model.getDayIntervals()[0]);
                SnapTaskToDate snap = (SnapTaskToDate) getAdapter(SnapTaskToDate.class);
                if (snap != null) {
                    return snap.snapDate(tmpcal.getTime());
                } else {
                    return tmpcal.getTime();
                }
            }
        }
        return null;
    }

    public Point getPointFromDate(final Date date) {
        if (date == null) {
            return null;
        }
        TasksCalendarFigure fig = (TasksCalendarFigure) getFigure();
        TasksCalendarModel model = (TasksCalendarModel) getModel();
        tmpcal.setTime(date);
        for (int i = 0; i < model.getNumDays(); i++) {
            if ((tmpcal.get(Calendar.DAY_OF_YEAR) == model.getDay(i)[1]) && (tmpcal.get(Calendar.YEAR) == model.getDay(i)[0])) {
                long timeOfDay = (tmpcal.get(Calendar.HOUR_OF_DAY) * 1000 * 60 * 60) + (tmpcal.get(Calendar.MINUTE) * 1000 * 60) + (tmpcal.get(Calendar.SECOND) * 1000) + tmpcal.get(Calendar.MILLISECOND);
                double per = (double) (timeOfDay - model.getDayIntervals()[0]) / (double) (model.getDayIntervals()[3] - model.getDayIntervals()[0]);
                int offset = (int) (per * fig.getTaskArea().height);
                int x = (int) (fig.getTaskArea().x + (fig.getDayWidth() * i));
                int y = fig.getTaskArea().y + offset;
                return new Point(x, y);
            }
        }
        return null;
    }

    public long getSnapToDateResolution() {
        return snapToDateResolution;
    }

    public void setSnapToDateResolution(final long dateSnapResolution) {
        snapToDateResolution = dateSnapResolution;
    }

    public double getDayWidth() {
        return ((TasksCalendarFigure) getFigure()).getDayWidth();
    }
}
