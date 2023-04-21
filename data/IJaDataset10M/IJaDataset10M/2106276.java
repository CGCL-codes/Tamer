package org.j2eebuilder.driver;

import java.io.*;
import java.util.*;
import org.jdom.*;
import org.jdom.Element;
import org.jdom.Attribute;
import org.apache.log4j.BasicConfigurator;
import org.j2eebuilder.util.LogManager;
import org.j2eebuilder.*;

/**
 * 
 * @(#)DefaultBuilderDriver.java 1.3.1 10/15/2002
 * 
 *                               Default driver implementation for reading
 *                               j2ee-builder.xml .
 * 
 *                               <relation>
 * 
 *                               <name></name>
 * 
 *                               <description></description>
 * 
 *                               <relation-attributes>
 * 
 *                               <attribute-name>activityTypeID</attribute-name>
 * 
 *                               <relation-attributes>
 * 
 *                               <component-ref>
 * 
 *                               <name>ActivityType</name>
 * 
 *                               <relation-attributes>
 * 
 *                               <attribute-name>activityTypeID</attribute-name>
 * 
 *                               <relation-attributes> <!-- optional for 1..*
 *                               relationship definitions --> <factory-method>
 *                               <name>findByGroupID</name> <type>select</type>
 *                               <method-params> <method-param>
 *                               <param-seq>1</param-seq> <param-value>
 *                               <component-name>Group</component-name>
 *                               <useBean-id>groupBean</useBean-id>
 *                               <useBean-class
 *                               -method>getGroupID</useBean-class-method>
 *                               </param-value> </method-param> </method-params>
 *                               </factory-method>
 * 
 *                               </component-ref>
 * 
 *                               <type>1</type>
 * 
 *                               </relation>
 * 
 * 
 * 
 * 
 * 
 *                               <interface>
 * 
 *                               <name>input</name>
 * 
 *                               <description>input interface</description>
 * 
 *                               <interface-rule>
 * 
 *                               <name>input1</name>
 * 
 *                               <description>input interface level
 *                               1</description>
 * 
 *                               <interface-attribute>
 * 
 *                               <attribute-name>inputID</attribute-name>
 * 
 *                               <source-attribute>
 * 
 *                               <component-name>Buyer</component-name>
 * 
 *                               <attribute-name>buyerID</attribute-name>
 * 
 *                               <attribute-value />
 * 
 *                               </source-attribute>
 * 
 *                               </interface-attribute>
 * 
 *                               <interface-attribute>
 * 
 *                               <attribute-name>inputTypeID</attribute-name>
 * 
 *                               <source-attribute>
 * 
 *                               <component-name>InputType</component-name>
 * 
 *                               <attribute-name>inputTypeID</attribute-name>
 * 
 *                               <attribute-value></attribute-value>
 * 
 *                               </source-attribute>
 * 
 *                               </interface-attribute>
 * 
 *                               <interface-attribute>
 * 
 *                               <attribute-name>description</attribute-name>
 * 
 *                               <source-attribute>
 * 
 *                               <component-name>Buyer</component-name>
 * 
 *                               <attribute-name>description</attribute-name>
 * 
 *                               <attribute-value />
 * 
 *                               </source-attribute>
 * 
 *                               </interface-attribute>
 * 
 *                               </interface-rule>
 * 
 *                               </interface>
 * 
 *                               </component>
 */
public class DefaultBuilderDriver extends AbstractBuilderDriver {

    private static transient LogManager log = new LogManager(DefaultBuilderDriver.class);

    public static final String INTERFACE = "interface";

    public static final String INTERFACE_RULE = "interface-rule";

    public static final String INTERFACE_ATTRIBUTE = "interface-attribute";

    public static final String SOURCE_ATTRIBUTE = "source-attribute";

    public static final String COMPONENT_NAME = "component-name";

    public static final String ATTRIBUTE_VALUE = "attribute-value";

    public static final String APPLICATION = "application";

    public static final String MODULES = "modules";

    public static final String MODULE = "module";

    public static final String MODULE_REF = "module-ref";

    public static final String MODULE_URL = "url";

    public static final String NAME = "name";

    public static final String DESCRIPTION = "description";

    public static final String SIZE = "size";

    public static final String MAXLENGTH = "maxlength";

    public static final String TYPE = "type";

    public static final String LAYOUTS = "layouts";

    public static final String LAYOUT = "layout";

    public static final String LAYOUT_URL = "url";

    public static final String COMPONENT = "component";

    public static final String EXTENDS = "extends";

    public static final String IS_SUBTYPE = "true";

    public static final String SERVLET_PATH = "servlet-path";

    public static final String NAVIGATE_TO = "navigate-to";

    public static final String IMAGE = "image";

    /**
	 * NAVIGATOR: used to overwrite the default Search.jsp <component>
	 * <navigator
	 * >/com/ohioedge/j2ee/api/org/proc/ActivityNavigator.jsp</navigator>
	 */
    public static final String NAVIGATOR = "navigator";

    public static final String DATA = "data";

    public static final String MENU = "menu";

    public static final String SERVER_NAME = "server-name";

    public static final String TABLE_NAME = "tableName";

    public static final String JSP_DECLARATION = "non-managed-beans";

    public static final String STATE_MANAGEMENT_PREFERENCE = "state-management-preference";

    public static final String CACHE_MANAGEMENT_PREFERENCE = "cache-management-preference";

    public static final String USEBEAN = "non-managed-bean";

    public static final String USEBEAN_ID = "non-managed-bean-id";

    public static final String USEBEAN_CLASS_METHOD_NAME = "non-managed-bean-class-method";

    public static final String ID = "id";

    public static final String SCOPE = "scope";

    public static final String CLASS_NAME = "class-name";

    public static final String BEAN_PROPERTY = "bean-property";

    public static final String PROPERTY_NAME = "property-name";

    public static final String PRIMARY_KEY = "primary-key";

    public static final String ATTRIBUTE_NAME = "attribute-name";

    public static final String CONSTRAINT = "constraint";

    public static final String REMOTE = "remote";

    public static final String HOME = "home";

    public static final String ENTITY = "entity";

    public static final String JNDI = "jndi";

    public static final String MANAGED_BEAN = "managed-bean";

    public static final String ATTRIBUTES = "attributes";

    public static final String ATTRIBUTE = "attribute";

    public static final String VALUE = "value";

    public static final String WRITABLE_ATTRIBUTES = "write-attributes";

    public static final String RELATION = "relation";

    public static final String TRAVERSE_MODE = "traverse-mode";

    public static final String RELATION_ATTRIBUTES = "relation-attributes";

    public static final String COMPONENT_REFERENCE = "component-ref";

    public static final String PARAM_SEQ = "param-seq";

    public static final String PARAM_NAME = "param-name";

    public static final String PARAM_VALUE = "param-value";

    public static final String METHOD_PARAMS = "method-params";

    public static final String METHOD_PARAM = "method-param";

    public static final String FACTORY_METHODS = "factory-methods";

    public static final String FACTORY_METHOD = "factory-method";

    /**
	 * 
	 * Constructor.
	 */
    public DefaultBuilderDriver() {
        super();
    }

    /**
	 * 
	 * Get a short text describing the driver.
	 * 
	 * @return a text
	 */
    public String getDescription() {
        return "Driver for reading adapter definition files";
    }

    /**
	 * 
	 * Get the name of the driver.
	 * 
	 * @return a text
	 */
    public String getName() {
        return "Adapter Text 0.8";
    }

    /**
	 * 
	 * Export Adapter definition
	 * 
	 * 
	 */
    public Document exportApplicationDefinition(ApplicationDefinition ad) throws BuilderDriverException {
        return null;
    }

    /**
	 * 
	 * Import a adapter definition, i.e., build it from an xml document.
	 * 
	 * @return a adapter definition
	 * 
	 * @param doc
	 *            an xml document
	 * 
	 * @exception an
	 *                DefinitionException is thrown if import fails
	 */
    public ApplicationDefinition importApplicationDefinition(Document doc) throws DefinitionException {
        try {
            Element root = doc.getRootElement();
            if (compare(root.getName(), APPLICATION)) {
                return importApplicationDefinition(root);
            } else {
                throw new DefinitionException("No adapter root element found in xml source");
            }
        } catch (DuplicateDefinitionException e) {
            throw new DefinitionException("DuplicateDefinitionException::" + e.getMessage());
        }
    }

    /**
	 * 
	 * Import a adapter definition.
	 * 
	 * @return a adapter definition
	 * 
	 * @param e
	 *            an xml element
	 * 
	 * @exception an
	 *                DefinitionException is thrown if import fails
	 */
    public ApplicationDefinition importApplicationDefinition(Element e) throws DuplicateDefinitionException, DefinitionException {
        ApplicationDefinition def = new ApplicationDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), NAME)) {
                def.setName(next.getTextTrim());
            } else if (compare(next.getName(), DESCRIPTION)) {
                def.setDescription(next.getTextTrim());
            } else if (compare(next.getName(), MODULES)) {
                def.setModulesDefinition(importModulesDefinition(next));
            } else if (compare(next.getName(), LAYOUTS)) {
                def.setLayoutsDefinition(importLayoutsDefinition(next));
            } else {
            }
        }
        return def;
    }

    /**
	 * 
	 * Export Adapter definition
	 * 
	 * 
	 */
    public Document exportModuleDefinition(ModuleDefinition ad) throws BuilderDriverException {
        return null;
    }

    /**
	 * 
	 * Import a adapter definition, i.e., build it from an xml document.
	 * 
	 * @return a adapter definition
	 * 
	 * @param doc
	 *            an xml document
	 * 
	 * @exception an
	 *                DefinitionException is thrown if import fails
	 */
    public ModuleDefinition importModuleDefinition(Document doc) throws DefinitionException {
        try {
            Element root = doc.getRootElement();
            if (compare(root.getName(), MODULE)) {
                return importModuleDefinition(root);
            } else {
                throw new DefinitionException("No adapter root element found in xml source");
            }
        } catch (DuplicateDefinitionException e) {
            throw new DefinitionException("DuplicateDefinitionException::" + e.getMessage());
        }
    }

    /**
	 * 
	 * Import a adapter definition.
	 * 
	 * @return a adapter definition
	 * 
	 * @param e
	 *            an xml element
	 * 
	 * @exception an
	 *                DefinitionException is thrown if import fails
	 */
    public ModuleDefinition importModuleDefinition(Element e) throws DuplicateDefinitionException, DefinitionException {
        ModuleDefinition def = new ModuleDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), NAME)) {
                def.setName(next.getTextTrim());
            } else if (compare(next.getName(), DESCRIPTION)) {
                def.setDescription(next.getTextTrim());
            } else if (compare(next.getName(), LAYOUT)) {
                def.setLayout(next.getTextTrim());
            } else if (compare(next.getName(), COMPONENT)) {
                def.addComponentDefinition(importComponentDefinition(next));
            } else {
            }
        }
        return def;
    }

    /**
	 * 
	 * Import a file definition.
	 * 
	 * @return a file definition
	 * 
	 * @param e
	 *            an xml element
	 * 
	 * @exception an
	 *                DefinitionException is thrown if import fails
	 */
    public ComponentDefinition importComponentDefinition(Element e) throws DefinitionException {
        ComponentDefinition def = new ComponentDefinition();
        Attribute superComponentName = e.getAttribute(EXTENDS);
        if (superComponentName != null) {
            def.setSubtype(this.IS_SUBTYPE);
            def.setSuperComponentName(superComponentName.getValue());
            log.debug("setting super component [" + superComponentName.getValue() + "]: of subtype component []");
        }
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), NAME)) {
                def.setName(next.getTextTrim());
            } else if (compare(next.getName(), DESCRIPTION)) {
                def.setDescription(next.getTextTrim());
            } else if (compare(next.getName(), TYPE)) {
                def.setType(next.getTextTrim());
            } else if (compare(next.getName(), SERVLET_PATH)) {
                def.setServletPath(next.getTextTrim());
            } else if (compare(next.getName(), NAVIGATE_TO)) {
                def.setController(next.getTextTrim());
            } else if (compare(next.getName(), NAVIGATOR)) {
                def.setNavigator(next.getTextTrim());
            } else if (compare(next.getName(), HOME)) {
                def.setHome(next.getTextTrim());
            } else if (compare(next.getName(), DATA)) {
                def.setData(next.getTextTrim());
            } else if (compare(next.getName(), MENU)) {
                def.setMenu(next.getTextTrim());
            } else if (compare(next.getName(), SERVER_NAME)) {
                def.setServerName(next.getTextTrim());
            } else if (compare(next.getName(), TABLE_NAME)) {
                def.setTableName(next.getTextTrim());
            } else if (compare(next.getName(), IMAGE)) {
                def.setImage(next.getTextTrim());
            } else if (compare(next.getName(), CACHE_MANAGEMENT_PREFERENCE)) {
                def.setCacheManagementPreference(next.getTextTrim());
            } else if (compare(next.getName(), JSP_DECLARATION)) {
                def.setNonManagedBeansDefinition(importJspDeclaration(next));
            } else if (compare(next.getName(), BEAN_PROPERTY)) {
                def.setBeanPropertyDefinition(importBeanPropertyDefinition(next));
            } else if (compare(next.getName(), ATTRIBUTES)) {
                def.setAttributesDefinition(importAttributesDefinition(next));
            } else if (compare(next.getName(), WRITABLE_ATTRIBUTES)) {
                def.setWritableAttributesDefinition(importWritableAttributesDefinition(next));
            } else if (compare(next.getName(), MANAGED_BEAN)) {
                def.setManagedBeanDefinition(importEjbDefinition(next));
            } else if (compare(next.getName(), RELATION)) {
                def.addRelationDefinition(importRelationDefinition(next));
            } else if (compare(next.getName(), INTERFACE)) {
                def.addInterfaceDefinition(importInterfaceDefinition(next));
            } else {
                log.warn("importComponentDefinition(): Cannot build file definition from element <" + next.getName() + ">..");
            }
        }
        return def;
    }

    /**
	 * 
	 * Import a adapter definition.
	 * 
	 * @return a adapter definition
	 * 
	 * @param e
	 *            an xml element
	 * 
	 * @exception an
	 *                DefinitionException is thrown if import fails
	 */
    public NonManagedBeansDefinition importJspDeclaration(Element e) throws DefinitionException {
        NonManagedBeansDefinition def = new NonManagedBeansDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), STATE_MANAGEMENT_PREFERENCE)) {
                def.setStateManagementPreference(next.getTextTrim());
            } else if (compare(next.getName(), USEBEAN)) {
                def.addNonManagedBeanDefinition(importNonManagedBeanDefinition(next));
            } else {
            }
        }
        return def;
    }

    /**
	 * 
	 * Import a record definition.
	 * 
	 * @return a record definition
	 * 
	 * @param e
	 *            an xml element
	 * 
	 * @exception an
	 *                DefinitionException is thrown if import fails
	 */
    public NonManagedBeanDefinition importNonManagedBeanDefinition(Element e) throws DefinitionException {
        NonManagedBeanDefinition def = new NonManagedBeanDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), ID)) {
                def.setId(next.getTextTrim());
            } else if (compare(next.getName(), SCOPE)) {
                def.setScope(next.getTextTrim());
            } else if (compare(next.getName(), CLASS_NAME)) {
                def.setClassName(next.getTextTrim());
            } else if (compare(next.getName(), TYPE)) {
                def.setType(next.getTextTrim());
            } else if (compare(next.getName(), ATTRIBUTES)) {
                def.setAttributesDefinition(importAttributesDefinition(next));
            } else {
            }
        }
        return def;
    }

    /**
	 * 
	 * Import a record definition.
	 * 
	 * @return a record definition
	 * 
	 * @param e
	 *            an xml element
	 * 
	 * @exception an
	 *                DefinitionException is thrown if import fails
	 */
    public BeanPropertyDefinition importBeanPropertyDefinition(Element e) throws DefinitionException {
        BeanPropertyDefinition def = new BeanPropertyDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), PROPERTY_NAME)) {
                def.setPropertyName(next.getTextTrim());
            } else {
            }
        }
        return def;
    }

    public PrimaryKeyDefinition importPrimaryKeyDefinition(Element e) throws DefinitionException {
        PrimaryKeyDefinition def = new PrimaryKeyDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), CLASS_NAME)) {
                def.setClassName(next.getTextTrim());
            } else if (compare(next.getName(), ATTRIBUTE_NAME)) {
                def.addAttributeName(next.getTextTrim());
            } else {
            }
        }
        return def;
    }

    public RelationDefinition importRelationDefinition(Element e) throws DefinitionException {
        RelationDefinition def = new RelationDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), NAME)) {
                def.setName(next.getTextTrim());
            } else if (compare(next.getName(), DESCRIPTION)) {
                def.setDescription(next.getTextTrim());
            } else if (compare(next.getName(), TYPE)) {
                def.setType(next.getTextTrim());
            } else if (compare(next.getName(), TRAVERSE_MODE)) {
                def.setTraverseMode(next.getTextTrim());
            } else if (compare(next.getName(), COMPONENT_REFERENCE)) {
                def.setComponentReferenceDefinition(importComponentReferenceDefinition(next));
            } else if (compare(next.getName(), RELATION_ATTRIBUTES)) {
                def.setRelationAttributesDefinition(importRelationAttributesDefinition(next));
            } else {
            }
        }
        return def;
    }

    public ComponentReferenceDefinition importComponentReferenceDefinition(Element e) throws DefinitionException {
        ComponentReferenceDefinition def = new ComponentReferenceDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), NAME)) {
                def.setName(next.getTextTrim());
            } else if (compare(next.getName(), RELATION_ATTRIBUTES)) {
                def.setRelationAttributesDefinition(importRelationAttributesDefinition(next));
            } else if (compare(next.getName(), FACTORY_METHOD)) {
                def.setFactoryMethodDefinition(importFactoryMethodDefinition(next));
            } else {
            }
        }
        return def;
    }

    public ManagedBeanDefinition importEjbDefinition(Element e) throws DefinitionException {
        ManagedBeanDefinition def = new ManagedBeanDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), REMOTE)) {
                def.setRemote(next.getTextTrim());
            } else if (compare(next.getName(), HOME)) {
                def.setHome(next.getTextTrim());
            } else if (compare(next.getName(), ENTITY)) {
                def.setEntity(next.getTextTrim());
            } else if (compare(next.getName(), JNDI)) {
                def.setJndi(next.getTextTrim());
            } else if (compare(next.getName(), PRIMARY_KEY)) {
                def.setPrimaryKeyDefinition(importPrimaryKeyDefinition(next));
            } else if (compare(next.getName(), FACTORY_METHODS)) {
                def.setFactoryMethodsDefinition(importFactoryMethodsDefinition(next));
            } else {
            }
        }
        return def;
    }

    public AttributeDefinition importAttributeDefinition(Element e) throws DefinitionException {
        AttributeDefinition def = new AttributeDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), NAME)) {
                log.debug("nonmanagedbeandefinition: adding attribute [" + next.getTextTrim() + "]");
                def.setName(next.getTextTrim());
            } else if (compare(next.getName(), TYPE)) {
                def.setType(next.getTextTrim());
            } else if (compare(next.getName(), PROPERTY_NAME)) {
                def.setPropertyName(next.getTextTrim());
            } else if (compare(next.getName(), DESCRIPTION)) {
                def.setDescription(next.getTextTrim());
            } else if (compare(next.getName(), SIZE)) {
                if (next.getTextTrim() != null && next.getTextTrim().trim() != "") {
                    def.setSize(new Integer(next.getTextTrim()));
                }
            } else if (compare(next.getName(), MAXLENGTH)) {
                if (next.getTextTrim() != null && next.getTextTrim().trim() != "") {
                    def.setMaxLength(new Integer(next.getTextTrim()));
                }
            } else if (compare(next.getName(), CONSTRAINT)) {
                def.addConstraint(next.getTextTrim());
            } else if (compare(next.getName(), ATTRIBUTE_VALUE)) {
                def.setAttributeValue(next.getTextTrim());
            } else {
            }
        }
        return def;
    }

    public AttributesDefinition importAttributesDefinition(Element e) throws DefinitionException {
        AttributesDefinition def = new AttributesDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), ATTRIBUTE)) {
                def.addAttributeDefinition(this.importAttributeDefinition(next));
            } else {
            }
        }
        return def;
    }

    public WritableAttributesDefinition importWritableAttributesDefinition(Element e) throws DefinitionException {
        WritableAttributesDefinition def = new WritableAttributesDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), ATTRIBUTE_NAME)) {
                def.addAttributeName(next.getTextTrim());
            } else {
            }
        }
        return def;
    }

    public RelationAttributesDefinition importRelationAttributesDefinition(Element e) throws DefinitionException {
        RelationAttributesDefinition def = new RelationAttributesDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), ATTRIBUTE_NAME)) {
                def.addAttributeName(next.getTextTrim());
            } else {
            }
        }
        return def;
    }

    /**
	 * 
	 * Interface tag
	 **/
    public InterfaceDefinition importInterfaceDefinition(Element e) throws DefinitionException {
        InterfaceDefinition def = new InterfaceDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), NAME)) {
                def.setName(next.getTextTrim());
            } else if (compare(next.getName(), DESCRIPTION)) {
                def.setDescription(next.getTextTrim());
            } else if (compare(next.getName(), INTERFACE_RULE)) {
                def.addInterfaceRuleDefinition(importInterfaceRuleDefinition(next));
            } else {
            }
        }
        return def;
    }

    public InterfaceRuleDefinition importInterfaceRuleDefinition(Element e) throws DefinitionException {
        InterfaceRuleDefinition def = new InterfaceRuleDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), NAME)) {
                def.setName(next.getTextTrim());
            } else if (compare(next.getName(), DESCRIPTION)) {
                def.setDescription(next.getTextTrim());
            } else if (compare(next.getName(), INTERFACE_ATTRIBUTE)) {
                def.addInterfaceAttributeDefinition(importInterfaceAttributeDefinition(next));
            } else {
            }
        }
        return def;
    }

    public InterfaceAttributeDefinition importInterfaceAttributeDefinition(Element e) throws DefinitionException {
        InterfaceAttributeDefinition def = new InterfaceAttributeDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), ATTRIBUTE_NAME)) {
                def.setAttributeName(next.getTextTrim());
            } else if (compare(next.getName(), SOURCE_ATTRIBUTE)) {
                def.setSourceAttributeDefinition(importSourceAttributeDefinition(next));
            } else {
            }
        }
        return def;
    }

    public SourceAttributeDefinition importSourceAttributeDefinition(Element e) throws DefinitionException {
        SourceAttributeDefinition def = new SourceAttributeDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), ATTRIBUTE_NAME)) {
                def.setAttributeName(next.getTextTrim());
            } else if (compare(next.getName(), COMPONENT_NAME)) {
                def.setComponentName(next.getTextTrim());
            } else if (compare(next.getName(), ATTRIBUTE_VALUE)) {
                def.setAttributeValue(next.getTextTrim());
            } else {
            }
        }
        return def;
    }

    /**
	 * 
	 * 
	 * 
	 * 
	 <factory-methods>
	 * 
	 * <factory-method>
	 * 
	 * <name>ejbCreate</name>
	 * 
	 * <method-params>
	 * 
	 * <method-param>
	 * 
	 * <param-seq>1</param-seq>
	 * 
	 * <param-value>
	 * 
	 * <component-name>MimeType</component-name>
	 * 
	 * <useBean-id>mimeTypeBean</useBean-id>
	 * 
	 * <useBean-class-method>getUniqueID</useBean-class-method>
	 * 
	 * </param-value>
	 * 
	 * </method-param>
	 * 
	 * <method-param>
	 * 
	 * <param-seq>2</param-seq>
	 * 
	 * <param-value>
	 * 
	 * <component-name>MimeType</component-name>
	 * 
	 * <useBean-id>mimeTypeBean</useBean-id>
	 * 
	 * <useBean-class-method>getName</useBean-class-method>
	 * 
	 * </param-value>
	 * 
	 * </method-param>
	 * 
	 * <method-param>
	 * 
	 * <param-seq>3</param-seq>
	 * 
	 * <param-value>
	 * 
	 * <component-name>MimeType</component-name>
	 * 
	 * <useBean-id>mimeTypeBean</useBean-id>
	 * 
	 * <useBean-class-method>getDescription</useBean-class-method>
	 * 
	 * </param-value>
	 * 
	 * </method-param>
	 * 
	 * <method-param>
	 * 
	 * <param-seq>4</param-seq>
	 * 
	 * <param-value>
	 * 
	 * <component-name>MimeType</component-name>
	 * 
	 * <useBean-id>mimeTypeBean</useBean-id>
	 * 
	 * <useBean-class-method>getMechanismID</useBean-class-method>
	 * 
	 * </param-value>
	 * 
	 * </method-param>
	 * 
	 * </method-params>
	 * 
	 * </factory-method>
	 * 
	 * </factory-methods>
	 **/
    public FactoryMethodsDefinition importFactoryMethodsDefinition(Element e) throws DefinitionException {
        FactoryMethodsDefinition def = new FactoryMethodsDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), CLASS_NAME)) {
                def.setClassName(next.getTextTrim());
            } else if (compare(next.getName(), FACTORY_METHOD)) {
                def.addFactoryMethodDefinition(importFactoryMethodDefinition(next));
            } else {
            }
        }
        return def;
    }

    public FactoryMethodDefinition importFactoryMethodDefinition(Element e) throws DefinitionException {
        FactoryMethodDefinition def = new FactoryMethodDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), NAME)) {
                def.setName(next.getTextTrim());
            } else if (compare(next.getName(), TYPE)) {
                def.setType(next.getTextTrim());
            } else if (compare(next.getName(), METHOD_PARAMS)) {
                def.setMethodParamsDefinition(importMethodParamsDefinition(next));
            } else {
            }
        }
        return def;
    }

    public MethodParamsDefinition importMethodParamsDefinition(Element e) throws DefinitionException {
        MethodParamsDefinition def = new MethodParamsDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), METHOD_PARAM)) {
                def.addMethodParamDefinition(importMethodParamDefinition(next));
            } else {
            }
        }
        return def;
    }

    public MethodParamDefinition importMethodParamDefinition(Element e) throws DefinitionException {
        MethodParamDefinition def = new MethodParamDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), PARAM_SEQ)) {
                def.setSequence(new Integer(next.getTextTrim()));
            } else if (compare(next.getName(), PARAM_NAME)) {
                def.setParamName(next.getTextTrim());
            } else if (compare(next.getName(), PARAM_VALUE)) {
                def.setParamValueDefinition(importParamValueDefinition(next));
            } else {
            }
        }
        return def;
    }

    public ParamValueDefinition importParamValueDefinition(Element e) throws DefinitionException {
        ParamValueDefinition def = new ParamValueDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), COMPONENT_NAME)) {
                def.setComponentName(next.getTextTrim());
            } else if (compare(next.getName(), USEBEAN_ID)) {
                def.setUseBeanId(next.getTextTrim());
            } else if (compare(next.getName(), USEBEAN_CLASS_METHOD_NAME)) {
                def.setUseBeanClassMethodName(next.getTextTrim());
            } else {
            }
        }
        return def;
    }

    public ModulesDefinition importModulesDefinition(Element e) throws DuplicateDefinitionException, DefinitionException {
        ModulesDefinition def = new ModulesDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), NAME)) {
                def.setName(next.getTextTrim());
            } else if (compare(next.getName(), DESCRIPTION)) {
                def.setDescription(next.getTextTrim());
            } else if (compare(next.getName(), TYPE)) {
                def.setType(next.getTextTrim());
            } else if (compare(next.getName(), MODULE_REF)) {
                def.addModuleReferenceDefinition(importModuleReferenceDefinition(next));
            } else {
            }
        }
        return def;
    }

    public ModuleReferenceDefinition importModuleReferenceDefinition(Element e) throws DefinitionException {
        ModuleReferenceDefinition def = new ModuleReferenceDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), NAME)) {
                def.setName(next.getTextTrim());
            } else if (compare(next.getName(), MODULE_URL)) {
                def.setUrl(next.getTextTrim());
            } else {
            }
        }
        return def;
    }

    public LayoutsDefinition importLayoutsDefinition(Element e) throws DuplicateDefinitionException, DefinitionException {
        LayoutsDefinition def = new LayoutsDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), NAME)) {
                def.setName(next.getTextTrim());
            } else if (compare(next.getName(), DESCRIPTION)) {
                def.setDescription(next.getTextTrim());
            } else if (compare(next.getName(), TYPE)) {
                def.setType(next.getTextTrim());
            } else if (compare(next.getName(), LAYOUT)) {
                def.addLayoutDefinition(importLayoutDefinition(next));
            } else {
            }
        }
        return def;
    }

    public LayoutDefinition importLayoutDefinition(Element e) throws DefinitionException {
        LayoutDefinition def = new LayoutDefinition();
        List all = e.getChildren();
        Element next = null;
        for (Iterator it = all.iterator(); it.hasNext(); ) {
            next = (Element) it.next();
            if (compare(next.getName(), NAME)) {
                def.setName(next.getTextTrim());
            } else if (compare(next.getName(), SERVER_NAME)) {
                def.setServerName(next.getTextTrim());
            } else if (compare(next.getName(), LAYOUT_URL)) {
                def.setUrl(next.getTextTrim());
            } else {
            }
        }
        return def;
    }
}
