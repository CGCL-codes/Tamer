package com.taobao.api.extra;

import com.taobao.api.RestClient;
import com.taobao.api.TaobaoPublicApi;

/**
 * 此接口中包括TopClient中所有的客户端API 每个API被调用后均返回一个Response结果，此结果中包括：错误码 code，错误信息
 * msg，重定向URL redirectUrl， 以及Http请求返回的结果的body部分 ，另包括调用这个API成功时返回的真实结果
 * 
 * @version 2008-11-7
 * @author ruowang
 * 
 */
public interface TaobaoExtraRestClient extends TaobaoPublicApi, TaobaoExtraApi, RestClient {
}
