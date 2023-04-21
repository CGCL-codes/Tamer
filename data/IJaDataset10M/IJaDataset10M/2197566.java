package org.osmius.model.aux;

public class SlaPrcs {

    private String identifier;

    private Float prcOK;

    private Float prcAva;

    private Float prcMn;

    private Long MTBF;

    private Long MTTRM;

    private String dtiIni;

    private String dtiFin;

    private String timeLeft;

    public SlaPrcs(String indSla, Float prcOK, Float prcAva, Float prcMn, Long MTBF, Long MTTRM, String dtiIni, String dtiFin, String timeLeft) {
        this.identifier = indSla;
        this.prcOK = prcOK;
        this.prcAva = prcAva;
        this.prcMn = prcMn;
        this.MTBF = MTBF;
        this.MTTRM = MTTRM;
        this.dtiIni = dtiIni;
        this.dtiFin = dtiFin;
        this.timeLeft = timeLeft;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Float getPrcOK() {
        return prcOK;
    }

    public void setPrcOK(Float prcOK) {
        this.prcOK = prcOK;
    }

    public Float getPrcAva() {
        return prcAva;
    }

    public void setPrcAva(Float prcAva) {
        this.prcAva = prcAva;
    }

    public Float getPrcMn() {
        return prcMn;
    }

    public void setPrcMn(Float prcMn) {
        this.prcMn = prcMn;
    }

    public Long getMTBF() {
        return MTBF;
    }

    public void setMTBF(Long MTBF) {
        this.MTBF = MTBF;
    }

    public Long getMTTRM() {
        return MTTRM;
    }

    public void setMTTRM(Long MTTRM) {
        this.MTTRM = MTTRM;
    }

    public String getDtiIni() {
        return dtiIni;
    }

    public void setDtiIni(String dtiIni) {
        this.dtiIni = dtiIni;
    }

    public String getDtiFin() {
        return dtiFin;
    }

    public void setDtiFin(String dtiFin) {
        this.dtiFin = dtiFin;
    }

    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }
}
