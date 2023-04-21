package com.technoetic.xplanner.importer.spreadsheet;

import java.util.Date;
import com.technoetic.xplanner.importer.SpreadsheetStory;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: May 19, 2005
 * Time: 10:22:45 PM
 */
public class SpreadsheetStoryFilter {

    private Date startDate;

    private Date endDate;

    public SpreadsheetStoryFilter(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        if (startDate.after(endDate)) throw new IllegalArgumentException("Filter start date after end date " + this);
    }

    public boolean matches(SpreadsheetStory story) {
        Date storyEndDate = story.getEndDate();
        if (storyEndDate == null) return false;
        if (storyEndDate.before(startDate)) return false;
        if (storyEndDate.after(endDate)) return false;
        return true;
    }

    public String toString() {
        return "SpreadsheetStoryFilter{" + "startDate=" + startDate + " - endDate=" + endDate + "}";
    }
}
