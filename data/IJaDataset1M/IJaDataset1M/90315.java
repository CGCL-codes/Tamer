package net.sf.esims.model.valueobject;

import java.util.Date;
import net.sf.esims.util.EsimsUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 
 * This valueobject represents a 'exam for a subject' Typically this will span
 * only one day, and maybe for a maximum of 3-4 hours.
 * 
 * @author jvictor
 * 
 */
public class IndividualExam implements IndividualExamIfc {

    private String subjectName;

    private Date dateOfExam;

    private String startTime;

    private String endTime;

    private Integer passMark;

    private Integer maxMarks;

    /**
	 * 
	 * Do not use this constructor.
	 * 
	 */
    public IndividualExam() {
    }

    public void setDateOfExam(Date dateOfExam) {
        if (dateOfExam == null) throw new IllegalArgumentException("The date of Exam for a subject cannot be null");
        this.dateOfExam = dateOfExam;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setMaxMarks(Integer maxMarks) {
        if ((maxMarks == null || maxMarks.intValue() <= 0)) throw new IllegalArgumentException("The maximum mark for a Subject cannot be null, 0 or -ve");
        this.maxMarks = maxMarks;
    }

    public void setPassMark(Integer passmark) {
        if ((passmark == null || passmark.intValue() <= 0)) throw new IllegalArgumentException("The pass mark for a Subject cannot be null, 0 or -ve");
        this.passMark = passmark;
    }

    public Date getDateOfExam() {
        return dateOfExam;
    }

    public Integer getMaxMarks() {
        return maxMarks;
    }

    public Integer getPassMark() {
        return passMark;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public boolean equals(Object other) {
        if (!(other instanceof IndividualExam)) return false;
        if (this == other) return true;
        IndividualExam otherExamForSub = (IndividualExam) other;
        return new EqualsBuilder().appendSuper(super.equals(other)).append(subjectName, otherExamForSub.subjectName).append(startTime, otherExamForSub.startTime).append(endTime, otherExamForSub.endTime).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 71).append(subjectName).append(startTime).toHashCode();
    }

    public String toString() {
        return this.subjectName;
    }

    public void setStartTime(String _startTime) {
        EsimsUtils.validateStrings(_startTime, 8);
        this.startTime = _startTime;
    }

    public void setEndTime(String _endTime) {
        EsimsUtils.validateStrings(_endTime, 8);
        this.endTime = _endTime;
    }
}
