package com.corratech.opensuite.componentservices.impl;

import org.apache.log4j.Logger;
import com.corratech.opensuite.componentservices.api.KnowledgeTreeComponentService;

/**
 * @author aleksandr.kryzhak
 *
 */
public class KnowledgeTreeComponentServiceImpl implements KnowledgeTreeComponentService {

    private static final Logger log = Logger.getLogger(KnowledgeTreeComponentServiceImpl.class);

    private boolean isAvailable = false;

    /**
	 * 
	 */
    protected KnowledgeTreeComponentServiceImpl() {
    }

    /**
	 * @return the isAvailable
	 */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
	 * @param isAvailable the isAvailable to set
	 */
    protected void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
