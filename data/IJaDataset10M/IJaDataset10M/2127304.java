package org.ibit.avanthotel.offer.web.delegate;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import org.apache.log4j.Category;
import org.ibit.j2eeUtils.jndi.ServiceLocatorException;
import org.ibit.avanthotel.offer.web.util.OfferWebServiceLocator;
import org.ibit.avanthotel.persistence.logic.interfaces.facade.StandarFacade;
import org.ibit.avanthotel.persistence.logic.util.DelegateSysException;

/**
 * @created 19 / novembre / 2003
 */
public class StandarDelegate {

    /** */
    static Category logger = Category.getInstance(StandarDelegate.class);

    private static StandarDelegate instance;

    private StandarFacade standarFacade;

    /**
    *Construeix una nova instancia de StandarDelegate
    *
    * @throws DelegateSysException
    */
    private StandarDelegate() throws DelegateSysException {
        try {
            standarFacade = OfferWebServiceLocator.getStandarFacade();
        } catch (CreateException e) {
            throw new DelegateSysException(e);
        } catch (RemoteException e) {
            throw new DelegateSysException(e);
        } catch (ServiceLocatorException e) {
            throw new DelegateSysException(e);
        }
    }

    /**
    * retorna el valor per la propietat instace
    *
    * @return valor de instace
    * @exception DelegateSysException
    */
    public static synchronized StandarDelegate getInstace() throws DelegateSysException {
        if (instance == null) {
            instance = new StandarDelegate();
        }
        return instance;
    }

    /**
    * @param lang
    * @return coleccio de TraduccioReferentDistanciaData ( id, nom )
    * @exception DelegateSysException
    */
    public Collection getIdiomes(String lang) throws DelegateSysException {
        try {
            return standarFacade.getIdiomes(lang);
        } catch (FinderException e) {
            throw new DelegateSysException("idiomes no existeixen");
        } catch (RemoteException e) {
            throw new DelegateSysException(e);
        }
    }

    /**
    * @param lang
    * @return coleccio de BasicData ( id, nom )
    * @exception DelegateSysException
    */
    public Collection getTipusTuristes(String lang) throws DelegateSysException {
        try {
            return standarFacade.getTipusTurista(lang);
        } catch (FinderException e) {
            throw new DelegateSysException("idiomes no existeixen");
        } catch (RemoteException e) {
            throw new DelegateSysException(e);
        }
    }

    /**
    * @return coleccio de BasicData ( id, nom )
    * @exception DelegateSysException
    */
    public Map getDiccionariHabitacions() throws DelegateSysException {
        try {
            return standarFacade.getTraduccionsDiccionariHabitacions();
        } catch (FinderException e) {
            throw new DelegateSysException("getDiccionariHabitacionsDelegate");
        } catch (RemoteException e) {
            throw new DelegateSysException(e);
        }
    }
}
