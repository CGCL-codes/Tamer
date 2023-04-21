package com.bugull.mongo.encoder;

import com.bugull.mongo.BuguDao;
import com.bugull.mongo.BuguEntity;
import com.bugull.mongo.BuguMapper;
import com.bugull.mongo.annotations.Default;
import com.bugull.mongo.annotations.RefList;
import com.bugull.mongo.cache.DaoCache;
import com.bugull.mongo.mapper.DataType;
import com.bugull.mongo.mapper.StringUtil;
import com.mongodb.DBRef;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Frank Wen(xbwen@hotmail.com)
 */
@SuppressWarnings("unchecked")
public class RefListEncoder extends AbstractEncoder {

    private RefList refList;

    public RefListEncoder(Object obj, Field field) {
        super(obj, field);
        refList = field.getAnnotation(RefList.class);
    }

    @Override
    public String getFieldName() {
        String fieldName = field.getName();
        String name = refList.name();
        if (!name.equals(Default.NAME)) {
            fieldName = name;
        }
        return fieldName;
    }

    @Override
    public Object encode() {
        Class<?> type = field.getType();
        if (type.isArray()) {
            return encodeArray(type.getComponentType());
        } else {
            return encodeList(type);
        }
    }

    private Object encodeArray(Class<?> clazz) {
        int len = Array.getLength(value);
        List<DBRef> result = new ArrayList<DBRef>();
        BuguDao dao = DaoCache.getInstance().get(clazz);
        for (int i = 0; i < len; i++) {
            BuguEntity entity = (BuguEntity) Array.get(value, i);
            if (entity != null) {
                doCascade(dao, entity);
                result.add(BuguMapper.toDBRef(entity));
            }
        }
        return result;
    }

    private Object encodeList(Class type) {
        ParameterizedType paramType = (ParameterizedType) field.getGenericType();
        Type[] types = paramType.getActualTypeArguments();
        if (DataType.isList(type)) {
            List<BuguEntity> list = (List<BuguEntity>) value;
            List<DBRef> result = new ArrayList<DBRef>();
            BuguDao dao = DaoCache.getInstance().get((Class) types[0]);
            for (BuguEntity entity : list) {
                if (entity != null) {
                    doCascade(dao, entity);
                    result.add(BuguMapper.toDBRef(entity));
                }
            }
            return result;
        } else if (DataType.isSet(type)) {
            Set<BuguEntity> set = (Set<BuguEntity>) value;
            Set<DBRef> result = new HashSet<DBRef>();
            BuguDao dao = DaoCache.getInstance().get((Class) types[0]);
            for (BuguEntity entity : set) {
                if (entity != null) {
                    doCascade(dao, entity);
                    result.add(BuguMapper.toDBRef(entity));
                }
            }
            return result;
        } else if (DataType.isMap(type)) {
            Map<Object, BuguEntity> map = (Map<Object, BuguEntity>) value;
            Map<Object, DBRef> result = new HashMap<Object, DBRef>();
            BuguDao dao = DaoCache.getInstance().get((Class) types[1]);
            for (Object key : map.keySet()) {
                BuguEntity entity = map.get(key);
                if (entity != null) {
                    doCascade(dao, entity);
                    result.put(key, BuguMapper.toDBRef(entity));
                } else {
                    result.put(key, null);
                }
            }
            return result;
        } else {
            return null;
        }
    }

    private void doCascade(BuguDao dao, BuguEntity entity) {
        String idStr = entity.getId();
        if (refList.cascade().toUpperCase().indexOf(Default.CASCADE_CREATE) != -1 && StringUtil.isEmpty(idStr)) {
            dao.insert(entity);
        }
        if (refList.cascade().toUpperCase().indexOf(Default.CASCADE_UPDATE) != -1 && !StringUtil.isEmpty(idStr)) {
            dao.save(entity);
        }
    }
}
