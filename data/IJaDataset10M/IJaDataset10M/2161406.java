package org.jaffa.patterns.library.object_maintenance_meta_1_0.domain;

/**
 * Java content class for annonymous complex type.
 *  <p>The following schema fragment specifies the expected content contained within this java content object.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PatternTemplate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Application" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Module" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Component" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BasePackage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DomainObject" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DomainPackage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CreateTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UpdateTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="KeyFields" type="{}keyFields"/>
 *         &lt;element name="Fields" type="{}fields"/>
 *         &lt;element name="ForeignObjects" type="{}foreignObjects" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface RootType {

    java.lang.String getModule();

    void setModule(java.lang.String value);

    java.lang.String getPatternTemplate();

    void setPatternTemplate(java.lang.String value);

    java.lang.String getBasePackage();

    void setBasePackage(java.lang.String value);

    org.jaffa.patterns.library.object_maintenance_meta_1_0.domain.Fields getFields();

    void setFields(org.jaffa.patterns.library.object_maintenance_meta_1_0.domain.Fields value);

    org.jaffa.patterns.library.object_maintenance_meta_1_0.domain.KeyFields getKeyFields();

    void setKeyFields(org.jaffa.patterns.library.object_maintenance_meta_1_0.domain.KeyFields value);

    java.lang.String getCreateTitle();

    void setCreateTitle(java.lang.String value);

    java.lang.String getUpdateTitle();

    void setUpdateTitle(java.lang.String value);

    org.jaffa.patterns.library.object_maintenance_meta_1_0.domain.ForeignObjects getForeignObjects();

    void setForeignObjects(org.jaffa.patterns.library.object_maintenance_meta_1_0.domain.ForeignObjects value);

    java.lang.String getDomainObject();

    void setDomainObject(java.lang.String value);

    java.lang.String getDomainPackage();

    void setDomainPackage(java.lang.String value);

    java.lang.String getComponent();

    void setComponent(java.lang.String value);

    java.lang.String getApplication();

    void setApplication(java.lang.String value);
}
