package edu.vt.eng.swat.workflow.db.base;

import org.apache.log4j.Logger;
import edu.vt.eng.swat.workflow.db.base.dao.Category1Dao;
import edu.vt.eng.swat.workflow.db.base.dao.Category2Dao;
import edu.vt.eng.swat.workflow.db.base.dao.CustTypeDao;
import edu.vt.eng.swat.workflow.db.base.dao.CustomerDao;
import edu.vt.eng.swat.workflow.db.base.dao.DepartmentDao;
import edu.vt.eng.swat.workflow.db.base.dao.EquipTypeDao;
import edu.vt.eng.swat.workflow.db.base.dao.EquipmentDao;
import edu.vt.eng.swat.workflow.db.base.dao.LoanDao;
import edu.vt.eng.swat.workflow.db.base.dao.MakeDao;
import edu.vt.eng.swat.workflow.db.base.dao.OsDao;
import edu.vt.eng.swat.workflow.db.base.dao.RecordDao;
import edu.vt.eng.swat.workflow.db.base.dao.SectionDao;
import edu.vt.eng.swat.workflow.db.base.dao.SignInDao;
import edu.vt.eng.swat.workflow.db.base.dao.TaskDao;
import edu.vt.eng.swat.workflow.db.base.dao.UserDao;
import edu.vt.eng.swat.workflow.db.sqlite.SQLiteFactory;

public abstract class DBFactory {

    private static Logger LOG = Logger.getLogger(DBFactory.class);

    private static DBFactory factory;

    public static DBFactory getFactory() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Getting DBFactory");
        }
        if (factory == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Creating DBFactory");
            }
            factory = new SQLiteFactory();
        }
        return factory;
    }

    public abstract DepartmentDao getDepartmentDao();

    public abstract MakeDao getMakeDao();

    public abstract OsDao getOsDao();

    public abstract Category1Dao getCategory1Dao();

    public abstract Category2Dao getCategory2Dao();

    public abstract SignInDao getSignInDao();

    public abstract CustTypeDao getCustTypeDao();

    public abstract CustomerDao getCustomerDao();

    public abstract EquipmentDao getEquipmentDao();

    public abstract EquipTypeDao getEquipTypeDao();

    public abstract LoanDao getLoanDao();

    public abstract SectionDao getSectionDao();

    public abstract UserDao getUserDao();

    public abstract RecordDao getRecordDao();

    public abstract TaskDao getTaskDao();
}
