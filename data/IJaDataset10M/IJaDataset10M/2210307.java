package org.nightlabs.calendar.ui.detailviewer;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

class CalendarMouseListener implements MouseListener, MouseMoveListener {

    private DetailCalendar calendar;

    private boolean down = false;

    public CalendarMouseListener(DetailCalendar calendar) {
        this.calendar = calendar;
    }

    private class SelectionMarker {

        DetailCalendarDayContent composite;

        int x;

        int y;

        /**
     * Create a new SelectionMarker.
     */
        public SelectionMarker(DetailCalendarDayContent composite, int x, int y) {
            super();
            this.composite = composite;
            this.x = x;
            this.y = y;
        }
    }

    private SelectionMarker selectionFrom;

    public void mouseDoubleClick(MouseEvent e) {
        down = false;
    }

    public void mouseDown(MouseEvent e) {
        System.out.println("mouse down " + e);
        down = true;
        if (DetailCalendarDayContent.class.isAssignableFrom(e.widget.getClass())) {
            DetailCalendarDayContent sourceDay = (DetailCalendarDayContent) e.widget;
            sourceDay.setFocus();
            if (e.button == 1) {
                selectionFrom = new SelectionMarker(sourceDay, e.x, e.y);
                setSelection(sourceDay, e.y);
            } else if (e.button == 3) {
                if (!sourceDay.isFieldSelected(e.y)) setSelection(sourceDay, e.y);
            }
        } else {
            DetailCalendarDayHeader sourceDayHeader = getDayHeader(e.widget);
            if (sourceDayHeader != null) {
                calendar.days[getHeaderIdx(sourceDayHeader)].setFocus();
            }
        }
    }

    public void mouseUp(MouseEvent e) {
        down = false;
    }

    public void mouseMove(MouseEvent e) {
        if (down) {
            if (DetailCalendarDayContent.class.isAssignableFrom(e.widget.getClass())) {
                int[] selectionCounts = new int[calendar.days.length];
                for (int i = 0; i < calendar.days.length; i++) selectionCounts[i] = calendar.days[i].getSelectedFields().size();
                clearAllSelections();
                DetailCalendarDayContent sourceDay = (DetailCalendarDayContent) e.widget;
                Rectangle clientArea = sourceDay.getClientArea();
                if (e.x < clientArea.x || e.x > clientArea.x + clientArea.width) {
                    Point displayRelative = sourceDay.toDisplay(e.x, e.y);
                    Point compositeParentRelative = sourceDay.getParent().toControl(displayRelative);
                    Control child = LayoutUtil.getChildAt(sourceDay.getParent(), compositeParentRelative);
                    if (DetailCalendarDayContent.class.isAssignableFrom(child.getClass())) {
                        DetailCalendarDayContent targetDay = (DetailCalendarDayContent) child;
                        Point childRelative = targetDay.toControl(displayRelative);
                        int beginIdx = getDayIdx(selectionFrom.composite);
                        int endIdx = getDayIdx(targetDay);
                        if (endIdx > beginIdx) {
                            sourceDay.setFieldSelection(selectionFrom.y, getEndY(sourceDay));
                            targetDay.setFieldSelection(getBeginY(targetDay), childRelative.y);
                            for (int midIdx = beginIdx + 1; midIdx < endIdx; midIdx++) calendar.days[midIdx].setFieldSelection(getBeginY(calendar.days[midIdx]), getEndY(calendar.days[midIdx]));
                        } else if (endIdx < beginIdx) {
                            sourceDay.setFieldSelection(getBeginY(sourceDay), selectionFrom.y);
                            targetDay.setFieldSelection(childRelative.y, getEndY(targetDay));
                            for (int midIdx = endIdx + 1; midIdx < beginIdx; midIdx++) calendar.days[midIdx].setFieldSelection(getBeginY(calendar.days[midIdx]), getEndY(calendar.days[midIdx]));
                        }
                    }
                } else {
                    sourceDay.setFieldSelection(selectionFrom.y, e.y);
                }
                for (int i = 0; i < calendar.days.length; i++) if (selectionCounts[i] != calendar.days[i].getSelectedFields().size()) calendar.days[i].redraw();
            }
        }
    }

    private void clearAllSelections() {
        for (DetailCalendarDayContent day : calendar.days) day.clearFieldSelection();
    }

    private void setSelection(DetailCalendarDayContent child, int y) {
        for (DetailCalendarDayContent day : calendar.days) if (day != child && !day.getSelectedFields().isEmpty()) {
            day.clearFieldSelection();
            day.redraw();
        }
        child.setFieldSelection(y);
        child.redraw();
    }

    private int getDayIdx(DetailCalendarDayContent child) {
        for (int i = 0; i < calendar.days.length; i++) {
            if (calendar.days[i] == child) return i;
        }
        return -1;
    }

    private int getHeaderIdx(DetailCalendarDayHeader child) {
        for (int i = 0; i < calendar.days.length; i++) {
            if (calendar.headers[i] == child) return i;
        }
        return -1;
    }

    private DetailCalendarDayHeader getDayHeader(Widget w) {
        if (!(Control.class.isAssignableFrom(w.getClass()))) return null;
        Control c = (Control) w;
        while (Composite.class.isAssignableFrom(c.getClass())) {
            if (DetailCalendarDayHeader.class.isAssignableFrom(c.getClass())) return (DetailCalendarDayHeader) c;
            c = c.getParent();
        }
        return null;
    }

    private int getBeginY(DetailCalendarDayContent child) {
        return child.getPaintCoordinates().paintSettings.getInnerHeight(0) + 1;
    }

    private int getEndY(DetailCalendarDayContent child) {
        return child.getPaintCoordinates().paintSettings.getInnerHeight(child.getPaintCoordinates().paintSettings.scale.getSize()) - 1;
    }
}
