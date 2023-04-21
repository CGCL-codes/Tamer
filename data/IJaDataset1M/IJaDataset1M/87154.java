package com.techstar.dmis.dto.transfer;

import java.io.Serializable;

/**
 * Domain classe for 变电站
 * This classe is based on ValueObject Pattern
 */
public class TransMsubstationDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public TransMsubstationDto() {
    }

    private String fname;

    private String fdescription;

    private String faliasname;

    private String fmemberof_subcontrolarea;

    private String fid;

    private int version;

    /**
     * getters and setters
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFname() {
        return fname;
    }

    public void setFdescription(String fdescription) {
        this.fdescription = fdescription;
    }

    public String getFdescription() {
        return fdescription;
    }

    public void setFaliasname(String faliasname) {
        this.faliasname = faliasname;
    }

    public String getFaliasname() {
        return faliasname;
    }

    public void setFmemberof_subcontrolarea(String fmemberof_subcontrolarea) {
        this.fmemberof_subcontrolarea = fmemberof_subcontrolarea;
    }

    public String getFmemberof_subcontrolarea() {
        return fmemberof_subcontrolarea;
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
}
