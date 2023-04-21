package il.co.entrypoint.service;

import il.co.entrypoint.api.PunchService;
import il.co.entrypoint.model.Activity;
import il.co.entrypoint.model.Employe;
import il.co.entrypoint.dao.ActivityDAO;
import il.co.entrypoint.dao.EmployeDAO;
import javax.jws.WebService;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

/**
 * Implements {@link PunchService} webservice via <code>XFire</code> library.
 *  Methods {@link #PunchIn(long)} and {@link #PunchOut(long)} are called by
 * <code>XFireServlet</code> while recieving <code>SOAP</code> message and these
 * methods insert current time to DB, as time of entrance/exit of {@link Employe}
 * with <code>id</code> specified as argument.<br>
 * Time inserted to DB as {@link Activity} report with <code>Employe</code> and
 * <code>timeStart</code> / <code>timeEnd</code> only.<p>
 *
 * DAO objects are used to achieve possibility of easy adaptation to various DB's
 * <br><code>Spring</code> beans are also used   
 *
 * @author Grinfeld Igor, Entrypoint Ltd. igorg@entrypoint.co.il
 * @version 1.0
 */
@WebService
@Transactional
public class PunchServiceImpl implements PunchService {

    /**
     * Constant DB query for punchIn. Finds last <code>Activity</code> without
     *  startTime
     **/
    public static final String START_TIME_EMPTY = "from Activity ent where ent.deleted=0 and ent.timeStart=null " + "and ent.employe=? and ent.date=? and ent.timeEnd>=?";

    /**
     * Constant DB query for punchOut. Finds last <code>Activity</code> without
     * endTime
     */
    public static final String END_TIME_EMPTY = "from Activity ent where ent.deleted=0 and ent.timeEnd=null " + "and ent.employe=? and ent.date=? and ent.timeStart<=?";

    private ActivityDAO hoursDao;

    private EmployeDAO employeDao;

    /**
     * Used by Spring to locate bean's file
     * @return name of context bean's file
     */
    public String[] getConfigLocations() {
        return new String[] { "applicationContext-hibernate.xml" };
    }

    /**
     * Used by Spring to convert hours reports DAO bean into object
     * @param dao DAO object from bean
     */
    public void setHoursDao(ActivityDAO dao) {
        hoursDao = dao;
    }

    /**
     * Used by Spring to convert hours reports DAO bean into object
     * @return DAO object from application 
     */
    public ActivityDAO getHoursDao() {
        return hoursDao;
    }

    /**
     * Used by Spring to convert employee DAO bean into object
     * @param dao DAO object from bean
     */
    public void setEmployeDao(EmployeDAO dao) {
        employeDao = dao;
    }

    /**
     * Used by Spring to convert employee DAO bean into object
     * @return DAO object from application
     */
    public EmployeDAO getEmployeDao() {
        return employeDao;
    }

    /**
     * Searches for last <code>Activity</code> of this employee without
     * <code>timeStart</code> value and with <code>timeEnd</code> value. Then
     * inserts current time into this report. If such report doesn't exist, new
     *  <code>Activity</code> report with <code>Employe</code> and
     * <code>timeStart</code> only, is created
     * @param id <code>id</code> of <code>Employe</code>
     * @return date inserted if succeded, or null
     */
    public Date PunchIn(long id) {
        return punchService(id, true);
    }

    /**
     * Searches for last <code>Activity</code> of this employee without
     * <code>timeEnd</code> value and with <code>timeStart</code> value. Then
     * inserts current time into this report. If such report doesn't exist, new
     *  <code>Activity</code> report with <code>Employe</code> and
     * <code>timeEnd</code> only, is created
     * @param id <code>id</code> of <code>Employe</code>
     * @return date inserted if succeded, or null
     */
    public Date PunchOut(long id) {
        return punchService(id, false);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    private Date punchService(long id, boolean in) {
        Employe emp = employeDao.findById(id, false);
        if (emp == null) {
            return null;
        }
        Date today = new Date();
        String query = in ? START_TIME_EMPTY : END_TIME_EMPTY;
        Collection<Activity> hoursSet = hoursDao.findByQuery(query, emp, today, today);
        Activity current;
        Activity report;
        Iterator<Activity> itr = hoursSet.iterator();
        if (hoursSet.isEmpty()) {
            report = new Activity();
            report.setEmploye(emp);
            report.setDate(today);
        } else {
            report = itr.next();
        }
        if (in) {
            for (int i = 1; i < hoursSet.size() && itr.hasNext(); i++) {
                current = itr.next();
                if (report.getTimeEnd() != null && current.getTimeEnd() != null && report.getTimeEnd().compareTo(current.getTimeEnd()) >= 0) {
                    report = current;
                }
            }
            report.setTimeStart(today);
        } else {
            for (int i = 1; i < hoursSet.size() && itr.hasNext(); i++) {
                current = itr.next();
                if (report.getTimeStart() != null && current.getTimeStart() != null && report.getTimeStart().compareTo(current.getTimeStart()) <= 0) {
                    report = current;
                }
            }
            report.setTimeEnd(today);
        }
        try {
            hoursDao.saveOrUpdate(report, true);
        } catch (Exception e) {
            return null;
        }
        return today;
    }
}
