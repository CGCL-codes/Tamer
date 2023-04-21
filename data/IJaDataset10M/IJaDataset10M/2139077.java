package cz.muni.fi.pclis.domain.learningContracts;

import cz.muni.fi.pclis.commons.date.Period;
import cz.muni.fi.pclis.commons.domain.DomainObject;
import cz.muni.fi.pclis.domain.Course;
import cz.muni.fi.pclis.domain.Term;
import javax.persistence.*;

/**
 * Entity representing Learning Contracts Behaviour
 * User: Ľuboš Pecho
 * Date: 9.2.2010
 * Time: 20:11:51
 *
 */
@Entity
@NamedQueries({ @NamedQuery(name = "getLearningContractsBehaviourByCourseAndTerm", query = "select lcb from LearningContractsBehaviour lcb where lcb.course = :course and lcb.term = :term") })
public class LearningContractsBehaviour extends DomainObject {

    /**
     * Course for which is this behaviour valid
     */
    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;

    /**
     * Term in which is the behaviour valid
     */
    @ManyToOne
    @JoinColumn(name = "termId")
    private Term term;

    /**
     * Count of accepted contracts
     */
    private int acceptContracts;

    /**
     * Count of votes each student has
     */
    private int votesPerStudent;

    /**
     * Flag if students can add contract on their own
     */
    private boolean canStudentAddContract;

    /**
     * Period when the contracts can be filled
     */
    @Embedded
    private Period duration;

    /**
     *
     * @return course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the course
     * @param course
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     *
     * @return term
     */
    public Term getTerm() {
        return term;
    }

    /**
     * 
     * @param term
     */
    public void setTerm(Term term) {
        this.term = term;
    }

    /**
     *
     * @return accept contracts count
     */
    public int getAcceptContracts() {
        return acceptContracts;
    }

    /**
     * Serts the acceptContracts count
     * @param acceptContracts
     */
    public void setAcceptContracts(int acceptContracts) {
        this.acceptContracts = acceptContracts;
    }

    /**
     *
     * @return votes per student count
     */
    public int getVotesPerStudent() {
        return votesPerStudent;
    }

    /**
     * Sets the votes per student count
     * @param votesPerStudent
     */
    public void setVotesPerStudent(int votesPerStudent) {
        this.votesPerStudent = votesPerStudent;
    }

    /**
     *
     * @return duration
     */
    public Period getDuration() {
        return duration;
    }

    /**
     * Sets the duration
     * @param duration
     */
    public void setDuration(Period duration) {
        this.duration = duration;
    }

    /**
     * Returns true if students can add their own contracts
     * @return
     */
    public boolean isCanStudentAddContract() {
        return canStudentAddContract;
    }

    /**
     * Sets wheter students can add their own contracts
     * @param canStudentAddContract
     */
    public void setCanStudentAddContract(boolean canStudentAddContract) {
        this.canStudentAddContract = canStudentAddContract;
    }
}
