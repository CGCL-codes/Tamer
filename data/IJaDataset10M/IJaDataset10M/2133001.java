package org.okkam.core.api.data.entityprofile;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.okkam.core.api.data.entityprofile package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.okkam.core.api.data.entityprofile
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ReferenceType }
     * 
     */
    public ReferenceType createReferenceType() {
        return new ReferenceType();
    }

    /**
     * Create an instance of {@link ReferencesType }
     * 
     */
    public ReferencesType createReferencesType() {
        return new ReferencesType();
    }

    /**
     * Create an instance of {@link LabelsType }
     * 
     */
    public LabelsType createLabelsType() {
        return new LabelsType();
    }

    /**
     * Create an instance of {@link LabelType }
     * 
     */
    public LabelType createLabelType() {
        return new LabelType();
    }

    /**
     * Create an instance of {@link EntityProfile }
     * 
     */
    public EntityProfile createEntityProfile() {
        return new EntityProfile();
    }

    /**
     * Create an instance of {@link AlternativeIdentifiersType }
     * 
     */
    public AlternativeIdentifiersType createAlternativeIdentifiersType() {
        return new AlternativeIdentifiersType();
    }

    /**
     * Create an instance of {@link AssertionsOfIdentityType }
     * 
     */
    public AssertionsOfIdentityType createAssertionsOfIdentityType() {
        return new AssertionsOfIdentityType();
    }
}
