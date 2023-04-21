package com.gamalocus.sgs.profile.viewer.timeline_panel;

import java.awt.Color;
import java.util.ArrayList;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimeTableXYDataset;
import com.gamalocus.sgs.profile.listener.report.RawProfileReport;

abstract class RunningTimeSeries {

    final TimeTableXYDataset taskDataset;

    final String title;

    final Color color;

    int count = 0;

    long lastEvent = 0;

    ArrayList<String> tooltips = new ArrayList<String>();

    ArrayList<RawProfileReport> currentReports = new ArrayList<RawProfileReport>();

    RunningTimeSeries(TimeTableXYDataset taskDataset, String title, Color color) {
        this.taskDataset = taskDataset;
        this.title = title;
        this.color = color;
    }

    protected abstract int getCount(RawProfileReport report);

    void reportBegin(RawProfileReport report) {
        if (lastEvent != 0) {
            if (lastEvent != report.getStartTime()) {
                taskDataset.add(new SimpleTimePeriod(lastEvent, report.getStartTime()), count, title);
                tooltips.add(generateToolTip());
            }
        }
        int count2 = getCount(report);
        count += count2;
        lastEvent = report.getStartTime();
        if (count2 > 0) {
            currentReports.add(report);
        }
    }

    void reportEnd(RawProfileReport report) {
        if (lastEvent != report.getEndTime()) {
            taskDataset.add(new SimpleTimePeriod(lastEvent, report.getEndTime()), count, title);
            tooltips.add(generateToolTip());
        }
        int count2 = getCount(report);
        count -= count2;
        lastEvent = report.getEndTime();
        if (count2 > 0) {
            if (!currentReports.remove(report)) {
                throw new RuntimeException("report " + report + " was not in the list");
            }
        }
    }

    private String generateToolTip() {
        StringBuffer s = new StringBuffer("<html><center><b>" + title + " (" + currentReports.size() + " tasks):</b></center>");
        for (RawProfileReport r : currentReports) {
            s.append(r.getSimpleClassName() + "(" + r.txnId + ")<br />");
        }
        s.append("</html>");
        return s.toString();
    }
}
