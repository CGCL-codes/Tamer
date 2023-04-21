package nl.hajari.wha.service.impl;

import java.util.Date;
import java.util.List;
import nl.hajari.wha.domain.BizLog;
import nl.hajari.wha.domain.Employee;
import nl.hajari.wha.domain.Timesheet;
import nl.hajari.wha.domain.User;
import nl.hajari.wha.service.LogService;
import org.springframework.stereotype.Service;

/**
 * 
 * @author <a href="mailto:saeid3@gmail.com">Saeid Moradi</a>
 */
@Service("logService")
public class LogServiceImpl implements LogService {

    @Override
    public void log(String username, User user, Employee employee, Timesheet timesheet, String details) {
        BizLog bizLog = createBizLog(username, user, employee, timesheet, details);
        bizLog.persist();
    }

    private BizLog createBizLog(String username, User user, Employee employee, Timesheet timesheet, String details) {
        BizLog bl = new BizLog();
        bl.setTime(new Date());
        bl.setDetails(details);
        bl.setUsername(username);
        bl.setUser(user);
        bl.setEmployee(employee);
        bl.setTimesheet(timesheet);
        return bl;
    }

    @Override
    public boolean deleteLogByTimesheet(Timesheet timesheet) {
        List<BizLog> bls = BizLog.findBizLogsByTimesheet(timesheet).getResultList();
        try {
            for (BizLog bl : bls) {
                bl.remove();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
