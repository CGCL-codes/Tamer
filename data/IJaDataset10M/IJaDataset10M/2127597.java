package com.misyshealthcare.connect.ihe.audit.jaxb;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.misyshealthcare.connect.ihe.audit.jaxb package. 
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
public class ObjectFactory extends com.misyshealthcare.connect.ihe.audit.jaxb.impl.runtime.DefaultJAXBContextImpl {

    private static java.util.HashMap defaultImplementations = new java.util.HashMap(16, 0.75F);

    private static java.util.HashMap rootTagMap = new java.util.HashMap();

    public static final com.misyshealthcare.connect.ihe.audit.jaxb.impl.runtime.GrammarInfo grammarInfo = new com.misyshealthcare.connect.ihe.audit.jaxb.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (com.misyshealthcare.connect.ihe.audit.jaxb.ObjectFactory.class));

    public static final java.lang.Class version = (com.misyshealthcare.connect.ihe.audit.jaxb.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((com.misyshealthcare.connect.ihe.audit.jaxb.ActiveParticipantType.class), "com.misyshealthcare.connect.ihe.audit.jaxb.impl.ActiveParticipantTypeImpl");
        defaultImplementations.put((com.misyshealthcare.connect.ihe.audit.jaxb.EventIdentificationType.class), "com.misyshealthcare.connect.ihe.audit.jaxb.impl.EventIdentificationTypeImpl");
        defaultImplementations.put((com.misyshealthcare.connect.ihe.audit.jaxb.AuditSourceIdentificationType.class), "com.misyshealthcare.connect.ihe.audit.jaxb.impl.AuditSourceIdentificationTypeImpl");
        defaultImplementations.put((com.misyshealthcare.connect.ihe.audit.jaxb.ParticipantObjectIdentificationType.class), "com.misyshealthcare.connect.ihe.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl");
        defaultImplementations.put((com.misyshealthcare.connect.ihe.audit.jaxb.TypeValuePairType.class), "com.misyshealthcare.connect.ihe.audit.jaxb.impl.TypeValuePairTypeImpl");
        defaultImplementations.put((com.misyshealthcare.connect.ihe.audit.jaxb.AuditMessageType.ActiveParticipantType.class), "com.misyshealthcare.connect.ihe.audit.jaxb.impl.AuditMessageTypeImpl.ActiveParticipantTypeImpl");
        defaultImplementations.put((com.misyshealthcare.connect.ihe.audit.jaxb.AuditMessageType.class), "com.misyshealthcare.connect.ihe.audit.jaxb.impl.AuditMessageTypeImpl");
        defaultImplementations.put((com.misyshealthcare.connect.ihe.audit.jaxb.ParticipantObjectIdentificationType.ParticipantObjectIDTypeCodeType.class), "com.misyshealthcare.connect.ihe.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.ParticipantObjectIDTypeCodeTypeImpl");
        defaultImplementations.put((com.misyshealthcare.connect.ihe.audit.jaxb.AuditSourceIdentificationType.AuditSourceTypeCodeType.class), "com.misyshealthcare.connect.ihe.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.AuditSourceTypeCodeTypeImpl");
        defaultImplementations.put((com.misyshealthcare.connect.ihe.audit.jaxb.CodedValueType.class), "com.misyshealthcare.connect.ihe.audit.jaxb.impl.CodedValueTypeImpl");
        defaultImplementations.put((com.misyshealthcare.connect.ihe.audit.jaxb.AuditMessage.class), "com.misyshealthcare.connect.ihe.audit.jaxb.impl.AuditMessageImpl");
        rootTagMap.put(new javax.xml.namespace.QName("", "AuditMessage"), (com.misyshealthcare.connect.ihe.audit.jaxb.AuditMessage.class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.misyshealthcare.connect.ihe.audit.jaxb
     * 
     */
    public ObjectFactory() {
        super(grammarInfo);
    }

    /**
     * Create an instance of the specified Java content interface.
     * 
     * @param javaContentInterface
     *     the Class object of the javacontent interface to instantiate
     * @return
     *     a new instance
     * @throws JAXBException
     *     if an error occurs
     */
    public java.lang.Object newInstance(java.lang.Class javaContentInterface) throws javax.xml.bind.JAXBException {
        return super.newInstance(javaContentInterface);
    }

    /**
     * Get the specified property. This method can only be
     * used to get provider specific properties.
     * Attempting to get an undefined property will result
     * in a PropertyException being thrown.
     * 
     * @param name
     *     the name of the property to retrieve
     * @return
     *     the value of the requested property
     * @throws PropertyException
     *     when there is an error retrieving the given property or value
     */
    public java.lang.Object getProperty(java.lang.String name) throws javax.xml.bind.PropertyException {
        return super.getProperty(name);
    }

    /**
     * Set the specified property. This method can only be
     * used to set provider specific properties.
     * Attempting to set an undefined property will result
     * in a PropertyException being thrown.
     * 
     * @param value
     *     the value of the property to be set
     * @param name
     *     the name of the property to retrieve
     * @throws PropertyException
     *     when there is an error processing the given property or value
     */
    public void setProperty(java.lang.String name, java.lang.Object value) throws javax.xml.bind.PropertyException {
        super.setProperty(name, value);
    }

    /**
     * Create an instance of ActiveParticipantType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public com.misyshealthcare.connect.ihe.audit.jaxb.ActiveParticipantType createActiveParticipantType() throws javax.xml.bind.JAXBException {
        return new com.misyshealthcare.connect.ihe.audit.jaxb.impl.ActiveParticipantTypeImpl();
    }

    /**
     * Create an instance of EventIdentificationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public com.misyshealthcare.connect.ihe.audit.jaxb.EventIdentificationType createEventIdentificationType() throws javax.xml.bind.JAXBException {
        return new com.misyshealthcare.connect.ihe.audit.jaxb.impl.EventIdentificationTypeImpl();
    }

    /**
     * Create an instance of AuditSourceIdentificationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public com.misyshealthcare.connect.ihe.audit.jaxb.AuditSourceIdentificationType createAuditSourceIdentificationType() throws javax.xml.bind.JAXBException {
        return new com.misyshealthcare.connect.ihe.audit.jaxb.impl.AuditSourceIdentificationTypeImpl();
    }

    /**
     * Create an instance of ParticipantObjectIdentificationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public com.misyshealthcare.connect.ihe.audit.jaxb.ParticipantObjectIdentificationType createParticipantObjectIdentificationType() throws javax.xml.bind.JAXBException {
        return new com.misyshealthcare.connect.ihe.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl();
    }

    /**
     * Create an instance of TypeValuePairType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public com.misyshealthcare.connect.ihe.audit.jaxb.TypeValuePairType createTypeValuePairType() throws javax.xml.bind.JAXBException {
        return new com.misyshealthcare.connect.ihe.audit.jaxb.impl.TypeValuePairTypeImpl();
    }

    /**
     * Create an instance of AuditMessageTypeActiveParticipantType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public com.misyshealthcare.connect.ihe.audit.jaxb.AuditMessageType.ActiveParticipantType createAuditMessageTypeActiveParticipantType() throws javax.xml.bind.JAXBException {
        return new com.misyshealthcare.connect.ihe.audit.jaxb.impl.AuditMessageTypeImpl.ActiveParticipantTypeImpl();
    }

    /**
     * Create an instance of AuditMessageType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public com.misyshealthcare.connect.ihe.audit.jaxb.AuditMessageType createAuditMessageType() throws javax.xml.bind.JAXBException {
        return new com.misyshealthcare.connect.ihe.audit.jaxb.impl.AuditMessageTypeImpl();
    }

    /**
     * Create an instance of ParticipantObjectIdentificationTypeParticipantObjectIDTypeCodeType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public com.misyshealthcare.connect.ihe.audit.jaxb.ParticipantObjectIdentificationType.ParticipantObjectIDTypeCodeType createParticipantObjectIdentificationTypeParticipantObjectIDTypeCodeType() throws javax.xml.bind.JAXBException {
        return new com.misyshealthcare.connect.ihe.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.ParticipantObjectIDTypeCodeTypeImpl();
    }

    /**
     * Create an instance of AuditSourceIdentificationTypeAuditSourceTypeCodeType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public com.misyshealthcare.connect.ihe.audit.jaxb.AuditSourceIdentificationType.AuditSourceTypeCodeType createAuditSourceIdentificationTypeAuditSourceTypeCodeType() throws javax.xml.bind.JAXBException {
        return new com.misyshealthcare.connect.ihe.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.AuditSourceTypeCodeTypeImpl();
    }

    /**
     * Create an instance of CodedValueType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public com.misyshealthcare.connect.ihe.audit.jaxb.CodedValueType createCodedValueType() throws javax.xml.bind.JAXBException {
        return new com.misyshealthcare.connect.ihe.audit.jaxb.impl.CodedValueTypeImpl();
    }

    /**
     * Create an instance of AuditMessage
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public com.misyshealthcare.connect.ihe.audit.jaxb.AuditMessage createAuditMessage() throws javax.xml.bind.JAXBException {
        return new com.misyshealthcare.connect.ihe.audit.jaxb.impl.AuditMessageImpl();
    }
}
