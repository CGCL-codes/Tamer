package org.atlantal.impl.portal.app.ptl;

import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.atlantal.api.app.exception.ServiceException;
import org.atlantal.api.portal.app.ptl.PortletManager;
import javax.portlet.PortletException;
import org.atlantal.impl.app.service.AbstractService;
import org.atlantal.utils.UpdateListener;
import org.atlantal.utils.UpdatedEvent;
import org.atlantal.utils.wrapper.ObjectWrapper;

/**
 * Titre :        Atlantal Framework
 * Description :
 * Copyright :    Copyright (c) 2002
 * Soci�t� :      Mably Multimedia
 * @author <a href="mailto:masurel@mably.com">Francois MASUREL</a>
 * @version 1.0
 */
public abstract class AbstractPortletManager extends AbstractService implements PortletManager, UpdateListener {

    private static final Logger LOGGER = Logger.getLogger(AbstractPortletManager.class);

    static {
        LOGGER.setLevel(Level.INFO);
    }

    private String userresource;

    private String cache;

    /**
     * Constructeur
     */
    public AbstractPortletManager() {
    }

    /**
     * Init
     * {@inheritDoc}
     */
    public void init(Map params) throws ServiceException {
        super.init(params);
        this.userresource = (String) params.get("userresource");
        this.cache = (String) params.get("cache");
        if (this.cache == null) {
            this.cache = "Cache";
        }
    }

    /**
     * getPortletWrapper
     * @param id PortletInstance Id
     * @return PortletWrapper
     * @throws PortletException PortletException
     */
    public abstract ObjectWrapper getPortletWrapper(Object id) throws PortletException;

    /**
     * getCconfig
     * @return config
     */
    public String getConfig() {
        return "config";
    }

    /**
     * getUserResource
     * @return String
     */
    public String getUserResource() {
        return this.userresource;
    }

    /**
     * getCache
     * @return cache
     */
    public String getCache() {
        return this.cache;
    }

    /**
     * update
     * {@inheritDoc}
     */
    public void update(UpdatedEvent event) {
    }
}
