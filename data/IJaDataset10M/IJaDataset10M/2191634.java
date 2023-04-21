package com.hk.bean;

import java.util.Map;
import com.hk.frame.dao.annotation.Column;
import com.hk.frame.dao.annotation.Id;
import com.hk.frame.dao.annotation.Table;
import com.hk.frame.util.DataUtil;
import com.hk.svr.pub.Err;

/**
 * 足迹的其他设置
 * 
 * @author akwei
 */
@Table(name = "cmpotherinfo")
public class CmpOtherInfo {

    public static final byte ADCLOSE_N = 0;

    public static final byte ADCLOSE_Y = 1;

    @Id
    private long companyId;

    /**
	 * 营业时间,json格式 b:表示开始时间,e:表示结束时间
	 */
    @Column
    private String durationdata;

    /**
	 * 用户预约频率，每个用户每天预约次数
	 */
    @Column
    private int svrrate;

    @Column
    private byte adclose;

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getDurationdata() {
        return durationdata;
    }

    public void setDurationdata(String durationdata) {
        this.durationdata = durationdata;
    }

    public int getSvrrate() {
        return svrrate;
    }

    public void setSvrrate(int svrrate) {
        this.svrrate = svrrate;
    }

    public String getBeginTime() {
        if (this.durationdata != null) {
            Map<String, String> map = DataUtil.getMapFromJson(this.durationdata);
            return map.get("b");
        }
        return null;
    }

    public String getEndTime() {
        if (this.durationdata != null) {
            Map<String, String> map = DataUtil.getMapFromJson(this.durationdata);
            return map.get("e");
        }
        return null;
    }

    public int validate() {
        if (this.getEndTime() != null && this.getBeginTime() != null) {
            if (!this.getBeginTime().endsWith("00") && !this.getBeginTime().endsWith("30")) {
                return Err.CMPOTHERINFO_DURATION_ERROR;
            }
            if (!this.getEndTime().endsWith("00") && !this.getEndTime().endsWith("30")) {
                return Err.CMPOTHERINFO_DURATION_ERROR;
            }
        }
        return Err.SUCCESS;
    }

    public byte getAdclose() {
        return adclose;
    }

    public void setAdclose(byte adclose) {
        this.adclose = adclose;
    }

    /**
	 * 是否显示火酷广告
	 * 
	 * @return
	 *         2010-8-20
	 */
    public boolean isShowHkAd() {
        if (this.adclose == ADCLOSE_N) {
            return true;
        }
        return false;
    }
}
