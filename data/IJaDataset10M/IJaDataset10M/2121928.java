package org.safu.tools.Blocker;

import org.apache.log4j.Logger;
import org.safu.bean.FraudEntity;
import org.safu.bean.FraudResponse;
import org.safu.bean.Score;
import org.safu.bean.TransactionData;
import org.safu.config.Configuration;
import org.safu.config.blocker.BlockerConfiguration;
import org.safu.config.blocker.BlockerConfigurator;
import org.safu.tools.FraudScreener;

/**
 * 
 * 
 * @author ybozcaada
 * 
 */
public class Blocker implements FraudScreener {

    private FraudEntity blockable;

    private static Logger log = Logger.getLogger(Blocker.class);

    private BlockerConfiguration blockerConfig = null;

    public Blocker(FraudEntity blockable) {
        this.blockable = blockable;
        blockerConfig = BlockerConfigurator.getConfiguration(blockable);
        if (blockerConfig == null) {
            log.warn("NO BLOCKER CONFIG DEFINED FOR A " + blockable.getClass().getName());
        }
    }

    public Configuration getConfiguration() {
        return blockerConfig;
    }

    public FraudResponse screen(TransactionData data) {
        log.trace("Blocker Name: " + blockerConfig.getConfigName() + " ,screening.");
        FraudResponse response = new FraudResponse();
        FraudEntity entity = data.getEntity(blockable);
        response.setEntityScreened(entity);
        response.setScreener(this);
        response.setScreenerName(blockerConfig.getConfigName());
        if (entity == null) {
            log.debug("transaction did not contain blockable: " + blockable.getClass().getName());
            response.setAccepted(true);
            response.setScore(Score.ACCEPT);
        } else {
            if (inBlockList(entity)) {
                response.setScore(Score.REJECT);
                response.setAccepted(false);
                response.setFraudMsg("Entity: " + entity + " is blocked");
            } else {
                response.setScore(Score.ACCEPT);
                response.setAccepted(true);
            }
        }
        return response;
    }

    public FraudEntity getBlockable() {
        return blockable;
    }

    private boolean inBlockList(FraudEntity entity) {
        boolean listed = false;
        if (blockerConfig == null) {
            log.warn("There is no configuration for " + blockable.getClass().getName());
        } else {
            try {
                if (blockerConfig.getSearchEntity() != null) {
                    String result = null;
                    Class partTypes[] = new Class[0];
                    Object arglist[] = new Object[0];
                    result = (String) blockerConfig.getSearchEntity().invoke(entity, arglist);
                }
            } catch (Exception e) {
                log.error(e);
            }
        }
        return listed;
    }
}
