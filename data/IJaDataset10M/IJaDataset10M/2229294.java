package com.taobao.top.request;

import java.util.Map;
import com.taobao.top.util.TopHashMap;

/**
 * TOP API: taobao.favorite.search
 * 
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
public class FavoriteSearchRequest implements TopRequest {

    private String collectType;

    private Integer pageNo;

    private String userNick;

    public void setCollectType(String collectType) {
        this.collectType = collectType;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getApiName() {
        return "taobao.favorite.search";
    }

    public Map<String, String> getTextParams() {
        TopHashMap params = new TopHashMap();
        params.put("collect_type", this.collectType);
        params.put("page_no", this.pageNo);
        params.put("user_nick", this.userNick);
        return params;
    }
}
