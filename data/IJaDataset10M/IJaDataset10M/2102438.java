package shellkk.qiq.jdm.engine.persist;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import shellkk.qiq.jdm.JDMExceptionUtil;
import shellkk.qiq.jdm.base.AlgorithmSettingsImpl;
import shellkk.qiq.jdm.base.BuildSettingsImpl;

public abstract class BuildSettingsAccessObject implements IBuildSettingsAccessObject {

    protected List<IAlgorithmSettingsAccessObject> daos = new ArrayList();

    public List<IAlgorithmSettingsAccessObject> getDaos() {
        return daos;
    }

    public void setDaos(List<IAlgorithmSettingsAccessObject> daos) {
        this.daos = daos;
    }

    public IAlgorithmSettingsAccessObject getDAO(BuildSettingsImpl bs) {
        for (IAlgorithmSettingsAccessObject dao : daos) {
            if (dao.getMiningAlgorithm().name().equals(bs.getAlgorithmName())) {
                return dao;
            }
        }
        return null;
    }

    public void delete(BuildSettingsImpl bs, Session session) throws Exception {
        IAlgorithmSettingsAccessObject dao = getDAO(bs);
        if (dao == null) {
            JDMExceptionUtil.throwUnsupportAlgorithmSettingsPersist(bs.getAlgorithmName());
        }
        dao.deleteAlgorithmSettings(bs, session);
        deleteBuildSettings(bs, session);
    }

    public void load(BuildSettingsImpl bs, Session session) throws Exception {
        IAlgorithmSettingsAccessObject dao = getDAO(bs);
        if (dao == null) {
            JDMExceptionUtil.throwUnsupportAlgorithmSettingsPersist(bs.getAlgorithmName());
        }
        AlgorithmSettingsImpl as = dao.loadAlgorithmSettings(bs, session);
        bs.setAlgorithmSettings(as);
        loadBuildSettings(bs, session);
    }

    public void saveOrUpdate(BuildSettingsImpl bs, Session session) throws Exception {
        IAlgorithmSettingsAccessObject dao = getDAO(bs);
        if (dao == null) {
            JDMExceptionUtil.throwUnsupportAlgorithmSettingsPersist(bs.getAlgorithmName());
        }
        saveOrUpdateBuildSettings(bs, session);
        dao.saveOrUpdateAlgorithmSettings(bs, session);
    }

    protected abstract void deleteBuildSettings(BuildSettingsImpl bs, Session session) throws Exception;

    protected abstract void loadBuildSettings(BuildSettingsImpl bs, Session session) throws Exception;

    protected abstract void saveOrUpdateBuildSettings(BuildSettingsImpl bs, Session session) throws Exception;
}
