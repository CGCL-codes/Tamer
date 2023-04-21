package cn.myapps.core.workflow.storage.runtime.dao;

import java.util.Collection;
import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;

public class HibernateFlowStateRTDAO extends HibernateBaseDAO implements FlowStateRTDAO {

    public HibernateFlowStateRTDAO(String voClassName) {
        super(voClassName);
    }

    public FlowStateRT findFlowStateRTByDocidAndFlowid(String docid, String flowid) throws Exception {
        String hql = "FROM " + _voClazzName + " vo WHERE " + "vo.docid='" + docid + "' AND vo.flowid='" + flowid + "'";
        System.out.println(hql);
        Collection datas = this.getDatas(hql, null);
        FlowStateRT flowStateRT = null;
        Object[] obj = datas.toArray();
        if (obj.length > 0) {
            flowStateRT = (FlowStateRT) (datas.toArray())[0];
        }
        return flowStateRT;
    }
}
