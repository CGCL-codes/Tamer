package net.itsite.os;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 项目标签
 * @Description：
 * @author: 李岩飞
 * @Time: 2011-9-1 下午12:38:25
 */
public class OSTagBean extends AbstractIdDataObjectBean {

    private ID catalogId;

    private EOSTag tag;

    private String nname;

    public void setTag(EOSTag tag) {
        this.tag = tag;
    }

    public EOSTag getTag() {
        return tag;
    }

    public void setNname(String nname) {
        this.nname = nname;
    }

    public String getNname() {
        return nname;
    }

    public ID getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(ID catalogId) {
        this.catalogId = catalogId;
    }
}
