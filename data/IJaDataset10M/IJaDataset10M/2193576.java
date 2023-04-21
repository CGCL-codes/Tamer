package org.openuss.lecture;

import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.time.DateUtils;

/**
 * @author Ingo D�ppe
 * 
 * @see org.openuss.lecture.Period
 */
public class PeriodImpl extends org.openuss.lecture.PeriodBase implements org.openuss.lecture.Period {

    private static final long serialVersionUID = 9086031628022698697L;

    @Override
    public void add(Course course) {
        Validate.notNull(course, "Period.add(Course) - course cannot be null");
        if (this.getCourses().contains(course)) {
            throw new IllegalStateException("The Course has already been in the List.");
        }
        this.getCourses().add(course);
        course.setPeriod(this);
    }

    @Override
    public void remove(Course course) {
        getCourses().remove(course);
    }

    @Override
    public boolean isActive() {
        Date now = new Date();
        return this.getStartdate().before(now) && this.getEnddate().after(now);
    }

    @Override
    public void setEnddate(Date enddate) {
        Date date = DateUtils.truncate(enddate, Calendar.DATE);
        date = DateUtils.addDays(date, 1);
        date = DateUtils.addMilliseconds(date, -1);
        super.setEnddate(date);
    }

    @Override
    public void setStartdate(Date startdate) {
        super.setStartdate(DateUtils.truncate(startdate, Calendar.DATE));
    }
}
