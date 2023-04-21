package org.jaffa.patterns.library.object_finder_meta_2_0.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for Root element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="Root">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="PatternTemplate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="Application" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="Module" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="Component" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="BasePackage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="DomainObject" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="DomainPackage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="CriteriaTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="ResultsTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="ConsolidatedCriteriaAndResultsTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="MainLayout" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="FinderLayout" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="FinderExcelLayout" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="FinderXmlLayout" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="CriteriaFields" type="{}criteriaFields"/>
 *           &lt;element name="ResultsFields" type="{}resultsFields"/>
 *           &lt;element name="OrderByFieldCombos" type="{}orderByFieldCombos" minOccurs="0"/>
 *           &lt;element name="Creator" type="{}creator" minOccurs="0"/>
 *           &lt;element name="Viewer" type="{}viewer" minOccurs="0"/>
 *           &lt;element name="Updator" type="{}updator" minOccurs="0"/>
 *           &lt;element name="Deletor" type="{}deletor" minOccurs="0"/>
 *           &lt;element name="KeyFields" type="{}keyFields"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "patternTemplate", "application", "module", "component", "basePackage", "domainObject", "domainPackage", "criteriaTitle", "resultsTitle", "consolidatedCriteriaAndResultsTitle", "mainLayout", "finderLayout", "finderExcelLayout", "finderXmlLayout", "criteriaFields", "resultsFields", "orderByFieldCombos", "creator", "viewer", "updator", "deletor", "keyFields" })
@XmlRootElement(name = "Root")
public class ObjectFinderMeta {

    @XmlElement(name = "PatternTemplate", required = true)
    protected String patternTemplate;

    @XmlElement(name = "Application", required = true)
    protected String application;

    @XmlElement(name = "Module", required = true)
    protected String module;

    @XmlElement(name = "Component", required = true)
    protected String component;

    @XmlElement(name = "BasePackage", required = true)
    protected String basePackage;

    @XmlElement(name = "DomainObject", required = true)
    protected String domainObject;

    @XmlElement(name = "DomainPackage", required = true)
    protected String domainPackage;

    @XmlElement(name = "CriteriaTitle")
    protected String criteriaTitle;

    @XmlElement(name = "ResultsTitle")
    protected String resultsTitle;

    @XmlElement(name = "ConsolidatedCriteriaAndResultsTitle")
    protected String consolidatedCriteriaAndResultsTitle;

    @XmlElement(name = "MainLayout")
    protected String mainLayout;

    @XmlElement(name = "FinderLayout")
    protected String finderLayout;

    @XmlElement(name = "FinderExcelLayout")
    protected String finderExcelLayout;

    @XmlElement(name = "FinderXmlLayout")
    protected String finderXmlLayout;

    @XmlElement(name = "CriteriaFields", required = true)
    protected CriteriaFields criteriaFields;

    @XmlElement(name = "ResultsFields", required = true)
    protected ResultsFields resultsFields;

    @XmlElement(name = "OrderByFieldCombos")
    protected OrderByFieldCombos orderByFieldCombos;

    @XmlElement(name = "Creator")
    protected Creator creator;

    @XmlElement(name = "Viewer")
    protected Viewer viewer;

    @XmlElement(name = "Updator")
    protected Updator updator;

    @XmlElement(name = "Deletor")
    protected Deletor deletor;

    @XmlElement(name = "KeyFields", required = true)
    protected KeyFields keyFields;

    /**
     * Gets the value of the patternTemplate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPatternTemplate() {
        return patternTemplate;
    }

    /**
     * Sets the value of the patternTemplate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPatternTemplate(String value) {
        this.patternTemplate = value;
    }

    /**
     * Gets the value of the application property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplication() {
        return application;
    }

    /**
     * Sets the value of the application property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplication(String value) {
        this.application = value;
    }

    /**
     * Gets the value of the module property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModule() {
        return module;
    }

    /**
     * Sets the value of the module property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModule(String value) {
        this.module = value;
    }

    /**
     * Gets the value of the component property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComponent() {
        return component;
    }

    /**
     * Sets the value of the component property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComponent(String value) {
        this.component = value;
    }

    /**
     * Gets the value of the basePackage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBasePackage() {
        return basePackage;
    }

    /**
     * Sets the value of the basePackage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBasePackage(String value) {
        this.basePackage = value;
    }

    /**
     * Gets the value of the domainObject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainObject() {
        return domainObject;
    }

    /**
     * Sets the value of the domainObject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainObject(String value) {
        this.domainObject = value;
    }

    /**
     * Gets the value of the domainPackage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainPackage() {
        return domainPackage;
    }

    /**
     * Sets the value of the domainPackage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainPackage(String value) {
        this.domainPackage = value;
    }

    /**
     * Gets the value of the criteriaTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriteriaTitle() {
        return criteriaTitle;
    }

    /**
     * Sets the value of the criteriaTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriteriaTitle(String value) {
        this.criteriaTitle = value;
    }

    /**
     * Gets the value of the resultsTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultsTitle() {
        return resultsTitle;
    }

    /**
     * Sets the value of the resultsTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultsTitle(String value) {
        this.resultsTitle = value;
    }

    /**
     * Gets the value of the consolidatedCriteriaAndResultsTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsolidatedCriteriaAndResultsTitle() {
        return consolidatedCriteriaAndResultsTitle;
    }

    /**
     * Sets the value of the consolidatedCriteriaAndResultsTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsolidatedCriteriaAndResultsTitle(String value) {
        this.consolidatedCriteriaAndResultsTitle = value;
    }

    /**
     * Gets the value of the mainLayout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMainLayout() {
        return mainLayout;
    }

    /**
     * Sets the value of the mainLayout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMainLayout(String value) {
        this.mainLayout = value;
    }

    /**
     * Gets the value of the finderLayout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinderLayout() {
        return finderLayout;
    }

    /**
     * Sets the value of the finderLayout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinderLayout(String value) {
        this.finderLayout = value;
    }

    /**
     * Gets the value of the finderExcelLayout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinderExcelLayout() {
        return finderExcelLayout;
    }

    /**
     * Sets the value of the finderExcelLayout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinderExcelLayout(String value) {
        this.finderExcelLayout = value;
    }

    /**
     * Gets the value of the finderXmlLayout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinderXmlLayout() {
        return finderXmlLayout;
    }

    /**
     * Sets the value of the finderXmlLayout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinderXmlLayout(String value) {
        this.finderXmlLayout = value;
    }

    /**
     * Gets the value of the criteriaFields property.
     * 
     * @return
     *     possible object is
     *     {@link CriteriaFields }
     *     
     */
    public CriteriaFields getCriteriaFields() {
        return criteriaFields;
    }

    /**
     * Sets the value of the criteriaFields property.
     * 
     * @param value
     *     allowed object is
     *     {@link CriteriaFields }
     *     
     */
    public void setCriteriaFields(CriteriaFields value) {
        this.criteriaFields = value;
    }

    /**
     * Gets the value of the resultsFields property.
     * 
     * @return
     *     possible object is
     *     {@link ResultsFields }
     *     
     */
    public ResultsFields getResultsFields() {
        return resultsFields;
    }

    /**
     * Sets the value of the resultsFields property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultsFields }
     *     
     */
    public void setResultsFields(ResultsFields value) {
        this.resultsFields = value;
    }

    /**
     * Gets the value of the orderByFieldCombos property.
     * 
     * @return
     *     possible object is
     *     {@link OrderByFieldCombos }
     *     
     */
    public OrderByFieldCombos getOrderByFieldCombos() {
        return orderByFieldCombos;
    }

    /**
     * Sets the value of the orderByFieldCombos property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderByFieldCombos }
     *     
     */
    public void setOrderByFieldCombos(OrderByFieldCombos value) {
        this.orderByFieldCombos = value;
    }

    /**
     * Gets the value of the creator property.
     * 
     * @return
     *     possible object is
     *     {@link Creator }
     *     
     */
    public Creator getCreator() {
        return creator;
    }

    /**
     * Sets the value of the creator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Creator }
     *     
     */
    public void setCreator(Creator value) {
        this.creator = value;
    }

    /**
     * Gets the value of the viewer property.
     * 
     * @return
     *     possible object is
     *     {@link Viewer }
     *     
     */
    public Viewer getViewer() {
        return viewer;
    }

    /**
     * Sets the value of the viewer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Viewer }
     *     
     */
    public void setViewer(Viewer value) {
        this.viewer = value;
    }

    /**
     * Gets the value of the updator property.
     * 
     * @return
     *     possible object is
     *     {@link Updator }
     *     
     */
    public Updator getUpdator() {
        return updator;
    }

    /**
     * Sets the value of the updator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Updator }
     *     
     */
    public void setUpdator(Updator value) {
        this.updator = value;
    }

    /**
     * Gets the value of the deletor property.
     * 
     * @return
     *     possible object is
     *     {@link Deletor }
     *     
     */
    public Deletor getDeletor() {
        return deletor;
    }

    /**
     * Sets the value of the deletor property.
     * 
     * @param value
     *     allowed object is
     *     {@link Deletor }
     *     
     */
    public void setDeletor(Deletor value) {
        this.deletor = value;
    }

    /**
     * Gets the value of the keyFields property.
     * 
     * @return
     *     possible object is
     *     {@link KeyFields }
     *     
     */
    public KeyFields getKeyFields() {
        return keyFields;
    }

    /**
     * Sets the value of the keyFields property.
     * 
     * @param value
     *     allowed object is
     *     {@link KeyFields }
     *     
     */
    public void setKeyFields(KeyFields value) {
        this.keyFields = value;
    }
}
