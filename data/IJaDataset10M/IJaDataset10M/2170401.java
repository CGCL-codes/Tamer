package ops.model;

import java.util.Map;

public class Class extends Entity {

    private int code;

    private int subject;

    private int size;

    private Map<Long, Double> studentsGrade;

    private int term;

    private long teacher;

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Map<Long, Double> getStudentsGrade() {
        return studentsGrade;
    }

    public void setStudentsGrade(Map<Long, Double> studentsGrade) {
        this.studentsGrade = studentsGrade;
    }

    public long getTeacher() {
        return teacher;
    }

    public void setTeacher(long teacher) {
        this.teacher = teacher;
    }
}
