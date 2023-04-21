package org.wfp.rita.web.mbeans;

import java.util.List;
import org.apache.log4j.Logger;
import org.mortbay.util.ajax.JSON;
import org.wfp.rita.base.RitaException;
import org.wfp.rita.datafacade.DashboardDao;
import org.wfp.rita.datafacade.DashboardDao.DispatchByDest;
import org.wfp.rita.datafacade.DashboardDao.DispatchByMonth;
import org.wfp.rita.datafacade.DashboardDao.DispatchByOrgType;
import org.wfp.rita.datafacade.DashboardDao.DispatchBySector;

/**
 * Javascript data source for dashboard reports
 */
public class DashboardBean extends BeanBase {

    private DashboardDao m_DAO;

    private synchronized DashboardDao getDao() throws RitaException {
        if (m_DAO == null) {
            m_DAO = getFacade().getDashboardDao();
        }
        return m_DAO;
    }

    public List<DispatchBySector> getDispatchesByProductFamily() throws RitaException {
        return getDao().getDashboardDispatchesByProductFamily();
    }

    public String getDispatchesByProductFamilyJson() throws RitaException {
        return new JSON().toJSON(getDispatchesByProductFamily());
    }

    private static Logger logger = Logger.getLogger(DashboardBean.class);

    public List<DispatchByOrgType> getDispatchesByOrgType() throws RitaException {
        return getDao().getDashboardDispatchesByOrgType();
    }

    public String getDispatchesByOrgTypeJson() throws RitaException {
        return new JSON().toJSON(getDispatchesByOrgType());
    }

    public List<DispatchByDest> getDispatchesByDest() throws RitaException {
        return getDao().getDashboardDispatchesByDest();
    }

    public String getDispatchesByDestJson() throws RitaException {
        return new JSON().toJSON(getDispatchesByDest());
    }

    public List<DispatchByMonth> getDispatchesByMonth() throws RitaException {
        return getDao().getDashboardDispatchesByMonth();
    }

    public String getDispatchesByMonthJson() throws RitaException {
        return new JSON().toJSON(getDispatchesByMonth());
    }
}
