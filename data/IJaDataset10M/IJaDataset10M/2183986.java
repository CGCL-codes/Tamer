package com.rb.ft.database.pojo;

import com.rb.ft.database.pojo.FtItem;
import com.rb.ft.database.pojo.FtSeason;

/**
 * @类功能说明: FtItemSeason entity. 
 * @类修改者:     
 * @修改日期:   
 * @修改说明:   
 * @作者:       robin
 * @创建时间:   2011-7-20 下午01:06:52
 * @版本:       1.0.0
 */
public class FtItemSeason implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -21060198220090513L;

    private Long isid;

    private FtSeason ftSeason;

    private FtItem ftItem;

    /** default constructor */
    public FtItemSeason() {
    }

    /** full constructor */
    public FtItemSeason(FtSeason ftSeason, FtItem ftItem) {
        this.ftSeason = ftSeason;
        this.ftItem = ftItem;
    }

    public Long getIsid() {
        return this.isid;
    }

    public void setIsid(Long isid) {
        this.isid = isid;
    }

    public FtSeason getFtSeason() {
        return this.ftSeason;
    }

    public void setFtSeason(FtSeason ftSeason) {
        this.ftSeason = ftSeason;
    }

    public FtItem getFtItem() {
        return this.ftItem;
    }

    public void setFtItem(FtItem ftItem) {
        this.ftItem = ftItem;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((isid == null) ? 0 : isid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        FtItemSeason other = (FtItemSeason) obj;
        if (isid == null) {
            if (other.isid != null) return false;
        } else if (!isid.equals(other.isid)) return false;
        return true;
    }
}
