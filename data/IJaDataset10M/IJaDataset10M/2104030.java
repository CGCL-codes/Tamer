package org.atlantal.impl.cms.display.app;

import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.atlantal.api.app.exception.ServiceException;
import org.atlantal.api.cms.app.source.ContentSourceService;
import org.atlantal.api.cms.display.Interface;
import org.atlantal.api.cms.display.InterfaceChild;
import org.atlantal.api.cms.display.app.DisplayService;
import org.atlantal.api.cms.display.app.InterfaceService;
import org.atlantal.api.cms.exception.DisplayException;
import org.atlantal.impl.app.service.AbstractService;
import org.atlantal.utils.UpdateListener;
import org.atlantal.utils.wrapper.ObjectWrapper;

/**
 * <p>Titre : Ewaloo</p>
 * <p>Description : Moteur de recherche structur�</p>
 * <p>Copyright : Copyright (c) 2002</p>
 * <p>Soci�t� : Mably Multim�dia</p>
 * @author Fran�ois MASUREL
 * @version 1.0
 */
public abstract class InterfaceServiceInstance extends AbstractService implements UpdateListener, InterfaceService {

    private static Logger logger = Logger.getLogger(InterfaceServiceInstance.class);

    static {
        logger.setLevel(Level.INFO);
    }

    private ContentSourceService contentsourceservice;

    private DisplayService displayservice;

    /**
     * {@inheritDoc}
     */
    public void init(Map params) throws ServiceException {
        super.init(params);
        String cntsrcstr = (String) params.get("template");
        contentsourceservice = (ContentSourceService) getService(cntsrcstr);
        String dispservstr = (String) params.get("display");
        displayservice = (DisplayService) getService(dispservstr);
        reload();
    }

    /**
     * {@inheritDoc}
     */
    public void reload() {
    }

    /**
     * {@inheritDoc}
     */
    public ObjectWrapper getInterface(int id) throws DisplayException {
        if (id > 0) {
            return getInterface(Integer.valueOf(id));
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public abstract ObjectWrapper getEnvironment(Integer id) throws DisplayException;

    /**
     * {@inheritDoc}
     */
    public abstract ObjectWrapper getInterface(Integer id) throws DisplayException;

    /**
     * {@inheritDoc}
     */
    public Interface getInterfaceObject(Integer id) throws DisplayException {
        return (Interface) getInterface(id).getWrappedObject();
    }

    /**
     * {@inheritDoc}
     */
    public ObjectWrapper getInterfaceChild(int id) throws DisplayException {
        if (id > 0) {
            return getInterfaceChild(Integer.valueOf(id));
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public abstract ObjectWrapper getInterfaceChild(Integer id) throws DisplayException;

    /**
     * {@inheritDoc}
     */
    public InterfaceChild getInterfaceChildObject(Integer id) throws DisplayException {
        return (InterfaceChild) getInterfaceChild(id).getWrappedObject();
    }

    /**
     * {@inheritDoc}
     */
    public DisplayService getDisplayService() {
        return displayservice;
    }

    /**
     * {@inheritDoc}
     */
    public void setDisplayService(DisplayService service) {
        displayservice = service;
    }

    /**
     * {@inheritDoc}
     */
    public ContentSourceService getContentSourceService() {
        return contentsourceservice;
    }

    /**
     * {@inheritDoc}
     */
    public void setContentSourceService(ContentSourceService service) {
        contentsourceservice = service;
    }
}
