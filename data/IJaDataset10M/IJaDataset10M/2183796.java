package org.osmius.model;

import java.util.Date;

public class OsmAdvertise extends BaseObject implements java.io.Serializable {

    private Long idnAdvertise;

    private Integer indRegistered;

    private Date dtiExpiration;

    private String txtContent;

    private String idnLanguage;

    public OsmAdvertise() {
    }

    public OsmAdvertise(Long idnAdvertise, Integer indRegistered, Date dtiExpiration, String txtContent, String idnLanguage) {
        this.idnAdvertise = idnAdvertise;
        this.indRegistered = indRegistered;
        this.dtiExpiration = dtiExpiration;
        this.txtContent = txtContent;
        this.idnLanguage = idnLanguage;
    }

    public Long getIdnAdvertise() {
        return idnAdvertise;
    }

    public void setIdnAdvertise(Long idnAdvertise) {
        this.idnAdvertise = idnAdvertise;
    }

    public Integer getIndRegistered() {
        return indRegistered;
    }

    public void setIndRegistered(Integer indRegistered) {
        this.indRegistered = indRegistered;
    }

    public Date getDtiExpiration() {
        return dtiExpiration;
    }

    public void setDtiExpiration(Date dtiExpiration) {
        this.dtiExpiration = dtiExpiration;
    }

    public String getTxtContent() {
        return txtContent;
    }

    public void setTxtContent(String txtContent) {
        this.txtContent = txtContent;
    }

    public String getIdnLanguage() {
        return idnLanguage;
    }

    public void setIdnLanguage(String idnLanguage) {
        this.idnLanguage = idnLanguage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OsmAdvertise that = (OsmAdvertise) o;
        if (!dtiExpiration.equals(that.dtiExpiration)) return false;
        if (!idnAdvertise.equals(that.idnAdvertise)) return false;
        if (!idnLanguage.equals(that.idnLanguage)) return false;
        if (!indRegistered.equals(that.indRegistered)) return false;
        if (txtContent != null ? !txtContent.equals(that.txtContent) : that.txtContent != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = idnAdvertise.hashCode();
        result = 31 * result + indRegistered.hashCode();
        result = 31 * result + dtiExpiration.hashCode();
        result = 31 * result + (txtContent != null ? txtContent.hashCode() : 0);
        result = 31 * result + idnLanguage.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "OsmAdvertise{" + "idnAdvertise=" + idnAdvertise + ", indRegistered=" + indRegistered + ", dtiExpiration=" + dtiExpiration + ", txtContent='" + txtContent + '\'' + ", idnLanguage='" + idnLanguage + '\'' + '}';
    }
}
