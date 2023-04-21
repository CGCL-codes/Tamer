package au.edu.ausstage.networks.types;

import au.edu.ausstage.utils.InputUtils;
import org.json.simple.*;

/**
 * a class used to represent an event for the generation of data
 * for the event-centric network exports
 */
public class Event implements JSONAware, Comparable<Event> {

    private String id;

    private String name = null;

    private String url = null;

    private String firstDate = null;

    private String lastDate = null;

    private String venue = null;

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setMyName(String value) {
        name = value;
    }

    public void setMyFirstDate(String value) {
        firstDate = value;
    }

    /**
	 * constructor for this class
	 *
	 * @param id the unique identifier for this event
	 */
    public Event(String id) {
        if (InputUtils.isValidInt(id) == false) {
            throw new IllegalArgumentException("Error: The id parameter is expected to be a valid integer");
        } else {
            this.id = id;
        }
    }

    /**
	 * set a new id value for this event
	 * 
	 * @param value the new id value
	 */
    public void setId(String value) {
        if (InputUtils.isValidInt(value) == false) {
            throw new IllegalArgumentException("Error: The parameter is expected to be a valid integer");
        } else {
            id = value;
        }
    }

    /**
	 * get the id value for this event
	 *
	 * @return the id value for this event
	 */
    public String getId() {
        return id;
    }

    public int getIntId() {
        return Integer.parseInt(id);
    }

    /**
	 * set a new name value for this event
	 * 
	 * @param value the new name value
	 */
    public void setName(String value) {
        if (InputUtils.isValid(value) == false) {
            throw new IllegalArgumentException("Error: The parameter cannot be null");
        } else {
            name = value;
        }
    }

    /**
	 * get the name value for this event
	 *
	 * @return the name value for this event
	 */
    public String getName() {
        return name;
    }

    /**
	 * set a new url value for this event
	 * 
	 * @param value the new url value
	 */
    public void setUrl(String value) {
        if (InputUtils.isValid(value) == false) {
            throw new IllegalArgumentException("Error: The parameter cannot be null");
        } else {
            url = value;
        }
    }

    /**
	 * get the url value for this event
	 *
	 * @return the url value for this event
	 */
    public String getUrl() {
        return url;
    }

    /**
	 * set a new firstDate value for this event
	 * 
	 * @param value the new firstDate value
	 */
    public void setFirstDate(String value) {
        if (InputUtils.isValid(value) == false) {
            throw new IllegalArgumentException("Error: The parameter cannot be null");
        } else {
            firstDate = value;
        }
    }

    /**
	 * get the firstDate value for this event
	 *
	 * @return the firstDate value for this event
	 */
    public String getFirstDate() {
        return firstDate;
    }

    /**
	 * set a new lastDate value for this event
	 * 
	 * @param value the new lastDate value
	 */
    public void setLastDate(String value) {
        if (InputUtils.isValid(value) == false) {
            throw new IllegalArgumentException("Error: The parameter cannot be null");
        } else {
            lastDate = value;
        }
    }

    /**
	 * get the lastDate value for this event
	 *
	 * @return the lastDate value for this event
	 */
    public String getLastDate() {
        return lastDate;
    }

    /**
	 * get the JSONObject representation of this object
	 *
	 * @return the JSONObject representation of this object
	 */
    @SuppressWarnings("unchecked")
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);
        object.put("url", url);
        object.put("firstDate", firstDate);
        return object;
    }

    /**
	 * get the JSON String representation of this object
	 *
	 * @return the JSON string representation of this object
	 */
    @SuppressWarnings("unchecked")
    public String toJSONString() {
        return getJSONObject().toString();
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSONObj(int index, boolean central) {
        JSONObject object = new JSONObject();
        object.put("index", index);
        object.put("id", id);
        object.put("nodeName", name);
        object.put("startDate", firstDate);
        object.put("venue", venue);
        object.put("central", central);
        return object;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSONObj(int index) {
        JSONObject object = new JSONObject();
        object.put("index", index);
        object.put("id", id);
        object.put("nodeName", name);
        object.put("startDate", firstDate);
        object.put("venue", venue);
        return object;
    }

    /**
	 * A method to determine if one event is the same as another
	 *
	 * @param o the object to compare this one to
	 *
	 * @return  true if they are equal, false if they are not
	 */
    public boolean equals(Object o) {
        if ((o instanceof Event) == false) {
            return false;
        }
        Event e = (Event) o;
        return id.equals(e.getId());
    }

    /**
	 * Overide the default hashcode method
	 * 
	 * @return a hashcode for this object
	 */
    public int hashCode() {
        return 31 * id.hashCode();
    }

    /**
     * The compareTo method compares the receiving object with the specified object and returns a 
     * negative integer, 0, or a positive integer depending on whether the receiving object is 
     * less than, equal to, or greater than the specified object.
     *
     * @param e the event to compare this one to
     *
     * @return  an integer indicating comparison result
     */
    public int compareTo(Event e) {
        int myId = Integer.parseInt(id);
        int yourId = Integer.parseInt(e.getId());
        if (myId == yourId) {
            return 0;
        } else {
            return myId - yourId;
        }
    }
}
