package org.personalsmartspace.pss_sm_session.impl;

import java.util.HashMap;
import java.util.logging.Logger;
import java.util.Date;
import org.personalsmartspace.pss_sm_api.api.pss3p.SessionID;
import org.personalsmartspace.pss_sm_composition.api.platform.CompositionID;
import org.personalsmartspace.pss_sm_composition.api.platform.CompositionPlan;
import org.personalsmartspace.pss_sm_dbc.impl.Dbc;

public final class CompositionStore {

    private HashMap<String, CompositionPlan> compositionMap;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private static CompositionStore instance = new CompositionStore();

    /**
     * private constructor to ensure that class acts as a Singleton
     */
    private CompositionStore() {
        compositionMap = new HashMap<String, CompositionPlan>();
        logger.fine(" Created at : " + new Date());
    }

    /**
     * public accessor for retrieving the CompositionStore single instance
     * 
     * @return CompositionStore
     */
    public static CompositionStore getInstance() {
        return instance;
    }

    /**
     * Add a Composition Plan to the store
     * 
     * @param compositionPlan
     */
    public void addPlan(CompositionPlan compositionPlan) {
        Dbc.require(null != compositionPlan);
        String mapKey = this.generateMapKey(compositionPlan.getCompositionId().getId());
        logger.fine("Add a Composition plan - key " + mapKey);
        compositionMap.put(mapKey, compositionPlan);
    }

    /**
     * remove a Composition Plan from the store
     * 
     * @param planID
     */
    public void removePlan(CompositionID planID) {
        Dbc.require(null != planID && planID.getId() > 0);
        String mapKey = this.generateMapKey(planID.getId());
        logger.fine("Remove a Composition plan - key " + mapKey);
        compositionMap.remove(mapKey);
    }

    /**
     * retrieve a Composition Plan from the store by the CompositionID
     * 
     * @param planID
     * @return CompositionPlan
     */
    public CompositionPlan retrievePlan(CompositionID planID) {
        Dbc.require(null != planID && planID.getId() > 0);
        String mapKey = this.generateMapKey(planID.getId());
        logger.fine("Retrieve a Composition plan - key " + mapKey);
        Object object = compositionMap.get(mapKey);
        return null != object ? (CompositionPlan) object : null;
    }

    /**
     * Retrieve a Composition Plan from the store by the SessionID. This method
     * relies on the fact that the SessionID corresponds to the CompositionID.
     * If this changes the Session Plan will need to identify its corresponding
     * Composition Plan ID.
     * 
     * @param sessionID
     * @return CompositionPlan
     */
    public CompositionPlan retrievePlan(SessionID sessionID) {
        Dbc.require(null != sessionID && sessionID.getId() > 0);
        String mapKey = this.generateMapKey(sessionID.getId());
        logger.fine("Retrieve a Composition plan by Session ID - key " + mapKey);
        Object object = compositionMap.get(mapKey);
        return null != object ? (CompositionPlan) object : null;
    }

    public int storeSize() {
        logger.fine("Store size " + compositionMap.keySet().size());
        return compositionMap.keySet().size();
    }

    /**
     * Empty the store. Use primarily for testing purposes.
     */
    public void emptyStore() {
        logger.fine("Empty store ");
        compositionMap = new HashMap();
    }

    /**
     * Generate a map key : currently uses Composition Plan ID
     * 
     * @param compositionID
     * @return
     */
    private String generateMapKey(long planID) {
        String mapKey = new Long(planID).toString();
        return mapKey;
    }
}
