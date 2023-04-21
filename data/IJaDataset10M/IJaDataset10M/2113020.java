package com.jspx.sober.util;

import com.jspx.sober.config.SoberColumn;
import com.jspx.sober.config.SoberNexus;
import com.jspx.sober.config.SoberTable;
import com.jspx.sober.config.SoberCalcUnique;
import com.jspx.utils.BeanUtil;
import com.jspx.utils.StringUtil;
import com.jspx.sober.annotation.*;
import com.jspx.sober.sequences.SequencesDAO;
import com.jspx.sober.NullEntity;
import java.lang.reflect.Field;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User:chenYuan (mail:cayurain@21cn.com)
 * Date: 2007-2-5
 * Time: 23:49:08
 */
public abstract class AnnotationUtils {

    private AnnotationUtils() {
    }

    /**
     * 自动生成ID
     * @param object
     * @param fieldName
     * @return
     */
    public static void autoSetId(Object object, String fieldName, SequencesDAO sequencesDAO) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            Id idf = field.getAnnotation(Id.class);
            if (idf != null) {
                if (idf.auto() && StringUtil.isNULL((String) BeanUtil.getProperty(object, field.getName(), null))) {
                    if (field.getType() == Integer.class || field.getType() == int.class) {
                        BeanUtil.setProperty(object, field.getName(), StringUtil.toInt(sequencesDAO.getNextKey(object.getClass().getName())));
                    } else if (field.getType() == Long.class || field.getType() == long.class) {
                        if ("seq".equalsIgnoreCase(idf.type())) BeanUtil.setProperty(object, field.getName(), StringUtil.toLong(sequencesDAO.getNextKey(object.getClass().getName()))); else BeanUtil.setProperty(object, field.getName(), UUID.randomUUID().getMostSignificantBits());
                    } else if (field.getType() == String.class || field.getType() == String.class) {
                        if ("seq".equalsIgnoreCase(idf.type())) BeanUtil.setProperty(object, field.getName(), sequencesDAO.getNextKey(object.getClass().getName())); else BeanUtil.setProperty(object, field.getName(), UUID.randomUUID().toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到数据库字段关系
     * @param cls 类
     * @return List<SoberColumn>
     */
    public static Map<String, SoberColumn> getSoberColumn(Class cls) {
        Map<String, SoberColumn> soberColumns = new LinkedHashMap<String, SoberColumn>();
        Field[] fields = BeanUtil.getDeclaredFields(cls);
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                SoberColumn soberColumn = new SoberColumn();
                soberColumn.setName(field.getName());
                soberColumn.setClassType(field.getType());
                soberColumn.setCaption(column.caption());
                soberColumn.setOption(column.option());
                soberColumn.setLength(column.length());
                soberColumn.setDefaultValue(column.defaultValue());
                soberColumn.setNotNull(column.notNull());
                soberColumns.put(field.getName(), soberColumn);
            }
        }
        return soberColumns;
    }

    /**
     * 得到映射关系
     * @param cls 类
     * @return List<SoberNexus>
     */
    public static Map<String, SoberNexus> getSoberNexus(Class cls) {
        Map<String, SoberNexus> soberColumns = new LinkedHashMap<String, SoberNexus>();
        Field[] fields = BeanUtil.getDeclaredFields(cls);
        for (Field field : fields) {
            Nexus nexus = field.getAnnotation(Nexus.class);
            if (nexus != null) {
                SoberNexus soberNexus = new SoberNexus();
                soberNexus.setMapping(nexus.mapping());
                soberNexus.setName(nexus.name());
                soberNexus.setTargetName(nexus.targetName());
                soberNexus.setTargetEntity(nexus.targetEntity());
                soberNexus.setOrderBy(nexus.orderBy());
                soberColumns.put(field.getName(), soberNexus);
            }
        }
        return soberColumns;
    }

    /**
     * 得到映射关系
     * @param cls 类
     * @return List<SoberNexus>
     */
    public static Map<String, SoberCalcUnique> getSoberCalcUnique(Class cls) {
        Map<String, SoberCalcUnique> soberCalcUniques = new LinkedHashMap<String, SoberCalcUnique>();
        Field[] fields = BeanUtil.getDeclaredFields(cls);
        for (Field field : fields) {
            CalcUnique calcUnique = field.getAnnotation(CalcUnique.class);
            if (calcUnique != null) {
                SoberCalcUnique soberCalcUnique = new SoberCalcUnique();
                soberCalcUnique.setName(field.getName());
                soberCalcUnique.setCaption(calcUnique.caption());
                soberCalcUnique.setSql(calcUnique.sql());
                if (calcUnique.entity().equals(NullEntity.class) || calcUnique.entity() == null) soberCalcUnique.setEntity(null); else soberCalcUnique.setEntity(calcUnique.entity());
                soberCalcUniques.put(field.getName(), soberCalcUnique);
            }
        }
        return soberCalcUniques;
    }

    /**
     * 得到映射关系中的表名
     * @param cls 类
     * @return 表名
     */
    public static Table getSoberTableName(Class cls) {
        Annotation[] annotation = cls.getAnnotations();
        for (Annotation anAnnotation : annotation) {
            if (anAnnotation instanceof Table) {
                return (Table) anAnnotation;
            }
        }
        return null;
    }

    /**
     * 生成 SoberTable
     * @param cls 实体对象
     * @return  SoberTable
     */
    public static SoberTable getSoberTable(Class cls) {
        SoberTable soberTable = new SoberTable();
        soberTable.setEntity(cls);
        Table table = getSoberTableName(cls);
        if (table != null) {
            soberTable.setTableName(table.name());
            soberTable.setTableCaption(table.caption());
        }
        soberTable.setColumns(getSoberColumn(cls));
        soberTable.setCalcUniques(getSoberCalcUnique(cls));
        soberTable.setNexusMap(getSoberNexus(cls));
        Field[] fields = BeanUtil.getDeclaredFields(cls);
        for (Field field : fields) {
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                soberTable.setPrimary(field.getName());
                soberTable.setAutoId(id.auto());
            }
        }
        return soberTable;
    }
}
