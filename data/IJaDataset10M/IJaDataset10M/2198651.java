package beans;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "Studentsubject.findAll", query = "select o from Studentsubject o") })
@IdClass(StudentsubjectPK.class)
public class Studentsubject implements Serializable {

    private Long puljeid;

    @Id
    @Column(nullable = false, insertable = false, updatable = false)
    private Long studentid;

    @Id
    @Column(nullable = false, insertable = false, updatable = false)
    private Long subjectid;

    @ManyToOne
    @JoinColumn(name = "SUBJECTID")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "STUDENTID")
    private Student student;

    public Studentsubject() {
    }

    public Studentsubject(Long puljeid, Student student, Subject subject) {
        this.puljeid = puljeid;
        this.student = student;
        this.subject = subject;
    }

    public Long getPuljeid() {
        return puljeid;
    }

    public void setPuljeid(Long puljeid) {
        this.puljeid = puljeid;
    }

    public Long getStudentid() {
        return studentid;
    }

    public void setStudentid(Long studentid) {
        this.studentid = studentid;
    }

    public Long getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(Long subjectid) {
        this.subjectid = subjectid;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
        if (subject != null) {
            this.subjectid = subject.getId();
        }
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
        if (student != null) {
            this.studentid = student.getId();
        }
    }
}
