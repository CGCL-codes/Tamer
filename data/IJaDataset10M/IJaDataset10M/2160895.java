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
public class Course {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String courseCode;

    @Persistent
    private String school;

    @Persistent
    private String title;

    @Persistent
    private String description;

    @Persistent
    private String examDate;

    @Persistent
    private String examStart;

    @Persistent
    private String examEnd;

    public Course(String courseCode, String school, String title, String description, String examDate, String examStart, String examEnd) {
        this.courseCode = courseCode;
        this.school = school;
        this.title = title;
        this.description = description;
        this.examDate = examDate;
        this.examStart = examStart;
        this.examEnd = examEnd;
    }

    public Key getKey() {
        return key;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public void setExamEnd(String examEnd) {
        this.examEnd = examEnd;
    }

    public void setExamStart(String examStart) {
        this.examStart = examStart;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public String getExamDate() {
        return examDate;
    }

    public String getExamEnd() {
        return examEnd;
    }

    public String getExamStart() {
        return examStart;
    }

    public String getSchool() {
        return school;
    }

    public String getTitle() {
        return title;
    }
}
