package org.streets.database.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import org.apache.tapestry5.ioc.services.ClassFab;
import org.apache.tapestry5.ioc.services.ClassFactory;
import org.apache.tapestry5.ioc.services.MethodSignature;
import org.apache.tapestry5.ioc.util.BodyBuilder;
import org.slf4j.Logger;
import org.streets.commons.util.ClassUtils;
import org.streets.commons.util.StringUtils;
import org.streets.database.Limits;
import org.streets.database.RecordHandler;
import org.streets.database.SQLUtils;
import org.streets.database.annotations.NestedFetch;

/**
 * For internal use only<br>
 * Builds a {@link RecordHandler} by reflection on the given {@link Class}
 * annotated with JPA
 * 
 * @author dzb
 * 
 */
public final class JPAHandlerBuilderUtils {

    /**
	 * 
	 * @param <T>
	 * @param clazz
	 *            the {@link Class} for which a {@link RecordHandler} is
	 *            built
	 * @return a {@link RecordHandler} for the given {@link Class} obtained
	 *         by introspection on the given {@link Class}
	 * @throws Exception
	 */
    public static String fv(String resultSet, String columnName, Class<?> fieldType) {
        StringBuilder sb = new StringBuilder();
        if (fieldType.isPrimitive()) {
            if (fieldType.equals(boolean.class)) {
                sb.append(resultSet).append(".getBoolean(\"");
                sb.append(columnName);
                sb.append("\")");
            } else if (fieldType.equals(int.class)) {
                sb.append(resultSet).append(".getInt(\"");
                sb.append(columnName);
                sb.append("\")");
            } else if (fieldType.equals(long.class)) {
                sb.append(resultSet).append(".getLong(\"");
                sb.append(columnName);
                sb.append("\")");
            } else if (fieldType.equals(double.class)) {
                sb.append(resultSet).append(".getDouble(\"");
                sb.append(columnName);
                sb.append("\")");
            } else if (fieldType.equals(float.class)) {
                sb.append(resultSet).append(".getFloat(\"");
                sb.append(columnName);
                sb.append("\")");
            } else if (fieldType.equals(short.class)) {
                sb.append(resultSet).append(".getShort(\"");
                sb.append(columnName);
                sb.append("\")");
            } else if (fieldType.equals(char.class)) {
                sb.append(resultSet).append(".getString(\"");
                sb.append(columnName);
                sb.append("\")");
            } else if (fieldType.equals(byte.class)) {
                sb.append(resultSet).append(".getByte(\"");
                sb.append(columnName);
                sb.append("\")");
            } else {
                throw new RuntimeException(fieldType.getCanonicalName() + " primitive type not recognized");
            }
        } else {
            if (fieldType.equals(String.class)) {
                sb.append(resultSet).append(".getString(\"").append(columnName).append("\")");
            } else if (fieldType.equals(Boolean.class)) {
                sb.append("Boolean.valueOf(").append(resultSet).append(".getBoolean(\"");
                sb.append(columnName).append("\"))");
            } else if (fieldType.equals(Long.class)) {
                sb.append("Long.valueOf(").append(resultSet).append(".getLong(\"");
                sb.append(columnName).append("\"))");
            } else if (fieldType.equals(Integer.class)) {
                sb.append("Integer.valueOf(").append(resultSet).append(".getInt(\"").append(columnName).append("\"))");
            } else if (fieldType.equals(Double.class)) {
                sb.append("Double.valueOf(").append(resultSet).append(".getDouble(\"").append(columnName).append("\"))");
            } else if (fieldType.equals(java.sql.Date.class)) {
                sb.append(resultSet).append(".getDate(\"").append(columnName).append("\")");
            } else if (fieldType.equals(java.sql.Time.class)) {
                sb.append(resultSet).append(".getTime(\"").append(columnName).append("\")");
            } else if (fieldType.equals(java.sql.Timestamp.class)) {
                sb.append(resultSet).append(".getTimestamp(\"").append(columnName).append("\")");
            } else if (fieldType.equals(Float.class)) {
                sb.append("Float.valueOf(").append(resultSet).append(".getFloat(\"").append(columnName).append("\"))");
            } else if (fieldType.equals(Short.class)) {
                sb.append("Short.valueOf(").append(resultSet).append(".getShort(\"").append(columnName).append("\"))");
            } else if (fieldType.equals(Byte.class)) {
                sb.append("Byte.valueOf(").append(resultSet).append(".getByte(\"").append(columnName).append("\"))");
            } else if (ClassUtils.isSubclassOf(fieldType, Collection.class)) {
                sb.append("null");
            } else {
                sb.append('(').append(fieldType.getCanonicalName()).append(")");
                sb.append(resultSet).append(".getObject(\"").append(columnName).append("\")");
            }
        }
        return sb.toString();
    }

    /**
	 * 生成实体beanRecordHandler的实现代码
	 * @param logger
	 * @param body
	 * @param resultSetName
	 * @param beanName
	 * @param beanType
	 */
    private static void processBeanFields(Logger logger, BodyBuilder body, String resultSetName, String beanName, Class<?> beanType) {
        Field[] fields = ClassUtils.getDeclaredFields(beanType);
        List<Field> nest_fields = new ArrayList<Field>();
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            if (annotations.length == 0) continue;
            if (field.getAnnotation(NestedFetch.class) != null) {
                nest_fields.add(field);
                continue;
            }
            if (field.getAnnotation(Column.class) == null && field.getAnnotation(Id.class) == null) {
                continue;
            }
            String c_n = SQLUtils.resolveColumnName(field);
            body.begin();
            body.addln("Object v = %s.getObject(\"%s\");", resultSetName, c_n);
            if ((field.getModifiers() | Modifier.PUBLIC) == Modifier.PUBLIC) {
                body.addln("if (v == null) { %s.%s = null;}", beanName, field.getName());
                body.addln("else {%s.%s = %s;}", beanName, field.getName(), fv(resultSetName, c_n, field.getType()));
            } else {
                String m_n = "set" + StringUtils.capitalize(field.getName());
                try {
                    beanType.getMethod(m_n, field.getType());
                    body.addln("if (v == null) { %s.%s(null);}", beanName, m_n);
                    body.addln("else {%s.%s(%s);}", beanName, m_n, fv(resultSetName, c_n, field.getType()));
                } catch (Exception e) {
                    logger.warn("Class [" + beanType + "] no mehotd[" + m_n + "]! Ignored set field[" + field.getName() + "]");
                    logger.error("JPAHandlerBuilder error : [" + beanType + "] can not find method" + m_n, e);
                }
            }
            body.end();
        }
        processNestFields(logger, body, beanName, nest_fields);
    }

    /**
	 * 处理NestFetch标注的类型
	 * @param logger
	 * @param body
	 * @param beanName
	 * @param nestFields
	 */
    private static void processNestFields(Logger logger, BodyBuilder body, String beanName, List<Field> nestFields) {
        for (Field field : nestFields) {
            String nest_sql = field.getAnnotation(NestedFetch.class).value();
            Class<?> nest_type = field.getAnnotation(NestedFetch.class).type();
            if (nest_type.equals(Object.class)) {
                nest_type = field.getType();
            }
            if (StringUtils.isEmpty(nest_sql)) continue;
            body.begin();
            body.addln("String[] names = %s.parseParameterNames(\"%s\");", SQLUtils.class.getName(), nest_sql);
            body.addln("Object[] params = %s.buildJPAFieldValues(names, bean);", SQLUtils.class.getName());
            body.addln("String _sql = %s.parsePreparedSQL(\"%s\");", SQLUtils.class.getName(), nest_sql);
            String nest_instance_name = StringUtils.lowerCase(beanName + "_" + field.getName());
            if (field.getType().isAssignableFrom(List.class)) {
                body.addln("%s %s = $2.query(_sql, Class.forName(\"%s\"), %s.none(), params);", List.class.getName(), nest_instance_name, nest_type.getName(), Limits.class.getName());
            } else {
                body.addln("%s %s = null;", nest_type.getName(), nest_instance_name);
                body.addln("%s _rs = $2.query(_sql, %s.none(), params);", ResultSet.class.getName(), Limits.class.getName());
                body.addln("if (_rs.next())");
                body.begin();
                body.add("%s = new %s();", nest_instance_name, nest_type.getName());
                processBeanFields(logger, body, "_rs", nest_instance_name, nest_type);
                body.end();
            }
            if ((field.getModifiers() | Modifier.PUBLIC) == Modifier.PUBLIC) {
                body.addln("%s.%s = %s;", beanName, field.getName(), nest_instance_name);
            } else {
                body.addln("%s.set%s(%s);", beanName, StringUtils.capitalize(field.getName()), nest_instance_name);
            }
            body.end();
        }
    }

    @SuppressWarnings("unchecked")
    public static synchronized <T> RecordHandler<T> create(ClassFactory classFactory, Class<T> clazz, Logger logger) throws Exception {
        ClassFab classFab = classFactory.newClass(RecordHandler.class);
        classFab.addInterface(RecordHandler.class);
        MethodSignature signature = new MethodSignature(Object.class, "mapping", new Class[] { java.sql.ResultSet.class, org.streets.database.SQLConnection.class }, new Class[] { Exception.class });
        BodyBuilder body = new BodyBuilder();
        body.begin();
        body.addln("%s bean = new %s();", clazz.getName(), clazz.getName());
        processBeanFields(logger, body, "$1", "bean", clazz);
        body.addln("return bean;");
        body.end();
        classFab.addMethod(Modifier.PUBLIC, signature, body.toString());
        if (logger.isDebugEnabled()) {
            logger.info("----------------------------------");
            logger.info(classFab.toString());
            logger.info("----------------------------------");
        }
        Object instance = classFab.createClass().newInstance();
        return (RecordHandler<T>) instance;
    }
}
