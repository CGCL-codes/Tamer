package org.bpmsuite.dto.bpm.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import org.bpmsuite.constants.BpmTaskTypes;
import org.bpmsuite.constants.ProcessVariables;
import org.bpmsuite.constants.ResourceBundles;
import org.bpmsuite.constants.Services;
import org.bpmsuite.dto.AbstractBpmTask;
import org.bpmsuite.dto.DataTransferObjectException;
import org.bpmsuite.service.LeaveService;
import org.bpmsuite.vo.leave.ApplicationForLeave;

/**
 * @author Dirk Weiser
 */
public class LeaveBpmTask extends AbstractBpmTask {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3276497468015827305L;

    private LeaveService _leaveService;

    public LeaveBpmTask() {
        super();
        _type = BpmTaskTypes.LEAVE;
    }

    protected void initServices(Map services) throws DataTransferObjectException {
        Object leaveService = services.get(Services.LEAVE);
        if (leaveService != null) {
            _leaveService = (LeaveService) leaveService;
        } else {
            throw new DataTransferObjectException("Mandatory service is null!");
        }
    }

    protected void useServices() throws DataTransferObjectException {
        Object id = getProcessVariables().get(ProcessVariables.ID_APPLICATION_FOR_LEAVE);
        if (id != null) {
            try {
                ApplicationForLeave applicationForLeave = _leaveService.retrieveApplicationForLeaveById((Long) id);
                setModel(applicationForLeave);
            } catch (Exception e) {
                new DataTransferObjectException(e.getMessage());
            }
        } else {
            throw new DataTransferObjectException("csv id parameter is null!");
        }
    }

    public String getAdditionalTasklistInformation(Locale locale) {
        String pattern = ResourceBundle.getBundle(ResourceBundles.TASKLIST, locale).getString("leave.approve.data.submit.date");
        MessageFormat format = new MessageFormat(pattern, locale);
        Long timestamp = (Long) getProcessVariables().get(ProcessVariables.TIMESTAMP);
        return (timestamp != null) ? format.format(new Object[] { new Date(timestamp.longValue()) }) : "";
    }

    public void doBeforeFinishingProcess(Map params) throws DataTransferObjectException {
    }
}
