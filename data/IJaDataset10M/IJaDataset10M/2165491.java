package com.synweb.client.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Land_DTO implements IsSerializable {

    private int landId;

    private String bLand;

    private String land;

    public Land_DTO() {
    }

    public String getBLand() {
        return bLand;
    }

    public void setBLand(String land) {
        bLand = land;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public int getLandId() {
        return landId;
    }

    public void setLandId(int landId) {
        this.landId = landId;
    }
}
