package net.sf.tacos.components.timeline;

import java.util.Calendar;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.json.JSONLiteral;

/**
 * Simple bean holding event data for http://simile.mit.edu/timeline/
 * dhtml control.
 *
 * @author Andreas Andreou, amplafi.com
 */
public class TimelineEvent {

    private Calendar start;

    private Calendar end;

    private String title;

    private String link;

    private String description;

    public Calendar getStart() {
        return start;
    }

    /**
     * Sets the start of the event.
     * If the given date is after the ending, they're swapped.
     * @param start
     */
    public void setStart(Calendar start) {
        this.start = start;
        fixDates();
    }

    public Calendar getEnd() {
        return end;
    }

    /**
     * Sets the end of the event.
     * If the given date is before the starting, they're swapped.
     * @param end
     */
    public void setEnd(Calendar end) {
        this.end = end;
        fixDates();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private void fixDates() {
        if (start != null && end != null && start.after(end)) {
            Calendar temp = start;
            start = end;
            end = temp;
        }
    }

    private JSONLiteral toDate(Calendar date) {
        return new JSONLiteral("new Date(" + date.get(Calendar.YEAR) + "," + date.get(Calendar.MONTH) + "," + date.get(Calendar.DAY_OF_MONTH) + "," + date.get(Calendar.HOUR_OF_DAY) + "," + date.get(Calendar.MINUTE) + "," + date.get(Calendar.SECOND) + ")");
    }

    public JSONObject appendTo(JSONObject obj) {
        JSONObject event = new JSONObject();
        event.put("start", toDate(start));
        if (end != null) {
            event.put("end", toDate(end));
        }
        event.put("title", title).put("link", link).put("description", (description == null) ? "" : description);
        obj.accumulate("events", event);
        return obj;
    }
}
