package com.cbsgmbh.xi.eDIFACT;

public class CTypeC829_Type extends com.sap.aii.proxy.xiruntime.core.AbstractType implements java.io.Serializable {

    private static final long serialVersionUID = -892186912L;

    private static final com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor staticDescriptor;

    private static final com.sap.aii.proxy.xiruntime.core.GenerationInfo staticGenerationInfo = new com.sap.aii.proxy.xiruntime.core.GenerationInfo("3.0", 1139599640116L);

    private CTypeC829_Type.MetaData metadata = new MetaData(this);

    static {
        com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor descriptor = createNewBaseTypeDescriptor(com.sap.aii.proxy.xiruntime.core.XsdlConstants.XSDL_COMPLEX_TYPE, "http://cbsgmbh.com/xi/EDIFACT", "cTypeC829", 2, 0, com.cbsgmbh.xi.eDIFACT.CTypeC829_Type.class, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML, null, 0, 0, 0, null);
        staticDescriptor = descriptor;
        descriptorSetElementProperties(descriptor, 0, "C82901", null, null, "unqualified", "http://www.w3.org/2001/XMLSchema:string", "http://cbsgmbh.com/xi/EDIFACT", false, 0, 1, false, null, "c82901", com.cbsgmbh.xi.eDIFACT.Type5495_Type.class, null, new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
        descriptorSetElementProperties(descriptor, 1, "C82902", null, null, "unqualified", "http://www.w3.org/2001/XMLSchema:string", "http://cbsgmbh.com/xi/EDIFACT", false, 0, 1, false, null, "c82902", java.lang.String.class, null, new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
    }

    public CTypeC829_Type() {
        super(staticDescriptor, staticGenerationInfo, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML);
    }

    protected CTypeC829_Type(com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor descriptor, com.sap.aii.proxy.xiruntime.core.GenerationInfo staticGenerationInfo, int connectionType) {
        super(descriptor, staticGenerationInfo, connectionType);
    }

    public void unsetC82902() {
        baseTypeData().deleteElementValue(1);
    }

    public com.cbsgmbh.xi.eDIFACT.CTypeC829_Type.MetaData metadataCTypeC829_Type() {
        return metadata;
    }

    public boolean isSetC82902() {
        return baseTypeData().hasElementValue(1);
    }

    public com.cbsgmbh.xi.eDIFACT.Type5495_Type getC82901() {
        return (com.cbsgmbh.xi.eDIFACT.Type5495_Type) baseTypeData().getElementValue(0);
    }

    public boolean isSetC82901() {
        return baseTypeData().hasElementValue(0);
    }

    public void setC82902(java.lang.String c82902) {
        baseTypeData().setElementValue(1, c82902);
    }

    public void unsetC82901() {
        baseTypeData().deleteElementValue(0);
    }

    public java.lang.String getC82902() {
        return (java.lang.String) baseTypeData().getElementValueAsString(1);
    }

    public void setC82901(com.cbsgmbh.xi.eDIFACT.Type5495_Type c82901) {
        baseTypeData().setElementValue(0, c82901);
    }

    public static class MetaData implements java.io.Serializable {

        private CTypeC829_Type parent;

        private static final long serialVersionUID = -386313361L;

        protected MetaData(CTypeC829_Type parent) {
            this.parent = parent;
        }

        public com.sap.aii.proxy.xiruntime.core.ElementMetaData getC82901() {
            return parent.baseTypeMetaData().getElement(0);
        }

        public com.sap.aii.proxy.xiruntime.core.TypeMetaData typeMetadata() {
            return (com.sap.aii.proxy.xiruntime.core.TypeMetaData) parent.baseTypeMetaData();
        }

        public com.sap.aii.proxy.xiruntime.core.ElementMetaData getC82902() {
            return parent.baseTypeMetaData().getElement(1);
        }
    }
}
