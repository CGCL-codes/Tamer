package net.cygeek.tech.client.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import net.cygeek.tech.client.HsHrEmpDependents;
import net.cygeek.tech.client.data.EmpDependents;
import net.cygeek.tech.client.logic.EmpDependentsLogic;
import java.util.ArrayList;

/**
 * Author: Thilina Hasantha
 */
public class EmpDependentsServiceImpl extends RemoteServiceServlet implements EmpDependentsService {

    EmpDependentsLogic logic = EmpDependentsLogic.getInstance();

    public ArrayList getEmpDependentss() {
        ArrayList a = new ArrayList();
        ArrayList<HsHrEmpDependents> k = logic.getEmpDependentss();
        for (HsHrEmpDependents h : k) {
            a.add(EmpDependents.getProxy(h));
        }
        return a;
    }

    public ArrayList getEmpDependentss(int empNumber) {
        ArrayList a = new ArrayList();
        ArrayList<HsHrEmpDependents> k = logic.getEmpDependents(empNumber);
        for (HsHrEmpDependents h : k) {
            a.add(EmpDependents.getProxy(h));
        }
        return a;
    }

    public Boolean addEmpDependents(EmpDependents mEmpDependents, boolean isNew) {
        HsHrEmpDependents bean = EmpDependents.getClass(mEmpDependents);
        bean.setNew(isNew);
        return logic.addEmpDependents(bean);
    }

    public Boolean deleteEmpDependents(Double edSeqno, int empNumber) {
        return logic.delEmpDependents(edSeqno, empNumber);
    }

    public ArrayList<EmpDependents> getEmpDependents(int empNumber) {
        ArrayList a = new ArrayList();
        ArrayList<HsHrEmpDependents> k = logic.getEmpDependents(empNumber);
        for (HsHrEmpDependents h : k) {
            a.add(EmpDependents.getProxy(h));
        }
        return a;
    }
}
