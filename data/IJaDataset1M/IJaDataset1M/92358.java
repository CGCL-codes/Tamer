package es.devel.opentrats.model.common;

import java.io.Serializable;

public class PropertiesBean implements Serializable {

    private static final long serialVersionUID = -698010436427366641L;

    private String tempFolder;

    private Float clubLoadRatio;

    public PropertiesBean() {
    }

    public String getTempFolder() {
        return tempFolder;
    }

    public void setTempFolder(String tempFolder) {
        this.tempFolder = tempFolder;
    }

    public Float getClubLoadRatio() {
        return clubLoadRatio;
    }

    public void setClubLoadRatio(Float clubLoadRatio) {
        this.clubLoadRatio = clubLoadRatio;
    }
}
