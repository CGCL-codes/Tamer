package com.taobao.api.extra.model;

import java.util.Date;
import com.taobao.api.model.TaobaoModel;

/**
 * get fields
 * 
 * @author ruowang
 *
 */
public class CatStat extends TaobaoModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -791909569447438014L;

    private String cid;

    private String catName;

    private String percent;

    private Integer itemNum;

    private String volume;

    private Date created;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public Integer getItemNum() {
        return itemNum;
    }

    public void setItemNum(Integer itemNum) {
        this.itemNum = itemNum;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
