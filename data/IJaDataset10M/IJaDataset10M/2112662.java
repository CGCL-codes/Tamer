package com.taobao.top.request;

import java.util.Map;
import com.taobao.top.util.TopHashMap;

/**
 * TOP API: taobao.users.get
 * 
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
public class UsersGetRequest implements TopRequest {

    private String fields;

    private String nicks;

    public void setFields(String fields) {
        this.fields = fields;
    }

    public void setNicks(String nicks) {
        this.nicks = nicks;
    }

    public String getApiName() {
        return "taobao.users.get";
    }

    public Map<String, String> getTextParams() {
        TopHashMap params = new TopHashMap();
        params.put("fields", this.fields);
        params.put("nicks", this.nicks);
        return params;
    }
}
