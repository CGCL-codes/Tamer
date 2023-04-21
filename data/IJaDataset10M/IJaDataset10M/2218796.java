package cn.jsprun.foreg.vo.statistic;

import java.util.ArrayList;
import java.util.List;

public class Stats_agentVO {

    Stats_navbarVO navbar = new Stats_navbarVO();

    private List<PageInfoWithImage> operatingSystemList = new ArrayList<PageInfoWithImage>();

    private List<PageInfoWithImage> browserList = new ArrayList<PageInfoWithImage>();

    public List<PageInfoWithImage> getBrowserList() {
        return browserList;
    }

    public void setBrowserList(List<PageInfoWithImage> browserList) {
        this.browserList = browserList;
    }

    public List<PageInfoWithImage> getOperatingSystemList() {
        return operatingSystemList;
    }

    public void setOperatingSystemList(List<PageInfoWithImage> operatingSystemList) {
        this.operatingSystemList = operatingSystemList;
    }

    public Stats_navbarVO getNavbar() {
        return navbar;
    }
}
