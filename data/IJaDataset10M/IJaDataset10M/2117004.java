package org.smartweb.fiveaside.persistence;

import org.smartweb.fiveaside.business.Player;
import net.smartlab.web.BusinessObjectFactory;

/**
 * @author gperrone
 * 
 */
public class PlayerFactory extends BusinessObjectFactory {

    private static PlayerFactory instance;

    public Class getMappedClass() {
        return Player.class;
    }

    private PlayerFactory() {
        super();
    }

    public static PlayerFactory getInstance() {
        if (instance == null) {
            instance = new PlayerFactory();
        }
        return instance;
    }
}
