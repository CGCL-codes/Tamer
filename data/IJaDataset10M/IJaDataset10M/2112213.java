package ge.telasi.tasks.controller;

import ge.telasi.tasks.model.AppConfig;
import ge.telasi.tasks.model.User;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Application configuration controller.
 *
 * @author dimitri
 */
public class AppConfigController extends Controller {

    public String getAppConfigValue(EntityManager em, String key) {
        AppConfig exst = em.find(AppConfig.class, key);
        return exst == null ? null : exst.getValue();
    }

    public List<AppConfig> getAppConfigs(EntityManager em, User user) {
        requireAdmin(em, user);
        return em.createNamedQuery("Config.selectAll").getResultList();
    }

    public AppConfig updateAppConfig(EntityManager em, User user, AppConfig config) {
        requireAdmin(em, user);
        AppConfig exst = em.find(AppConfig.class, config.getKey());
        exst.setValue(config.getValue());
        return exst;
    }
}
