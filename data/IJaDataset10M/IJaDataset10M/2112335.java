package com.liferay.portlet.messageboards.model.impl;

import com.liferay.documentlibrary.NoSuchDirectoryException;
import com.liferay.documentlibrary.service.DLServiceUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.messageboards.model.MBCategory;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.service.MBCategoryLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBThreadLocalServiceUtil;
import com.liferay.portlet.messageboards.util.BBCodeUtil;
import com.liferay.portlet.tags.service.TagsEntryLocalServiceUtil;
import java.rmi.RemoteException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="MBMessageImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class MBMessageImpl extends MBMessageModelImpl implements MBMessage {

    public static final long DEFAULT_PARENT_MESSAGE_ID = 0;

    public MBMessageImpl() {
    }

    public String getUserUuid() throws SystemException {
        return PortalUtil.getUserValue(getUserId(), "uuid", _userUuid);
    }

    public void setUserUuid(String userUuid) {
        _userUuid = userUuid;
    }

    public MBCategory getCategory() {
        MBCategory category = null;
        try {
            if (getCategoryId() == CompanyConstants.SYSTEM) {
                category = MBCategoryLocalServiceUtil.getSystemCategory();
            } else {
                category = MBCategoryLocalServiceUtil.getCategory(getCategoryId());
            }
        } catch (Exception e) {
            category = new MBCategoryImpl();
            _log.error(e);
        }
        return category;
    }

    public boolean isRoot() {
        if (getParentMessageId() == DEFAULT_PARENT_MESSAGE_ID) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isReply() {
        return !isRoot();
    }

    public boolean isDiscussion() {
        if (getCategoryId() == CompanyConstants.SYSTEM) {
            return true;
        } else {
            return false;
        }
    }

    public String getBody(boolean translate) {
        String body = getBody();
        if (translate) {
            try {
                body = BBCodeUtil.getHTML(body);
            } catch (Exception e) {
                _log.error("Could not parse message " + getMessageId() + " " + e.getMessage());
            }
        }
        return body;
    }

    public String getThreadAttachmentsDir() {
        return "messageboards/" + getThreadId();
    }

    public String getAttachmentsDir() {
        if (_attachmentDirs == null) {
            _attachmentDirs = getThreadAttachmentsDir() + "/" + getMessageId();
        }
        return _attachmentDirs;
    }

    public void setAttachmentsDir(String attachmentsDir) {
        _attachmentDirs = attachmentsDir;
    }

    public String[] getAttachmentsFiles() throws PortalException, SystemException {
        String[] fileNames = new String[0];
        try {
            fileNames = DLServiceUtil.getFileNames(getCompanyId(), CompanyConstants.SYSTEM, getAttachmentsDir());
        } catch (NoSuchDirectoryException nsde) {
        } catch (RemoteException re) {
            _log.error(re);
        }
        return fileNames;
    }

    public double getPriority() throws PortalException, SystemException {
        if (_priority == -1) {
            _priority = MBThreadLocalServiceUtil.getThread(getThreadId()).getPriority();
        }
        return _priority;
    }

    public void setPriority(double priority) {
        _priority = priority;
    }

    public String[] getTagsEntries() throws SystemException {
        return TagsEntryLocalServiceUtil.getEntryNames(MBMessage.class.getName(), getMessageId());
    }

    private static Log _log = LogFactory.getLog(MBMessageImpl.class);

    private String _userUuid;

    private double _priority = -1;

    private String _attachmentDirs;
}
