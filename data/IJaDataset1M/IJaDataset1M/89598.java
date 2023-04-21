package fitnesse.responders.fitClipse;

import java.sql.Timestamp;

public class FitTestEntity {

    private long id;

    private int numRight;

    private int numWrong;

    private int numExceptions;

    private int numIgnored;

    private Timestamp startTime;

    private Timestamp endTime;

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public int getNumExceptions() {
        return numExceptions;
    }

    public void setNumExceptions(int numExceptions) {
        this.numExceptions = numExceptions;
    }

    public int getNumIgnored() {
        return numIgnored;
    }

    public void setNumIgnored(int numIgnored) {
        this.numIgnored = numIgnored;
    }

    public int getNumRight() {
        return numRight;
    }

    public void setNumRight(int numRight) {
        this.numRight = numRight;
    }

    public int getNumWrong() {
        return numWrong;
    }

    public void setNumWrong(int numWrong) {
        this.numWrong = numWrong;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
