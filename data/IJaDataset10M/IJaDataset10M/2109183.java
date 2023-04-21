package com.liferay.portlet.imagegallery.model;

/**
 * <a href="IGFolder.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>IGFolder</code> table
 * in the database.
 * </p>
 *
 * <p>
 * Customize <code>com.liferay.portlet.imagegallery.service.model.impl.IGFolderImpl</code>
 * and rerun the ServiceBuilder to generate the new methods.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.imagegallery.service.model.IGFolderModel
 * @see com.liferay.portlet.imagegallery.service.model.impl.IGFolderImpl
 * @see com.liferay.portlet.imagegallery.service.model.impl.IGFolderModelImpl
 *
 */
public interface IGFolder extends IGFolderModel {

    public java.lang.String getUserUuid() throws com.liferay.portal.SystemException;

    public void setUserUuid(java.lang.String userUuid);

    public boolean isRoot();
}
