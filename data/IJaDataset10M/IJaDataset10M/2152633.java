package com.hcs.protocol.utils;

/**
 * 协议属性常量
 * 
 * @author nianchun.li
 * @createTime 2011/5/4 8:54
 */
public class ProtocolContanst {

    /** UserTag */
    public static String USER_TAG = "000000000000";

    /** 项目访问URL */
    public static String URL = "http://10.10.107.13:8080/MobileMarket/servlet/HCSMobileApp?userTag=";

    /** 后台XML数据下载URL */
    public static String DOWNLOAD_XML_URL = "http://211.152.35.242:58080/MobileMarketTest/doc/xml/arrays.xml";

    /** 后台DB更新信息下载URL */
    public static String DOWNLOAD_DB_URL = "http://211.152.35.242:58080/MobileMarketTest/doc/dbScript.sql";

    /** APK更新下载URL */
    public static String DOWNLOAD_APK_URL = "http://211.152.35.242:58080/MobileMarketTest/";

    /** 超时时间　*/
    public static int USERTAG_CLEAR_MINUTE = 1;

    /** 字符编码　*/
    public static String CODE = "utf-8";

    /** 用户登录消息ＩＤ　*/
    public static final short LOGIN_MSG_ID = 1001;

    /** 获取工作简报（当天）消息ＩＤ　*/
    public static final short BRIEF_MSG_ID = 1002;

    /** 获取最新公告信息消息ＩＤ　*/
    public static final short NEW_NOTICE_MSG_ID = 1003;

    /** 历史信息查询消息ＩＤ　*/
    public static final short HISTORY_INFO_MSG_ID = 1004;

    /** 获取历史详细信息消息ＩＤ　*/
    public static final short HISTORY_DETAIL_MSG_ID = 1005;

    /** 信息查询消息ＩＤ　*/
    public static final short INFO_LIST_MSG_ID = 1006;

    /** 简报查询消息ＩＤ　*/
    public static final short BRIEF_LIST_MSG_ID = 1007;

    /** 根据类型和指定时间获取详细简报信息消息ＩＤ　*/
    public static final short BRIEF_DETAIL_MSG_ID = 1008;

    /** 新建客户信息消息ＩＤ　*/
    public static final short NEW_CUSTOMER_MSG_ID = 1009;

    /** 新建预购信息消息ＩＤ　*/
    public static final short NEW_ORDER_MSG_ID = 1010;

    /** 新建拜访信息消息ＩＤ　*/
    public static final short NEW_VISITED_MSG_ID = 1011;

    /** 根据用户ＩＤ获取收藏电话列表消息ＩＤ　*/
    public static final short PHONE_LIST_MSG_ID = 1012;

    /** 新增加电话记录消息ＩＤ　*/
    public static final short ADD_PHONE_MSG_ID = 1013;

    /** 获取预购指导信息列表消息ＩＤ　*/
    public static final short ORDER_DIRECT_MSG_ID = 1014;

    /** 获取预购指导详细信息消息ＩＤ　*/
    public static final short DIRECT_DETAIL_MSG_ID = 1015;

    /** 回复指导信息消息ＩＤ　*/
    public static final short RE_DIRECT_MSG_ID = 1016;

    /** 修改密码消息ＩＤ　*/
    public static final short RE_PWD_MSG_ID = 1017;

    /** 获取轮询内容消息ＩＤ　*/
    public static final short GET_POLLING_MSG_ID = 1018;

    /** 更新数据消息ＩＤ　*/
    public static final short UP_DATA_MSG_ID = 1019;

    /** 心跳消息ＩＤ　*/
    public static final short HEARTBEAT_MSG_ID = 1020;

    /** 轮询反馈ＩＤ　*/
    public static final short FEEDBACK_POLLING_MSG_ID = 1021;

    /** 更新版本ID　*/
    public static final short UPDATE_VERSION_MSG_ID = 1022;

    /** 操作成功　*/
    public static final String ADSTATUSOK = "000";

    /** 数据校验失败　*/
    public static final String CHECK_EXCEPTION = "001";

    /** 解密失败　*/
    public static final String DECRYPT_EXCEPTION = "002";

    /** 用户未登录　*/
    public static final String NOT_LOGIN_EXCEPTION = "003";

    /** session过期　*/
    public static final String SESSION_EXPIRE = "004";

    /** 没有找到匹配协议　*/
    public static final String NOT_MATE_PROTOCOL = "005";

    /** 黑名单　*/
    public static final String BLACK_LIST = "006";

    /** 程序异常　*/
    public static final String SYS_EXCEPTION = "007";
}
