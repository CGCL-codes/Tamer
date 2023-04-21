package com.sitechasia.webx.core.cache;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实体类属性缓存，缓存实体类的属性名称及类型等
 * 
 * @author MarkDong
 * @version 1.0
 * @see
 */
public class ClassPropertiesCache {

    ConcurrentHashMap<Class<? extends Object>, Map<String, PropertyInfo>> entityMap = new ConcurrentHashMap<Class<? extends Object>, Map<String, PropertyInfo>>();

    private static ClassPropertiesCache instance = null;

    private ClassPropertiesCache() {
    }

    public static synchronized ClassPropertiesCache getInstance() {
        if (instance == null) instance = new ClassPropertiesCache();
        return instance;
    }

    /**
	 * 将字符串值转换为某个实体类属性的对应类型
	 * 
	 * @param entity 实体类
	 * @param property 属性名称
	 * @param value 字符串型值
	 * @return 对应类型的值，若无法转换返回null
	 */
    public Object transportPropertyValue(Class<?> entity, String property, String value) {
        Class<?> propType = this.getPropClass(entity, property);
        if (propType.equals(int.class) || propType.equals(Integer.class)) return Integer.parseInt(value);
        if (propType.equals(float.class) || propType.equals(Float.class)) return Float.parseFloat(value);
        if (propType.equals(double.class) || propType.equals(Double.class)) return Double.parseDouble(value);
        if (propType.equals(byte.class) || propType.equals(Byte.class)) return Byte.parseByte(value);
        if (propType.equals(short.class) || propType.equals(Short.class)) return Short.parseShort(value);
        if (propType.equals(long.class) || propType.equals(Long.class)) return Long.parseLong(value);
        if (propType.equals(char.class) || propType.equals(Character.class)) return value.toCharArray()[0];
        if (propType.equals(boolean.class) || propType.equals(Boolean.class)) return Boolean.parseBoolean(value);
        return null;
    }

    /**
	 * 取得实体类的某个属性的类型
	 * 
	 * @param entity 实体类
	 * @param property 属性名称
	 * @return 该属性类型
	 */
    public Class<? extends Object> getPropClass(Class<? extends Object> entity, String property) {
        Class<? extends Object> propType = null;
        Map<String, PropertyInfo> propMap = entityMap.get(entity);
        if (propMap == null) {
            propMap = this.createPropMap(entity);
            Map<String, PropertyInfo> prev = entityMap.putIfAbsent(entity, propMap);
            if (prev != null) propMap = prev;
        }
        PropertyInfo info = propMap.get(property);
        if (info != null) {
            propType = info.type;
        }
        return propType;
    }

    /**
	 * 判断Token是否正确有效
	 * 
	 * @param token 待校验token
	 * @return true or false
	 */
    public boolean checkToken(CacheToken token) {
        Class<? extends Object> propType = this.getPropClass(token.getClazz(), token.getProperty());
        if (token.getOperator() == CacheToken.MATCH_ALL) return true;
        if (propType == null) {
            return false;
        } else if (!propType.equals(token.getValue().getClass())) {
            Class<? extends Object> valueClass = token.getValue().getClass();
            if (propType.equals(int.class) && valueClass.equals(Integer.class)) return true;
            if (propType.equals(float.class) && valueClass.equals(Float.class)) return true;
            if (propType.equals(double.class) && valueClass.equals(Double.class)) return true;
            if (propType.equals(byte.class) && valueClass.equals(Byte.class)) return true;
            if (propType.equals(short.class) && valueClass.equals(Short.class)) return true;
            if (propType.equals(long.class) && valueClass.equals(Long.class)) return true;
            if (propType.equals(char.class) && valueClass.equals(Character.class)) return true;
            if (propType.equals(boolean.class) && valueClass.equals(Boolean.class)) return true;
            return false;
        } else {
            return true;
        }
    }

    /**
	 * 利用反射机制获取Entity的所有属性及属性的类型
	 * 
	 * @param entity 实体类型
	 * @return 属性Map
	 */
    private Map<String, PropertyInfo> createPropMap(Class<? extends Object> entity) {
        if (entity == null) return null;
        Map<String, PropertyInfo> propMap = new HashMap<String, PropertyInfo>();
        Field[] fields = entity.getDeclaredFields();
        for (Field field : fields) {
            PropertyInfo info = new PropertyInfo();
            info.name = field.getName();
            info.type = field.getType();
            propMap.put(info.name, info);
        }
        return propMap;
    }

    class PropertyInfo {

        public String name;

        public Class<? extends Object> type;
    }
}
