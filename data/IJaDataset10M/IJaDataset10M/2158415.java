package entities;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Table;
import abstractEntities.AbstractMastercoach;

/**
 * Mastercoach entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "mastercoach", catalog = "skillworld")
public class Mastercoach extends AbstractMastercoach implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8961628093359687332L;

    /** default constructor */
    public Mastercoach() {
    }

    /** minimal constructor */
    public Mastercoach(Departament departament, User user, Integer date, Integer points) {
        super(departament, user, date, points);
    }

    /** full constructor */
    public Mastercoach(Departament departament, User user, Integer date, Integer points, Set<Student> students, Set<Course> courses) {
        super(departament, user, date, points, students, courses);
    }
}
