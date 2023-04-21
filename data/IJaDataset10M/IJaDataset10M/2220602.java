package entitiesIds;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * LessonstudentId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class LessonstudentId implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5576378731307319795L;

    private Integer idStudent;

    private Integer idLesson;

    /** default constructor */
    public LessonstudentId() {
    }

    /** full constructor */
    public LessonstudentId(Integer idStudent, Integer idLesson) {
        this.idStudent = idStudent;
        this.idLesson = idLesson;
    }

    @Column(name = "idStudent", nullable = false)
    public Integer getIdStudent() {
        return this.idStudent;
    }

    public void setIdStudent(Integer idStudent) {
        this.idStudent = idStudent;
    }

    @Column(name = "idLesson", nullable = false)
    public Integer getIdLesson() {
        return this.idLesson;
    }

    public void setIdLesson(Integer idLesson) {
        this.idLesson = idLesson;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof LessonstudentId)) return false;
        LessonstudentId castOther = (LessonstudentId) other;
        return ((this.getIdStudent() == castOther.getIdStudent()) || (this.getIdStudent() != null && castOther.getIdStudent() != null && this.getIdStudent().equals(castOther.getIdStudent()))) && ((this.getIdLesson() == castOther.getIdLesson()) || (this.getIdLesson() != null && castOther.getIdLesson() != null && this.getIdLesson().equals(castOther.getIdLesson())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getIdStudent() == null ? 0 : this.getIdStudent().hashCode());
        result = 37 * result + (getIdLesson() == null ? 0 : this.getIdLesson().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "LessonstudentId [idStudent=" + idStudent + ", idLesson=" + idLesson + "]";
    }
}
