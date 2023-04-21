package com.expantion.metier;

import com.expantion.metier.impl.RechercheManagerImpl;

public class RechercheManagerFactoryImpl implements RechercheManagerFactory {

    private static RechercheManagerFactory _instance;

    private RechercheManager _manager;

    private RechercheManagerFactoryImpl() {
        _manager = new RechercheManagerImpl();
    }

    @Override
    public RechercheManager getManager() {
        return _manager;
    }

    /**
	 * Retourne l'instance de la factory.
	 * @return
	 */
    public static RechercheManagerFactory getInstance() {
        if (_instance == null) {
            _instance = new RechercheManagerFactoryImpl();
        }
        return _instance;
    }
}
