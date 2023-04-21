package com.ivis.xprocess.ui.editors.dynamic.elements.specific.dates.graphical;

import com.ivis.xprocess.util.Day;

class Scaler {

    private final Day myStartDay;

    private final Day myEndDay;

    private int myDiagramWidth;

    public Scaler(final Day startDay, final Day endDay, final int diagramWidth) {
        myStartDay = startDay;
        myEndDay = endDay;
        myDiagramWidth = diagramWidth;
    }

    public double getDayWidth() {
        return (double) getDiagramWidth(false) / myStartDay.daysTo(myEndDay);
    }

    int getStartX(Day day) {
        if (Day.NEVER.equals(day)) {
            return getDiagramWidth(true) - 1;
        }
        int margin = (int) (myDiagramWidth * Constants.DIAGRAM_MARGIN_RATIO);
        double result = ((myStartDay.daysTo(day) - 1) * getDayWidth()) + margin;
        return (int) result;
    }

    int getEndX(Day day) {
        return getStartX(day.addDays(1));
    }

    private int getDiagramWidth(boolean full) {
        int margin = (int) (myDiagramWidth * Constants.DIAGRAM_MARGIN_RATIO);
        return full ? myDiagramWidth : (myDiagramWidth - (2 * margin));
    }

    public void setDiagramWidth(int width) {
        myDiagramWidth = width;
    }

    public int getCenterX(Day day) {
        if (day.isBefore(myStartDay)) {
            return 0;
        } else if (day.isAfter(myEndDay)) {
            return myDiagramWidth;
        } else {
            double result = getStartX(day) + (getDayWidth() / 2);
            return (int) result;
        }
    }
}
