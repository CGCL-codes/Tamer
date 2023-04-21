package com.liferay.portal.ejb;

import java.util.Date;

/**
 * <a href="ReleaseHBM.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.2 $
 *
 */
public class ReleaseHBM {

    protected ReleaseHBM() {
    }

    protected ReleaseHBM(String releaseId) {
        _releaseId = releaseId;
    }

    protected ReleaseHBM(String releaseId, Date createDate, Date modifiedDate, int buildNumber, Date buildDate) {
        _releaseId = releaseId;
        _createDate = createDate;
        _modifiedDate = modifiedDate;
        _buildNumber = buildNumber;
        _buildDate = buildDate;
    }

    public String getPrimaryKey() {
        return _releaseId;
    }

    protected void setPrimaryKey(String pk) {
        _releaseId = pk;
    }

    protected String getReleaseId() {
        return _releaseId;
    }

    protected void setReleaseId(String releaseId) {
        _releaseId = releaseId;
    }

    protected Date getCreateDate() {
        return _createDate;
    }

    protected void setCreateDate(Date createDate) {
        _createDate = createDate;
    }

    protected Date getModifiedDate() {
        return _modifiedDate;
    }

    protected void setModifiedDate(Date modifiedDate) {
        _modifiedDate = modifiedDate;
    }

    protected int getBuildNumber() {
        return _buildNumber;
    }

    protected void setBuildNumber(int buildNumber) {
        _buildNumber = buildNumber;
    }

    protected Date getBuildDate() {
        return _buildDate;
    }

    protected void setBuildDate(Date buildDate) {
        _buildDate = buildDate;
    }

    private String _releaseId;

    private Date _createDate;

    private Date _modifiedDate;

    private int _buildNumber;

    private Date _buildDate;
}
