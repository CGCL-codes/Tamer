package com.rb.ft.database.pojo;

/**
 * @类功能说明: FtGameShooterId entity.   
 * @类修改者:     
 * @修改日期:   
 * @修改说明:   
 * @作者:       robin
 * @创建时间:   2011-7-20 下午01:06:04
 * @版本:       1.0.0
 */
public class FtGameShooterId implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7951122331403088746L;

    private FtItemSeason ftItemSeason;

    private FtGameRecord ftGameRecord;

    private FtFootballer ftFootballer;

    /** default constructor */
    public FtGameShooterId() {
    }

    /** minimal constructor */
    public FtGameShooterId(FtItemSeason ftItemSeason, FtGameRecord ftGameRecord) {
        this.ftItemSeason = ftItemSeason;
        this.ftGameRecord = ftGameRecord;
    }

    /** full constructor */
    public FtGameShooterId(FtItemSeason ftItemSeason, FtGameRecord ftGameRecord, FtFootballer ftFootballer) {
        this.ftItemSeason = ftItemSeason;
        this.ftGameRecord = ftGameRecord;
        this.ftFootballer = ftFootballer;
    }

    public FtItemSeason getFtItemSeason() {
        return this.ftItemSeason;
    }

    public void setFtItemSeason(FtItemSeason ftItemSeason) {
        this.ftItemSeason = ftItemSeason;
    }

    public FtGameRecord getFtGameRecord() {
        return this.ftGameRecord;
    }

    public void setFtGameRecord(FtGameRecord ftGameRecord) {
        this.ftGameRecord = ftGameRecord;
    }

    public FtFootballer getFtFootballer() {
        return this.ftFootballer;
    }

    public void setFtFootballer(FtFootballer ftFootballer) {
        this.ftFootballer = ftFootballer;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof FtGameShooterId)) return false;
        FtGameShooterId castOther = (FtGameShooterId) other;
        return ((this.getFtItemSeason() == castOther.getFtItemSeason()) || (this.getFtItemSeason() != null && castOther.getFtItemSeason() != null && this.getFtItemSeason().equals(castOther.getFtItemSeason()))) && ((this.getFtGameRecord() == castOther.getFtGameRecord()) || (this.getFtGameRecord() != null && castOther.getFtGameRecord() != null && this.getFtGameRecord().equals(castOther.getFtGameRecord()))) && ((this.getFtFootballer() == castOther.getFtFootballer()) || (this.getFtFootballer() != null && castOther.getFtFootballer() != null && this.getFtFootballer().equals(castOther.getFtFootballer())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getFtItemSeason() == null ? 0 : this.getFtItemSeason().hashCode());
        result = 37 * result + (getFtGameRecord() == null ? 0 : this.getFtGameRecord().hashCode());
        result = 37 * result + (getFtFootballer() == null ? 0 : this.getFtFootballer().hashCode());
        return result;
    }
}
