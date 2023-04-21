package com.taobao.api.model;

/**
 * @author gaoweibin.tw
 *
 */
public class ItemImg extends TaobaoModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7251642765531379576L;

    private String itemimgId;

    private String url;

    private long position;

    private String created;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getItemimgId() {
        return itemimgId;
    }

    public void setItemimgId(String itemimgId) {
        this.itemimgId = itemimgId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
