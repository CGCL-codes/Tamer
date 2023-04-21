package fi.hip.gb.gridbank.server;

import java.util.Properties;
import fi.hip.gb.gridbank.db.SQL.MySqlSettings;

public class ServerSettings extends Properties {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public ServerSettings(Properties prop, boolean useSQL) throws Exception {
        super(prop);
        if (useSQL) {
            MySqlSettings.setSettings(prop);
        }
        ServerCryptoProps.setCryptoProps(prop);
    }

    /**
	 * 
	 * @return
	 */
    public MySqlSettings getDBSettings() {
        return MySqlSettings.getSettings();
    }

    public ServerCryptoProps getCryptoProps() throws Exception {
        return ServerCryptoProps.getCryptoProps();
    }
}
