package org.jaffa.patterns.library.object_finder_meta_2_0.domain;

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
 *         &lt;element name="CriteriaTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ResultsTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ConsolidatedCriteriaAndResultsTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MainLayout" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FinderLayout" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FinderExcelLayout" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FinderXmlLayout" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CriteriaFields" type="{}criteriaFields"/>
 *         &lt;element name="ResultsFields" type="{}resultsFields"/>
 *         &lt;element name="OrderByFieldCombos" type="{}orderByFieldCombos" minOccurs="0"/>
 *         &lt;element name="Creator" type="{}creator" minOccurs="0"/>
 *         &lt;element name="Viewer" type="{}viewer" minOccurs="0"/>
 *         &lt;element name="Updator" type="{}updator" minOccurs="0"/>
 *         &lt;element name="Deletor" type="{}deletor" minOccurs="0"/>
 *         &lt;element name="KeyFields" type="{}keyFields"/>
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

    java.lang.String getConsolidatedCriteriaAndResultsTitle();

    void setConsolidatedCriteriaAndResultsTitle(java.lang.String value);

    java.lang.String getMainLayout();

    void setMainLayout(java.lang.String value);

    java.lang.String getPatternTemplate();

    void setPatternTemplate(java.lang.String value);

    org.jaffa.patterns.library.object_finder_meta_2_0.domain.Viewer getViewer();

    void setViewer(org.jaffa.patterns.library.object_finder_meta_2_0.domain.Viewer value);

    java.lang.String getDomainObject();

    void setDomainObject(java.lang.String value);

    java.lang.String getComponent();

    void setComponent(java.lang.String value);

    java.lang.String getApplication();

    void setApplication(java.lang.String value);

    org.jaffa.patterns.library.object_finder_meta_2_0.domain.Updator getUpdator();

    void setUpdator(org.jaffa.patterns.library.object_finder_meta_2_0.domain.Updator value);

    org.jaffa.patterns.library.object_finder_meta_2_0.domain.ResultsFields getResultsFields();

    void setResultsFields(org.jaffa.patterns.library.object_finder_meta_2_0.domain.ResultsFields value);

    org.jaffa.patterns.library.object_finder_meta_2_0.domain.OrderByFieldCombos getOrderByFieldCombos();

    void setOrderByFieldCombos(org.jaffa.patterns.library.object_finder_meta_2_0.domain.OrderByFieldCombos value);

    java.lang.String getFinderExcelLayout();

    void setFinderExcelLayout(java.lang.String value);

    org.jaffa.patterns.library.object_finder_meta_2_0.domain.Creator getCreator();

    void setCreator(org.jaffa.patterns.library.object_finder_meta_2_0.domain.Creator value);

    java.lang.String getFinderXmlLayout();

    void setFinderXmlLayout(java.lang.String value);

    org.jaffa.patterns.library.object_finder_meta_2_0.domain.CriteriaFields getCriteriaFields();

    void setCriteriaFields(org.jaffa.patterns.library.object_finder_meta_2_0.domain.CriteriaFields value);

    java.lang.String getBasePackage();

    void setBasePackage(java.lang.String value);

    org.jaffa.patterns.library.object_finder_meta_2_0.domain.KeyFields getKeyFields();

    void setKeyFields(org.jaffa.patterns.library.object_finder_meta_2_0.domain.KeyFields value);

    org.jaffa.patterns.library.object_finder_meta_2_0.domain.Deletor getDeletor();

    void setDeletor(org.jaffa.patterns.library.object_finder_meta_2_0.domain.Deletor value);

    java.lang.String getCriteriaTitle();

    void setCriteriaTitle(java.lang.String value);

    java.lang.String getDomainPackage();

    void setDomainPackage(java.lang.String value);

    java.lang.String getFinderLayout();

    void setFinderLayout(java.lang.String value);

    java.lang.String getResultsTitle();

    void setResultsTitle(java.lang.String value);
}
