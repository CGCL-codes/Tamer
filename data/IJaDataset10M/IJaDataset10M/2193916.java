package com.techstar.dmis.dto;

import java.io.Serializable;
import com.techstar.framework.service.dto.DictionaryBaseDto;

/**
 * Domain classe for 故障录波器台帐
 * This classe is based on ValueObject Pattern
 * @author 
 * @date
 */
public class ParaFaullistDto implements Serializable {

    public ParaFaullistDto() {
    }

    private String fprotectedequip;

    private java.sql.Date fverifydate;

    private java.sql.Date fdrivedate;

    private String fisinuse;

    private String fdispatchauthority;

    private String fmaterialcodify;

    private String fremark;

    private String feqpid;

    private int version;

    private com.techstar.dmis.dto.EtsEquipmentDto zparafaullist2;

    /**
     * getters and setters
     */
    public void setFprotectedequip(String fprotectedequip) {
        this.fprotectedequip = fprotectedequip;
    }

    public String getFprotectedequip() {
        return fprotectedequip;
    }

    public void setFverifydate(java.sql.Date fverifydate) {
        this.fverifydate = fverifydate;
    }

    public java.sql.Date getFverifydate() {
        return fverifydate;
    }

    public void setFdrivedate(java.sql.Date fdrivedate) {
        this.fdrivedate = fdrivedate;
    }

    public java.sql.Date getFdrivedate() {
        return fdrivedate;
    }

    public void setFisinuse(String fisinuse) {
        this.fisinuse = fisinuse;
    }

    public String getFisinuse() {
        return fisinuse;
    }

    public void setFdispatchauthority(String fdispatchauthority) {
        this.fdispatchauthority = fdispatchauthority;
    }

    public String getFdispatchauthority() {
        return fdispatchauthority;
    }

    public void setFmaterialcodify(String fmaterialcodify) {
        this.fmaterialcodify = fmaterialcodify;
    }

    public String getFmaterialcodify() {
        return fmaterialcodify;
    }

    public void setFremark(String fremark) {
        this.fremark = fremark;
    }

    public String getFremark() {
        return fremark;
    }

    public void setFeqpid(String feqpid) {
        this.feqpid = feqpid;
    }

    public String getFeqpid() {
        return feqpid;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setZparafaullist2(com.techstar.dmis.dto.EtsEquipmentDto zparafaullist2) {
        this.zparafaullist2 = zparafaullist2;
    }

    public com.techstar.dmis.dto.EtsEquipmentDto getZparafaullist2() {
        return zparafaullist2;
    }
}
