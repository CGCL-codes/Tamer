package foursquare4j.types;

import java.io.Serializable;

public class Mayor implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;

    private String checkins;

    private User user;

    private String message;

    private String count;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCheckins() {
        return checkins;
    }

    public void setCheckins(String checkins) {
        this.checkins = checkins;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
