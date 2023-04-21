package onepoint.project.modules.work;

import onepoint.persistence.OpEntityException;
import onepoint.project.modules.custom_attribute.OpCustomizableObject;
import onepoint.project.modules.project.OpActivity;
import onepoint.project.util.OpProjectCalendar;

/**
 * Entity representing a time record for the advanced time tracking.
 *
 * @author horia.chiorean
 */
public class OpTimeRecord extends OpCustomizableObject {

    public static final String TIME_RECORD = "OpTimeRecord";

    /**
    * Start and finish fields, representing the number of minutes in the day (workslip date).
    */
    private Integer start = null;

    private Integer finish = null;

    private String comment;

    /**
    *
    */
    public OpTimeRecord() {
        super();
    }

    /**
    * The duration of the time record.
    */
    private Integer duration = null;

    /**
    * The associated work record.
    */
    @Deprecated
    private OpWorkRecord workRecord = null;

    private OpResourceWorkRecord resourceWorkRecord = null;

    /**
    * Gets the start of the time record.
    * @return an <code>Integer</code> representing a number of minutes.
    */
    public Integer getStart() {
        return start;
    }

    /**
    * Sets the start of the time record.
    * @param start an <code>Integer</code> representing a number of minutes.
    */
    public void setStart(Integer start) {
        this.start = start;
        if (finish != null && start != null) {
            duration = finish - start;
        }
    }

    /**
    * Gets the finish of the time record.
    * @return an <code>Integer</code> representing a number of minutes.
    */
    public Integer getFinish() {
        return finish;
    }

    /**
    * Sets the start of the time record.
    * @param finish an <code>Integer</code> representing a number of minutes.
    */
    public void setFinish(Integer finish) {
        this.finish = finish;
        if (finish != null && start != null) {
            duration = finish - start;
        }
    }

    /**
    * Gets the duration of the time record.
    * @return an <code>Integer</code> representing a number of minutes.
    */
    public Integer getDuration() {
        return duration;
    }

    /**
    * Sets the duration of the time record.
    * @param duration an <code>Integer</code> representing a number of minutes.
    */
    public void setDuration(Integer duration) {
        this.duration = duration;
        if (start != null && duration != null) {
            finish = start + duration;
        }
    }

    /**
    * Gets the workrecord for the time record.
    * @return a <code>OpWorkRecord</code> entity.
    */
    @Deprecated
    public OpWorkRecord getWorkRecord() {
        return workRecord;
    }

    /**
    * Sets the workrecord for the time record.
    * @param workRecord a  <code>OpWorkRecord</code> entity.
    */
    @Deprecated
    public void setWorkRecord(OpWorkRecord workRecord) {
        this.workRecord = workRecord;
    }

    /**
    * Returns the record's activity if it has one, <code>null</code> otherwise.
    *
    * @return a <code>OpActivity</code> instance of <code>null</code>.
    */
    public OpActivity getActivity() {
        if (this.getWorkRecord() == null) {
            return null;
        }
        return this.getWorkRecord().getAssignment().getActivity();
    }

    /**
    * Checks if the fields of the time record are valid
    *
    * @throws onepoint.persistence.OpEntityException
    *          if some validation constraints are broken
    */
    public void validate() throws OpEntityException {
        if (start < 0) {
            throw new OpEntityException(OpWorkError.START_TIME_IS_NEGATIVE);
        } else if (finish < 0) {
            throw new OpEntityException(OpWorkError.FINISH_TIME_IS_NEGATIVE);
        } else if (start >= OpProjectCalendar.MINUTES_PER_DAY) {
            throw new OpEntityException(OpWorkError.START_TIME_IS_TOO_LARGE);
        } else if (finish >= OpProjectCalendar.MINUTES_PER_DAY) {
            throw new OpEntityException(OpWorkError.FINISH_TIME_IS_TOO_LARGE);
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setResourceWorkRecord(OpResourceWorkRecord resourceWorkRecord) {
        this.resourceWorkRecord = resourceWorkRecord;
    }

    public OpResourceWorkRecord getResourceWorkRecord() {
        return resourceWorkRecord;
    }
}
