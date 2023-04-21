package com.hcs.service;

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
	 * @return 登录是否成功（0.失败 1.成功）
	 */
    public int login(String userName, String password);

    /**
	 * 心跳
	 * 
	 * @return 1为请求心跳成功 0为请求心跳失败
	 */
    public int requestHeartBeat();
}
