package org.jaffa.patterns.library.object_viewer_meta_1_0.domain;

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
 *         &lt;element name="Title" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CriteriaFields" type="{}criteriaFields"/>
 *         &lt;element name="ResultsFields" type="{}resultsFields"/>
 *         &lt;element name="RelatedObjects" type="{}relatedObjects" minOccurs="0"/>
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

    org.jaffa.patterns.library.object_viewer_meta_1_0.domain.CriteriaFields getCriteriaFields();

    void setCriteriaFields(org.jaffa.patterns.library.object_viewer_meta_1_0.domain.CriteriaFields value);

    java.lang.String getBasePackage();

    void setBasePackage(java.lang.String value);

    java.lang.String getDomainObject();

    void setDomainObject(java.lang.String value);

    java.lang.String getDomainPackage();

    void setDomainPackage(java.lang.String value);

    java.lang.String getComponent();

    void setComponent(java.lang.String value);

    org.jaffa.patterns.library.object_viewer_meta_1_0.domain.RelatedObjects getRelatedObjects();

    void setRelatedObjects(org.jaffa.patterns.library.object_viewer_meta_1_0.domain.RelatedObjects value);

    java.lang.String getApplication();

    void setApplication(java.lang.String value);

    org.jaffa.patterns.library.object_viewer_meta_1_0.domain.ResultsFields getResultsFields();

    void setResultsFields(org.jaffa.patterns.library.object_viewer_meta_1_0.domain.ResultsFields value);

    java.lang.String getTitle();

    void setTitle(java.lang.String value);
}
