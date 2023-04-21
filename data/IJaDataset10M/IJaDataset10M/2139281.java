package com.cbsgmbh.xi.eDIFACT;

public class TypeDTM_Type extends com.sap.aii.proxy.xiruntime.core.AbstractType implements java.io.Serializable {

    private static final long serialVersionUID = -1046001162L;

    private static final com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor staticDescriptor;

    private static final com.sap.aii.proxy.xiruntime.core.GenerationInfo staticGenerationInfo = new com.sap.aii.proxy.xiruntime.core.GenerationInfo("3.0", 1139599640226L);

    private TypeDTM_Type.MetaData metadata = new MetaData(this);

    static {
        com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor descriptor = createNewBaseTypeDescriptor(com.sap.aii.proxy.xiruntime.core.XsdlConstants.XSDL_COMPLEX_TYPE, "http://cbsgmbh.com/xi/EDIFACT", "typeDTM", 1, 0, com.cbsgmbh.xi.eDIFACT.TypeDTM_Type.class, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML, null, 0, 0, 0, null);
        staticDescriptor = descriptor;
        descriptorSetElementProperties(descriptor, 0, "DTM01", null, null, "unqualified", "http://cbsgmbh.com/xi/EDIFACT:cTypeC507", "http://cbsgmbh.com/xi/EDIFACT", false, 1, 1, false, null, "dTM01", com.cbsgmbh.xi.eDIFACT.CTypeC507_Type.class, new com.cbsgmbh.xi.eDIFACT.CTypeC507_Type(), new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
    }

    protected TypeDTM_Type(com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor descriptor, com.sap.aii.proxy.xiruntime.core.GenerationInfo staticGenerationInfo, int connectionType) {
        super(descriptor, staticGenerationInfo, connectionType);
    }

    public TypeDTM_Type() {
        super(staticDescriptor, staticGenerationInfo, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML);
    }

    public void setDTM01(com.cbsgmbh.xi.eDIFACT.CTypeC507_Type dTM01) {
        baseTypeData().setElementValue(0, dTM01);
    }

    public com.cbsgmbh.xi.eDIFACT.TypeDTM_Type.MetaData metadataTypeDTM_Type() {
        return metadata;
    }

    public com.cbsgmbh.xi.eDIFACT.CTypeC507_Type getDTM01() {
        return (com.cbsgmbh.xi.eDIFACT.CTypeC507_Type) baseTypeData().getElementValue(0);
    }

    public static class MetaData implements java.io.Serializable {

        private TypeDTM_Type parent;

        private static final long serialVersionUID = -386313361L;

        protected MetaData(TypeDTM_Type parent) {
            this.parent = parent;
        }

        public com.sap.aii.proxy.xiruntime.core.TypeMetaData typeMetadata() {
            return (com.sap.aii.proxy.xiruntime.core.TypeMetaData) parent.baseTypeMetaData();
        }

        public com.sap.aii.proxy.xiruntime.core.ElementMetaData getDTM01() {
            return parent.baseTypeMetaData().getElement(0);
        }
    }
}
