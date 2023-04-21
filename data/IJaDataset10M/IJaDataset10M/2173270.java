package teammate;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Set;
import java.util.TreeSet;

@PersistenceCapable
public class Instructor {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private User user;

    @Persistent
    private Set<Key> courses;

    public Instructor(User user) {
        this.user = user;
        this.courses = new TreeSet<Key>();
    }

    public Key getKey() {
        return key;
    }

    public User getUser() {
        return user;
    }

    public Set<Key> getCourses() {
        return courses;
    }

    public void setCourses(Set<Key> courses) {
        this.courses = courses;
    }
}
