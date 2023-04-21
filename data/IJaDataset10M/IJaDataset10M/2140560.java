package oasis.names.tc.xacml._2_0.policy.schema.os;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for PolicyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PolicyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Description" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}PolicyDefaults" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}CombinerParameters" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Target"/>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}CombinerParameters" minOccurs="0"/>
 *           &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}RuleCombinerParameters" minOccurs="0"/>
 *           &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}VariableDefinition"/>
 *           &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Rule"/>
 *         &lt;/choice>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Obligations" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="PolicyId" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="Version" type="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}VersionType" default="1.0" />
 *       &lt;attribute name="RuleCombiningAlgId" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolicyType", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", propOrder = { "description", "policyDefaults", "combinerParameters", "target", "combinerParametersOrRuleCombinerParametersOrVariableDefinition", "obligations" })
public class PolicyType {

    @XmlElement(name = "Description", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os")
    protected String description;

    @XmlElement(name = "PolicyDefaults", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os")
    protected DefaultsType policyDefaults;

    @XmlElement(name = "CombinerParameters", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os")
    protected CombinerParametersType combinerParameters;

    @XmlElement(name = "Target", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", required = true)
    protected TargetType target;

    @XmlElements({ @XmlElement(name = "RuleCombinerParameters", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = RuleCombinerParametersType.class), @XmlElement(name = "VariableDefinition", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = VariableDefinitionType.class), @XmlElement(name = "CombinerParameters", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = CombinerParametersType.class), @XmlElement(name = "Rule", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = RuleType.class) })
    protected List<Object> combinerParametersOrRuleCombinerParametersOrVariableDefinition;

    @XmlElement(name = "Obligations", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os")
    protected ObligationsType obligations;

    @XmlAttribute(name = "PolicyId", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String policyId;

    @XmlAttribute(name = "Version")
    protected String version;

    @XmlAttribute(name = "RuleCombiningAlgId", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String ruleCombiningAlgId;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the policyDefaults property.
     * 
     * @return
     *     possible object is
     *     {@link DefaultsType }
     *     
     */
    public DefaultsType getPolicyDefaults() {
        return policyDefaults;
    }

    /**
     * Sets the value of the policyDefaults property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefaultsType }
     *     
     */
    public void setPolicyDefaults(DefaultsType value) {
        this.policyDefaults = value;
    }

    /**
     * Gets the value of the combinerParameters property.
     * 
     * @return
     *     possible object is
     *     {@link CombinerParametersType }
     *     
     */
    public CombinerParametersType getCombinerParameters() {
        return combinerParameters;
    }

    /**
     * Sets the value of the combinerParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link CombinerParametersType }
     *     
     */
    public void setCombinerParameters(CombinerParametersType value) {
        this.combinerParameters = value;
    }

    /**
     * Gets the value of the target property.
     * 
     * @return
     *     possible object is
     *     {@link TargetType }
     *     
     */
    public TargetType getTarget() {
        return target;
    }

    /**
     * Sets the value of the target property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetType }
     *     
     */
    public void setTarget(TargetType value) {
        this.target = value;
    }

    /**
     * Gets the value of the combinerParametersOrRuleCombinerParametersOrVariableDefinition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the combinerParametersOrRuleCombinerParametersOrVariableDefinition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCombinerParametersOrRuleCombinerParametersOrVariableDefinition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RuleCombinerParametersType }
     * {@link VariableDefinitionType }
     * {@link CombinerParametersType }
     * {@link RuleType }
     * 
     * 
     */
    public List<Object> getCombinerParametersOrRuleCombinerParametersOrVariableDefinition() {
        if (combinerParametersOrRuleCombinerParametersOrVariableDefinition == null) {
            combinerParametersOrRuleCombinerParametersOrVariableDefinition = new ArrayList<Object>();
        }
        return this.combinerParametersOrRuleCombinerParametersOrVariableDefinition;
    }

    /**
     * Gets the value of the obligations property.
     * 
     * @return
     *     possible object is
     *     {@link ObligationsType }
     *     
     */
    public ObligationsType getObligations() {
        return obligations;
    }

    /**
     * Sets the value of the obligations property.
     * 
     * @param value
     *     allowed object is
     *     {@link ObligationsType }
     *     
     */
    public void setObligations(ObligationsType value) {
        this.obligations = value;
    }

    /**
     * Gets the value of the policyId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPolicyId() {
        return policyId;
    }

    /**
     * Sets the value of the policyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPolicyId(String value) {
        this.policyId = value;
    }

    /**
     * FIXME should we validate the version format? I think JAXB might not check it
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        if (version == null) {
            return "1.0";
        } else {
            return version;
        }
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the ruleCombiningAlgId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuleCombiningAlgId() {
        return ruleCombiningAlgId;
    }

    /**
     * Sets the value of the ruleCombiningAlgId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuleCombiningAlgId(String value) {
        this.ruleCombiningAlgId = value;
    }
}
