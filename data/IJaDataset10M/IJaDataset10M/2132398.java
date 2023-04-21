package org.carp.engine;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.carp.assemble.Assemble;
import org.carp.exception.CarpException;
import org.carp.type.TypesMapping;

public class QueryMetaData extends MetaData {

    private List<String> nameList = new ArrayList<String>();

    private List<Field> fieldList = new ArrayList<Field>();

    private Set<String> names = new HashSet<String>();

    public QueryMetaData(Class<?> cls, ResultSet rs) throws Exception {
        super(rs);
        initClassField(cls);
        processMetadata(cls);
    }

    private void processMetadata(Class<?> cls) throws Exception {
        List<String> cols = this.getColumns();
        this.getAssembles().clear();
        this.getColumnJavaType().clear();
        for (String col : cols) {
            String name = this.getFieldName(col.toLowerCase());
            if (name.equals("rowNum")) continue;
            if (!this.names.contains(name)) throw new CarpException("��ѯ�ֶ��б��е��ֶ��� Class " + cls.getName() + " ,û�ж�Ӧ�� Field : " + name);
            nameList.add(name);
            Field f = cls.getDeclaredField(name);
            fieldList.add(f);
            this.getColumnJavaType().add(f.getType());
            this.getAssembles().add((Assemble) TypesMapping.getAssembleClass(f.getType()).newInstance());
            if (logger.isDebugEnabled()) logger.debug("Column : " + name + ", Type : " + f.getType());
        }
    }

    private void initClassField(Class<?> cls) {
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) names.add(f.getName());
    }

    private String getFieldName(String col) {
        String[] cols = col.split("_");
        String field = "";
        for (String s : cols) {
            if (s.trim().equals("")) continue;
            if (field.equals("")) field = s; else {
                if (s.length() == 1) field += s.toUpperCase(); else field += s.substring(0, 1).toUpperCase() + s.substring(1);
            }
        }
        return field;
    }

    public List<String> getNameList() {
        return nameList;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public Field getField(int index) {
        return this.fieldList.get(index);
    }
}
