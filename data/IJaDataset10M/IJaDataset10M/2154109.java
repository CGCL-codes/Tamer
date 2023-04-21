package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.Check;
import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.jianghu.fan.check response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class JianghuFanCheckResponse extends TaobaoResponse {

    private static final long serialVersionUID = 4249677649555395283L;

    /** 
	 * true or false
	 */
    @ApiField("result")
    private Check result;

    public void setResult(Check result) {
        this.result = result;
    }

    public Check getResult() {
        return this.result;
    }
}
