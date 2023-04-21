package JDOModel;

import com.google.appengine.api.datastore.Key;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 *
 * @author yewwei.tay.2009
 */
@PersistenceCapable
public class CourseCompleted {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String userId;

    @Persistent
    private String courseCode;

    public CourseCompleted(String userId, String courseCode) {
        this.userId = userId;
        this.courseCode = courseCode;
    }

    public Key getKey() {
        return key;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
