package model;

import java.util.Vector;

/**
 *
 * @author William Correa
 */
public class Event extends Element {

    public static final String PREFIXO = "evt";

    public static final String CLASS_NAME = "Event";

    private Activity activity;

    private int time;

    private int value;

    private String description;

    /**
     * 
     */
    public Event() {
        super();
        this.activity = new Activity();
        this.time = 0;
        this.value = 0;
        this.description = "";
    }

    /**
     * 
     * @param activity
     * @param time
     * @param value
     * @param description
     */
    public Event(Activity activity, int time, int value, String description) {
        super();
        this.activity = activity;
        this.time = time;
        this.value = value;
        this.description = description;
    }

    /**
     * 
     * @return
     */
    public Vector<String> getRow() {
        Vector<String> row = new Vector<String>();
        row.add(getId());
        row.add(String.valueOf(time));
        row.add(activity.getName());
        row.add(description);
        return row;
    }

    /**
     * 
     * @return
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * 
     * @param activity
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     */
    public int getTime() {
        return time;
    }

    /**
     * 
     * @param time
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * 
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * 
     * @param value
     */
    public void setValue(int value) {
        this.value = value;
    }
}
