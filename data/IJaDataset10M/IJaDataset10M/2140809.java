package com.cbsgmbh.xi.eDIFACT;

public class TypeMOA_Type extends com.sap.aii.proxy.xiruntime.core.AbstractType implements java.io.Serializable {

    private static final long serialVersionUID = 1268289044L;

    private static final com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor staticDescriptor;

    private static final com.sap.aii.proxy.xiruntime.core.GenerationInfo staticGenerationInfo = new com.sap.aii.proxy.xiruntime.core.GenerationInfo("3.0", 1139599640036L);

    private TypeMOA_Type.MetaData metadata = new MetaData(this);

    static {
        com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor descriptor = createNewBaseTypeDescriptor(com.sap.aii.proxy.xiruntime.core.XsdlConstants.XSDL_COMPLEX_TYPE, "http://cbsgmbh.com/xi/EDIFACT", "typeMOA", 1, 0, com.cbsgmbh.xi.eDIFACT.TypeMOA_Type.class, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML, null, 0, 0, 0, null);
        staticDescriptor = descriptor;
        descriptorSetElementProperties(descriptor, 0, "MOA01", null, null, "unqualified", "http://cbsgmbh.com/xi/EDIFACT:cTypeC516", "http://cbsgmbh.com/xi/EDIFACT", false, 1, 1, false, null, "mOA01", com.cbsgmbh.xi.eDIFACT.CTypeC516_Type.class, new com.cbsgmbh.xi.eDIFACT.CTypeC516_Type(), new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
    }

    public TypeMOA_Type() {
        super(staticDescriptor, staticGenerationInfo, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML);
    }

    protected TypeMOA_Type(com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor descriptor, com.sap.aii.proxy.xiruntime.core.GenerationInfo staticGenerationInfo, int connectionType) {
        super(descriptor, staticGenerationInfo, connectionType);
    }

    public void setMOA01(com.cbsgmbh.xi.eDIFACT.CTypeC516_Type mOA01) {
        baseTypeData().setElementValue(0, mOA01);
    }

    public com.cbsgmbh.xi.eDIFACT.CTypeC516_Type getMOA01() {
        return (com.cbsgmbh.xi.eDIFACT.CTypeC516_Type) baseTypeData().getElementValue(0);
    }

    public com.cbsgmbh.xi.eDIFACT.TypeMOA_Type.MetaData metadataTypeMOA_Type() {
        return metadata;
    }

    public static class MetaData implements java.io.Serializable {

        private TypeMOA_Type parent;

        private static final long serialVersionUID = -386313361L;

        protected MetaData(TypeMOA_Type parent) {
            this.parent = parent;
        }

        public com.sap.aii.proxy.xiruntime.core.TypeMetaData typeMetadata() {
            return (com.sap.aii.proxy.xiruntime.core.TypeMetaData) parent.baseTypeMetaData();
        }

        public com.sap.aii.proxy.xiruntime.core.ElementMetaData getMOA01() {
            return parent.baseTypeMetaData().getElement(0);
        }
    }
}
