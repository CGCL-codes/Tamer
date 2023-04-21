package com.liferay.portlet.journal.model;

/**
 * <a href="JournalFeed.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>JournalFeed</code> table
 * in the database.
 * </p>
 *
 * <p>
 * Customize <code>com.liferay.portlet.journal.model.impl.JournalFeedImpl</code>
 * and rerun the ServiceBuilder to generate the new methods.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.journal.model.JournalFeedModel
 * @see com.liferay.portlet.journal.model.impl.JournalFeedImpl
 * @see com.liferay.portlet.journal.model.impl.JournalFeedModelImpl
 *
 */
public interface JournalFeed extends JournalFeedModel {

    public java.lang.String getUserUuid() throws com.liferay.portal.SystemException;

    public void setUserUuid(java.lang.String userUuid);
}
