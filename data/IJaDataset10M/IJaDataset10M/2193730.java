package com.liferay.portlet.social.model.impl;

import com.liferay.portal.kernel.bean.ReadOnlyBeanHandler;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.model.ExpandoBridgeImpl;
import com.liferay.portlet.social.model.SocialRelation;
import com.liferay.portlet.social.model.SocialRelationSoap;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <a href="SocialRelationModelImpl.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is a model that represents the <code>SocialRelation</code> table
 * in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.social.model.SocialRelation
 * @see com.liferay.portlet.social.model.SocialRelationModel
 * @see com.liferay.portlet.social.model.impl.SocialRelationImpl
 *
 */
public class SocialRelationModelImpl extends BaseModelImpl {

    public static final String TABLE_NAME = "SocialRelation";

    public static final Object[][] TABLE_COLUMNS = { { "uuid_", new Integer(Types.VARCHAR) }, { "relationId", new Integer(Types.BIGINT) }, { "companyId", new Integer(Types.BIGINT) }, { "createDate", new Integer(Types.TIMESTAMP) }, { "userId1", new Integer(Types.BIGINT) }, { "userId2", new Integer(Types.BIGINT) }, { "type_", new Integer(Types.INTEGER) } };

    public static final String TABLE_SQL_CREATE = "create table SocialRelation (uuid_ VARCHAR(75) null,relationId LONG not null primary key,companyId LONG,createDate DATE null,userId1 LONG,userId2 LONG,type_ INTEGER)";

    public static final String TABLE_SQL_DROP = "drop table SocialRelation";

    public static final String DATA_SOURCE = "liferayDataSource";

    public static final String SESSION_FACTORY = "liferaySessionFactory";

    public static final String TX_MANAGER = "liferayTransactionManager";

    public static final boolean CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get("value.object.finder.cache.enabled.com.liferay.portlet.social.model.SocialRelation"), true);

    public static SocialRelation toModel(SocialRelationSoap soapModel) {
        SocialRelation model = new SocialRelationImpl();
        model.setUuid(soapModel.getUuid());
        model.setRelationId(soapModel.getRelationId());
        model.setCompanyId(soapModel.getCompanyId());
        model.setCreateDate(soapModel.getCreateDate());
        model.setUserId1(soapModel.getUserId1());
        model.setUserId2(soapModel.getUserId2());
        model.setType(soapModel.getType());
        return model;
    }

    public static List<SocialRelation> toModels(SocialRelationSoap[] soapModels) {
        List<SocialRelation> models = new ArrayList<SocialRelation>(soapModels.length);
        for (SocialRelationSoap soapModel : soapModels) {
            models.add(toModel(soapModel));
        }
        return models;
    }

    public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get("lock.expiration.time.com.liferay.portlet.social.model.SocialRelation"));

    public SocialRelationModelImpl() {
    }

    public long getPrimaryKey() {
        return _relationId;
    }

    public void setPrimaryKey(long pk) {
        setRelationId(pk);
    }

    public Serializable getPrimaryKeyObj() {
        return new Long(_relationId);
    }

    public String getUuid() {
        return GetterUtil.getString(_uuid);
    }

    public void setUuid(String uuid) {
        if ((uuid != null) && (uuid != _uuid)) {
            _uuid = uuid;
        }
    }

    public long getRelationId() {
        return _relationId;
    }

    public void setRelationId(long relationId) {
        if (relationId != _relationId) {
            _relationId = relationId;
        }
    }

    public long getCompanyId() {
        return _companyId;
    }

    public void setCompanyId(long companyId) {
        if (companyId != _companyId) {
            _companyId = companyId;
        }
    }

    public Date getCreateDate() {
        return _createDate;
    }

    public void setCreateDate(Date createDate) {
        if (((createDate == null) && (_createDate != null)) || ((createDate != null) && (_createDate == null)) || ((createDate != null) && (_createDate != null) && !createDate.equals(_createDate))) {
            _createDate = createDate;
        }
    }

    public long getUserId1() {
        return _userId1;
    }

    public void setUserId1(long userId1) {
        if (userId1 != _userId1) {
            _userId1 = userId1;
        }
    }

    public long getUserId2() {
        return _userId2;
    }

    public void setUserId2(long userId2) {
        if (userId2 != _userId2) {
            _userId2 = userId2;
        }
    }

    public int getType() {
        return _type;
    }

    public void setType(int type) {
        if (type != _type) {
            _type = type;
        }
    }

    public SocialRelation toEscapedModel() {
        if (isEscapedModel()) {
            return (SocialRelation) this;
        } else {
            SocialRelation model = new SocialRelationImpl();
            model.setNew(isNew());
            model.setEscapedModel(true);
            model.setUuid(HtmlUtil.escape(getUuid()));
            model.setRelationId(getRelationId());
            model.setCompanyId(getCompanyId());
            model.setCreateDate(getCreateDate());
            model.setUserId1(getUserId1());
            model.setUserId2(getUserId2());
            model.setType(getType());
            model = (SocialRelation) Proxy.newProxyInstance(SocialRelation.class.getClassLoader(), new Class[] { SocialRelation.class }, new ReadOnlyBeanHandler(model));
            return model;
        }
    }

    public ExpandoBridge getExpandoBridge() {
        if (_expandoBridge == null) {
            _expandoBridge = new ExpandoBridgeImpl(SocialRelation.class.getName(), getPrimaryKey());
        }
        return _expandoBridge;
    }

    public Object clone() {
        SocialRelationImpl clone = new SocialRelationImpl();
        clone.setUuid(getUuid());
        clone.setRelationId(getRelationId());
        clone.setCompanyId(getCompanyId());
        clone.setCreateDate(getCreateDate());
        clone.setUserId1(getUserId1());
        clone.setUserId2(getUserId2());
        clone.setType(getType());
        return clone;
    }

    public int compareTo(Object obj) {
        if (obj == null) {
            return -1;
        }
        SocialRelationImpl socialRelation = (SocialRelationImpl) obj;
        long pk = socialRelation.getPrimaryKey();
        if (getPrimaryKey() < pk) {
            return -1;
        } else if (getPrimaryKey() > pk) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        SocialRelationImpl socialRelation = null;
        try {
            socialRelation = (SocialRelationImpl) obj;
        } catch (ClassCastException cce) {
            return false;
        }
        long pk = socialRelation.getPrimaryKey();
        if (getPrimaryKey() == pk) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (int) getPrimaryKey();
    }

    private String _uuid;

    private long _relationId;

    private long _companyId;

    private Date _createDate;

    private long _userId1;

    private long _userId2;

    private int _type;

    private ExpandoBridge _expandoBridge;
}
