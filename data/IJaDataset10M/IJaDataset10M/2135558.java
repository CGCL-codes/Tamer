package com.liferay.mail.model;

import com.liferay.portal.model.BaseModel;
import java.util.Date;

/**
 * <a href="AccountModel.java.html"><b><i>View Source</i></b></a>
 *
 * @author Scott Lee
 *
 */
public interface AccountModel extends BaseModel {

    public long getPrimaryKey();

    public void setPrimaryKey(long pk);

    public long getAccountId();

    public void setAccountId(long accountId);

    public long getCompanyId();

    public void setCompanyId(long companyId);

    public long getUserId();

    public void setUserId(long userId);

    public Date getCreateDate();

    public void setCreateDate(Date createDate);

    public Date getModifiedDate();

    public void setModifiedDate(Date modifiedDate);

    public long getDraftFolderId();

    public void setDraftFolderId(long draftFolderId);

    public long getInboxFolderId();

    public void setInboxFolderId(long inboxFolderId);

    public boolean getInitialized();

    public boolean isInitialized();

    public void setInitialized(boolean initialized);

    public String getMailAddress();

    public void setMailAddress(String mailAddress);

    public String getMailInHostName();

    public void setMailInHostName(String mailInHostName);

    public int getMailInPort();

    public void setMailInPort(int mailInPort);

    public boolean getMailInSecure();

    public boolean isMailInSecure();

    public void setMailInSecure(boolean mailInSecure);

    public String getMailOutHostName();

    public void setMailOutHostName(String mailOutHostName);

    public int getMailOutPort();

    public void setMailOutPort(int mailOutPort);

    public boolean getMailOutSecure();

    public boolean isMailOutSecure();

    public void setMailOutSecure(boolean mailOutSecure);

    public Date getMailSynchronizedDate();

    public void setMailSynchronizedDate(Date mailSynchronizedDate);

    public String getPassword();

    public void setPassword(String password);

    public long getSentFolderId();

    public void setSentFolderId(long sentFolderId);

    public long getTrashFolderId();

    public void setTrashFolderId(long trashFolderId);

    public String getType();

    public void setType(String type);

    public String getUsername();

    public void setUsername(String username);

    public Account toEscapedModel();
}
