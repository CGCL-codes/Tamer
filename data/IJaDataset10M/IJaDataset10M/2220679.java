package cw.coursemanagementmodul.pojo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import com.jgoodies.binding.beans.Model;
import cw.boardingschoolmanagement.persistence.AnnotatedClass;

/**
 *
 * @author André Salmhofer
 */
@Entity
public class CourseAddition extends Model implements AnnotatedClass {

    private Long id;

    private Course course;

    private List<Activity> activities;

    private List<Subject> subjects;

    private double alreadyPayedAmount;

    private Boolean posted;

    private double individualPrice;

    public static final String PROPERTYNAME_ID = "id";

    public static final String PROPERTYNAME_COURSEPARTICIPANT = "courseParticipant";

    public static final String PROPERTYNAME_COURSE = "course";

    public static final String PROPERTYNAME_ACTIVITIES = "activities";

    public static final String PROPERTYNAME_SUBJECTS = "subjects";

    public static final String PROPERTYNAME_ALREADYPAYEDAMOUNT = "alreadyPayedAmount";

    public static final String PROPERTYNAME_ACCOUNTINGS = "accountings";

    public static final String PROPERTYNAME_INDIVIDUALPRICE = "individualPrice";

    public CourseAddition() {
        course = new Course();
        activities = new ArrayList();
        subjects = new ArrayList();
        posted = false;
        alreadyPayedAmount = 0;
    }

    @ManyToMany
    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activitys) {
        List<Activity> old = this.activities;
        this.activities = activitys;
        firePropertyChange(PROPERTYNAME_ACTIVITIES, old, activities);
    }

    @ManyToMany
    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        List<Subject> old = this.subjects;
        this.subjects = subjects;
        firePropertyChange(PROPERTYNAME_SUBJECTS, old, subjects);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        Long old = this.id;
        this.id = id;
        firePropertyChange(PROPERTYNAME_ID, old, id);
    }

    @OneToOne
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        Course old = this.course;
        this.course = course;
        firePropertyChange(PROPERTYNAME_COURSE, old, course);
    }

    public double getAlreadyPayedAmount() {
        return alreadyPayedAmount;
    }

    public void setAlreadyPayedAmount(double alreadyPayedAmount) {
        this.alreadyPayedAmount = alreadyPayedAmount;
    }

    public Boolean isPosted() {
        return posted;
    }

    public void setPosted(Boolean posted) {
        this.posted = posted;
    }

    public double getIndividualPrice() {
        return individualPrice;
    }

    public void setIndividualPrice(double individualPrice) {
        this.individualPrice = individualPrice;
    }
}
