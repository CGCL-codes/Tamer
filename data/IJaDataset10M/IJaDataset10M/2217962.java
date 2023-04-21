package li.kaeppe.travel.tracker.web.bean;

import java.util.Date;
import org.apache.myfaces.custom.fileupload.UploadedFile;

public class UploadFileBean {

    private UploadedFile file;

    private long tripId;

    private Date startDate = null;

    private boolean timeRequired = false;

    private String test;

    public void reset() {
        this.setFile(null);
        this.setStartDate(null);
        this.setTimeRequired(false);
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public boolean isTimeRequired() {
        return timeRequired;
    }

    public void setTimeRequired(boolean timeRequired) {
        this.timeRequired = timeRequired;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
