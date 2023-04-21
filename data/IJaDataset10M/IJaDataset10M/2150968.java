package tr.model.future;

import java.util.Calendar;
import java.util.Date;
import tr.model.thought.Thought;
import tr.model.topic.Topic;
import tr.model.topic.TopicChangeCookie;
import tr.util.Observable;
import tr.util.ObservableImpl;
import tr.util.Observer;
import tr.util.Utils;

/**
 * Future item.
 *
 * @author Jeremy Moore (jeremyimoore@yahoo.com.au)
 */
public class Future extends ObservableImpl implements Comparable, Observer, TopicChangeCookie {

    private Date created;

    private Thought thought;

    private String description;

    private Topic topic;

    private String notes;

    private Date tickle;

    /** Observable changes. @since 2.0.1 */
    public static enum Change {

        DESCRIPTION, TOPIC, NOTES, TICKLE
    }

    /**
     * Constructs a new instance.
     */
    public Future() {
        created = Calendar.getInstance().getTime();
        setThought(null);
        setDescription("");
        setTopic(Topic.getDefault());
        setNotes("");
    }

    public Date getCreated() {
        return created;
    }

    public Thought getThought() {
        return thought;
    }

    public void setThought(Thought thought) {
        if (Utils.equal(this.thought, thought)) return;
        this.thought = thought;
        notifyObservers(this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (Utils.equal(this.description, description)) return;
        this.description = description;
        notifyObservers(this, Change.DESCRIPTION);
    }

    public Topic getTopic() {
        if (topic == null) {
            return Topic.getDefault();
        }
        if (topic.getName().equals(Topic.getDefault().getName())) {
            return Topic.getDefault();
        }
        return topic;
    }

    public void setTopic(Topic topic) {
        if (Utils.equal(this.topic, topic)) return;
        this.topic = topic;
        notifyObservers(this, Change.TOPIC);
    }

    /**
     * Gets the notes.
     * @return the notes.
     */
    public String getNotes() {
        return (notes == null) ? "" : notes;
    }

    /**
     * Sets the notes.
     * @param notes The notes.
     */
    public void setNotes(String notes) {
        if (Utils.equal(this.notes, notes)) {
            return;
        }
        this.notes = notes;
        notifyObservers(this, Change.NOTES);
    }

    /**
     * Gets the tickle date.
     * @return the tickle date.
     * @since 2.0.1
     */
    public Date getTickle() {
        return tickle;
    }

    /**
     * Sets the tickle date.
     * @param date The date to set.
     * @since 2.0.1
     */
    public void setTickle(Date date) {
        if (!Utils.equal(tickle, date)) {
            tickle = date;
            notifyObservers(this, Change.TICKLE);
        }
    }

    /**
     * Implement Comparable to provide ordering by topic and description.
     * @param object The Object to compare to.
     * @return -1, 0, 1 if this is less than, equal to or greater than object respectively.
     */
    public int compareTo(Object object) {
        if (!(object instanceof Future)) return -1;
        Future other = (Future) object;
        int result = topic.compareTo(other.topic);
        if (result != 0) {
            return result;
        }
        return description.compareToIgnoreCase(other.description);
    }

    public boolean equals(Object object) {
        if (!(object instanceof Future)) return false;
        Future future = (Future) object;
        if (!Utils.equal(created, future.created)) return false;
        if (!Utils.equal(thought, future.thought)) return false;
        if (!Utils.equal(description, future.description)) return false;
        if (!Utils.equal(topic, future.topic)) return false;
        if (!Utils.equal(notes, future.notes)) return false;
        return true;
    }

    public String toString() {
        return description;
    }

    /**
     * Implement Observer to pass on changes to Observers.
     */
    public void update(Observable observable, Object arguement) {
        notifyObservers(observable, arguement);
    }
}
