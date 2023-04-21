package com.st.rrd;

import java.awt.Font;
import java.awt.Paint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import com.st.rrd.model.GraphDef;
import com.st.rrd.model.RrdGraphConstants;
import com.st.rrd.model.TimeAxisSetting;

class TimeAxis implements RrdGraphConstants {

    private static final TimeAxisSetting[] tickSettings = { new TimeAxisSetting(0, SECOND, 30, MINUTE, 5, MINUTE, 5, 0, "HH:mm"), new TimeAxisSetting(2, MINUTE, 1, MINUTE, 5, MINUTE, 5, 0, "HH:mm"), new TimeAxisSetting(5, MINUTE, 2, MINUTE, 10, MINUTE, 10, 0, "HH:mm"), new TimeAxisSetting(10, MINUTE, 5, MINUTE, 20, MINUTE, 20, 0, "HH:mm"), new TimeAxisSetting(30, MINUTE, 10, HOUR, 1, HOUR, 1, 0, "HH:mm"), new TimeAxisSetting(60, MINUTE, 30, HOUR, 2, HOUR, 2, 0, "HH:mm"), new TimeAxisSetting(180, HOUR, 1, HOUR, 6, HOUR, 6, 0, "HH:mm"), new TimeAxisSetting(600, HOUR, 6, DAY, 1, DAY, 1, 24 * 3600, "EEE"), new TimeAxisSetting(1800, HOUR, 12, DAY, 1, DAY, 2, 24 * 3600, "EEE"), new TimeAxisSetting(3600, DAY, 1, WEEK, 1, WEEK, 1, 7 * 24 * 3600, "'Week 'w"), new TimeAxisSetting(3 * 3600, WEEK, 1, MONTH, 1, WEEK, 2, 7 * 24 * 3600, "'Week 'w"), new TimeAxisSetting(6 * 3600, MONTH, 1, MONTH, 1, MONTH, 1, 30 * 24 * 3600, "MMM"), new TimeAxisSetting(48 * 3600, MONTH, 1, MONTH, 3, MONTH, 3, 30 * 24 * 3600, "MMM"), new TimeAxisSetting(10 * 24 * 3600, YEAR, 1, YEAR, 1, YEAR, 1, 365 * 24 * 3600, "yy"), new TimeAxisSetting(-1, MONTH, 0, MONTH, 0, MONTH, 0, 0, "") };

    private TimeAxisSetting tickSetting;

    private double secPerPix;

    private Calendar calendar;

    private GraphDef gdef;

    private ImageParameters im;

    private Mapper mapper;

    private ImageWorker worker;

    TimeAxis(ImageParameters im, GraphDef gdef, Mapper mapper, ImageWorker worker) {
        this.gdef = gdef;
        this.im = im;
        this.worker = worker;
        this.mapper = mapper;
        this.secPerPix = (im.getEnd() - im.getStart()) / (double) im.getXsize();
        this.calendar = Calendar.getInstance(Locale.getDefault());
        this.calendar.setFirstDayOfWeek(gdef.getFirstDayOfWeek());
    }

    void draw() {
        chooseTickSettings();
        if (tickSetting == null) return;
        drawMinor();
        drawMajor();
        drawLabels();
    }

    private void drawMinor() {
        if (!gdef.isNoMinorGrid()) {
            adjustStartingTime(tickSetting.getMinorUnit(), tickSetting.getMinorUnitCount());
            Paint color = RrdGraphConstants.DEFAULT_GRID_COLOR;
            int y0 = im.getYorigin();
            int y1 = y0 - im.getYsize();
            for (int status = getTimeShift(); status <= 0; status = getTimeShift()) {
                if (status == 0) {
                    long time = calendar.getTime().getTime() / 1000L;
                    int x = mapper.xtr(time);
                    worker.drawLine(x, y0 - 1, x, y0 + 1, color, TICK_STROKE);
                    worker.drawLine(x, y0, x, y1, color, GRID_STROKE);
                }
                findNextTime(tickSetting.getMinorUnit(), tickSetting.getMinorUnitCount());
            }
        }
    }

    private void drawMajor() {
        adjustStartingTime(tickSetting.getMajorUnit(), tickSetting.getMajorUnitCount());
        Paint color = RrdGraphConstants.DEFAULT_MGRID_COLOR;
        int y0 = im.getYorigin();
        int y1 = y0 - im.getYsize();
        for (int status = getTimeShift(); status <= 0; status = getTimeShift()) {
            if (status == 0) {
                long time = calendar.getTime().getTime() / 1000L;
                int x = mapper.xtr(time);
                worker.drawLine(x, y0 - 2, x, y0 + 2, color, TICK_STROKE);
                worker.drawLine(x, y0, x, y1, color, GRID_STROKE);
            }
            findNextTime(tickSetting.getMajorUnit(), tickSetting.getMajorUnitCount());
        }
    }

    private void drawLabels() {
        String labelFormat = tickSetting.getFormat().replaceAll("([^%]|^)%([^%t])", "$1%t$2");
        Font font = gdef.getSmallFont();
        Paint color = RrdGraphConstants.DEFAULT_FONT_COLOR;
        adjustStartingTime(tickSetting.getLabelUnit(), tickSetting.getLabelUnitCount());
        int y = im.getYorigin() + (int) worker.getFontHeight(font) + 2;
        for (int status = getTimeShift(); status <= 0; status = getTimeShift()) {
            String label = formatLabel(labelFormat, calendar.getTime());
            long time = calendar.getTime().getTime() / 1000L;
            int x1 = mapper.xtr(time);
            int x2 = mapper.xtr(time + tickSetting.getLabelSpan());
            int labelWidth = (int) worker.getStringWidth(label, font);
            int x = x1 + (x2 - x1 - labelWidth) / 2;
            if (x >= im.getXorigin() && x + labelWidth <= im.getXorigin() + im.getXsize()) {
                worker.drawString(label, x, y, font, color);
            }
            findNextTime(tickSetting.getLabelUnit(), tickSetting.getLabelUnitCount());
        }
    }

    private static String formatLabel(String format, Date date) {
        if (format.contains("%")) {
            return String.format(format, date);
        } else {
            return new SimpleDateFormat(format).format(date);
        }
    }

    private void findNextTime(int timeUnit, int timeUnitCount) {
        switch(timeUnit) {
            case SECOND:
                calendar.add(Calendar.SECOND, timeUnitCount);
                break;
            case MINUTE:
                calendar.add(Calendar.MINUTE, timeUnitCount);
                break;
            case HOUR:
                calendar.add(Calendar.HOUR_OF_DAY, timeUnitCount);
                break;
            case DAY:
                calendar.add(Calendar.DAY_OF_MONTH, timeUnitCount);
                break;
            case WEEK:
                calendar.add(Calendar.DAY_OF_MONTH, 7 * timeUnitCount);
                break;
            case MONTH:
                calendar.add(Calendar.MONTH, timeUnitCount);
                break;
            case YEAR:
                calendar.add(Calendar.YEAR, timeUnitCount);
                break;
        }
    }

    private int getTimeShift() {
        long time = calendar.getTime().getTime() / 1000L;
        return (time < im.getStart()) ? -1 : (time > im.getEnd()) ? +1 : 0;
    }

    private void adjustStartingTime(int timeUnit, int timeUnitCount) {
        calendar.setTime(new Date(im.getStart() * 1000L));
        switch(timeUnit) {
            case SECOND:
                calendar.add(Calendar.SECOND, -(calendar.get(Calendar.SECOND) % timeUnitCount));
                break;
            case MINUTE:
                calendar.set(Calendar.SECOND, 0);
                calendar.add(Calendar.MINUTE, -(calendar.get(Calendar.MINUTE) % timeUnitCount));
                break;
            case HOUR:
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.add(Calendar.HOUR_OF_DAY, -(calendar.get(Calendar.HOUR_OF_DAY) % timeUnitCount));
                break;
            case DAY:
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                break;
            case WEEK:
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                int diffDays = calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
                if (diffDays < 0) {
                    diffDays += 7;
                }
                calendar.add(Calendar.DAY_OF_MONTH, -diffDays);
                break;
            case MONTH:
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.add(Calendar.MONTH, -(calendar.get(Calendar.MONTH) % timeUnitCount));
                break;
            case YEAR:
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.MONTH, 0);
                calendar.add(Calendar.YEAR, -(calendar.get(Calendar.YEAR) % timeUnitCount));
                break;
        }
    }

    private void chooseTickSettings() {
        if (gdef.getTimeAxisSetting() != null) {
            tickSetting = new TimeAxisSetting(gdef.getTimeAxisSetting());
        } else {
            for (int i = 0; tickSettings[i].getSecPerPix() >= 0 && secPerPix > tickSettings[i].getSecPerPix(); i++) {
                tickSetting = tickSettings[i];
            }
        }
    }
}
