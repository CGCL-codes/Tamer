package com.liferay.portal.model;

/**
 * <a href="UserGroup.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>UserGroup</code> table in
 * the database.
 * </p>
 *
 * <p>
 * Customize <code>com.liferay.portal.service.model.impl.UserGroupImpl</code> and
 * rerun the ServiceBuilder to generate the new methods.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.model.UserGroupModel
 * @see com.liferay.portal.service.model.impl.UserGroupImpl
 * @see com.liferay.portal.service.model.impl.UserGroupModelImpl
 *
 */
public interface UserGroup extends UserGroupModel {

    public com.liferay.portal.model.Group getGroup();
}
