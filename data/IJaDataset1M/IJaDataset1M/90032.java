package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.taohua.previewurl.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class TaohuaPreviewurlGetResponse extends TaobaoResponse {

    private static final long serialVersionUID = 6658656739393649849L;

    /** 
	 * 预览链接
	 */
    @ApiField("url")
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }
}
