package com.hcs.protocol;

import com.hcs.protocol.model.Account;

/**
 * 系统名：HCSMobileApp 
 * 子系统名：与服务器端接口(基本接口)
 * 著作权：COPYRIGHT (C) 2011 HAND INFORMATION SYSTEMS
 * 			CORPORATION ALL RIGHTS RESERVED.
 * 
 * @author nianchun.li
 * @createTime May 9, 2011
 */
public interface IProtocolService {

    /**
	 * 登录
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @return 返回登录账户信息Account
	 */
    public Account login(String userName, String password) throws Exception;

    /**
	 * 心跳
	 * 
	 * @return 1为请求心跳成功 0为请求心跳失败
	 */
    public int requestHeartBeat() throws Exception;
}
