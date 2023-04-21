package com.togethersoft.modules.togmap;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * MappingModel.java
 *
 *
 * Created: Thu Oct  4 19:35:58 2001
 *
 * @author <a href="mailto:markus@blaurocks.de">Markus Blaurock</a>
 * @version $Revision: 1.7 $
 */
public class MappingModel {

    /**
	 * Creates a new <code>MappingModel</code> instance.
	 *
	 */
    public MappingModel() {
        myClasses = new HashMap();
        myAssociations = new HashMap();
        setForeignKeyPostfix("_id");
        typeMapping = new HashMap();
        initializeMapping();
    }

    /**
	 * Describe <code>addMappingClass</code> method here.
	 *
	 * @param mappingClass a <code>MappingClass</code> value
	 */
    public void addMappingClass(MappingClass mappingClass) {
        myClasses.put(mappingClass.getFullyQualifiedName(), mappingClass);
    }

    /**
	 * getting a mapping class if it is not there, create it and
	 * make a note in the changelog
	 *
	 * @param name a <code>String</code> value
	 * @return a <code>MappingClass</code> value
	 */
    public MappingClass getMappingClass(String name) {
        MappingClass x = (MappingClass) myClasses.get(name);
        if (x == null) {
            x = new MappingClass(name);
            ChangeLog.makeNote("new Class " + name + " added.");
            x.setModel(this);
            x.setId(IdGenerator.getInstance().getNext());
            if (name.length() > 0) {
                myClasses.put(name, x);
            }
        }
        return x;
    }

    /**
	 * getting a mapping class without adding to the changelog
	 *
	 * @param name a <code>String</code> value
	 * @return a <code>MappingClass</code> value
	 */
    public MappingClass getMappingClassNC(String name) {
        MappingClass x = (MappingClass) myClasses.get(name);
        if (x == null) {
            x = new MappingClass(name);
            x.setModel(this);
            x.setId(IdGenerator.getInstance().getNext());
            if (name.length() > 0) {
                myClasses.put(name, x);
            }
        }
        return x;
    }

    /**
	 * Describe <code>getMappingClassSet</code> method here.
	 *
	 * @return a <code>Set</code> value
	 */
    public Set getMappingClassSet() {
        return myClasses.keySet();
    }

    /**
	 * Describe <code>addMappingAssociation</code> method here.
	 *
	 * @param mappingAssociation a <code>MappingAssociation</code> value
	 */
    public void addMappingAssociation(MappingAssociation mappingAssociation) {
        myAssociations.put(mappingAssociation.getIdSet(), mappingAssociation);
    }

    /**
	 * look if the model already has such an association
	 *
	 * @param id a <code>Set</code> value
	 * @return a <code>boolean</code> value
	 */
    public boolean hasMappingAssociation(Set id) {
        return myAssociations.containsKey(id);
    }

    /**
	 * get a MappingAssociation object for the association with this id,
	 * create one if necessary
	 *
	 * @param id a <code>Set</code> value
	 * @return a <code>MappingAssociation</code> value
	 */
    public MappingAssociation getMappingAssociation(Set id) {
        MappingAssociation x = (MappingAssociation) myAssociations.get(id);
        if (x == null) {
            x = new MappingAssociation(id);
            x.setModel(this);
            x.setId(IdGenerator.getInstance().getNext());
            myAssociations.put(id, x);
        }
        return x;
    }

    /**
	 * returns a Set of ids of all Associations stored in this model
	 *
	 * @return a <code>Set</code> value
	 */
    public Set getMappingAssociationSet() {
        return myAssociations.keySet();
    }

    /**
	 * converts a java-Type into a SQL-Type. Uses a default mapping, 
	 * that can be overwritten by the configuration.
	 *
	 * @param classname the name of java.* class
	 * @return the SQL-Type as String
	 */
    public String getDatabaseType(String classname) {
        String x = (String) typeMapping.get(classname);
        if (x == null) {
            return classname;
        }
        return x;
    }

    /**
	 * Describe <code>readModel</code> method here.
	 *
	 */
    public void readModel() {
        Log.debug("MappingModel: readModel");
        MappingWriter cw = null;
        if (getMappingFileFormat().equals("Castor")) {
            cw = new CastorMappingWriter(this);
        }
        if (getMappingFileFormat().equals("OJB")) {
            cw = new OJBMappingWriter(this);
        }
        try {
            cw.readModel(mappingFile);
        } catch (IOException io) {
            Log.error("MappingModel: readModel IOException " + io);
        }
        Log.debug("MappingModel: readModel done");
    }

    /**
	 * Describe <code>writeModel</code> method here.
	 *
	 */
    public void writeModel() {
        Log.debug("MappingModel: writeModel");
        MappingWriter cw = null;
        if (getMappingFileFormat().equals("Castor")) {
            cw = new CastorMappingWriter(this);
        }
        if (getMappingFileFormat().equals("OJB")) {
            cw = new OJBMappingWriter(this);
        }
        try {
            cw.writeModel(mappingFile);
        } catch (IOException io) {
            Log.error("MappingModel: writeModel IOException " + io);
        }
        Log.debug("MappingModel: writeModel done");
    }

    /**
	 * Describe <code>writeSchema</code> method here.
	 *
	 */
    public void writeSchema() {
        Log.debug("MappingModel: writeSchema");
        SchemaWriter cw = null;
        if (getSchemaFileFormat().equals("Oracle")) {
            cw = new OracleSchemaWriter(this);
        }
        if (getSchemaFileFormat().equals("Postgres")) {
            cw = new PostgresSchemaWriter(this);
        }
        try {
            cw.writeSchema(schemaFile);
        } catch (IOException io) {
            Log.error("MappingModel: writeSchema IOException " + io);
        }
        Log.debug("MappingModel: writeSchema done");
    }

    /**
	 * Describe <code>initializeMapping</code> method here.
	 *
	 */
    protected void initializeMapping() {
        setTypeMapping("java.lang.String", "varchar");
        setTypeMapping("java.lang.Long", "bigint");
        setTypeMapping("java.lang.Integer", "integer");
        setTypeMapping("java.lang.Boolean", "bit");
        setTypeMapping("java.lang.Double", "float");
        setTypeMapping("java.lang.Short", "smallint");
        setTypeMapping("java.lang.Byte", "tinyint");
    }

    /**
	 * Describe <code>setTypeMapping</code> method here.
	 *
	 * @param javaClass a <code>String</code> value
	 * @param jdbcType a <code>String</code> value
	 */
    public void setTypeMapping(String javaClass, String jdbcType) {
        typeMapping.put(javaClass, jdbcType);
    }

    /**
	 * Get the value of primitiveTypesAllowed.
	 * @return value of primitiveTypesAllowed.
	 */
    public boolean isPrimitiveTypesAllowed() {
        return primitiveTypesAllowed;
    }

    /**
	 * Set the value of primitiveTypesAllowed.
	 * @param v  Value to assign to primitiveTypesAllowed.
	 */
    public void setPrimitiveTypesAllowed(boolean v) {
        this.primitiveTypesAllowed = v;
    }

    /**
	 * Get the value of includingCompositionClass.
	 * @return value of includingCompositionClass.
	 */
    public boolean isIncludingCompositionClass() {
        return includingCompositionClass;
    }

    /**
	 * Set the value of includingCompositionClass.
	 * @param v  Value to assign to includingCompositionClass.
	 */
    public void setIncludingCompositionClass(boolean v) {
        this.includingCompositionClass = v;
    }

    /**
	 * Get the value of mappingFileFormat.
	 * @return value of mappingFileFormat.
	 */
    public String getMappingFileFormat() {
        return mappingFileFormat;
    }

    /**
	 * Set the value of mappingFileFormat.
	 * @param v  Value to assign to mappingFileFormat.
	 */
    public void setMappingFileFormat(String v) {
        this.mappingFileFormat = v;
    }

    /**
	 * Get the value of mappingFile.
	 * @return value of mappingFile.
	 */
    public File getMappingFile() {
        return mappingFile;
    }

    /**
	 * Set the value of mappingFile.
	 * @param v  Value to assign to mappingFile.
	 */
    public void setMappingFile(File v) {
        this.mappingFile = v;
    }

    /**
	 * Get the value of foreignKeyPostfix.
	 * @return value of foreignKeyPostfix.
	 */
    public String getForeignKeyPostfix() {
        return foreignKeyPostfix;
    }

    /**
	 * Set the value of foreignKeyPostfix.
	 * @param v  Value to assign to foreignKeyPostfix.
	 */
    public void setForeignKeyPostfix(String v) {
        this.foreignKeyPostfix = v;
    }

    /**
	 * Get the value of debug.
	 * @return value of debug.
	 */
    public boolean isDebug() {
        return debug;
    }

    /**
	 * Set the value of debug.
	 * @param v  Value to assign to debug.
	 */
    public void setDebug(boolean v) {
        this.debug = v;
    }

    /**
	 * Get the value of schemaFileFormat.
	 * @return value of schemaFileFormat.
	 */
    public String getSchemaFileFormat() {
        return schemaFileFormat;
    }

    /**
	 * Set the value of schemaFileFormat.
	 * @param v  Value to assign to schemaFileFormat.
	 */
    public void setSchemaFileFormat(String v) {
        this.schemaFileFormat = v;
    }

    /**
	 * Get the value of schemaFile.
	 * @return value of schemaFile.
	 */
    public File getSchemaFile() {
        return schemaFile;
    }

    /**
	 * Set the value of schemaFile.
	 * @param v  Value to assign to schemaFile.
	 */
    public void setSchemaFile(File v) {
        this.schemaFile = v;
    }

    private File schemaFile;

    private String schemaFileFormat;

    private boolean debug;

    private File mappingFile;

    private String mappingFileFormat;

    private String foreignKeyPostfix;

    private boolean includingCompositionClass;

    private boolean primitiveTypesAllowed;

    private Map myClasses;

    private Map myAssociations;

    private Map typeMapping;
}
