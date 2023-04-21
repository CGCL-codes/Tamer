package com.magicpwd._docs;

/**
 * VDisk常量
 * <p/><b>0 和 600或以上的err_code为通用代码,其他代码的含义与特定调用有关!</b>
 * 
 * <p/>如果发现任何bug或有任何意见建议,请发送邮件到 vdisk4j@wendal.net
 * <p/>项目主页: http://vdisk4j.googlecode.com
 * <p/>
 * <p/>我的博客: http://wendal.net
 * @author wendal(vdisk4j@wendal.net)
 */
public class VDiskConstant {

    /**
	 * 服务器API地址
	 */
    public static String SERVER = "http://openapi.vdisk.me/";

    /**
	 * 成功
	 */
    public static final int Success = 0;

    /**
	 * 空间已满
	 */
    public static final int Disk_Full = 601;

    /**
	 * 客户端的dologid已经过期
	 */
    public static final int Dolog_Old = 602;

    /**
	 * 缺少参数,VDiskAPI的大部分方法都不允许传入null值
	 */
    public static final int miss_parms = 701;

    /**
	 * 无效token,未调用getToken方法或者长时间闲置,导致token过期
	 */
    public static final int invalid_token = 702;

    /**
	 * 调用次数过多,被锁了!!
	 */
    public static final int over_limit = 900;

    /**
	 * VDiskResponse.err_code的默认值
	 * <p/>如果得到这个值,表示服务器返回的响应没有包含VDiskResponse.err_code
	 */
    public static final int UNKOWN = Integer.MAX_VALUE;

    public static final int Signature_Fail = 1;

    public static final int account_fail = 2;

    public static final int time_fail = 3;
}
