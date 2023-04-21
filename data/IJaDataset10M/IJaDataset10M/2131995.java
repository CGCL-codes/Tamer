package cn.jsprun.foreg.vo.statistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Stats_manageStatisticVO {

    private Stats_navbarVO navbar = new Stats_navbarVO();

    private boolean showUsername;

    private boolean showThisMonthManageStatistic = false;

    private String tdWidth;

    private List<String> titleInfoList = new ArrayList<String>();

    private List<TimeInfo> timeInfoList = new ArrayList<TimeInfo>();

    private List<ContentInfo> contentInfoList = new ArrayList<ContentInfo>();

    private Map<String, Map<String, String>> columnOfAllNumberMap = new HashMap<String, Map<String, String>>();

    private Map<String, String> titleInfoMap = new HashMap<String, String>();

    public class ContentInfo {

        private String before;

        private String uid;

        private String username;

        private String timeOfDay;

        private Map<String, Map<String, String>> columnOfNumberMapMap = new HashMap<String, Map<String, String>>();

        public String getBefore() {
            return before;
        }

        public void setBefore(String before) {
            this.before = before;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Map<String, Map<String, String>> getColumnOfNumberMapMap() {
            return columnOfNumberMapMap;
        }

        public void setColumnOfNumberMapMap(Map<String, Map<String, String>> columnOfNumberMapMap) {
            this.columnOfNumberMapMap = columnOfNumberMapMap;
        }

        public String getTimeOfDay() {
            return timeOfDay;
        }

        public void setTimeOfDay(String timeOfDay) {
            this.timeOfDay = timeOfDay;
        }
    }

    public class TimeInfo {

        private boolean nowTime;

        private String time;

        private String before;

        private String uid;

        public String getBefore() {
            return before;
        }

        public void setBefore(String before) {
            this.before = before;
        }

        public boolean isNowTime() {
            return nowTime;
        }

        public void setNowTime(boolean nowTime) {
            this.nowTime = nowTime;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }

    public List<ContentInfo> getContentInfoList() {
        return contentInfoList;
    }

    public void setContentInfoList(List<ContentInfo> contentInfoList) {
        this.contentInfoList = contentInfoList;
    }

    public List<TimeInfo> getTimeInfoList() {
        return timeInfoList;
    }

    public void setTimeInfoList(List<TimeInfo> timeInfoList) {
        this.timeInfoList = timeInfoList;
    }

    public List<String> getTitleInfoList() {
        return titleInfoList;
    }

    public void setTitleInfoList(List<String> titleInfoList) {
        this.titleInfoList = titleInfoList;
    }

    public boolean isShowUsername() {
        return showUsername;
    }

    public void setShowUsername(boolean showUsername) {
        this.showUsername = showUsername;
    }

    public String getTdWidth() {
        return tdWidth;
    }

    public void setTdWidth(String tdWidth) {
        this.tdWidth = tdWidth;
    }

    public boolean isShowThisMonthManageStatistic() {
        return showThisMonthManageStatistic;
    }

    public void setShowThisMonthManageStatistic(boolean showThisMonthManageStatistic) {
        this.showThisMonthManageStatistic = showThisMonthManageStatistic;
    }

    public Map<String, Map<String, String>> getColumnOfAllNumberMap() {
        return columnOfAllNumberMap;
    }

    public void setColumnOfAllNumberMap(Map<String, Map<String, String>> columnOfAllNumberMap) {
        this.columnOfAllNumberMap = columnOfAllNumberMap;
    }

    public Stats_navbarVO getNavbar() {
        return navbar;
    }

    public Map<String, String> getTitleInfoMap() {
        return titleInfoMap;
    }
}
