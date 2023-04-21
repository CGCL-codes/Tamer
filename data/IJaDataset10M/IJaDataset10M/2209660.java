package com.kongur.network.erp.domain.uc.query;

import com.kongur.network.erp.common.Pagination;
import com.kongur.network.erp.domain.uc.UserDO;

/**
 * @author gaojf
 * @version $Id: UserQuery.java,v 0.1 2011-12-12 ����10:29:00 gaojf Exp $
 */
public class UserQuery extends Pagination<UserDO> {

    private static final long serialVersionUID = 40055003617252022L;

    private String groupId;

    private Integer platform;

    private Long sId;

    private String levelName;

    private String userName;

    private String status;

    private String startDate;

    private String endDate;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public Long getsId() {
        return sId;
    }

    public void setsId(Long sId) {
        this.sId = sId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
