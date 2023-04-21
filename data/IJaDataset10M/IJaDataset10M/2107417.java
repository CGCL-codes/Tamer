package com.ewaloo.impl.cms.app.definition;

import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.atlantal.api.app.exception.ServiceException;
import org.atlantal.api.cms.app.source.ContentSourceService;
import org.atlantal.impl.cms.app.definition.AbstractContentDefinitionService;
import com.ewaloo.api.cms.app.source.EwContentSourceService;

/**
 * <p>Titre : Ewaloo</p>
 * <p>Description : Moteur de recherche structur�</p>
 * <p>Copyright : Copyright (c) 2002</p>
 * <p>Soci�t� : Mably Multim�dia</p>
 * @author Fran�ois MASUREL
 * @version 1.0
 */
public abstract class AbstractEwContentDefinitionService extends AbstractContentDefinitionService {

    private static final Logger LOGGER = Logger.getLogger(AbstractEwContentDefinitionService.class);

    static {
        LOGGER.setLevel(Level.INFO);
    }

    private EwContentSourceService sourceService;

    /**
     * init
     * {@inheritDoc}
     */
    public void init(Map params) throws ServiceException {
        super.init(params);
        String sourcestr = (String) params.get("source");
        sourceService = (EwContentSourceService) this.getService(sourcestr);
    }

    /**
     * {@inheritDoc}
     */
    public ContentSourceService getContentSourceService() {
        return sourceService;
    }
}
