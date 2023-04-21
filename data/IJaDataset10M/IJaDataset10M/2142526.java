package cn.myapps.core.report.dataprepare.ejb;

import java.util.Collection;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.report.dataprepare.dao.DataPrepareDAO;

public class DataPrepareProcessBean extends AbstractDesignTimeProcessBean implements DataPrepareProcess {

    protected IDesignTimeDAO getDAO() throws Exception {
        return DAOFactory.getDefaultDAO(DataPrepare.class.getName());
    }

    public Collection getAllDataPrepareByApplication(String applicationid) throws Exception {
        return ((DataPrepareDAO) getDAO()).getAllDataPrepareByApplication(applicationid);
    }
}
