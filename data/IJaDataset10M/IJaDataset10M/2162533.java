package com.cbsgmbh.xi.eDIFACT;

public class CTypeC240_Type extends com.sap.aii.proxy.xiruntime.core.AbstractType implements java.io.Serializable {

    private static final long serialVersionUID = -1241769327L;

    private static final com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor staticDescriptor;

    private static final com.sap.aii.proxy.xiruntime.core.GenerationInfo staticGenerationInfo = new com.sap.aii.proxy.xiruntime.core.GenerationInfo("3.0", 1139599640046L);

    private CTypeC240_Type.MetaData metadata = new MetaData(this);

    static {
        com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor descriptor = createNewBaseTypeDescriptor(com.sap.aii.proxy.xiruntime.core.XsdlConstants.XSDL_COMPLEX_TYPE, "http://cbsgmbh.com/xi/EDIFACT", "cTypeC240", 5, 0, com.cbsgmbh.xi.eDIFACT.CTypeC240_Type.class, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML, null, 0, 0, 0, null);
        staticDescriptor = descriptor;
        descriptorSetElementProperties(descriptor, 0, "C24001", null, null, "unqualified", "http://www.w3.org/2001/XMLSchema:string", "http://cbsgmbh.com/xi/EDIFACT", false, 1, 1, false, null, "c24001", java.lang.String.class, null, new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
        descriptorSetElementProperties(descriptor, 1, "C24002", null, null, "unqualified", "http://www.w3.org/2001/XMLSchema:string", "http://cbsgmbh.com/xi/EDIFACT", false, 0, 1, false, null, "c24002", com.cbsgmbh.xi.eDIFACT.Type1131_Type.class, null, new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
        descriptorSetElementProperties(descriptor, 2, "C24003", null, null, "unqualified", "http://www.w3.org/2001/XMLSchema:string", "http://cbsgmbh.com/xi/EDIFACT", false, 0, 1, false, null, "c24003", com.cbsgmbh.xi.eDIFACT.Type3055_Type.class, null, new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
        descriptorSetElementProperties(descriptor, 3, "C24004", null, null, "unqualified", "http://www.w3.org/2001/XMLSchema:string", "http://cbsgmbh.com/xi/EDIFACT", false, 0, 1, false, null, "c24004", java.lang.String.class, null, new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
        descriptorSetElementProperties(descriptor, 4, "C24005", null, null, "unqualified", "http://www.w3.org/2001/XMLSchema:string", "http://cbsgmbh.com/xi/EDIFACT", false, 0, 1, false, null, "c24005", java.lang.String.class, null, new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
    }

    public CTypeC240_Type() {
        super(staticDescriptor, staticGenerationInfo, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML);
    }

    protected CTypeC240_Type(com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor descriptor, com.sap.aii.proxy.xiruntime.core.GenerationInfo staticGenerationInfo, int connectionType) {
        super(descriptor, staticGenerationInfo, connectionType);
    }

    public void setC24002(com.cbsgmbh.xi.eDIFACT.Type1131_Type c24002) {
        baseTypeData().setElementValue(1, c24002);
    }

    public void unsetC24004() {
        baseTypeData().deleteElementValue(3);
    }

    public void setC24003(com.cbsgmbh.xi.eDIFACT.Type3055_Type c24003) {
        baseTypeData().setElementValue(2, c24003);
    }

    public com.cbsgmbh.xi.eDIFACT.Type3055_Type getC24003() {
        return (com.cbsgmbh.xi.eDIFACT.Type3055_Type) baseTypeData().getElementValue(2);
    }

    public void unsetC24003() {
        baseTypeData().deleteElementValue(2);
    }

    public com.cbsgmbh.xi.eDIFACT.Type1131_Type getC24002() {
        return (com.cbsgmbh.xi.eDIFACT.Type1131_Type) baseTypeData().getElementValue(1);
    }

    public void setC24001(java.lang.String c24001) {
        baseTypeData().setElementValue(0, c24001);
    }

    public void unsetC24005() {
        baseTypeData().deleteElementValue(4);
    }

    public boolean isSetC24004() {
        return baseTypeData().hasElementValue(3);
    }

    public java.lang.String getC24005() {
        return (java.lang.String) baseTypeData().getElementValueAsString(4);
    }

    public boolean isSetC24002() {
        return baseTypeData().hasElementValue(1);
    }

    public boolean isSetC24003() {
        return baseTypeData().hasElementValue(2);
    }

    public com.cbsgmbh.xi.eDIFACT.CTypeC240_Type.MetaData metadataCTypeC240_Type() {
        return metadata;
    }

    public void setC24005(java.lang.String c24005) {
        baseTypeData().setElementValue(4, c24005);
    }

    public java.lang.String getC24001() {
        return (java.lang.String) baseTypeData().getElementValueAsString(0);
    }

    public boolean isSetC24005() {
        return baseTypeData().hasElementValue(4);
    }

    public java.lang.String getC24004() {
        return (java.lang.String) baseTypeData().getElementValueAsString(3);
    }

    public void setC24004(java.lang.String c24004) {
        baseTypeData().setElementValue(3, c24004);
    }

    public void unsetC24002() {
        baseTypeData().deleteElementValue(1);
    }

    public static class MetaData implements java.io.Serializable {

        private CTypeC240_Type parent;

        private static final long serialVersionUID = -386313361L;

        protected MetaData(CTypeC240_Type parent) {
            this.parent = parent;
        }

        public com.sap.aii.proxy.xiruntime.core.ElementMetaData getC24005() {
            return parent.baseTypeMetaData().getElement(4);
        }

        public com.sap.aii.proxy.xiruntime.core.ElementMetaData getC24003() {
            return parent.baseTypeMetaData().getElement(2);
        }

        public com.sap.aii.proxy.xiruntime.core.TypeMetaData typeMetadata() {
            return (com.sap.aii.proxy.xiruntime.core.TypeMetaData) parent.baseTypeMetaData();
        }

        public com.sap.aii.proxy.xiruntime.core.ElementMetaData getC24001() {
            return parent.baseTypeMetaData().getElement(0);
        }

        public com.sap.aii.proxy.xiruntime.core.ElementMetaData getC24004() {
            return parent.baseTypeMetaData().getElement(3);
        }

        public com.sap.aii.proxy.xiruntime.core.ElementMetaData getC24002() {
            return parent.baseTypeMetaData().getElement(1);
        }
    }
}
