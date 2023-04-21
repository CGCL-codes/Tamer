package org.nightlabs.calendar.ui.detailviewer;

class DetailCalendarPaintSettings {

    int workTimeBegin = 8;

    int workTimeEnd = 17;

    DetailCalendarScale scale = DetailCalendarScale.MINUTES_30;

    int fieldHeight = 25;

    int fieldBorderWidth = 1;

    int leftMarginWidth = 1;

    int leftBorderWidth = 1;

    int leftBorder2Width = 0;

    public int getInnerHeight(int fieldCount) {
        return fieldCount * (fieldHeight + fieldBorderWidth);
    }
}
