package ch.bfh.egov.internetapps.tos;

import java.io.Serializable;

/**
 * Transfer Object f�r Customizings.
 * 
 * @author Kompetenzzentrum E-Business, Simon Bergamin
 */
public class Customizing implements Serializable {

    static final long serialVersionUID = -309972355751606573L;

    private Integer customizingId;

    private Integer mandantId;

    private String name;

    private Long naUID;

    private Long opNuUID;

    private Boolean status;

    private Boolean naStatus;

    private Boolean opNuStatus;

    private Integer anzahlNaResultate;

    private Integer anzahlOpNuResultate;

    public Customizing() {
    }

    public Integer getCustomizingId() {
        return customizingId;
    }

    public void setCustomizingId(Integer customizingId) {
        this.customizingId = customizingId;
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

    public Long getNaUID() {
        return naUID;
    }

    public void setNaUID(Long naUID) {
        this.naUID = naUID;
    }

    public Long getOpNuUID() {
        return opNuUID;
    }

    public void setOpNuUID(Long opNuUID) {
        this.opNuUID = opNuUID;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getNaStatus() {
        return naStatus;
    }

    public void setNaStatus(Boolean naStatus) {
        this.naStatus = naStatus;
    }

    public Boolean getOpNuStatus() {
        return opNuStatus;
    }

    public void setOpNuStatus(Boolean opNuStatus) {
        this.opNuStatus = opNuStatus;
    }

    public Integer getAnzahlNaResultate() {
        return anzahlNaResultate;
    }

    public void setAnzahlNaResultate(Integer anzahlNaResultate) {
        this.anzahlNaResultate = anzahlNaResultate;
    }

    public Integer getAnzahlOpNuResultate() {
        return anzahlOpNuResultate;
    }

    public void setAnzahlOpNuResultate(Integer anzahlOpNuResultate) {
        this.anzahlOpNuResultate = anzahlOpNuResultate;
    }
}
