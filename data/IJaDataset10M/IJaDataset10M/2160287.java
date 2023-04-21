package uk.ac.ncl.neresc.dynasoar.vm;

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;

/**
 * Class VMConfigurationTypeDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class VMConfigurationTypeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

    /**
     * Field nsPrefix
     */
    private java.lang.String nsPrefix;

    /**
     * Field nsURI
     */
    private java.lang.String nsURI;

    /**
     * Field xmlName
     */
    private java.lang.String xmlName;

    /**
     * Field identity
     */
    private org.exolab.castor.xml.XMLFieldDescriptor identity;

    public VMConfigurationTypeDescriptor() {
        super();
        nsURI = "http://ncl.ac.uk/2006/dynasoar/vm";
        xmlName = "VMConfigurationType";
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.mapping.FieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_vmName", "vmName", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                VMConfigurationType target = (VMConfigurationType) object;
                return target.getVmName();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    VMConfigurationType target = (VMConfigurationType) object;
                    target.setVmName((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ncl.ac.uk/2006/dynasoar/vm");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_vmType", "vmType", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                VMConfigurationType target = (VMConfigurationType) object;
                return target.getVmType();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    VMConfigurationType target = (VMConfigurationType) object;
                    target.setVmType((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return new java.lang.String();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ncl.ac.uk/2006/dynasoar/vm");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        {
            NameValidator typeValidator = new NameValidator(NameValidator.NMTOKEN);
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_guestOS", "guestOS", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                VMConfigurationType target = (VMConfigurationType) object;
                return target.getGuestOS();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    VMConfigurationType target = (VMConfigurationType) object;
                    target.setGuestOS((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return new java.lang.String();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ncl.ac.uk/2006/dynasoar/vm");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        {
            NameValidator typeValidator = new NameValidator(NameValidator.NMTOKEN);
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_configFile", "configFile", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                VMConfigurationType target = (VMConfigurationType) object;
                return target.getConfigFile();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    VMConfigurationType target = (VMConfigurationType) object;
                    target.setConfigFile((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ncl.ac.uk/2006/dynasoar/vm");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(long.class, "_hardDiskCapacityGB", "hardDiskCapacityGB", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                VMConfigurationType target = (VMConfigurationType) object;
                if (!target.hasHardDiskCapacityGB()) return null;
                return new java.lang.Long(target.getHardDiskCapacityGB());
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    VMConfigurationType target = (VMConfigurationType) object;
                    if (value == null) return;
                    target.setHardDiskCapacityGB(((java.lang.Long) value).longValue());
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ncl.ac.uk/2006/dynasoar/vm");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        {
            LongValidator typeValidator = new LongValidator();
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(long.class, "_primaryMemoryMB", "primaryMemoryMB", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                VMConfigurationType target = (VMConfigurationType) object;
                if (!target.hasPrimaryMemoryMB()) return null;
                return new java.lang.Long(target.getPrimaryMemoryMB());
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    VMConfigurationType target = (VMConfigurationType) object;
                    if (value == null) return;
                    target.setPrimaryMemoryMB(((java.lang.Long) value).longValue());
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ncl.ac.uk/2006/dynasoar/vm");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        {
            LongValidator typeValidator = new LongValidator();
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(long.class, "_diskOutputRateKBpSec", "DiskOutputRateKBpSec", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                VMConfigurationType target = (VMConfigurationType) object;
                if (!target.hasDiskOutputRateKBpSec()) return null;
                return new java.lang.Long(target.getDiskOutputRateKBpSec());
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    VMConfigurationType target = (VMConfigurationType) object;
                    if (value == null) {
                        target.deleteDiskOutputRateKBpSec();
                        return;
                    }
                    target.setDiskOutputRateKBpSec(((java.lang.Long) value).longValue());
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ncl.ac.uk/2006/dynasoar/vm");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            LongValidator typeValidator = new LongValidator();
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(float.class, "_connectionSpeedKBpSec", "ConnectionSpeedKBpSec", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                VMConfigurationType target = (VMConfigurationType) object;
                if (!target.hasConnectionSpeedKBpSec()) return null;
                return new java.lang.Float(target.getConnectionSpeedKBpSec());
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    VMConfigurationType target = (VMConfigurationType) object;
                    if (value == null) {
                        target.deleteConnectionSpeedKBpSec();
                        return;
                    }
                    target.setConnectionSpeedKBpSec(((java.lang.Float) value).floatValue());
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ncl.ac.uk/2006/dynasoar/vm");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            FloatValidator typeValidator = new FloatValidator();
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(uk.ac.ncl.neresc.dynasoar.vm.DatabaseConfig.class, "_databaseConfig", "databaseConfig", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                VMConfigurationType target = (VMConfigurationType) object;
                return target.getDatabaseConfig();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    VMConfigurationType target = (VMConfigurationType) object;
                    target.setDatabaseConfig((uk.ac.ncl.neresc.dynasoar.vm.DatabaseConfig) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return new uk.ac.ncl.neresc.dynasoar.vm.DatabaseConfig();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ncl.ac.uk/2006/dynasoar/vm");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_tomcatInstancePath", "tomcatInstancePath", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                VMConfigurationType target = (VMConfigurationType) object;
                return target.getTomcatInstancePath();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    VMConfigurationType target = (VMConfigurationType) object;
                    target.setTomcatInstancePath((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ncl.ac.uk/2006/dynasoar/vm");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_tomcatPort", "tomcatPort", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                VMConfigurationType target = (VMConfigurationType) object;
                return target.getTomcatPort();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    VMConfigurationType target = (VMConfigurationType) object;
                    target.setTomcatPort((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ncl.ac.uk/2006/dynasoar/vm");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(uk.ac.ncl.neresc.dynasoar.vm.VmServiceList.class, "_vmServiceListList", "vmServiceList", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                VMConfigurationType target = (VMConfigurationType) object;
                return target.getVmServiceList();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    VMConfigurationType target = (VMConfigurationType) object;
                    target.addVmServiceList((uk.ac.ncl.neresc.dynasoar.vm.VmServiceList) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return new uk.ac.ncl.neresc.dynasoar.vm.VmServiceList();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ncl.ac.uk/2006/dynasoar/vm");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        {
        }
        desc.setValidator(fieldValidator);
    }

    /**
     * Method getAccessMode
     * 
     * 
     * 
     * @return AccessMode
     */
    public org.exolab.castor.mapping.AccessMode getAccessMode() {
        return null;
    }

    /**
     * Method getExtends
     * 
     * 
     * 
     * @return ClassDescriptor
     */
    public org.exolab.castor.mapping.ClassDescriptor getExtends() {
        return null;
    }

    /**
     * Method getIdentity
     * 
     * 
     * 
     * @return FieldDescriptor
     */
    public org.exolab.castor.mapping.FieldDescriptor getIdentity() {
        return identity;
    }

    /**
     * Method getJavaClass
     * 
     * 
     * 
     * @return Class
     */
    public java.lang.Class getJavaClass() {
        return uk.ac.ncl.neresc.dynasoar.vm.VMConfigurationType.class;
    }

    /**
     * Method getNameSpacePrefix
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String getNameSpacePrefix() {
        return nsPrefix;
    }

    /**
     * Method getNameSpaceURI
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String getNameSpaceURI() {
        return nsURI;
    }

    /**
     * Method getValidator
     * 
     * 
     * 
     * @return TypeValidator
     */
    public org.exolab.castor.xml.TypeValidator getValidator() {
        return this;
    }

    /**
     * Method getXMLName
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String getXMLName() {
        return xmlName;
    }
}
