package com.techstar.dmis.dto.transfer;

import java.io.Serializable;

/**
 * Domain classe for GPS设备
 * This classe is based on ValueObject Pattern
 */
public class TransParaGPSDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public TransParaGPSDto() {
    }

    private String fremark;

    private String fid;

    private int version;

    private String trans_zparagps1;

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

    public void setTrans_zparagps1(String trans_zparagps1) {
        this.trans_zparagps1 = trans_zparagps1;
    }

    public String getTrans_zparagps1() {
        return trans_zparagps1;
    }
}
