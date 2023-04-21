package com.cbsgmbh.xi.eDIFACT;

public class TypePGI_Type extends com.sap.aii.proxy.xiruntime.core.AbstractType implements java.io.Serializable {

    private static final long serialVersionUID = -374276191L;

    private static final com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor staticDescriptor;

    private static final com.sap.aii.proxy.xiruntime.core.GenerationInfo staticGenerationInfo = new com.sap.aii.proxy.xiruntime.core.GenerationInfo("3.0", 1139599640046L);

    private TypePGI_Type.MetaData metadata = new MetaData(this);

    static {
        com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor descriptor = createNewBaseTypeDescriptor(com.sap.aii.proxy.xiruntime.core.XsdlConstants.XSDL_COMPLEX_TYPE, "http://cbsgmbh.com/xi/EDIFACT", "typePGI", 2, 0, com.cbsgmbh.xi.eDIFACT.TypePGI_Type.class, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML, null, 0, 0, 0, null);
        staticDescriptor = descriptor;
        descriptorSetElementProperties(descriptor, 0, "PGI01", null, null, "unqualified", "http://www.w3.org/2001/XMLSchema:string", "http://cbsgmbh.com/xi/EDIFACT", false, 1, 1, false, null, "pGI01", com.cbsgmbh.xi.eDIFACT.Type5379_Type.class, null, new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
        descriptorSetElementProperties(descriptor, 1, "PGI02", null, null, "unqualified", "http://cbsgmbh.com/xi/EDIFACT:cTypeC288", "http://cbsgmbh.com/xi/EDIFACT", false, 1, 1, false, null, "pGI02", com.cbsgmbh.xi.eDIFACT.CTypeC288_Type.class, new com.cbsgmbh.xi.eDIFACT.CTypeC288_Type(), new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
    }

    public TypePGI_Type() {
        super(staticDescriptor, staticGenerationInfo, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML);
    }

    protected TypePGI_Type(com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor descriptor, com.sap.aii.proxy.xiruntime.core.GenerationInfo staticGenerationInfo, int connectionType) {
        super(descriptor, staticGenerationInfo, connectionType);
    }

    public com.cbsgmbh.xi.eDIFACT.TypePGI_Type.MetaData metadataTypePGI_Type() {
        return metadata;
    }

    public void setPGI01(com.cbsgmbh.xi.eDIFACT.Type5379_Type pGI01) {
        baseTypeData().setElementValue(0, pGI01);
    }

    public com.cbsgmbh.xi.eDIFACT.Type5379_Type getPGI01() {
        return (com.cbsgmbh.xi.eDIFACT.Type5379_Type) baseTypeData().getElementValue(0);
    }

    public com.cbsgmbh.xi.eDIFACT.CTypeC288_Type getPGI02() {
        return (com.cbsgmbh.xi.eDIFACT.CTypeC288_Type) baseTypeData().getElementValue(1);
    }

    public void setPGI02(com.cbsgmbh.xi.eDIFACT.CTypeC288_Type pGI02) {
        baseTypeData().setElementValue(1, pGI02);
    }

    public static class MetaData implements java.io.Serializable {

        private TypePGI_Type parent;

        private static final long serialVersionUID = -386313361L;

        protected MetaData(TypePGI_Type parent) {
            this.parent = parent;
        }

        public com.sap.aii.proxy.xiruntime.core.ElementMetaData getPGI01() {
            return parent.baseTypeMetaData().getElement(0);
        }

        public com.sap.aii.proxy.xiruntime.core.TypeMetaData typeMetadata() {
            return (com.sap.aii.proxy.xiruntime.core.TypeMetaData) parent.baseTypeMetaData();
        }

        public com.sap.aii.proxy.xiruntime.core.ElementMetaData getPGI02() {
            return parent.baseTypeMetaData().getElement(1);
        }
    }
}
