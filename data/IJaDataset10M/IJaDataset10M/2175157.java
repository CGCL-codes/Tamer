package org.openuss.lecture;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.Validate;

/**
 * @see org.openuss.lecture.University
 * @author Ron Haus
 */
public class UniversityImpl extends org.openuss.lecture.UniversityBase implements org.openuss.lecture.University {

    /**
	 * The serial version UID of this class. Needed for serialization.
	 */
    private static final long serialVersionUID = 7820983867751868169L;

    /**
	 * @see org.openuss.lecture.University#add(org.openuss.lecture.Department)
	 */
    public void add(org.openuss.lecture.Department department) {
        Validate.notNull(department, "University.add(Department) - department cannot be null");
        if (!this.getDepartments().contains(department)) {
            this.getDepartments().add(department);
            department.setUniversity(this);
        } else {
            department.setUniversity(this);
            throw new IllegalArgumentException("University.add(Department) - the Department has already been in the List");
        }
    }

    /**
	 * @see org.openuss.lecture.University#remove(org.openuss.lecture.Department)
	 */
    public void remove(org.openuss.lecture.Department department) {
        Validate.notNull(department, "University.remove(Department) - department cannot be null");
        if (!this.getDepartments().remove(department)) {
            if (department.getUniversity().equals(this)) {
                department.setUniversity(null);
            }
            throw new IllegalArgumentException("University.remove(Department) - the Department has not been in the List");
        }
        department.setUniversity(null);
    }

    /**
	 * @see org.openuss.lecture.University#add(org.openuss.lecture.Period)
	 */
    public void add(org.openuss.lecture.Period period) {
        Validate.notNull(period, "University.add(Period) - period cannot be null");
        if (!this.getPeriods().contains(period)) {
            this.getPeriods().add(period);
            period.setUniversity(this);
        } else {
            period.setUniversity(this);
            throw new IllegalArgumentException("University.add(Period) - the Period has already been in the List");
        }
    }

    /**
	 * @see org.openuss.lecture.University#remove(org.openuss.lecture.Period)
	 */
    public void remove(org.openuss.lecture.Period period) {
        Validate.notNull(period, "University.remove(Period) - period cannot be null");
        if (!this.getPeriods().remove(period)) {
            if (period.getUniversity().equals(this)) {
                period.setUniversity(null);
            }
            throw new IllegalArgumentException("University.remove(Period) - the Period has not been in the List");
        }
        period.setUniversity(null);
    }

    /**
	 * @see org.openuss.lecture.University#getActivePeriods()
	 */
    public List<Period> getActivePeriods() {
        List<Period> activePeriods = new ArrayList<Period>();
        if (this.getPeriods().isEmpty()) {
            activePeriods = null;
        } else {
            for (Period period : this.getPeriods()) {
                if (period.isActive()) {
                    activePeriods.add(period);
                }
            }
        }
        return activePeriods;
    }

    @Override
    public Period getDefaultPeriod() {
        return (Period) CollectionUtils.find(getPeriods(), new DefaultPredicate());
    }

    private static final class DefaultPredicate implements Predicate {

        public boolean evaluate(Object object) {
            return ((Period) object).isDefaultPeriod();
        }
    }
}
