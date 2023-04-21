package com.liferay.portlet.messageboards.service.impl;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBMessageFlag;
import com.liferay.portlet.messageboards.model.impl.MBMessageFlagImpl;
import com.liferay.portlet.messageboards.service.base.MBMessageFlagLocalServiceBaseImpl;
import java.util.List;

/**
 * <a href="MBMessageFlagLocalServiceImpl.java.html"><b><i>View Source</i></b>
 * </a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class MBMessageFlagLocalServiceImpl extends MBMessageFlagLocalServiceBaseImpl {

    public void addReadFlags(long userId, List<MBMessage> messages) throws PortalException, SystemException {
        User user = userPersistence.findByPrimaryKey(userId);
        if (user.isDefaultUser()) {
            return;
        }
        for (MBMessage message : messages) {
            MBMessageFlag messageFlag = mbMessageFlagPersistence.fetchByU_M(userId, message.getMessageId());
            if (messageFlag == null) {
                long messageFlagId = counterLocalService.increment();
                messageFlag = mbMessageFlagPersistence.create(messageFlagId);
                messageFlag.setUserId(userId);
                messageFlag.setMessageId(message.getMessageId());
                messageFlag.setFlag(MBMessageFlagImpl.READ_FLAG);
                mbMessageFlagPersistence.update(messageFlag, false);
            }
        }
    }

    public void deleteFlags(long userId) throws SystemException {
        mbMessageFlagPersistence.removeByUserId(userId);
    }

    public boolean hasReadFlag(long userId, long messageId) throws PortalException, SystemException {
        User user = userPersistence.findByPrimaryKey(userId);
        if (user.isDefaultUser()) {
            return true;
        }
        MBMessageFlag messageFlag = mbMessageFlagPersistence.fetchByU_M(userId, messageId);
        if (messageFlag != null) {
            return true;
        } else {
            return false;
        }
    }
}
