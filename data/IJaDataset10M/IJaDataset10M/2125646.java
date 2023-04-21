package com.patientis.client.state;

import com.patientis.framework.logging.Log;
import com.patientis.framework.scripting.ServiceUtility;
import com.patientis.model.common.DateTimeModel;
import com.patientis.model.reference.StateReportParametersReference;

/**
 * One line class description
 *
 */
public class ReportParameters {

    /**
	 * Default parameters
	 */
    static {
        try {
            String paramStart = ServiceUtility.getRefDisplay(StateReportParametersReference.LAST_SELECTED_START_DATE.getRefId());
            State.getReportParameters().put(paramStart, DateTimeModel.getNow().getStartOfDay().getTime());
            String paramStop = ServiceUtility.getRefDisplay(StateReportParametersReference.LAST_SELECTED_STOP_DATE.getRefId());
            State.getReportParameters().put(paramStop, DateTimeModel.getNow().getAddDay().getStartOfDay().getTime());
        } catch (Exception ex) {
            Log.exception(ex);
        }
    }

    /**
	 * Set the last_selected_start_date parameter from start of the date to adding a day
	 * 
	 * @param changeDate
	 */
    public static void setSelectedDay(DateTimeModel date) {
        try {
            if (date != null) {
                String paramStart = ServiceUtility.getRefDisplay(StateReportParametersReference.LAST_SELECTED_START_DATE.getRefId());
                State.getReportParameters().put(paramStart, date.getStartOfDay().getTime());
                String paramStop = ServiceUtility.getRefDisplay(StateReportParametersReference.LAST_SELECTED_STOP_DATE.getRefId());
                State.getReportParameters().put(paramStop, date.getAddDay().getStartOfDay().getTime());
            }
        } catch (Exception ex) {
            Log.exception(ex);
        }
    }
}
