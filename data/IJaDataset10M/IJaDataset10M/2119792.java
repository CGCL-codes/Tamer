package com.ivis.xprocess.ui.charts.gantt.impl;

import com.ivis.xprocess.ui.charts.gantt.GanttPreferences;
import com.ivis.xprocess.util.Day;

public class GanttPreferencesImpl extends TimeScalePreferencesImpl implements GanttPreferences {

    private boolean showForecast = true;

    private boolean showTarget = false;

    private LinkStyle myLinkStyle = LinkStyle.Off;

    private SortOrder mySortOrder = SortOrder.EndDate;

    private boolean show75Dates = true;

    private boolean show95Dates = true;

    private boolean merge75And95Dates = false;

    public Day fromDay;

    public Day toDay;

    public boolean workDensity;

    public boolean isShowForecast() {
        return showForecast;
    }

    public void setShowForecast(boolean showForecast) {
        this.showForecast = showForecast;
    }

    public boolean isShowTarget() {
        return showTarget;
    }

    public void setShowTarget(boolean showTarget) {
        this.showTarget = showTarget;
    }

    public boolean isShow75Dates() {
        return show75Dates;
    }

    public void setShow75Dates(boolean show75Dates) {
        this.show75Dates = show75Dates;
    }

    public boolean isShow95Dates() {
        return show95Dates;
    }

    public void setShow95Dates(boolean show95Dates) {
        this.show95Dates = show95Dates;
    }

    public boolean isMerge75And95Dates() {
        return merge75And95Dates;
    }

    public void setMerge75And95Dates(boolean merge75And95Dates) {
        this.merge75And95Dates = merge75And95Dates;
    }

    public void setFromDay(Day day) {
        fromDay = day;
    }

    public Day getFromDay() {
        return fromDay;
    }

    public void setToDay(Day day) {
        toDay = day;
    }

    public Day getToDay() {
        return toDay;
    }

    public void setShowWorkDensity(boolean showWorkDensity) {
        this.workDensity = showWorkDensity;
    }

    public boolean getShowWorkDensity() {
        return this.workDensity;
    }

    public LinkStyle getLinkStyle() {
        return myLinkStyle;
    }

    public void setLinkStyle(LinkStyle linkStyle) {
        myLinkStyle = linkStyle;
    }

    public SortOrder getSortOrder() {
        return mySortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        mySortOrder = sortOrder;
    }
}
