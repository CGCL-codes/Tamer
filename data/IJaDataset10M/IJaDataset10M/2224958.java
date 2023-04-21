package csiebug.domain.hibernateImpl;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import csiebug.domain.Dashboard;
import csiebug.domain.DashboardPortlet;

/**
 * 
 * @author George_Tsai
 * @version 2009/8/17
 *
 */
public class DashboardPortletImpl extends BasicObjectImpl implements DashboardPortlet {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String dashboardId;

    private String portletId;

    private String portletTitle;

    private Boolean visible;

    private Integer sortOrder;

    private String columnName;

    private Dashboard dashboard;

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DashboardPortletImpl)) {
            return false;
        }
        DashboardPortletImpl dashboardPortlet = (DashboardPortletImpl) obj;
        return new EqualsBuilder().append(this.userId, dashboardPortlet.getUserId()).append(this.dashboardId, dashboardPortlet.getDashboardId()).append(this.portletId, dashboardPortlet.getPortletId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(this.userId).append(this.dashboardId).append(this.portletId).toHashCode();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setDashboardId(String dashboardId) {
        this.dashboardId = dashboardId;
    }

    public String getDashboardId() {
        return dashboardId;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setPortletId(String portletId) {
        this.portletId = portletId;
    }

    public String getPortletId() {
        return portletId;
    }

    public void setPortletTitle(String portletTitle) {
        this.portletTitle = portletTitle;
    }

    public String getPortletTitle() {
        return portletTitle;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }
}
