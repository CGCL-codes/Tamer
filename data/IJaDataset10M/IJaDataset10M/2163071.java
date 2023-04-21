package com.liferay.mail.service.persistence;

/**
 * <a href="MessageFinder.java.html"><b><i>View Source</i></b></a>
 *
 * @author Scott Lee
 *
 */
public interface MessageFinder {

    public long countUnreadByFolderId(long folderId) throws com.liferay.portal.SystemException;
}
