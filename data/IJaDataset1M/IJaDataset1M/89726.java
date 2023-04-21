package com.techstar.dmis.dto;

import java.io.Serializable;
import com.techstar.framework.service.dto.DictionaryBaseDto;

/**
 * Domain classe for 防火墙
 * This classe is based on ValueObject Pattern
 * @author 
 * @date
 */
public class ParaFirewallDto implements Serializable {

    public ParaFirewallDto() {
    }

    private String fremark;

    private String fid;

    private int version;

    private com.techstar.dmis.dto.EtsEquipmentDto zparafirewall1;

    /**
     * getters and setters
     */
    public void setFremark(String fremark) {
        this.fremark = fremark;
    }

    public String getFremark() {
        return fremark;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFid() {
        return fid;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setZparafirewall1(com.techstar.dmis.dto.EtsEquipmentDto zparafirewall1) {
        this.zparafirewall1 = zparafirewall1;
    }

    public com.techstar.dmis.dto.EtsEquipmentDto getZparafirewall1() {
        return zparafirewall1;
    }
}
