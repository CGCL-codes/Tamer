package cn.chengdu.in.android.config;

import android.content.Context;
import cn.chengdu.in.android.R;

/**
 * @author Declan.Z(declan.zhang@gmail.com)
 * @date 2011-2-22
 */
public class Config {

    public static final int VERSION_ALPHA = 0;

    public static final int VERSION_BETA = 1;

    public static final int VERSION_DEVELOP = 2;

    public static final int VERSION_IN = 3;

    public static final int VERSION_GA = 4;

    public static final int VERSION_IN_ZHENGZHOU = 5;

    public static final int VERSION = VERSION_DEVELOP;

    public static final boolean DEBUG = (VERSION == VERSION_DEVELOP || VERSION == VERSION_IN) ? true : false;

    public static final boolean LOCATION_DEBUG = false;

    public static final String[] URL = { "http://api-a.out.chengdu.cn/v2/", "http://api.out.chengdu.cn/v2/", "http://192.168.1.115:8080/icdApi/api/", "http://api.in.chengdu.cn/v2/", "http://api.in.chengdu.cn/v2/", "http://223.4.93.206:8080/icdServer2/api/" };

    public static final String BASE_URL = URL[VERSION];

    public static final String getServerStatusUrl() {
        switch(VERSION) {
            case VERSION_IN_ZHENGZHOU:
                return "http://status.inzhengzhou.cn";
            default:
                return "http://status.in.chengdu.cn";
        }
    }

    /**
     * 总目录
     */
    public static final String getResourceDirPath(Context context) {
        return context.getResources().getString(R.string.resource_dir);
    }

    /**
     * 图片缓存目录
     */
    public static final String IMAGE_CACHE_PATH = "cache";

    /**
     * 数据缓存目录 
     */
    public static final String DATA_CACHE_PATH = "cache";

    /**
     * 文件下载目录
     */
    public static final String DOWNLOAD_PATH = "download";

    /**
     * 临时文件目录
     */
    public static final String TEMP_PATH = "temp";

    /**
     * 分页大小, 默认20, 20就不用传
     */
    public static final int QUERY_PAGE_SIZE = 20;

    /**
     * 用于gridview的分页大小
     */
    public static final int QUERY_GRID_PAGE_SIZE = 21;

    /**
     * 图片墙分页大小(暂不分页)
     */
    public static final int PAGE_SIZE_MAX = Integer.MAX_VALUE;

    /**
     * 默认检索距离范围  2km 
     */
    public static final int SEARCH_RADIUS = 2;

    /**
     * 默认图片质量
     */
    public static final int DEFAULT_JPG_QUALITY = 80;

    /**
     * 成都的纬度修正数值
     */
    public static final double FIX_LAT = -0.002602;

    /**
     * 成都的经度修正数值
     */
    public static final double FIX_LNG = 0.002424;

    /**
     * 可以签到的范围 单位 KM
     */
    public static final int CHECK_IN_DISTANCE = 1;

    /**
     * 启动画面停留时间
     */
    public static final int START_TIMEOUT = 500;

    /**
     * 定位超时
     */
    public static final int LOCATION_TIMEOUT = 15 * 1000;

    /**
     * GPS定位超时
     */
    public static final int LOCATION_GPS_TIMEOUT = 30 * 1000;

    /**
     * TAG更新的周期 
     */
    public static final long TAG_UPDATE_TIME = 24 * 60 * 60 * 1000;

    /**
     * 版本检测周期
     */
    public static final long VERSION_UPDATE_TIME = 24 * 60 * 60 * 1000;

    /**
     * 检测新消息, 新粉丝的周期
     */
    public static final int BACKGROUND_TASK_TIME = 2 * 60 * 1000;

    /**
     * 应用关闭后的轮询时间
     */
    public static final int BACKGROUND_MESSAGE_TASK_TIME = VERSION == VERSION_GA ? 4 * 60 * 60 * 1000 : 10 * 60 * 1000;

    /**
     * 需要重定位的时间
     */
    public static final int LOCATION_RELOCATION_TIME = Integer.MAX_VALUE;

    /**
     * 读取未读消息冷却时间
     */
    public static final int MESSAGE_COLD_TIME = 5 * 1000;
}
