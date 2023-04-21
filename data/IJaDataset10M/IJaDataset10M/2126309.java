package com.liferay.portlet.softwarecatalog.model;

import com.liferay.portal.model.BaseModel;
import java.util.Date;

/**
 * <a href="SCProductEntryModel.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>SCProductEntry</code> table
 * in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.softwarecatalog.service.model.SCProductEntry
 * @see com.liferay.portlet.softwarecatalog.service.model.impl.SCProductEntryImpl
 * @see com.liferay.portlet.softwarecatalog.service.model.impl.SCProductEntryModelImpl
 *
 */
public interface SCProductEntryModel extends BaseModel {

    public long getPrimaryKey();

    public void setPrimaryKey(long pk);

    public long getProductEntryId();

    public void setProductEntryId(long productEntryId);

    public long getGroupId();

    public void setGroupId(long groupId);

    public long getCompanyId();

    public void setCompanyId(long companyId);

    public long getUserId();

    public void setUserId(long userId);

    public String getUserName();

    public void setUserName(String userName);

    public Date getCreateDate();

    public void setCreateDate(Date createDate);

    public Date getModifiedDate();

    public void setModifiedDate(Date modifiedDate);

    public String getName();

    public void setName(String name);

    public String getType();

    public void setType(String type);

    public String getTags();

    public void setTags(String tags);

    public String getShortDescription();

    public void setShortDescription(String shortDescription);

    public String getLongDescription();

    public void setLongDescription(String longDescription);

    public String getPageURL();

    public void setPageURL(String pageURL);

    public String getAuthor();

    public void setAuthor(String author);

    public String getRepoGroupId();

    public void setRepoGroupId(String repoGroupId);

    public String getRepoArtifactId();

    public void setRepoArtifactId(String repoArtifactId);
}
