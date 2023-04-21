package org.polepos.teams.jpa;

import org.polepos.framework.RdbmsSettings;

/**
 * @author Christian Ernst
 */
public class JpaSettings extends RdbmsSettings {

    private static final String KEY_JPA = "jpa";

    private static final String KEY_ENHANCE = "enhance";

    private static final String KEY_ENHANCER = "enhancer";

    private static final String KEY_CONNECTURL = "javax.jdo.option.ConnectionURL";

    private static final String FILENAME = "settings/Jpa.properties";

    public JpaSettings() {
        super(FILENAME);
    }

    public String[] getJpaImplementations() {
        return getArray(KEY_JPA);
    }

    public String getConnectUrl() {
        return get(KEY_CONNECTURL);
    }

    public boolean enhance() {
        return getBoolean(KEY_ENHANCE);
    }

    public String enhancer() {
        return get(KEY_ENHANCER);
    }
}
