package com.uk.ui.fatura;

import java.io.Serializable;

public class TarifaFatura implements Serializable {

    private Integer id;

    private String kodi;

    private String pershkrimi;

    private String njesia;

    private Double sasia;

    private Double cmimi;

    private boolean aplikoTvsh;

    private double tvsh;

    private Double vlera;

    private String aplikoTvshString;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKodi() {
        return kodi;
    }

    public void setKodi(String kodi) {
        this.kodi = kodi;
    }

    public String getPershkrimi() {
        return pershkrimi;
    }

    public void setPershkrimi(String pershkrimi) {
        this.pershkrimi = pershkrimi;
    }

    public String getNjesia() {
        return njesia;
    }

    public void setNjesia(String njesia) {
        this.njesia = njesia;
    }

    public Double getSasia() {
        return sasia;
    }

    public void setSasia(Double sasia) {
        this.sasia = sasia;
    }

    public Double getCmimi() {
        return cmimi;
    }

    public void setCmimi(Double cmimi) {
        this.cmimi = cmimi;
    }

    public boolean isAplikoTvsh() {
        return aplikoTvsh;
    }

    public void setAplikoTvsh(boolean aplikoTvsh) {
        this.aplikoTvsh = aplikoTvsh;
    }

    public Double getVlera() {
        return vlera;
    }

    public void setVlera(Double vlera) {
        this.vlera = vlera;
    }

    public String getAplikoTvshString() {
        if (this.isAplikoTvsh()) return "PO"; else return "JO";
    }

    public void setAplikoTvshString(String aplikoTvshString) {
        this.aplikoTvshString = aplikoTvshString;
    }

    public double getTvsh() {
        if (this.aplikoTvsh) {
            this.tvsh = (this.sasia * this.cmimi) * 20 / 100;
        } else this.tvsh = 0;
        return tvsh;
    }

    public void setTvsh(double tvsh) {
        this.tvsh = tvsh;
    }
}
