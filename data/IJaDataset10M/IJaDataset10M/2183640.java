package clubmixer.server.prefs;

import clubmixer.server.IClubmixerServerProperties;
import com.slychief.clubmixer.server.library.DatabaseSettings;

/**
 * Class description
 *
 *
 * @version        0.7.5, 2009-11-24
 * @author         Slychief
 */
public class DefaultDatabaseSettings extends DatabaseSettings {

    /**
     * Constructs ...
     *
     */
    public DefaultDatabaseSettings() {
        super();
        this.setDbDriver(IClubmixerServerProperties.STANDARD_DB_DRIVER);
        this.setDbPath("jdbc:hsqldb:hsql://localhost:9001/db");
        this.setLogin(IClubmixerServerProperties.STANDARD_DB_USER);
        this.setPassword(IClubmixerServerProperties.STANDARD_DB_PASSWORD);
        this.setHibernateDialect(IClubmixerServerProperties.STANDARD_DB_DIALECT);
    }
}
