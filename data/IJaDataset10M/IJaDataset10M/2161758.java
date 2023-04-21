package org.nakedobjects.persistence.hibernate.nof2hbm;

import java.beans.Introspector;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DOMWriter;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Mappings;
import org.nakedobjects.application.collection.ListInternalCollection;
import org.nakedobjects.object.NakedObjectField;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.NakedObjectSpecificationException;
import org.nakedobjects.object.NakedObjects;
import org.nakedobjects.object.application.BasicApplicationContext;
import org.nakedobjects.object.application.User;
import org.nakedobjects.persistence.hibernate.property.ConverterFactory;
import org.nakedobjects.persistence.hibernate.property.ListInternalCollectionAccessor;
import org.nakedobjects.persistence.hibernate.property.NakedPropertyAccessor;
import org.nakedobjects.persistence.hibernate.property.OidAccessor;
import org.nakedobjects.persistence.hibernate.property.PropertyConverter;
import org.nakedobjects.persistence.hibernate.property.PropertyHelper;
import org.nakedobjects.persistence.hibernate.property.TimestampAccessor;
import org.nakedobjects.persistence.hibernate.property.UserAccessor;
import org.nakedobjects.persistence.hibernate.property.VersionAccessor;
import org.nakedobjects.persistence.hibernate.type.DomainModelResourceType;
import org.nakedobjects.utility.NakedObjectConfiguration;
import org.nakedobjects.utility.NakedObjectRuntimeException;

/**
 * Create hbm.xml mapping document for a Naked Object domain class.
 * <p>If property valueFieldAccess is true values are mapped with access="field", but field names must be
 * the same as the property name (i.e. method getXxx() and field xxx).  This will allow Hibernate to circumvent
 * any container.resolve() calls inside the getters, and dirty setting in setters.
 * <p>Similarly, if property associationFieldAccess is true assocations are mapped with access="field".
 */
public class Nof2HbmXml {

    private static final String PRIMARY_KEY_PREFIX = "PK";

    private static final String PRIMARY_KEY_SUFFIX = "ID";

    private static final String FOREIGN_KEY_PREFIX = "FK";

    private static final String COLUMN_PROPERTY_PREFIX = MappingHelper.PROPERTY_PREFIX + "column.";

    private static final String ID_TYPE = "long";

    private static final String FILE_SEPERATOR = System.getProperty("file.separator");

    private static final Logger LOG = Logger.getLogger(Nof2HbmXml.class);

    private final ConverterFactory converterFactory;

    private PersistentNakedClasses persistentClasses;

    /**
	 * Hibernate type for lists and arrays - "list" by default (generates "position" column on database)
	 * or "bag" (no "position" column)
	 */
    private final String listType;

    /**
	 * If "one" will not create a seperate collection table for each collection, if "many" it will
	 */
    private final String collections;

    /**
	 * Do we assume all relationships are bidirectional, so they are mapped with
	 * "inverse" in Hibernate
	 */
    private final boolean defaultAssociationFieldAccess;

    private final boolean defaultValueFieldAccess;

    private final String versionProperty;

    private final String modifiedByProperty;

    private final String modifiedOnProperty;

    private final String versionAccess;

    private final String modifiedByAccess;

    private final String modifiedOnAccess;

    /**
	 * If mapping associations/properties using field access then the prefix can
	 * be set in the configutation.
	 */
    private final String fieldPrefix;

    private final Properties columnNames;

    public Nof2HbmXml() {
        converterFactory = ConverterFactory.getInstance();
        NakedObjectConfiguration config = NakedObjects.getConfiguration();
        listType = config.getString(MappingHelper.PROPERTY_PREFIX + "list", "bag");
        collections = config.getString(MappingHelper.PROPERTY_PREFIX + "collections", "one");
        defaultAssociationFieldAccess = config.getBoolean(MappingHelper.PROPERTY_PREFIX + "associationFieldAccess", false);
        defaultValueFieldAccess = config.getBoolean(MappingHelper.PROPERTY_PREFIX + "valueFieldAccess", false);
        versionProperty = config.getString(MappingHelper.PROPERTY_PREFIX + "version");
        modifiedByProperty = config.getString(MappingHelper.PROPERTY_PREFIX + "modified_by");
        modifiedOnProperty = config.getString(MappingHelper.PROPERTY_PREFIX + "modified_on");
        versionAccess = config.getString(MappingHelper.PROPERTY_PREFIX + "version.access");
        modifiedByAccess = config.getString(MappingHelper.PROPERTY_PREFIX + "modified_by.access");
        modifiedOnAccess = config.getString(MappingHelper.PROPERTY_PREFIX + "modified_on.access");
        fieldPrefix = config.getString(MappingHelper.PROPERTY_PREFIX + "fieldPrefix", "");
        columnNames = config.getProperties(COLUMN_PROPERTY_PREFIX);
        MappingHelper.loadRequiredClasses();
        persistentClasses = new PersistentNakedClasses();
        if (LOG.isDebugEnabled()) {
            LOG.debug(persistentClasses.debugString());
        }
    }

    /**
     * Add all classes loaded in Naked Objects to the hibernate configuration
     * @param configuration
     */
    public void configure(final Configuration cfg) {
        Mappings mappings;
        if (cfg.getClass().getName().endsWith("AnnotationConfiguration")) {
            AnnotationConfiguration acfg = (AnnotationConfiguration) cfg;
            mappings = acfg.createExtendedMappings();
        } else {
            mappings = cfg.createMappings();
        }
        for (Iterator iter = persistentClasses.getPersistentClasses(); iter.hasNext(); ) {
            PersistentNakedClass persistentClass = (PersistentNakedClass) iter.next();
            String className = persistentClass.getName();
            if (mappings.getClass(className) == null) {
                LOG.debug("Binding persistent class " + className);
                org.w3c.dom.Document doc = createDom(persistentClass);
                cfg.addDocument(doc);
            } else {
                LOG.info("Class [" + className + "] is already mapped, skipping.. ");
            }
        }
    }

    protected org.w3c.dom.Document createDom(PersistentNakedClass persistentNakedClass) {
        DOMWriter writer = new DOMWriter();
        try {
            return writer.write(createDocument(persistentNakedClass));
        } catch (DocumentException e) {
            throw new NakedObjectRuntimeException(e);
        }
    }

    protected Document createDocument(final PersistentNakedClass persistentNakedClass) {
        LOG.info("Creating hbm.xml for class " + persistentNakedClass.getName());
        Document document = DocumentHelper.createDocument();
        document.addDocType("hibernate-mapping", "-//Hibernate/Hibernate Mapping DTD 3.0//EN", "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd");
        Element root = document.addElement("hibernate-mapping");
        if (persistentNakedClass.getName().equals(User.class.getName())) {
            createUserMapping(root);
            return document;
        }
        if (persistentNakedClass.isDuplicateUnqualifiedClassName()) {
            root.addAttribute("auto-import", "false");
        }
        boolean associationFieldAccess = defaultAssociationFieldAccess;
        boolean valueFieldAccess = defaultValueFieldAccess;
        if (persistentNakedClass.getName().equals(BasicApplicationContext.class.getName())) {
            associationFieldAccess = true;
            valueFieldAccess = true;
        }
        Element classElement;
        if (persistentNakedClass.getParent().isRoot()) {
            classElement = bindRootClass(persistentNakedClass, root, valueFieldAccess);
        } else {
            classElement = bindSubClass(persistentNakedClass, root);
        }
        if (persistentNakedClass.isAbstract()) {
            classElement.addAttribute("abstract", "true");
        }
        if (persistentNakedClass.isRequireVersion()) {
            bindVersion(classElement, valueFieldAccess);
        }
        String versionId = versionProperty == null ? null : versionProperty.toLowerCase();
        String modifiedById = modifiedByProperty == null ? null : modifiedByProperty.toLowerCase();
        String modifiedOnId = modifiedOnProperty == null ? null : modifiedOnProperty.toLowerCase();
        NakedObjectField[] allFields = persistentNakedClass.getUniqueFields();
        for (int i = 0; i < allFields.length; i++) {
            NakedObjectField field = allFields[i];
            if (allFields[i].isDerived()) {
                continue;
            }
            if (field.isCollection()) {
                bindCollection(classElement, persistentNakedClass, field, associationFieldAccess);
            } else if (field.isObject()) {
                LOG.debug("Binding persistent association [" + field.getName() + "]");
                Element assnElement;
                if (persistentClasses.isPersistentClass(field.getSpecification().getFullName())) {
                    assnElement = bindAssociation(classElement, persistentNakedClass, field, associationFieldAccess);
                } else {
                    LOG.warn("Binding persistent association as an ANY assocation! [class=" + persistentNakedClass.getName() + ", field=" + field.getName() + "]");
                    assnElement = classElement.addElement("any").addAttribute("name", getPropertyName(field, associationFieldAccess));
                    bindAnyAssociation(assnElement, field);
                }
                if (associationFieldAccess) {
                    assnElement.addAttribute("access", "field");
                }
            } else {
                String fieldId = field.getId();
                if ((fieldId.equals("id") && field.getName().equals("Id")) || fieldId.equals(versionId) || fieldId.equals(modifiedById) || fieldId.equals(modifiedOnId)) {
                    continue;
                } else {
                    bindProperty(field, classElement, valueFieldAccess);
                }
            }
        }
        return document;
    }

    /**
	 * Special case to create mapping for User class in a 
	 * non-standard way.
	 */
    private void createUserMapping(Element element) {
        Element classElement = element.addElement("class").addAttribute("name", User.class.getName()).addAttribute("table", "NOUSER").addAttribute("abstract", "true");
        classElement.addElement("id").addAttribute("name", "id").addAttribute("column", "PKuserID").addAttribute("type", "string");
        classElement.addElement("discriminator").addAttribute("column", "discriminator").addAttribute("type", "string");
        classElement.addElement("property").addAttribute("name", "name").addAttribute("column", "name").addAttribute("type", "string");
    }

    private void bindProperty(NakedObjectField field, Element classElement, boolean valueFieldAccess) {
        LOG.debug("Binding persistent property [" + field.getName() + "]");
        Element property = classElement.addElement("property").addAttribute("column", columnName(field.getId()));
        setType(field, property, "type", true, valueFieldAccess);
        Attribute access = property.attribute("access");
        boolean fieldAccess = access != null && access.getStringValue().equals("field");
        property.addAttribute("name", getPropertyName(field, fieldAccess));
    }

    private String columnName(final String column) {
        return columnNames.getProperty(COLUMN_PROPERTY_PREFIX + column, column);
    }

    /**
	 * Bind a root class (i.e. top of hierarchy)
	 */
    private Element bindRootClass(final PersistentNakedClass persistentNakedClass, final Element root, final boolean valueFieldAccess) {
        Element classElement = root.addElement("class").addAttribute("name", persistentNakedClass.getName()).addAttribute("table", persistentNakedClass.getTableName());
        Element id = classElement.addElement("id").addAttribute("name", "id").addAttribute("column", PRIMARY_KEY_PREFIX + persistentNakedClass.getTableName().toLowerCase() + PRIMARY_KEY_SUFFIX);
        addIdType(persistentNakedClass.getSpecification(), id, "type", true, valueFieldAccess);
        id.addElement("generator").addAttribute("class", "native");
        if (persistentNakedClass.hasSubClasses()) {
            classElement.addElement("discriminator").addAttribute("column", "discriminator").addAttribute("type", "string");
            if (!persistentNakedClass.isAbstract()) {
                classElement.addAttribute("discriminator-value", persistentNakedClass.getTableName());
            }
        }
        return classElement;
    }

    private void addIdType(final NakedObjectSpecification spec, final Element id, final String attributeName, final boolean nakedPropertyAccessor, final boolean valueFieldAccess) {
        try {
            NakedObjectField idField = spec.getField("id");
            if (idField.getName().equals("Id")) {
                setType(idField, id, attributeName, nakedPropertyAccessor, valueFieldAccess);
                return;
            }
        } catch (NakedObjectSpecificationException ignore) {
        }
        Method getIdMethod = extractGetMethod(spec, "Id");
        if (getIdMethod != null) {
            id.addAttribute(attributeName, getIdMethod.getReturnType().getName());
        } else {
            id.addAttribute(attributeName, ID_TYPE);
            if (nakedPropertyAccessor) {
                id.addAttribute("access", OidAccessor.class.getName());
            }
        }
    }

    private Element bindSubClass(final PersistentNakedClass persistentNakedClass, final Element root) {
        Element classElement = root.addElement("subclass").addAttribute("name", persistentNakedClass.getName()).addAttribute("extends", persistentNakedClass.getParent().getName());
        if (!persistentNakedClass.isAbstract()) {
            classElement.addAttribute("discriminator-value", persistentNakedClass.getTableName());
        }
        return classElement;
    }

    /**
	 * Get the property name for the field
	 * @param field
	 * @param fieldAccess 
	 */
    private String getPropertyName(final NakedObjectField field, final boolean fieldAccess) {
        String name = field.getName().replace(" ", "");
        if (fieldAccess) {
            name = fieldPrefix + name;
        }
        return Introspector.decapitalize(name);
    }

    /**
	 * Create a simple assocation (to a class/interface)
	 * @param persistentNakedClass 
	 * @param associationFieldAccess 
	 */
    private Element bindAssociation(final Element classElement, final PersistentNakedClass persistentNakedClass, final NakedObjectField field, final boolean associationFieldAccess) {
        Association assn = persistentNakedClass.getAssociation(field.getId());
        if (assn != null && assn.getField().isObject()) {
            if (assn.isInverse()) {
                return classElement.addElement("many-to-one").addAttribute("name", getPropertyName(field, associationFieldAccess)).addAttribute("column", FOREIGN_KEY_PREFIX + field.getId()).addAttribute("class", field.getSpecification().getFullName()).addAttribute("unique", "true");
            } else {
                return classElement.addElement("one-to-one").addAttribute("name", getPropertyName(field, associationFieldAccess)).addAttribute("class", field.getSpecification().getFullName()).addAttribute("property-ref", getPropertyName(assn.getField(), associationFieldAccess));
            }
        }
        return classElement.addElement("many-to-one").addAttribute("name", getPropertyName(field, associationFieldAccess)).addAttribute("column", FOREIGN_KEY_PREFIX + field.getId()).addAttribute("class", field.getSpecification().getFullName());
    }

    /**
	 * Create an assocation using an any mapping.
	 */
    private void bindAnyAssociation(final Element anyElement, final NakedObjectField field) {
        addIdType(field.getSpecification(), anyElement, "id-type", false, false);
        anyElement.addElement("column").addAttribute("name", field.getId() + "type");
        anyElement.addElement("column").addAttribute("name", field.getId() + PRIMARY_KEY_SUFFIX);
        return;
    }

    private Element bindCollection(final Element classElement, final PersistentNakedClass persistentNakedClass, final NakedObjectField field, final boolean associationFieldAccess) {
        LOG.debug("Binding persistent collection [" + field.getName() + "]");
        NakedObjectSpecification spec = persistentNakedClass.getSpecification();
        Class returnType = getReturnType(field, spec);
        String collectionType = getCollectionType(returnType);
        Element collElement = classElement.addElement(collectionType);
        boolean fieldAccess = associationFieldAccess;
        if (ListInternalCollection.class.isAssignableFrom(returnType)) {
            collElement.addAttribute("access", ListInternalCollectionAccessor.class.getName());
            fieldAccess = false;
        } else if (associationFieldAccess) {
            collElement.addAttribute("access", "field");
        }
        collElement.addAttribute("name", getPropertyName(field, fieldAccess));
        Element keyElement = collElement.addElement("key");
        String associationType = null;
        if (field.getSpecification().isResource()) {
            keyElement.addAttribute("column", FOREIGN_KEY_PREFIX + persistentNakedClass.getTableName().toLowerCase());
            associationType = "element";
            collElement.addAttribute("table", persistentNakedClass.getTableName() + "_" + field.getId().toUpperCase());
        } else {
            Association assn = persistentNakedClass.getAssociation(field.getId());
            if (assn != null) {
                NakedObjectField associatedField = assn.getField();
                if (associatedField.isObject()) {
                    associationType = "one-to-many";
                    collElement.addAttribute("inverse", "true");
                    keyElement.addAttribute("column", FOREIGN_KEY_PREFIX + associatedField.getId());
                } else {
                    associationType = "many-to-many";
                    keyElement.addAttribute("column", FOREIGN_KEY_PREFIX + persistentNakedClass.getTableName().toLowerCase());
                    PersistentNakedClass associatedClass = assn.getPersistentClass();
                    if (assn.isInverse()) {
                        collElement.addAttribute("table", associatedClass.getTableName() + "_" + persistentNakedClass.getTableName());
                        collElement.addAttribute("inverse", "true");
                    } else {
                        collElement.addAttribute("table", persistentNakedClass.getTableName() + "_" + associatedClass.getTableName());
                    }
                }
            } else if (persistentClasses.isPersistentClass(field.getSpecification().getFullName())) {
                associationType = collections + "-to-many";
                if ("many".equals(collections)) {
                    collElement.addAttribute("table", persistentNakedClass.getTableName() + "_" + field.getId().toUpperCase());
                    keyElement.addAttribute("column", FOREIGN_KEY_PREFIX + persistentNakedClass.getTableName().toLowerCase());
                } else {
                    keyElement.addAttribute("column", FOREIGN_KEY_PREFIX + persistentNakedClass.getTableName().toLowerCase() + "_" + field.getId());
                }
            } else {
                LOG.warn("Binding persistent association as an ANY assocation! [class=" + spec.getFullName() + ", field=" + field.getName() + "]");
                associationType = "many-to-any";
                collElement.addAttribute("table", persistentNakedClass.getTableName() + "_" + field.getId().toUpperCase());
                keyElement.addAttribute("column", FOREIGN_KEY_PREFIX + persistentNakedClass.getTableName().toLowerCase());
                Element anyElement = collElement.addElement("many-to-any");
                bindAnyAssociation(anyElement, field);
            }
        }
        if (returnType.isArray()) {
            collElement.addAttribute("lazy", "true");
        }
        if (collectionType.equals("list")) {
            Element listIndexElement = collElement.addElement("list-index");
            if (associationType.startsWith("one")) {
                listIndexElement.addAttribute("column", persistentNakedClass.getTableName().toLowerCase() + "_" + field.getId() + "_idx");
            } else {
                listIndexElement.addAttribute("column", "position");
            }
        }
        if (associationType.equals("element")) {
            collElement.addElement("element").addAttribute("type", DomainModelResourceType.class.getName()).addAttribute("column", field.getId().toLowerCase());
        } else if (!associationType.equals("many-to-any")) {
            Element assnElement = collElement.addElement(associationType).addAttribute("class", field.getSpecification().getFullName());
            if (associationType.equals("many-to-many")) {
                PersistentNakedClass associatedClass = persistentClasses.getPersistentClass(field.getSpecification().getFullName());
                assnElement.addAttribute("column", FOREIGN_KEY_PREFIX + associatedClass.getTableName().toLowerCase());
            }
        }
        return collElement;
    }

    private String getCollectionType(final Class returnType) {
        String type;
        if (returnType.equals(List.class)) {
            type = listType;
        } else if (returnType.equals(Set.class) || returnType.equals(SortedSet.class)) {
            type = "set";
        } else if (returnType.equals(Map.class) || returnType.equals(SortedMap.class)) {
            type = "map";
        } else if (ListInternalCollection.class.isAssignableFrom(returnType)) {
            type = listType;
        } else if (returnType.isArray()) {
            type = listType;
        } else {
            throw new NakedObjectRuntimeException("Unsupported collection type " + returnType.getName());
        }
        return type;
    }

    private Class getReturnType(final NakedObjectField field, final NakedObjectSpecification spec) {
        Method getMethod = extractPublicGetMethod(spec, field.getName());
        if (getMethod == null) {
            throw new NakedObjectRuntimeException("Cannot find get method for collection " + field.getName() + " in spec " + spec);
        }
        Class returnType = getMethod.getReturnType();
        return returnType;
    }

    private Method extractPublicGetMethod(final NakedObjectSpecification spec, final String name) {
        String nameWithNoSpaces = name.replace(" ", "");
        Method method = null;
        try {
            Class clazz = Class.forName(spec.getFullName());
            try {
                method = clazz.getMethod("get" + nameWithNoSpaces, (Class[]) null);
            } catch (NoSuchMethodException nsme) {
                try {
                    method = clazz.getMethod("get_" + nameWithNoSpaces, (Class[]) null);
                } catch (NoSuchMethodException nsme2) {
                }
            }
        } catch (Exception e) {
            throw new NakedObjectRuntimeException(e);
        }
        return method;
    }

    /**
	 * Method may be public/private/protected
	 */
    private Method extractGetMethod(final NakedObjectSpecification spec, final String name) {
        String nameWithNoSpaces = name.replace(" ", "");
        try {
            Class clazz = Class.forName(spec.getFullName());
            return getGetMethod(nameWithNoSpaces, clazz);
        } catch (Exception e) {
            throw new NakedObjectRuntimeException(e);
        }
    }

    private Method getGetMethod(final String name, final Class clazz) {
        if (clazz == Object.class || clazz == null) {
            return null;
        }
        Method method = null;
        try {
            method = clazz.getDeclaredMethod("get" + name, (Class[]) null);
        } catch (NoSuchMethodException nsme) {
            try {
                method = clazz.getDeclaredMethod("get_" + name, (Class[]) null);
            } catch (NoSuchMethodException nsme2) {
            }
        }
        if (method == null) {
            if (clazz.isInterface()) {
                Class[] interfaces = clazz.getInterfaces();
                for (int i = 0; i < interfaces.length; i++) {
                    method = getGetMethod(name, interfaces[i]);
                    if (method != null) {
                        return method;
                    }
                }
            } else {
                return getGetMethod(name, clazz.getSuperclass());
            }
        }
        return method;
    }

    private void setType(final NakedObjectField field, final Element property, final String attributeName, final boolean nakedPropertyAccessor, final boolean valueFieldAccess) {
        if (field.getSpecification().isResource()) {
            property.addAttribute("type", DomainModelResourceType.class.getName());
            if (valueFieldAccess) {
                property.addAttribute("access", "field");
            }
            return;
        }
        PropertyConverter propertyConverter = converterFactory.getConverter(field);
        if (propertyConverter != null) {
            property.addAttribute("type", propertyConverter.getHibernateType());
            if (nakedPropertyAccessor) {
                property.addAttribute("access", NakedPropertyAccessor.class.getName());
            } else if (valueFieldAccess) {
                property.addAttribute("access", "field");
            }
            return;
        }
        String fullName = field.getSpecification().getFullName();
        if (fullName.startsWith("org.nakedobjects.application.value.")) {
            String type = "org.nakedobjects.persistence.hibernate.type." + fullName.substring("org.nakedobjects.application.value.".length()) + "Type";
            property.addAttribute(attributeName, type);
        } else {
            property.addAttribute(attributeName, fullName);
        }
        if (valueFieldAccess) {
            property.addAttribute("access", "field");
        }
    }

    private void bindVersion(final Element classElement, final boolean valueFieldAccess) {
        Element version = classElement.addElement("version").addAttribute("name", "naked_version").addAttribute("type", "long").addAttribute("access", VersionAccessor.class.getName());
        setVersionColumnMeta(version, "version", versionProperty, versionAccess, valueFieldAccess);
        Element user = classElement.addElement("property").addAttribute("name", PropertyHelper.MODIFIED_BY).addAttribute("type", "string").addAttribute("access", UserAccessor.class.getName());
        setVersionColumnMeta(user, "modified_by", modifiedByProperty, modifiedByAccess, valueFieldAccess);
        Element timestamp = classElement.addElement("property").addAttribute("name", PropertyHelper.MODIFIED_ON).addAttribute("type", "timestamp").addAttribute("access", TimestampAccessor.class.getName());
        setVersionColumnMeta(timestamp, "modified_on", modifiedOnProperty, modifiedOnAccess, valueFieldAccess);
    }

    private void setVersionColumnMeta(final Element version, final String defaultColumn, final String property, final String access, final boolean valueFieldAccess) {
        if (property == null) {
            version.addAttribute("column", defaultColumn);
        } else {
            version.addAttribute("column", property.toLowerCase());
            String propertyWithPrefix = valueFieldAccess ? fieldPrefix + property : property;
            version.addElement("meta").addAttribute("attribute", PropertyHelper.NAKED_PROPERTY).addText(propertyWithPrefix);
            if (access != null) {
                version.addElement("meta").addAttribute("attribute", PropertyHelper.NAKED_ACCESS).addText(access);
            } else if (valueFieldAccess) {
                version.addElement("meta").addAttribute("attribute", PropertyHelper.NAKED_ACCESS).addText("field");
            }
        }
    }

    /**
	 * Export Hibernate mapping files for all Naked Objects currently in
	 * NakedObjects.
	 * @param outDir
	 */
    public void exportHbmXml(final String basedir) {
        OutputFormat outformat = OutputFormat.createPrettyPrint();
        File basedirFile = new File(basedir);
        for (Iterator iter = persistentClasses.getPersistentClasses(); iter.hasNext(); ) {
            PersistentNakedClass persistentClass = (PersistentNakedClass) iter.next();
            LOG.debug("Exporting hbm.xml for " + persistentClass.getName());
            Document doc = createDocument(persistentClass);
            String className = persistentClass.getName();
            String dir = "";
            String file;
            int pos = className.lastIndexOf(".");
            if (pos < 0) {
                file = className;
            } else {
                file = className.substring(pos + 1);
                dir = className.substring(0, pos).replace(".", FILE_SEPERATOR);
            }
            file += ".hbm.xml";
            File outputDir = new File(basedirFile, dir);
            outputDir.mkdirs();
            File outFile = new File(outputDir, file);
            LOG.info("Writing " + outFile.getAbsolutePath());
            serializeToXML(doc, outFile, outformat);
        }
    }

    private void serializeToXML(final Document doc, final File outFile, final OutputFormat outformat) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outFile);
            XMLWriter writer = new XMLWriter(fos, outformat);
            writer.write(doc);
            writer.flush();
        } catch (Exception e) {
            throw new NakedObjectRuntimeException(e);
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (Exception ignore) {
            }
        }
    }
}
