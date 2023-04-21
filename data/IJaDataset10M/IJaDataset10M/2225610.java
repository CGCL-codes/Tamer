package com.taobao.top.request;

import java.util.Map;
import com.taobao.top.util.TopHashMap;

/**
 * TOP API: taobao.sellercats.list.add
 * 
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
public class SellercatsListAddRequest implements TopRequest {

    private String name;

    private Integer parentCid;

    private String pictUrl;

    private Integer sortOrder;

    public void setName(String name) {
        this.name = name;
    }

    public void setParentCid(Integer parentCid) {
        this.parentCid = parentCid;
    }

    public void setPictUrl(String pictUrl) {
        this.pictUrl = pictUrl;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getApiName() {
        return "taobao.sellercats.list.add";
    }

    public Map<String, String> getTextParams() {
        TopHashMap params = new TopHashMap();
        params.put("name", this.name);
        params.put("parent_cid", this.parentCid);
        params.put("pict_url", this.pictUrl);
        params.put("sort_order", this.sortOrder);
        return params;
    }
}
