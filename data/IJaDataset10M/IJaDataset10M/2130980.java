package server.mwmysql;

import java.sql.Connection;
import server.campaign.CampaignMain;

public class JDBCConnectionHandler {

    public Connection getConnection() {
        return CampaignMain.cm.MySQL.getConnection();
    }

    public void returnConnection(Connection c) {
        CampaignMain.cm.MySQL.returnConnection(c);
    }

    public JDBCConnectionHandler() {
    }
}
