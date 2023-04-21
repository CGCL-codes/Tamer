package org.fao.fenix.web.client.vo;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FpiGaulVo implements IsSerializable {

    private int gid;

    private long feature_code;

    private long adm0_code;

    private String adm0_name;

    private String adm1_name;

    private long last_updat;

    private String continent;

    private String region;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public long getFeature_code() {
        return feature_code;
    }

    public void setFeature_code(long feature_code) {
        this.feature_code = feature_code;
    }

    public long getAdm0_code() {
        return adm0_code;
    }

    public void setAdm0_code(long adm0_code) {
        this.adm0_code = adm0_code;
    }

    public String getAdm0_name() {
        return adm0_name;
    }

    public void setAdm0_name(String adm0_name) {
        this.adm0_name = adm0_name;
    }

    public String getAdm1_name() {
        return adm1_name;
    }

    public void setAdm1_name(String adm1_name) {
        this.adm1_name = adm1_name;
    }

    public long getLast_updat() {
        return last_updat;
    }

    public void setLast_updat(long last_updat) {
        this.last_updat = last_updat;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
