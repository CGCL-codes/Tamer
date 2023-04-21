package dtec.wssfh.model;

import java.util.Set;

public class UserAdd {

    private String aid = "";

    private boolean genter = true;

    private Major major = null;

    private String class_ = "";

    private int score = 0;

    private String sno = "";

    private Set<Assignment> assignment = null;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public boolean isGenter() {
        return genter;
    }

    public void setGenter(boolean genter) {
        this.genter = genter;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getClass_() {
        return class_;
    }

    public void setClass_(String class_) {
        this.class_ = class_;
    }

    public Set<Assignment> getAssignment() {
        return assignment;
    }

    public void setAssignment(Set<Assignment> assignment) {
        this.assignment = assignment;
    }
}
