package ch.bfh.egov.internetapps.tos;

import java.io.Serializable;

/**
 * Transfer Object f�r Module
 * 
 * @author Kompetenzzentrum E-Business, Simon Bergamin
 */
public class Modul implements Serializable {

    private static final long serialVersionUID = -2259696911489547349L;

    private Integer modulId;

    private Integer mandantId;

    private String name;

    private Integer punkte;

    private Integer branchensichtId;

    private Integer geplant;

    public Modul() {
    }

    public Integer getModulId() {
        return modulId;
    }

    public void setModulId(Integer modulId) {
        this.modulId = modulId;
    }

    public Integer getMandantId() {
        return mandantId;
    }

    public void setMandantId(Integer mandantId) {
        this.mandantId = mandantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPunkte() {
        return punkte;
    }

    public void setPunkte(Integer punkte) {
        this.punkte = punkte;
    }

    public Integer getBranchensichtId() {
        return branchensichtId;
    }

    public void setBranchensichtId(Integer branchensichtId) {
        this.branchensichtId = branchensichtId;
    }

    public Integer getGeplant() {
        return geplant;
    }

    public void setGeplant(Integer geplant) {
        this.geplant = geplant;
    }

    public boolean equals(Object o) {
        Modul m = (Modul) o;
        if (this.modulId.equals(m.getModulId()) && this.mandantId.equals(m.getMandantId()) && this.name.equals(m.getName()) && this.punkte.equals(m.getPunkte())) return true;
        return false;
    }
}
