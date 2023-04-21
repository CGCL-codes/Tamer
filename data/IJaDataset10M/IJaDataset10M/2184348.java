package com.liferay.portal.model;

import com.liferay.portal.model.BaseModel;

/**
 * <a href="ClassNameModel.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>ClassName_</code> table in
 * the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.model.ClassName
 * @see com.liferay.portal.service.model.impl.ClassNameImpl
 * @see com.liferay.portal.service.model.impl.ClassNameModelImpl
 *
 */
public interface ClassNameModel extends BaseModel {

    public long getPrimaryKey();

    public void setPrimaryKey(long pk);

    public long getClassNameId();

    public void setClassNameId(long classNameId);

    public String getValue();

    public void setValue(String value);
}
