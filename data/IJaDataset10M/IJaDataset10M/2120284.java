package gatchan.jedit.rfcreader;

import java.util.List;

/**
 * @author Matthieu Casanova
 */
public class RFC {

    private int number;

    private String title;

    private List<Integer> obsoletes;

    private List<Integer> obsoletedBy;

    private List<Integer> updates;

    private List<Integer> updatedBy;

    private String also;

    private String status;

    private String date;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getObsoletes() {
        return obsoletes;
    }

    public void setObsoletes(List<Integer> obsoletes) {
        this.obsoletes = obsoletes;
    }

    public List<Integer> getObsoletedBy() {
        return obsoletedBy;
    }

    public void setObsoletedBy(List<Integer> obsoletedBy) {
        this.obsoletedBy = obsoletedBy;
    }

    public List<Integer> getUpdates() {
        return updates;
    }

    public void setUpdates(List<Integer> updates) {
        this.updates = updates;
    }

    public List<Integer> getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(List<Integer> updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getAlso() {
        return also;
    }

    public void setAlso(String also) {
        this.also = also;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String toString() {
        return number + " " + title;
    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof RFC) {
            RFC rfc = (RFC) obj;
            return rfc.number == number;
        }
        return false;
    }
}
