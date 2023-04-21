package de.armbruster.dezign2dods;

import java.util.*;
import de.armbruster.dezign2dods.doml3.*;
import de.armbruster.dezign2dods.doml41b.*;

/**
* internal representation of a table
*/
public class Attribute {

    protected Boolean isPk = Boolean.FALSE;

    protected Boolean isUnique = Boolean.FALSE;

    protected Boolean canBeNull = Boolean.FALSE;

    protected Boolean isQueryField = Boolean.FALSE;

    protected String name;

    protected String dbType;

    protected String doc = null;

    protected String defaultValue = null;

    protected String javaType = null;

    protected String size;

    protected String dec;

    public Attribute(String name) {
        this.name = name;
    }

    public void setIsPk(boolean isPk) {
        this.isPk = new Boolean(isPk);
    }

    public void setIsUnique(boolean isUnique) {
        this.isUnique = new Boolean(isUnique);
    }

    public void setIsRequired(boolean isRequired) {
        this.canBeNull = new Boolean(!isRequired);
    }

    public void setIsQueryField(boolean isQueryField) {
        this.isQueryField = new Boolean(isQueryField);
    }

    public String getIsQueryField() {
        return isQueryField.toString();
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
        if (dbType.equals("text")) this.size = "100000000";
    }

    public String getJavaType() {
        if (javaType == null) {
            String db = dbType.toUpperCase();
            if (javaTypes.containsKey(db)) return (String) javaTypes.get(db); else throw new RuntimeException("Can't convert DB Type " + db + " in java type");
        } else return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public void setDefault(String def) {
        if (def.length() > 0) this.defaultValue = def;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getName() {
        return name;
    }

    public Column getColumn_DOML3() {
        Column col = new ColumnImpl();
        col.setId(name);
        col.setUsedForQuery(isQueryField.toString());
        col.setIsUnique(isUnique.toString());
        col.setIsIndex("false");
        Type type = new TypeImpl();
        type.setDbType(dbType);
        if (!size.equals("0")) type.setSize(size);
        type.setJavaType(getJavaType());
        type.setCanBeNull(canBeNull.toString());
        col.setType(type);
        if (doc != null) {
            de.armbruster.dezign2dods.doml3.Javadoc jdoc = new de.armbruster.dezign2dods.doml3.JavadocImpl();
            jdoc.setValue(doc);
            col.setJavadoc(jdoc);
        }
        if (defaultValue != null) {
            InitialValue def = new InitialValueImpl();
            col.setInitialValue(def);
            def.setValue(defaultValue);
        }
        return col;
    }

    public Field getColumn_DOML41b() {
        Field col = new FieldImpl();
        col.setName(name);
        col.setDatabaseType(dbType);
        if (!size.equals("0")) col.setSize(size);
        col.setJavaType(getJavaType());
        col.setNullAllowed(canBeNull.toString());
        col.setCanQuery(isQueryField.toString());
        if (doc != null) {
            de.armbruster.dezign2dods.doml41b.Javadoc jdoc = new de.armbruster.dezign2dods.doml41b.JavadocImpl();
            jdoc.setValue(doc);
            col.setJavadoc(jdoc);
        }
        if (defaultValue != null) {
            Default def = new DefaultImpl();
            col.setXmldefault(def);
            def.setValue(defaultValue);
        }
        return col;
    }

    protected static Map javaTypes;

    static {
        javaTypes = new Hashtable();
        javaTypes.put("VARCHAR", "String");
        javaTypes.put("TEXT", "String");
        javaTypes.put("MEDIUMTEXT", "String");
        javaTypes.put("LONGTEXT", "String");
        javaTypes.put("DATE", "java.sql.Date");
        javaTypes.put("TIME", "java.sql.Time");
        javaTypes.put("DATETIME", "java.sql.Date");
        javaTypes.put("TIMESTAMP", "java.sql.Timestamp");
        javaTypes.put("INTEGER", "int");
        javaTypes.put("BOOLEAN", "boolean");
        javaTypes.put("ENUM", "String");
        javaTypes.put("BLOB", "byte[]");
        javaTypes.put("TINYINT", "short");
        javaTypes.put("SMALLINT", "short");
        javaTypes.put("CHAR", "String");
        javaTypes.put("DECIMAL", "float");
        javaTypes.put("FLOAT", "float");
        javaTypes.put("FLOAT4", "float");
        javaTypes.put("FLOAT8", "double");
        javaTypes.put("BIN", "boolean");
        javaTypes.put("BIGINT", "bigint");
    }
}
