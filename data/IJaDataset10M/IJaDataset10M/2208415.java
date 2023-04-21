package com.taobao.api.direct;

import com.taobao.api.RestClient;
import com.taobao.api.TaobaoPrivateApi;
import com.taobao.api.TaobaoPublicApi;

/**
 * 绑定帐号客户端，仅供内部使用
 * 
 * @version 2008-11-17
 * @author <a href="mailto:zixue@taobao.com">zixue</a>
 * 
 */
public interface TaobaoDirectRestClient extends TaobaoPublicApi, TaobaoPrivateApi, TaobaoDirectApi, RestClient {
}
