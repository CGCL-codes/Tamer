package com.patientis.business.calendar;

import com.patientis.framework.scheduler.IScheduleModel;

/**
 * One line class description
 *
 * 
 *   
 */
public interface IResourceSchedule extends IScheduleModel {

    /**
	 * Quantity of resource on this schedule
	 * 
	 * @return
	 */
    public double getResourceQuantity();

    /**
	 * Duration of instance
	 * 
	 * @return
	 */
    public double getDuration();

    /**
	 * Duration units
	 * 
	 * @return
	 */
    public long getDurationUnitRefId();
}
