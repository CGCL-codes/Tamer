package net.conselldemallorca.ad.util;

import java.util.Properties;
import org.springframework.core.io.Resource;

/**
 * Emmagatzema les propietats globals de l'aplicació
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class GlobalProperties extends Properties {

    private static GlobalProperties instance = null;

    public static GlobalProperties getInstance() {
        return instance;
    }

    public GlobalProperties(Resource resource) throws Exception {
        super();
        try {
            super.load(resource.getInputStream());
            if (instance == null) {
                instance = this;
            }
        } catch (Exception ex) {
            throw new Exception("No s'han pogut carregar les propietats globals de l'aplicació");
        }
    }

    private static final long serialVersionUID = 1L;
}
