package org.bpmsuite.taglib.calendar;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.bpmsuite.constants.LeaveStatus;
import org.bpmsuite.helper.dateandtime.CalendarHelper;
import org.bpmsuite.taglib.calendar.helper.TimeRecordHelper;
import org.bpmsuite.taglib.calendar.utils.CalendarUtils;
import org.bpmsuite.vo.timerecord.LeaveTimedetail;
import org.bpmsuite.vo.timerecord.Timerecord;

/**
 *  
 * @author Silvia Mischko
 */
public class CalendarCheckTR extends BodyTagSupport {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6595183134645027317L;

    private String _name = null;

    private String _type = null;

    private CalendarIterator _calItDay = null;

    private CalendarIterator _calItEmp = null;

    private CalendarMain _calItMain = null;

    public CalendarCheckTR() {
    }

    /**
     */
    public int doStartTag() {
        try {
            if (_name.equalsIgnoreCase(CalendarConstants.IS_WEEKEND)) {
                return checkIS_WEEKEND();
            }
            if (_name.equalsIgnoreCase(CalendarConstants.IS_NEW_WEEK)) {
                return checkIS_NEW_WEEK();
            }
            if (!retrieveMaps()) {
                return SKIP_BODY;
            }
            Timerecord timeRec = TimeRecordHelper.retrieveTimerecord(_calItMain.getEmployeesMap(), _calItEmp.getEmployee(), _calItDay.getDay(), _calItDay.getDayOfMonth(), _calItDay.getDayOfYear());
            if (_name.equalsIgnoreCase(CalendarConstants.HAS_TIMERECORD)) {
                return checkHAS_TIMERECORD(timeRec);
            }
            if (timeRec == null) {
                return SKIP_BODY;
            }
            Collection tdCol = timeRec.getTimedetails();
            if (_name.equalsIgnoreCase(CalendarConstants.HAS_TIMEDETAIL)) {
                return checkHAS_TIMEDETAIL(tdCol);
            }
            if (_name.equalsIgnoreCase(CalendarConstants.PLANNED_LEAVE)) {
                return checkPlannedLeave(tdCol);
            }
            if (_name.equalsIgnoreCase(CalendarConstants.APPROVED_LEAVE)) {
                return checkApprovedLeave(tdCol);
            }
            Collection interfacesCol = retrieveInterfacesCollection(tdCol);
            return checkTimedetail(interfacesCol);
        } catch (Exception e) {
            return SKIP_BODY;
        }
    }

    public int doEndTag() {
        return EVAL_PAGE;
    }

    public void setName(String name) {
        _name = name;
    }

    public void setType(String type) {
        _type = type;
    }

    private boolean retrieveMaps() throws Exception {
        _calItDay = (CalendarIterator) findAncestorWithClass(this, CalendarIterator.class);
        String type = _calItDay.getType();
        if (_calItDay != null && !type.startsWith(CalendarConstants.PRE_T_DAYS_OF)) {
            _calItDay = CalendarUtils.findAncestorByTypePrefix(_calItDay, CalendarConstants.PRE_T_DAYS_OF);
        }
        if (_calItDay == null) {
            return false;
        }
        _calItEmp = (CalendarIterator) findAncestorWithClass(this, CalendarIterator.class);
        if (_calItEmp != null && _calItEmp.getType() != CalendarConstants.T_EMPLOYEES) {
            _calItEmp = CalendarUtils.findAncestorByType(_calItEmp, CalendarConstants.T_EMPLOYEES);
        }
        if (_calItEmp == null) {
            return false;
        }
        _calItMain = (CalendarMain) findAncestorWithClass(this, CalendarMain.class);
        if (_calItMain == null) {
            return false;
        }
        return true;
    }

    private int checkHAS_TIMERECORD(Timerecord timeRec) throws Exception {
        if (timeRec == null) {
            if (_type.equalsIgnoreCase(CalendarConstants.CT_TRUE)) {
                return SKIP_BODY;
            }
            if (_type.equalsIgnoreCase(CalendarConstants.CT_FALSE)) {
                return EVAL_BODY_INCLUDE;
            }
        } else {
            if (_type.equalsIgnoreCase(CalendarConstants.CT_TRUE)) {
                return EVAL_BODY_INCLUDE;
            }
            if (_type.equalsIgnoreCase(CalendarConstants.CT_FALSE)) {
                return SKIP_BODY;
            }
        }
        return SKIP_BODY;
    }

    private int checkHAS_TIMEDETAIL(Collection tdCol) throws Exception {
        if (tdCol == null || tdCol.isEmpty()) {
            if (_type.equalsIgnoreCase(CalendarConstants.CT_TRUE)) {
                return SKIP_BODY;
            }
            if (_type.equalsIgnoreCase(CalendarConstants.CT_FALSE)) {
                return EVAL_BODY_INCLUDE;
            }
        } else {
            if (_type.equalsIgnoreCase(CalendarConstants.CT_TRUE)) {
                return EVAL_BODY_INCLUDE;
            }
            if (_type.equalsIgnoreCase(CalendarConstants.CT_FALSE)) {
                return SKIP_BODY;
            }
        }
        return SKIP_BODY;
    }

    private Collection retrieveInterfacesCollection(Collection tdCol) throws Exception {
        Collection interfacesCol = new HashSet();
        if (tdCol != null && !tdCol.isEmpty()) {
            Object[] objAry = tdCol.toArray();
            for (int i = 0; i < objAry.length; i++) {
                Class[] interfaceAry = objAry[i].getClass().getInterfaces();
                for (int j = 0; j < interfaceAry.length; j++) {
                    interfacesCol.add(interfaceAry[j].getName().toString());
                }
            }
            if (interfacesCol == null || interfacesCol.isEmpty()) {
                return null;
            } else {
                return interfacesCol;
            }
        } else {
            return null;
        }
    }

    private int checkPlannedLeave(Collection tdCol) throws Exception {
        int leaveStatus = ((LeaveTimedetail) tdCol.iterator().next()).getApplicationForLeave().getStatus().intValue();
        boolean isPlanned = (leaveStatus == LeaveStatus.PLANNED) || (leaveStatus == LeaveStatus.SUBMITTED_FOR_APPROVE);
        if (_type.equalsIgnoreCase(CalendarConstants.CT_FALSE)) {
            return (isPlanned) ? SKIP_BODY : EVAL_BODY_INCLUDE;
        } else {
            return (isPlanned) ? EVAL_BODY_INCLUDE : SKIP_BODY;
        }
    }

    private int checkApprovedLeave(Collection tdCol) throws Exception {
        int leaveStatus = ((LeaveTimedetail) tdCol.iterator().next()).getApplicationForLeave().getStatus().intValue();
        boolean isApproved = (leaveStatus == LeaveStatus.APPROVED);
        if (_type.equalsIgnoreCase(CalendarConstants.CT_FALSE)) {
            return (isApproved) ? SKIP_BODY : EVAL_BODY_INCLUDE;
        } else {
            return (isApproved) ? EVAL_BODY_INCLUDE : SKIP_BODY;
        }
    }

    private int checkTimedetail(Collection interfacesCol) throws Exception {
        if (interfacesCol == null || interfacesCol.isEmpty()) {
            if (_type.equalsIgnoreCase(CalendarConstants.CT_FALSE)) {
                return EVAL_BODY_INCLUDE;
            } else {
                return SKIP_BODY;
            }
        }
        if (_name == null || _name.length() < 1) {
            return SKIP_BODY;
        }
        Iterator it = interfacesCol.iterator();
        while (it.hasNext()) {
            if (it.next().toString().equals(_name)) {
                if (_type.equalsIgnoreCase(CalendarConstants.CT_TRUE)) {
                    return EVAL_BODY_INCLUDE;
                } else {
                    return SKIP_BODY;
                }
            }
        }
        if (_type.equalsIgnoreCase(CalendarConstants.CT_FALSE)) {
            return EVAL_BODY_INCLUDE;
        } else {
            return SKIP_BODY;
        }
    }

    private int checkIS_WEEKEND() throws Exception {
        _calItDay = (CalendarIterator) findAncestorWithClass(this, CalendarIterator.class);
        String type = _calItDay.getType();
        if (_calItDay != null && !type.startsWith(CalendarConstants.PRE_T_DAYS_OF)) {
            _calItDay = CalendarUtils.findAncestorByTypePrefix(_calItDay, CalendarConstants.PRE_T_DAYS_OF);
        }
        if (_calItDay == null) {
            return SKIP_BODY;
        }
        NumberFormat formatter = new DecimalFormat("00");
        String day = formatter.format(Long.valueOf(_calItDay.getDay()));
        StringBuffer sb = new StringBuffer();
        sb.append(day);
        sb.append(".");
        sb.append(_calItDay.getDayOfMonth());
        sb.append(".");
        sb.append(_calItDay.getDayOfYear());
        Date date = CalendarHelper.stringToDate(sb.toString(), "dd.MM.yyyy");
        if (CalendarHelper.isSaturday(date) || CalendarHelper.isSunday(date)) {
            if (_type.equalsIgnoreCase(CalendarConstants.CT_TRUE)) {
                return EVAL_BODY_INCLUDE;
            }
            if (_type.equalsIgnoreCase(CalendarConstants.CT_FALSE)) {
                return SKIP_BODY;
            }
        } else {
            if (_type.equalsIgnoreCase(CalendarConstants.CT_TRUE)) {
                return SKIP_BODY;
            }
            if (_type.equalsIgnoreCase(CalendarConstants.CT_FALSE)) {
                return EVAL_BODY_INCLUDE;
            }
        }
        return SKIP_BODY;
    }

    private int checkIS_NEW_WEEK() throws Exception {
        _calItDay = (CalendarIterator) findAncestorWithClass(this, CalendarIterator.class);
        String type = _calItDay.getType();
        if (_calItDay != null && !type.startsWith(CalendarConstants.PRE_T_DAYS_OF)) {
            _calItDay = CalendarUtils.findAncestorByTypePrefix(_calItDay, CalendarConstants.PRE_T_DAYS_OF);
        }
        if (_calItDay == null) {
            return SKIP_BODY;
        }
        NumberFormat formatter = new DecimalFormat("00");
        String day = formatter.format(Long.valueOf(_calItDay.getDay()));
        StringBuffer sb = new StringBuffer();
        sb.append(day);
        sb.append(".");
        sb.append(_calItDay.getDayOfMonth());
        sb.append(".");
        sb.append(_calItDay.getDayOfYear());
        Date date = CalendarHelper.stringToDate(sb.toString(), "dd.MM.yyyy");
        if (CalendarHelper.isMonday(date)) {
            if (_type.equalsIgnoreCase(CalendarConstants.CT_TRUE)) {
                return EVAL_BODY_INCLUDE;
            }
            if (_type.equalsIgnoreCase(CalendarConstants.CT_FALSE)) {
                return SKIP_BODY;
            }
        } else {
            if (_type.equalsIgnoreCase(CalendarConstants.CT_TRUE)) {
                return SKIP_BODY;
            }
            if (_type.equalsIgnoreCase(CalendarConstants.CT_FALSE)) {
                return EVAL_BODY_INCLUDE;
            }
        }
        return SKIP_BODY;
    }
}
