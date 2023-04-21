package oasis.names.tc.ebxml_regrep.xsd.rim._3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for RegistryObjectListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegistryObjectListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}Identifiable"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}Registry"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}Service"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}User"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}RegistryPackage"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}SpecificationLink"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}RegistryObject"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}AdhocQuery"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}AuditableEvent"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}ExternalIdentifier"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}Person"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}Association"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}Federation"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}ServiceBinding"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}ExternalLink"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}Subscription"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}ObjectRef"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}ClassificationScheme"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}ClassificationNode"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}ExtrinsicObject"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}Organization"/>
 *           &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}Classification"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegistryObjectListType", propOrder = { "identifiableOrRegistryOrService" })
public class RegistryObjectListType {

    @XmlElements({ @XmlElement(name = "ExternalIdentifier", type = ExternalIdentifierType.class, nillable = true), @XmlElement(name = "Association", type = AssociationType1.class, nillable = true), @XmlElement(name = "Federation", type = FederationType.class, nillable = true), @XmlElement(name = "ClassificationScheme", type = ClassificationSchemeType.class, nillable = true), @XmlElement(name = "Classification", type = ClassificationType.class, nillable = true), @XmlElement(name = "Registry", type = RegistryType.class, nillable = true), @XmlElement(name = "AuditableEvent", type = AuditableEventType.class, nillable = true), @XmlElement(name = "Organization", type = OrganizationType.class, nillable = true), @XmlElement(name = "ExternalLink", type = ExternalLinkType.class, nillable = true), @XmlElement(name = "SpecificationLink", type = SpecificationLinkType.class, nillable = true), @XmlElement(name = "RegistryPackage", type = RegistryPackageType.class, nillable = true), @XmlElement(name = "Person", type = PersonType.class, nillable = true), @XmlElement(name = "Subscription", type = SubscriptionType.class, nillable = true), @XmlElement(name = "Identifiable", nillable = true), @XmlElement(name = "ObjectRef", type = ObjectRefType.class, nillable = true), @XmlElement(name = "ServiceBinding", type = ServiceBindingType.class, nillable = true), @XmlElement(name = "User", type = UserType.class, nillable = true), @XmlElement(name = "Service", type = ServiceType.class, nillable = true), @XmlElement(name = "ClassificationNode", type = ClassificationNodeType.class, nillable = true), @XmlElement(name = "AdhocQuery", type = AdhocQueryType.class, nillable = true), @XmlElement(name = "RegistryObject", type = RegistryObjectType.class, nillable = true), @XmlElement(name = "ExtrinsicObject", type = ExtrinsicObjectType.class, nillable = true) })
    protected List<IdentifiableType> identifiableOrRegistryOrService;

    /**
     * Gets the value of the identifiableOrRegistryOrService property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the identifiableOrRegistryOrService property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdentifiableOrRegistryOrService().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExternalIdentifierType }
     * {@link AssociationType1 }
     * {@link FederationType }
     * {@link ClassificationSchemeType }
     * {@link ClassificationType }
     * {@link RegistryType }
     * {@link AuditableEventType }
     * {@link OrganizationType }
     * {@link ExternalLinkType }
     * {@link SpecificationLinkType }
     * {@link RegistryPackageType }
     * {@link PersonType }
     * {@link SubscriptionType }
     * {@link IdentifiableType }
     * {@link ObjectRefType }
     * {@link ServiceBindingType }
     * {@link UserType }
     * {@link ServiceType }
     * {@link ClassificationNodeType }
     * {@link AdhocQueryType }
     * {@link RegistryObjectType }
     * {@link ExtrinsicObjectType }
     * 
     * 
     */
    public List<IdentifiableType> getIdentifiableOrRegistryOrService() {
        if (identifiableOrRegistryOrService == null) {
            identifiableOrRegistryOrService = new ArrayList<IdentifiableType>();
        }
        return this.identifiableOrRegistryOrService;
    }
}
