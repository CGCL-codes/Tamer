package com.laoer.bbscs.service.imp;

import java.util.*;
import com.laoer.bbscs.bean.*;
import com.laoer.bbscs.dao.*;
import com.laoer.bbscs.exception.*;
import com.laoer.bbscs.service.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * <p>Title: Tianyi BBS</p>
 *
 * <p>Description: BBSCS</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Laoer.com</p>
 *
 * @author Gong Tianyi
 * @version 7.0
 */
public class ConfigServiceImp implements ConfigService {

    private static final Log logger = LogFactory.getLog(ConfigServiceImp.class);

    private ConfigDAO configDAO;

    public ConfigServiceImp() {
    }

    /**
   *
   * @param id String
   * @return Config
   * @todo Implement this com.laoer.bbscs.service.ConfigService method
   */
    public Config findConfigByID(String id) {
        return this.getConfigDAO().findConfigByID(id);
    }

    /**
   *
   * @return List
   * @todo Implement this com.laoer.bbscs.service.ConfigService method
   */
    public List findConfigs() {
        return this.getConfigDAO().findConfigs();
    }

    /**
   *
   * @param config Config
   * @return Config
   * @throws BbscsException
   * @todo Implement this com.laoer.bbscs.service.ConfigService method
   */
    public Config updateConfig(Config config) throws BbscsException {
        try {
            return this.getConfigDAO().updateConfig(config);
        } catch (Exception ex) {
            logger.error(ex);
            throw new BbscsException(ex);
        }
    }

    public void updateAllConfigs(HashMap configs) throws BbscsException {
        Iterator it = configs.values().iterator();
        try {
            while (it.hasNext()) {
                Config config = (Config) it.next();
                this.getConfigDAO().updateConfig(config);
            }
        } catch (Exception ex) {
            logger.error(ex);
            throw new BbscsException(ex);
        }
    }

    public ConfigDAO getConfigDAO() {
        return configDAO;
    }

    public void setConfigDAO(ConfigDAO configDAO) {
        this.configDAO = configDAO;
    }
}
