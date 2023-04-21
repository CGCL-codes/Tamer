package edu.ucmo.cis.enrollment.vo;

import java.util.List;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Jiang
 * Email:sjiangmo@gmail.com
 */
@PersistenceCapable
public class CourseType {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent(mappedBy = "courseType")
    private List<Course> courseSets;

    @Persistent
    private String courseTypeName;

    @Persistent
    private String courseTypeNotes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Course> getCourseSets() {
        return courseSets;
    }

    public void setCourseSets(List<Course> courseSets) {
        this.courseSets = courseSets;
    }

    public String getCourseTypeName() {
        return courseTypeName;
    }

    public void setCourseTypeName(String courseTypeName) {
        this.courseTypeName = courseTypeName;
    }

    public String getCourseTypeNotes() {
        return courseTypeNotes;
    }

    public void setCourseTypeNotes(String courseTypeNotes) {
        this.courseTypeNotes = courseTypeNotes;
    }
}
