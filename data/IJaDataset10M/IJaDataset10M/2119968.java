package cn.myapps.core.workflow.statelabel.dao;

import java.util.Collection;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.commoninfo.ejb.CommonInfo;

public class HibernateStateLabelDAO extends HibernateBaseDAO implements StateLabelDAO {

    public HibernateStateLabelDAO(String voClassName) {
        super(voClassName);
    }

    public Collection queryName(String application) throws Exception {
        String hql = "select name  from " + this._voClazzName + " vo where applicationid='" + application + "'";
        return getDatas(hql);
    }

    public Collection queryByName(String name, String application) throws Exception {
        String hql = "from " + _voClazzName + " vo where vo.name='" + name + "' and vo.applicationid = '" + application + "' order by vo.orderNo";
        return getDatas(hql);
    }

    public Collection queryStates(String application) throws Exception {
        String hql = "select distinct vo.value from " + this._voClazzName + " vo where applicationid= '" + application + "'";
        return this.getDatas(hql);
    }
}
