package com.hk.bean;

import com.hk.frame.dao.annotation.Column;
import com.hk.frame.dao.annotation.Id;
import com.hk.frame.dao.annotation.Table;

/**
 * 企业特殊服务配置
 * 
 * @author akwei
 */
@Table(name = "cmpsvrcnf")
public class CmpSvrCnf {

    public static final byte FLG_N = 0;

    public static final byte FLG_Y = 1;

    @Id
    private long companyId;

    /**
	 * 文件系统服务开关
	 */
    @Column
    private byte fileflg;

    /**
	 * 视频系统服务开关
	 */
    @Column
    private byte videoflg;

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public byte getFileflg() {
        return fileflg;
    }

    public void setFileflg(byte fileflg) {
        this.fileflg = fileflg;
    }

    public byte getVideoflg() {
        return videoflg;
    }

    public void setVideoflg(byte videoflg) {
        this.videoflg = videoflg;
    }

    public boolean isOpenFile() {
        if (this.fileflg == FLG_Y) {
            return true;
        }
        return false;
    }

    public boolean isOpenVideo() {
        if (this.videoflg == FLG_Y) {
            return true;
        }
        return false;
    }
}
