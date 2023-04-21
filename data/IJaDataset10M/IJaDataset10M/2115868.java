package org.jaffa.persistence.engines.jdbcengine.configservice;

import java.lang.reflect.AccessibleObject;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jaffa.persistence.engines.jdbcengine.configservice.exceptions.ClassMetaDataValidationRuntimeException;
import org.jaffa.persistence.engines.jdbcengine.util.Introspection;

/** This class holds all the meta deta pertaining to a particular persistent object */
public class ClassMetaData {

    private static final Logger log = Logger.getLogger(ClassMetaData.class);

    private URL m_xmlFileUrl;

    private String m_className;

    private String m_table;

    private boolean m_lockable = true;

    private Map<String, PropertyMetaData> m_fields = new LinkedHashMap<String, PropertyMetaData>();

    private Collection<String> m_keys = new LinkedList<String>();

    private Collection<String> m_nonAutoKeys = new LinkedList<String>();

    private Collection<String> m_attributes = new LinkedList<String>();

    /**
     * Constructs a new ClassMetaData object with the specified class name.
     * @param classname The fully-qualified classname of the class this meta object is to represent.
     */
    public ClassMetaData(String classname) {
        m_className = classname;
    }

    /**
     * Set the xmlFileUrl property of this meta data object.
     * @param xmlFileUrl The File object used to describe this persistent object.
     */
    public void setXmlFileUrl(URL xmlFileUrl) {
        m_xmlFileUrl = xmlFileUrl;
    }

    /**
     * Set the classname property of this meta data object.
     * @param classname The fully qualified class name of the class this meta
     * object represents.
     */
    public void setClassName(String classname) {
        m_className = classname;
    }

    /**
     * Set the table property of this meta data object
     *
     * @param table The database table name used to store this object
     */
    public void setTable(String table) {
        m_table = table;
    }

    /**
     * Set the lockable property of this meta data object
     * @param lockable The value of the lockable property.
     */
    public void setLockable(boolean lockable) {
        m_lockable = lockable;
    }

    /**
     * Add a primary key field attribute to this meta data object.
     * @param attName The name of the primary key attribute to be added.
     * @param type The Java type of the primary key attribute.
     * @param autogen Indicates if the key will be auto-generated.
     */
    public void addKeyField(String attName, String type, boolean autogen) {
        m_keys.add(attName);
        if (!autogen) m_nonAutoKeys.add(attName);
        PropertyMetaData pmd = obtainPropertyMetaData(attName);
        pmd.m_type = type;
        pmd.m_primaryKey = true;
        pmd.m_autogenKey = autogen;
    }

    /**
     * Add an attribute to this meta data object.
     * @param attName The attribute to be added.
     * @param type The Java type of the attribute to be added.
     */
    public void addAttribute(String attName, String type) {
        m_attributes.add(attName);
        PropertyMetaData pmd = obtainPropertyMetaData(attName);
        pmd.m_type = type;
        pmd.m_primaryKey = false;
        pmd.m_autogenKey = false;
    }

    /**
     * Add a new sql name for an attribute to this meta object.
     * @param attName The name of the attribute for which the sql name is to be added.
     * @param sqlName The sql name of the attribute.
     */
    public void addSqlName(String attName, String sqlName) {
        obtainPropertyMetaData(attName).m_sqlName = sqlName;
    }

    /**
     *  Add sql type data for a particular attribute in this meta data object.
     * @param attName The name of the attribute for which sql type is to be added.
     * @param type The sql type for the attribute.
     */
    public void addSqlType(String attName, String type) {
        obtainPropertyMetaData(attName).m_sqlType = type;
    }

    /**
     * Add a member for an attribute to this meta object.
     * @param attName The name of the attribute for which the member is to be added.
     * @param memberName The name of the member variable.
     */
    public void addMember(String attName, String memberName) {
        obtainPropertyMetaData(attName).m_member = memberName;
    }

    /**
     *  Adds the rpad value for a particular attribute in this meta data object.
     * @param attName The name of the attribute for which the rpad value is to be added.
     * @param rpad The padding to be applied to this attribute.
     */
    public void addRpad(String attName, Integer rpad) {
        obtainPropertyMetaData(attName).m_rpad = rpad;
    }

    /**
     * Uses the Introspection service to ensure that the correct accessors and
     * mutators exist for each attribtue specified in the mapping file. The runtime exception
     * ClassMetaDataValidationRuntimeException is thrown in case any error occurs in the introspection,
     * or if all accessors/mutators could not be located for all the attributes.
     */
    public void validate() {
        try {
            Class clazz = Class.forName(m_className);
            Map<String, String> fields = new LinkedHashMap<String, String>();
            Map<String, String> members = new LinkedHashMap<String, String>();
            for (PropertyMetaData pmd : m_fields.values()) {
                fields.put(pmd.m_name, pmd.m_type);
                if (pmd.m_member != null) members.put(pmd.m_name, pmd.m_member);
            }
            Map<String, AccessibleObject> accessors = Introspection.getAccessors(clazz, fields, members);
            Map<String, AccessibleObject> mutators = Introspection.getMutators(clazz, fields, members);
            if (accessors.size() < (m_attributes.size() + m_keys.size())) {
                String str = "All accessors could not be found for the attributes. Class='" + m_className + "', Mapping file='" + (m_xmlFileUrl != null ? m_xmlFileUrl.getFile() : null) + '\'';
                log.error(str);
                throw new ClassMetaDataValidationRuntimeException(str);
            } else if (mutators.size() < (m_attributes.size() + m_keys.size())) {
                String str = "All mutators could not be found for the attributes. Class='" + m_className + "', Mapping file='" + (m_xmlFileUrl != null ? m_xmlFileUrl.getFile() : null) + '\'';
                log.error(str);
                throw new ClassMetaDataValidationRuntimeException(str);
            }
            for (Map.Entry<String, AccessibleObject> me : accessors.entrySet()) getPropertyMetaData(me.getKey()).m_accessor = me.getValue();
            for (Map.Entry<String, AccessibleObject> me : mutators.entrySet()) getPropertyMetaData(me.getKey()).m_mutator = me.getValue();
            m_keys = Collections.unmodifiableCollection(m_keys);
            m_nonAutoKeys = Collections.unmodifiableCollection(m_nonAutoKeys);
            m_attributes = Collections.unmodifiableCollection(m_attributes);
        } catch (ClassNotFoundException e) {
            String str = "Class not found '" + m_className + "'  for the mapping file '" + (m_xmlFileUrl != null ? m_xmlFileUrl.getFile() : null) + '\'';
            log.error(str, e);
            throw new ClassMetaDataValidationRuntimeException(str, e);
        }
    }

    /**
     * Returns the URL object that was parsed to obtain the meta data for this
     * object.
     * @return The URL of the xml File that describes this object.
     */
    public URL getXmlFileUrl() {
        return m_xmlFileUrl;
    }

    /**
     * Returns the fully-qualified class name of the class that this meta object
     * represents.
     * @return Fully qualified class name of the class that this meta object
     * represents.
     */
    public String getClassName() {
        return m_className;
    }

    /**
     * Return the database table name that the object represented by this meta
     * object is to be stored in.
     * @return Database table name that this object is stored in.
     */
    public String getTable() {
        return m_table;
    }

    /**
     * Return the lockable property for this meta object.
     * @return the lockable property for this meta object..
     */
    public boolean isLockable() {
        return m_lockable;
    }

    /**
     * Collection of the key fields.
     * <p>
     * <b>Since 1.3</b> - This include both manual and auto genrated key fields, use
     * getNonAutoKeyFieldNames() to get just the manual key fields.
     *
     * @return Collection of the key fields.
     */
    public Collection<String> getAllKeyFieldNames() {
        return m_keys;
    }

    /**
     * Returns Collection of non-auto-generated key fields.
     * @since 1.3
     * @return Collection of non-auto-generated key fields.
     */
    public Collection<String> getNonAutoKeyFieldNames() {
        return m_nonAutoKeys;
    }

    /**
     * Returns Collection of non-primary-key attributes of the class.
     * @return Collection of non-primary-key attributes of the class.
     */
    public Collection<String> getAttributes() {
        return m_attributes;
    }

    /**
     * Returns true if the specified attribute is a key field.
     * @param attributeName The name of the attribute to be checked.
     * @return true if the specified attribute is a key field.
     */
    public boolean isKeyField(String attributeName) {
        PropertyMetaData pmd = getPropertyMetaData(attributeName);
        return pmd != null ? pmd.m_primaryKey : null;
    }

    /**
     * Returns the Java type of the specified attribute.
     * @param attributeName The name of the attribute to be checked.
     * @return The Java type of the specified attribute.
     */
    public String getType(String attributeName) {
        PropertyMetaData pmd = getPropertyMetaData(attributeName);
        return pmd != null ? pmd.m_type : null;
    }

    /**
     * Returns the name of the sql column in which the specified attribute
     * is stored, as defined in the mapping file.
     * @param attributeName Name of the attribute for which the sql name is required.
     * @return The column name for the specified attribute.
     */
    public String getSqlName(String attributeName) {
        PropertyMetaData pmd = getPropertyMetaData(attributeName);
        return pmd != null ? pmd.m_sqlName : null;
    }

    /**
     * Returns the database sql type for the specified attribute, as defined in
     * the mapping file.
     * @param attributeName Name of the attribute for which sql type is required.
     * @return The sql type for the specified attribtue.
     */
    public String getSqlType(String attributeName) {
        PropertyMetaData pmd = getPropertyMetaData(attributeName);
        return pmd != null ? pmd.m_sqlType : null;
    }

    /**
     * Returns the rpad value for the specified attribute, as defined in
     * the mapping file.
     * @param attributeName Name of the attribute for which rpad value is required.
     * @return The rpad value for the specified attribtue.
     */
    public Integer getRpad(String attributeName) {
        PropertyMetaData pmd = getPropertyMetaData(attributeName);
        return pmd != null ? pmd.m_rpad : null;
    }

    /**
     * Returns the accessor Field/Method for the specified attribute.
     * @param attributeName Name of the attribute for which accessor is required.
     * @return The accessor Field/Method for the attribute.
     */
    public AccessibleObject getAccessor(String attributeName) {
        PropertyMetaData pmd = getPropertyMetaData(attributeName);
        return pmd != null ? pmd.m_accessor : null;
    }

    /**
     * Returns the mutator Field/Method for the specified attribute.
     * @param attributeName Name of the attribute for which mutator is required.
     * @return The mutator Field/Method for the attribute.
     */
    public AccessibleObject getMutator(String attributeName) {
        PropertyMetaData pmd = getPropertyMetaData(attributeName);
        return pmd != null ? pmd.m_mutator : null;
    }

    /** This will obtain the PropertyMetaData instance for the specified attribute.
     * @param attributeName Name of the attribute.
     * @return the PropertyMetaData instance for the specified attribute.
     */
    private PropertyMetaData getPropertyMetaData(String attributeName) {
        return (PropertyMetaData) m_fields.get(obtainCaseInsensitiveName(attributeName));
    }

    /** This will obtain the PropertyMetaData instance for the specified attribute.
     * A new PropertyMetaData instance will be created, if one doesn't already exist.
     * @param attributeName Name of the attribute.
     * @return the PropertyMetaData instance for the specified attribute.
     */
    private PropertyMetaData obtainPropertyMetaData(String attributeName) {
        String caseInsensitiveName = obtainCaseInsensitiveName(attributeName);
        PropertyMetaData pmd = (PropertyMetaData) m_fields.get(caseInsensitiveName);
        if (pmd == null) {
            pmd = new PropertyMetaData();
            pmd.m_name = attributeName;
            m_fields.put(caseInsensitiveName, pmd);
        }
        return pmd;
    }

    /** Returns the upper-case version of the input, which is used for performing case-insensitive
     * lookup on the fieldMap.
     * @param attributeName Name of the attribute.
     * @return the upper-case version of the input.
     */
    private String obtainCaseInsensitiveName(String attributeName) {
        return attributeName.toUpperCase();
    }

    /**
     * This will hold the data passed via the <field> element inside the mapping file
     */
    public class PropertyMetaData {

        private String m_name;

        private String m_type;

        private String m_member;

        private boolean m_primaryKey;

        private boolean m_autogenKey;

        private String m_sqlName;

        private String m_sqlType;

        private Integer m_rpad;

        private AccessibleObject m_accessor;

        private AccessibleObject m_mutator;
    }
}
