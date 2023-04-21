package shellkk.qiq.jdm.engine.persist;

import java.util.ArrayList;
import java.util.List;
import javax.datamining.NamedObject;
import org.hibernate.Session;
import shellkk.qiq.jdm.JDMExceptionUtil;
import shellkk.qiq.jdm.MiningObjectImpl;
import shellkk.qiq.jdm.base.BuildSettingsImpl;
import shellkk.qiq.jdm.data.LogicalDataImpl;

public class BuildSettingsPersistEngine extends HibernatePersistEngine implements IBuildSettingsPersister {

    protected ILogicalDataPersister logicalDataPersister;

    protected List<IBuildSettingsAccessObject> daos = new ArrayList<IBuildSettingsAccessObject>();

    public ILogicalDataPersister getLogicalDataPersister() {
        return logicalDataPersister;
    }

    public void setLogicalDataPersister(ILogicalDataPersister logicalDataPersister) {
        this.logicalDataPersister = logicalDataPersister;
    }

    public List<IBuildSettingsAccessObject> getDaos() {
        return daos;
    }

    public void setDaos(List<IBuildSettingsAccessObject> daos) {
        this.daos = daos;
    }

    public IBuildSettingsAccessObject getDAO(BuildSettingsImpl bs) {
        for (IBuildSettingsAccessObject dao : daos) {
            if (dao.getMiningFunction().equals(bs.getMiningFunction())) {
                return dao;
            }
        }
        return null;
    }

    @Override
    protected Class getPersistClass() {
        return BuildSettingsImpl.class;
    }

    @Override
    protected void delete(MiningObjectImpl obj, Session session) throws Exception {
        BuildSettingsImpl bs = (BuildSettingsImpl) obj;
        LogicalDataImpl ld = bs.getLogicalData();
        IBuildSettingsAccessObject dao = getDAO(bs);
        if (dao == null) {
            JDMExceptionUtil.throwUnsupportPersist(bs.getClass());
        }
        dao.delete(bs, session);
        if (ld != null) {
            logicalDataPersister.delete(ld, session);
        }
    }

    @Override
    protected void load(MiningObjectImpl obj, Session session) throws Exception {
        BuildSettingsImpl bs = (BuildSettingsImpl) obj;
        LogicalDataImpl ld = bs.getLogicalData();
        IBuildSettingsAccessObject dao = getDAO(bs);
        if (dao == null) {
            JDMExceptionUtil.throwUnsupportPersist(bs.getClass());
        }
        dao.load(bs, session);
        if (ld != null) {
            logicalDataPersister.load(ld, session);
        }
    }

    @Override
    protected void saveOrUpdate(MiningObjectImpl obj, Session session) throws Exception {
        BuildSettingsImpl bs = (BuildSettingsImpl) obj;
        LogicalDataImpl ld = bs.getLogicalData();
        if (ld != null) {
            logicalDataPersister.saveOrUpdate(ld, session);
        }
        IBuildSettingsAccessObject dao = getDAO(bs);
        if (dao == null) {
            JDMExceptionUtil.throwUnsupportPersist(bs.getClass());
        }
        dao.saveOrUpdate(bs, session);
    }

    public NamedObject getPersistObjectType() {
        return NamedObject.buildSettings;
    }

    public void delete(BuildSettingsImpl bs, Session session) throws Exception {
        LogicalDataImpl ld = bs.getLogicalData();
        IBuildSettingsAccessObject dao = getDAO(bs);
        if (dao == null) {
            JDMExceptionUtil.throwUnsupportPersist(bs.getClass());
        }
        dao.delete(bs, session);
        if (ld != null) {
            logicalDataPersister.delete(ld, session);
        }
    }

    public void load(BuildSettingsImpl bs, Session session) throws Exception {
        LogicalDataImpl ld = bs.getLogicalData();
        IBuildSettingsAccessObject dao = getDAO(bs);
        if (dao == null) {
            JDMExceptionUtil.throwUnsupportPersist(bs.getClass());
        }
        dao.load(bs, session);
        if (ld != null) {
            logicalDataPersister.load(ld, session);
        }
    }

    public void saveOrUpdate(BuildSettingsImpl bs, Session session) throws Exception {
        LogicalDataImpl ld = bs.getLogicalData();
        if (ld != null) {
            logicalDataPersister.saveOrUpdate(ld, session);
        }
        IBuildSettingsAccessObject dao = getDAO(bs);
        if (dao == null) {
            JDMExceptionUtil.throwUnsupportPersist(bs.getClass());
        }
        dao.saveOrUpdate(bs, session);
    }

    @Override
    protected String getMinorTypeProperty() {
        return "functionName";
    }
}
