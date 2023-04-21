package org.perfectday.logicengine.core.industry;

import java.io.File;
import java.util.Set;
import org.perfectday.logicengine.core.configuration.Configuration;

/**
 *
 * @author Miguel Angel Lopez Montellano ( alakat@gmail.com )
 */
public class CombatKeepFactory extends IndexFactory {

    private static CombatKeepFactory instance;

    private CombatKeepFactory(File f) {
        super(f, false);
    }

    public static CombatKeepFactory getInstance() {
        if (instance == null) instance = new CombatKeepFactory(Configuration.getInstance().getCombatKeepFile());
        return instance;
    }

    @Override
    public Object create(String key) throws Exception {
        return ((Class) this.database.get(key)).getConstructor().newInstance();
    }
}
