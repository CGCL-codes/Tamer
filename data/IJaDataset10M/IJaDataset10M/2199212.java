package de.tudresden.inf.rn.mobilis.xmpp.beans.xhunt.model;

import java.util.HashMap;

public class AreaInfo {

    public int AreaId = -1;

    public String AreaName;

    public String AreaDescription;

    public int Version = -1;

    public HashMap<Integer, String> Tickettypes = new HashMap<Integer, String>();

    public AreaInfo() {
    }

    public AreaInfo(int areaId, String areaName, String areaDescription, int version, HashMap<Integer, String> ticketTypes) {
        this.AreaId = areaId;
        this.AreaName = areaName;
        this.AreaDescription = areaDescription;
        this.Version = version;
        this.Tickettypes = ticketTypes;
    }
}
