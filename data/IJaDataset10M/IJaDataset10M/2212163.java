package com.liferay.portlet.expando.model;

import com.liferay.portal.model.BaseModel;

/**
 * <a href="ExpandoValueModel.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>ExpandoValue</code>
 * table in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.expando.model.ExpandoValue
 * @see com.liferay.portlet.expando.model.impl.ExpandoValueImpl
 * @see com.liferay.portlet.expando.model.impl.ExpandoValueModelImpl
 *
 */
public interface ExpandoValueModel extends BaseModel {

    public long getPrimaryKey();

    public void setPrimaryKey(long pk);

    public long getValueId();

    public void setValueId(long valueId);

    public long getTableId();

    public void setTableId(long tableId);

    public long getColumnId();

    public void setColumnId(long columnId);

    public long getRowId();

    public void setRowId(long rowId);

    public String getClassName();

    public long getClassNameId();

    public void setClassNameId(long classNameId);

    public long getClassPK();

    public void setClassPK(long classPK);

    public String getData();

    public void setData(String data);

    public ExpandoValue toEscapedModel();
}
