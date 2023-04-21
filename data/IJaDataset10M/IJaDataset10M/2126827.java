package org.ael.battlenet.wow.realm;

import java.util.ArrayList;
import java.util.List;
import org.ael.battlenet.ServerZone;
import org.apache.commons.lang.StringUtils;

public class RealmStatusQuery {

    private ServerZone serverZone = ServerZone.UnitedStates;

    private List<String> serverList = new ArrayList<String>();

    public RealmStatusQuery() {
    }

    public RealmStatusQuery(ServerZone serverZone) {
        this.serverZone = serverZone;
    }

    public void setServerZone(ServerZone serverZone) {
        this.serverZone = serverZone;
    }

    public void addServer(String server) {
        if (!StringUtils.isEmpty(server)) {
            serverList.add(StringUtils.trim(server));
        }
    }

    public String getUrl() {
        StringBuffer sb = new StringBuffer();
        sb.append("http://").append(serverZone.getUrlPart()).append(".battle.net/api/wow/realm/status");
        if (!serverList.isEmpty()) {
            sb.append("?realms=");
            for (String s : serverList) {
                sb.append(s).append(',');
            }
        }
        return sb.toString();
    }
}
