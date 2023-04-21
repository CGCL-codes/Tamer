package projectatlast.tracking;

import projectatlast.data.JSONable;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.googlecode.objectify.annotation.Unindexed;

public class Mood implements JSONable {

    @Unindexed
    long comprehension = 50;

    @Unindexed
    long interest = 50;

    protected Mood() {
    }

    public Mood(long comprehension, long interest) {
        super();
        setComprehension(comprehension);
        setInterest(interest);
    }

    public long getComprehension() {
        return comprehension;
    }

    public void setComprehension(long comprehension) {
        this.comprehension = comprehension;
    }

    public long getInterest() {
        return interest;
    }

    public void setInterest(long interest) {
        this.interest = interest;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("comprehension", getComprehension());
        json.put("interest", getInterest());
        return json;
    }
}
