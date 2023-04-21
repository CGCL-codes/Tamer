package cw.coursemanagementmodul.pojo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import com.jgoodies.binding.beans.Model;
import cw.boardingschoolmanagement.persistence.AnnotatedClass;

/**
 *
 * @author André Salmhofer
 */
@Entity
public class PostingRun extends Model implements AnnotatedClass {

    private Long id = null;

    private List<CoursePosting> coursePostings = new ArrayList<CoursePosting>();

    private String name = "";

    private boolean alreadyStorniert = false;

    public static final String PROPERTYNAME_ID = "id";

    public static final String PROPERTYNAME_NAME = "name";

    public static final String PROPERTYNAME_COURSEPOSTINGS = "coursePostings";

    public static final String PROPERTYNAME_ALREADYSTORNIERT = "alreadyStorniert";

    public PostingRun() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        Long old = this.id;
        this.id = id;
        firePropertyChange(PROPERTYNAME_ID, old, name);
    }

    @OneToMany
    public List<CoursePosting> getCoursePostings() {
        return coursePostings;
    }

    public void setCoursePostings(List<CoursePosting> coursePostings) {
        List<CoursePosting> old = this.coursePostings;
        this.coursePostings = coursePostings;
        firePropertyChange(PROPERTYNAME_COURSEPOSTINGS, old, coursePostings);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        firePropertyChange(PROPERTYNAME_NAME, old, name);
    }

    public boolean getAlreadyStorniert() {
        return alreadyStorniert;
    }

    public void setAlreadyStorniert(boolean alreadyStorniert) {
        boolean old = this.alreadyStorniert;
        this.alreadyStorniert = alreadyStorniert;
        firePropertyChange(PROPERTYNAME_ALREADYSTORNIERT, old, alreadyStorniert);
    }
}
