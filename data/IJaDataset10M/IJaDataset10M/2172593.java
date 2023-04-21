package com.liferay.portlet.imagegallery.model;

/**
 * <a href="IGImage.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>IGImage</code> table
 * in the database.
 * </p>
 *
 * <p>
 * Customize <code>com.liferay.portlet.imagegallery.service.model.impl.IGImageImpl</code>
 * and rerun the ServiceBuilder to generate the new methods.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.imagegallery.service.model.IGImageModel
 * @see com.liferay.portlet.imagegallery.service.model.impl.IGImageImpl
 * @see com.liferay.portlet.imagegallery.service.model.impl.IGImageModelImpl
 *
 */
public interface IGImage extends IGImageModel {

    public java.lang.String getUserUuid() throws com.liferay.portal.SystemException;

    public void setUserUuid(java.lang.String userUuid);

    public com.liferay.portlet.imagegallery.model.IGFolder getFolder();

    public java.lang.String getNameWithExtension();

    public java.lang.String getImageType();

    public void setImageType(java.lang.String imageType);

    public int getImageSize();
}
