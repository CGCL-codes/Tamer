package net.cygeek.tech.client.service;

import java.util.ArrayList;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import net.cygeek.tech.client.logic.EmployeeLeaveQuotaLogic;
import net.cygeek.tech.client.HsHrEmployeeLeaveQuota;
import net.cygeek.tech.client.data.EmployeeLeaveQuota;
import java.util.Date;

/**
 * Author: Thilina Hasantha
 */
public class EmployeeLeaveQuotaServiceImpl extends RemoteServiceServlet implements EmployeeLeaveQuotaService {

    EmployeeLeaveQuotaLogic logic = EmployeeLeaveQuotaLogic.getInstance();

    public ArrayList getEmployeeLeaveQuotas() {
        ArrayList a = new ArrayList();
        ArrayList<HsHrEmployeeLeaveQuota> k = logic.getEmployeeLeaveQuotas();
        for (HsHrEmployeeLeaveQuota h : k) {
            a.add(EmployeeLeaveQuota.getProxy(h));
        }
        return a;
    }

    public Boolean addEmployeeLeaveQuota(EmployeeLeaveQuota mEmployeeLeaveQuota, boolean isNew) {
        HsHrEmployeeLeaveQuota bean = EmployeeLeaveQuota.getClass(mEmployeeLeaveQuota);
        bean.setNew(isNew);
        return logic.addEmployeeLeaveQuota(bean);
    }

    public Boolean deleteEmployeeLeaveQuota(String leaveTypeId, int year, int employeeId) {
        return logic.delEmployeeLeaveQuota(leaveTypeId, year, employeeId);
    }

    public EmployeeLeaveQuota getEmployeeLeaveQuota(String leaveTypeId, int year, int employeeId) {
        HsHrEmployeeLeaveQuota k = logic.getEmployeeLeaveQuota(leaveTypeId, year, employeeId);
        return EmployeeLeaveQuota.getProxy(k);
    }

    public ArrayList getEmployeeLeaveQuotas(int employeeId) {
        ArrayList a = new ArrayList();
        ArrayList<HsHrEmployeeLeaveQuota> k = logic.getEmployeeLeaveQuotas(employeeId);
        for (HsHrEmployeeLeaveQuota h : k) {
            a.add(EmployeeLeaveQuota.getProxy(h));
        }
        return a;
    }

    public ArrayList getEmployeeLeaveQuotasCurrent(int employeeId) {
        ArrayList a = new ArrayList();
        ArrayList<HsHrEmployeeLeaveQuota> k = logic.getEmployeeLeaveQuotasCurrent(employeeId);
        for (HsHrEmployeeLeaveQuota h : k) {
            a.add(EmployeeLeaveQuota.getProxy(h));
        }
        return a;
    }
}
