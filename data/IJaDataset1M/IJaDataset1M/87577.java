package net.cygeek.tech.client.service;

import java.util.ArrayList;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import net.cygeek.tech.client.logic.EmpChildrenLogic;
import net.cygeek.tech.client.HsHrEmpChildren;
import net.cygeek.tech.client.data.EmpChildren;
import java.util.Date;

/**
 * Author: Thilina Hasantha
 */
public class EmpChildrenServiceImpl extends RemoteServiceServlet implements EmpChildrenService {

    EmpChildrenLogic logic = EmpChildrenLogic.getInstance();

    public ArrayList getEmpChildrens() {
        ArrayList a = new ArrayList();
        ArrayList<HsHrEmpChildren> k = logic.getEmpChildrens();
        for (HsHrEmpChildren h : k) {
            a.add(EmpChildren.getProxy(h));
        }
        return a;
    }

    public Boolean addEmpChildren(EmpChildren mEmpChildren, boolean isNew) {
        HsHrEmpChildren bean = EmpChildren.getClass(mEmpChildren);
        bean.setNew(isNew);
        return logic.addEmpChildren(bean);
    }

    public Boolean deleteEmpChildren(Double ecSeqno, int empNumber) {
        return logic.delEmpChildren(ecSeqno, empNumber);
    }

    public EmpChildren getEmpChildren(Double ecSeqno, int empNumber) {
        HsHrEmpChildren k = logic.getEmpChildren(ecSeqno, empNumber);
        return EmpChildren.getProxy(k);
    }
}
