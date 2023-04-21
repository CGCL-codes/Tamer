package com.taobao.api.model;

/**
 * 获取卖家的运费模板时 需要传入的参数
 * @author gaoweibin.tw
 *
 */
public class PostagesGetRequest extends TaobaoRequest {

    private static final long serialVersionUID = 1778558570706731000L;

    private String fields;

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public PostagesGetRequest withFields(String fields) {
        setFields(fields);
        return this;
    }
}
