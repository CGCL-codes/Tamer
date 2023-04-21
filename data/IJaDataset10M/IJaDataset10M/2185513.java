package com.sitescape.team.remoting.ws.service.zone;

public interface ZoneService {

    public Long zone_addZone(String accessToken, String zoneName, String virtualHost, String mailDomain);

    public void zone_modifyZone(String accessToken, String zoneName, String virtualHost, String mailDomain);

    public void zone_deleteZone(String accessToken, String zoneName);
}
