package com.cbsgmbh.xi.eDIFACT;

public class TypeMEA_Type extends com.sap.aii.proxy.xiruntime.core.AbstractType implements java.io.Serializable {

    private static final long serialVersionUID = 983186826L;

    private static final com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor staticDescriptor;

    private static final com.sap.aii.proxy.xiruntime.core.GenerationInfo staticGenerationInfo = new com.sap.aii.proxy.xiruntime.core.GenerationInfo("3.0", 1139599640226L);

    private TypeMEA_Type.MetaData metadata = new MetaData(this);

    static {
        com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor descriptor = createNewBaseTypeDescriptor(com.sap.aii.proxy.xiruntime.core.XsdlConstants.XSDL_COMPLEX_TYPE, "http://cbsgmbh.com/xi/EDIFACT", "typeMEA", 4, 0, com.cbsgmbh.xi.eDIFACT.TypeMEA_Type.class, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML, null, 0, 0, 0, null);
        staticDescriptor = descriptor;
        descriptorSetElementProperties(descriptor, 0, "MEA01", null, null, "unqualified", "http://www.w3.org/2001/XMLSchema:string", "http://cbsgmbh.com/xi/EDIFACT", false, 1, 1, false, null, "mEA01", com.cbsgmbh.xi.eDIFACT.Type6311_Type.class, null, new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
        descriptorSetElementProperties(descriptor, 1, "MEA02", null, null, "unqualified", "http://cbsgmbh.com/xi/EDIFACT:cTypeC502", "http://cbsgmbh.com/xi/EDIFACT", false, 0, 1, false, null, "mEA02", com.cbsgmbh.xi.eDIFACT.CTypeC502_Type.class, new com.cbsgmbh.xi.eDIFACT.CTypeC502_Type(), new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
        descriptorSetElementProperties(descriptor, 2, "MEA03", null, null, "unqualified", "http://cbsgmbh.com/xi/EDIFACT:cTypeC174", "http://cbsgmbh.com/xi/EDIFACT", false, 0, 1, false, null, "mEA03", com.cbsgmbh.xi.eDIFACT.CTypeC174_Type.class, new com.cbsgmbh.xi.eDIFACT.CTypeC174_Type(), new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
        descriptorSetElementProperties(descriptor, 3, "MEA04", null, null, "unqualified", "http://www.w3.org/2001/XMLSchema:string", "http://cbsgmbh.com/xi/EDIFACT", false, 0, 1, false, null, "mEA04", com.cbsgmbh.xi.eDIFACT.Type7383_Type.class, null, new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
    }

    protected TypeMEA_Type(com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor descriptor, com.sap.aii.proxy.xiruntime.core.GenerationInfo staticGenerationInfo, int connectionType) {
        super(descriptor, staticGenerationInfo, connectionType);
    }

    public TypeMEA_Type() {
        super(staticDescriptor, staticGenerationInfo, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML);
    }

    public void unsetMEA04() {
        baseTypeData().deleteElementValue(3);
    }

    public boolean isSetMEA02() {
        return baseTypeData().hasElementValue(1);
    }

    public boolean isSetMEA04() {
        return baseTypeData().hasElementValue(3);
    }

    public void setMEA04(com.cbsgmbh.xi.eDIFACT.Type7383_Type mEA04) {
        baseTypeData().setElementValue(3, mEA04);
    }

    public com.cbsgmbh.xi.eDIFACT.Type7383_Type getMEA04() {
        return (com.cbsgmbh.xi.eDIFACT.Type7383_Type) baseTypeData().getElementValue(3);
    }

    public boolean isSetMEA03() {
        return baseTypeData().hasElementValue(2);
    }

    public void setMEA02(com.cbsgmbh.xi.eDIFACT.CTypeC502_Type mEA02) {
        baseTypeData().setElementValue(1, mEA02);
    }

    public com.cbsgmbh.xi.eDIFACT.CTypeC174_Type getMEA03() {
        return (com.cbsgmbh.xi.eDIFACT.CTypeC174_Type) baseTypeData().getElementValue(2);
    }

    public com.cbsgmbh.xi.eDIFACT.TypeMEA_Type.MetaData metadataTypeMEA_Type() {
        return metadata;
    }

    public void unsetMEA03() {
        baseTypeData().deleteElementValue(2);
    }

    public com.cbsgmbh.xi.eDIFACT.CTypeC502_Type getMEA02() {
        return (com.cbsgmbh.xi.eDIFACT.CTypeC502_Type) baseTypeData().getElementValue(1);
    }

    public void unsetMEA02() {
        baseTypeData().deleteElementValue(1);
    }

    public com.cbsgmbh.xi.eDIFACT.Type6311_Type getMEA01() {
        return (com.cbsgmbh.xi.eDIFACT.Type6311_Type) baseTypeData().getElementValue(0);
    }

    public void setMEA03(com.cbsgmbh.xi.eDIFACT.CTypeC174_Type mEA03) {
        baseTypeData().setElementValue(2, mEA03);
    }

    public void setMEA01(com.cbsgmbh.xi.eDIFACT.Type6311_Type mEA01) {
        baseTypeData().setElementValue(0, mEA01);
    }

    public static class MetaData implements java.io.Serializable {

        private TypeMEA_Type parent;

        private static final long serialVersionUID = -386313361L;

        protected MetaData(TypeMEA_Type parent) {
            this.parent = parent;
        }

        public com.sap.aii.proxy.xiruntime.core.ElementMetaData getMEA02() {
            return parent.baseTypeMetaData().getElement(1);
        }

        public com.sap.aii.proxy.xiruntime.core.TypeMetaData typeMetadata() {
            return (com.sap.aii.proxy.xiruntime.core.TypeMetaData) parent.baseTypeMetaData();
        }

        public com.sap.aii.proxy.xiruntime.core.ElementMetaData getMEA04() {
            return parent.baseTypeMetaData().getElement(3);
        }

        public com.sap.aii.proxy.xiruntime.core.ElementMetaData getMEA01() {
            return parent.baseTypeMetaData().getElement(0);
        }

        public com.sap.aii.proxy.xiruntime.core.ElementMetaData getMEA03() {
            return parent.baseTypeMetaData().getElement(2);
        }
    }
}
