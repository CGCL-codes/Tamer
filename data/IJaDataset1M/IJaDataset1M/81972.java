package ch.intertec.storybook.model;

import java.io.File;
import org.hibernate.Session;
import ch.intertec.storybook.SbConstants;
import ch.intertec.storybook.SbConstants.PreferenceKey;
import ch.intertec.storybook.model.hbn.dao.PreferenceDAOImpl;

/**
 * @author martin
 * 
 */
public class PreferenceModel extends AbstractModel {

    public PreferenceModel() {
        super();
        init();
    }

    public void init() {
        try {
            getPrefDir().mkdir();
            String dbName = getDbName();
            initSession(dbName, "preference.cfg.xml");
            Session session = beginTransaction();
            PreferenceDAOImpl dao = new PreferenceDAOImpl(session);
            dao.saveOrUpdate(PreferenceKey.STORYBOOK_VERSION.toString(), SbConstants.Storybook.PRODUCT_VERSION.toString());
            commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fireAgain() {
    }

    public File getHomeDir() {
        return new File(System.getProperty("user.home"));
    }

    private File getPrefDir() {
        File homeDir = getHomeDir();
        return new File(homeDir + File.separator + ".storybook");
    }

    private String getDbName() {
        return getPrefDir() + File.separator + SbConstants.Storybook.PREFERENCE_DB_NAME;
    }
}
