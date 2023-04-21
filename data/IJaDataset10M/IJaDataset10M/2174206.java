package org.bpmsuite.viewhelper;

import gnu.trove.TIntFloatHashMap;
import gnu.trove.TIntFloatIterator;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.bpmsuite.constants.LeaveStatus;
import org.bpmsuite.constants.Services;
import org.bpmsuite.helper.dateandtime.CalendarHelper;
import org.bpmsuite.helper.vo.ApplicationForLeaveHelper;
import org.bpmsuite.service.LeaveService;
import org.bpmsuite.service.PlatformService;
import org.bpmsuite.service.ServiceException;
import org.bpmsuite.vo.hrmetadata.Employee;
import org.bpmsuite.vo.leave.ApplicationForLeave;
import org.bpmsuite.vo.platform.PlatformUser;
import org.bpmsuite.vo.timerecord.LeaveTimedetail;
import org.bpmsuite.vo.timerecord.Timedetail;
import org.bpmsuite.vo.timerecord.Timerecord;

/**
 * @author Dirk Weiser
 **/
public class ApplicationForLeaveViewHelper {

    private Map _services;

    private ApplicationForLeave _applicationForLeave;

    private PlatformUser _platformUser;

    private ApplicationForLeaveViewHelper() {
    }

    public ApplicationForLeaveViewHelper(ApplicationForLeave applicationForLeave, Map services) {
        _applicationForLeave = applicationForLeave;
        _services = services;
    }

    public Date getCreationDate() {
        return _applicationForLeave.getCreationDate();
    }

    public Employee getEmployee() {
        return _applicationForLeave.getEmployee();
    }

    public Date getStartDate() {
        return _applicationForLeave.getStartDate();
    }

    public Date getEndDate() {
        return _applicationForLeave.getEndDate();
    }

    public Collection getTimerecords() {
        return _applicationForLeave.getTimerecords();
    }

    public Integer getStatus() {
        return _applicationForLeave.getStatus();
    }

    public Integer getType() {
        return _applicationForLeave.getType();
    }

    public Float getNumberOfLeaveDays() {
        return _applicationForLeave.getNumberOfLeaveDays();
    }

    public Long getId() {
        return _applicationForLeave.getId();
    }

    public PlatformUser getActor() throws ServiceException {
        if (_platformUser == null) {
            PlatformService platformService = (PlatformService) _services.get(Services.PLATFORM);
            _platformUser = platformService.retrievePlatformUserBySystemId(_applicationForLeave.getEmployee().getSystemId());
        }
        return _platformUser;
    }

    public boolean isPossible() {
        boolean result = isTheOnlyApplicationForLeave() && isNeedles();
        return result;
    }

    public boolean isTheOnlyApplicationForLeave() {
        boolean result = true;
        try {
            Timerecord timerecord;
            Timedetail timedetail;
            LeaveTimedetail leaveTimedetail;
            Iterator timedetailIterator;
            for (Iterator iterator = _applicationForLeave.getTimerecords().iterator(); iterator.hasNext(); ) {
                if (result) {
                    timerecord = (Timerecord) iterator.next();
                    if (timerecord.getTimedetails() != null && timerecord.getTimedetails().size() > 0) {
                        for (timedetailIterator = timerecord.getTimedetails().iterator(); timedetailIterator.hasNext(); ) {
                            timedetail = (Timedetail) timedetailIterator.next();
                            if (timedetail instanceof LeaveTimedetail) {
                                leaveTimedetail = (LeaveTimedetail) timedetail;
                                if (_applicationForLeave.getId() == null) {
                                    result = false;
                                } else if (!leaveTimedetail.getApplicationForLeave().getId().equals(_applicationForLeave.getId())) {
                                    int status = leaveTimedetail.getApplicationForLeave().getStatus().intValue();
                                    if ((status != LeaveStatus.CANCEL_PLANNED) && (status != LeaveStatus.CANCELED) && (status != LeaveStatus.NOT_APPROVED)) {
                                        result = false;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public boolean isNeedles() {
        boolean result = false;
        if (getNumberOfLeaveDays() != null && getNumberOfLeaveDays().floatValue() > 0) {
            result = true;
        }
        return result;
    }

    public boolean isEnoughAvailableDaysForPlanning() {
        try {
            TIntFloatHashMap daysPerYear = ApplicationForLeaveHelper.preCalculateLeaveDaysPerYear(_applicationForLeave);
            for (TIntFloatIterator iter = daysPerYear.iterator(); iter.hasNext(); ) {
                iter.advance();
                LeaveService leaveService = (LeaveService) _services.get(Services.LEAVE);
                float available = leaveService.getNumberOfAvailableLeaveDaysForPlanning(_applicationForLeave.getEmployee(), new Integer(iter.key())).floatValue();
                float planned = iter.value();
                if (planned > available) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ApplicationForLeave getApplicationForLeave() {
        return _applicationForLeave;
    }
}
