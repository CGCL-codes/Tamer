package cn.vlabs.duckling.vwb.services.resource;

/**
 * 配置项接口
 * @date Feb 5, 2010
 * @author xiejj@cnic.cn
 */
public interface ConfigItem {

    String getType();

    int getId();

    int getFooter();

    int getBanner();

    int getTopmenu();

    int getLeftmenu();

    int getParent();

    int getTrail();

    void init();

    public String getTitle();
}
