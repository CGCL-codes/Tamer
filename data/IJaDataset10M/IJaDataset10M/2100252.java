package com.cbsgmbh.xi.eDIFACT;

public class TypeContrlHeading_Type extends com.sap.aii.proxy.xiruntime.core.AbstractType implements java.io.Serializable {

    private static final long serialVersionUID = 1163663229L;

    private static final com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor staticDescriptor;

    private static final com.sap.aii.proxy.xiruntime.core.GenerationInfo staticGenerationInfo = new com.sap.aii.proxy.xiruntime.core.GenerationInfo("3.0", 1139599640306L);

    private TypeContrlHeading_Type.MetaData metadata = new MetaData(this);

    static {
        com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor descriptor = createNewBaseTypeDescriptor(com.sap.aii.proxy.xiruntime.core.XsdlConstants.XSDL_COMPLEX_TYPE, "http://cbsgmbh.com/xi/EDIFACT", "typeContrlHeading", 3, 0, com.cbsgmbh.xi.eDIFACT.TypeContrlHeading_Type.class, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML, null, 0, 0, 0, null);
        staticDescriptor = descriptor;
        descriptorSetElementProperties(descriptor, 0, "UNH", null, null, "unqualified", "http://cbsgmbh.com/xi/EDIFACT:typeUNH", "http://cbsgmbh.com/xi/EDIFACT", false, 1, 1, false, null, "uNH", com.cbsgmbh.xi.eDIFACT.TypeUNH_Type.class, new com.cbsgmbh.xi.eDIFACT.TypeUNH_Type(), new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
        descriptorSetElementProperties(descriptor, 1, "UCI", null, null, "unqualified", "http://cbsgmbh.com/xi/EDIFACT:typeUCI", "http://cbsgmbh.com/xi/EDIFACT", false, 1, 1, false, null, "uCI", com.cbsgmbh.xi.eDIFACT.TypeUCI_Type.class, new com.cbsgmbh.xi.eDIFACT.TypeUCI_Type(), new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
        descriptorSetElementProperties(descriptor, 2, "SG01", null, null, "unqualified", null, "http://cbsgmbh.com/xi/EDIFACT", false, 0, 999999, false, null, "sG01", SG01_List.class, new SG01_Type(), new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
    }

    public TypeContrlHeading_Type() {
        super(staticDescriptor, staticGenerationInfo, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML);
    }

    protected TypeContrlHeading_Type(com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor descriptor, com.sap.aii.proxy.xiruntime.core.GenerationInfo staticGenerationInfo, int connectionType) {
        super(descriptor, staticGenerationInfo, connectionType);
    }

    public boolean isSetSG01() {
        return baseTypeData().hasElementValue(2);
    }

    public com.cbsgmbh.xi.eDIFACT.TypeUCI_Type getUCI() {
        return (com.cbsgmbh.xi.eDIFACT.TypeUCI_Type) baseTypeData().getElementValue(1);
    }

    public SG01_List get_as_listSG01() {
        return (SG01_List) baseTypeData().getElementValue(2);
    }

    public void setSG01(SG01_Type[] sG01) {
        baseTypeData().setElementValue(2, new SG01_List());
        SG01_List list$ = get_as_listSG01();
        for (int i$ = 0; i$ < sG01.length; i$++) {
            list$.addSG01(sG01[i$]);
        }
    }

    public void setUNH(com.cbsgmbh.xi.eDIFACT.TypeUNH_Type uNH) {
        baseTypeData().setElementValue(0, uNH);
    }

    public com.cbsgmbh.xi.eDIFACT.TypeUNH_Type getUNH() {
        return (com.cbsgmbh.xi.eDIFACT.TypeUNH_Type) baseTypeData().getElementValue(0);
    }

    public void setUCI(com.cbsgmbh.xi.eDIFACT.TypeUCI_Type uCI) {
        baseTypeData().setElementValue(1, uCI);
    }

    public void unsetSG01() {
        baseTypeData().deleteElementValue(2);
    }

    public void setSG01(SG01_List sG01) {
        baseTypeData().setElementValue(2, sG01);
    }

    public SG01_Type[] getSG01() {
        SG01_List i$ = (SG01_List) baseTypeData().getElementValue(2);
        if (i$ == null) {
            return null;
        }
        return i$.toArraySG01();
    }

    public com.cbsgmbh.xi.eDIFACT.TypeContrlHeading_Type.MetaData metadataTypeContrlHeading_Type() {
        return metadata;
    }

    public static class MetaData implements java.io.Serializable {

        private TypeContrlHeading_Type parent;

        private static final long serialVersionUID = -386313361L;

        protected MetaData(TypeContrlHeading_Type parent) {
            this.parent = parent;
        }

        public com.sap.aii.proxy.xiruntime.core.ElementMetaData getUCI() {
            return parent.baseTypeMetaData().getElement(1);
        }

        public com.sap.aii.proxy.xiruntime.core.ElementMetaData getUNH() {
            return parent.baseTypeMetaData().getElement(0);
        }

        public com.sap.aii.proxy.xiruntime.core.TypeMetaData typeMetadata() {
            return (com.sap.aii.proxy.xiruntime.core.TypeMetaData) parent.baseTypeMetaData();
        }

        public com.sap.aii.proxy.xiruntime.core.ElementMetaData getSG01() {
            return parent.baseTypeMetaData().getElement(2);
        }
    }

    public static class SG01_Type extends com.sap.aii.proxy.xiruntime.core.AbstractType implements java.io.Serializable {

        private static final long serialVersionUID = 634306052L;

        private static final com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor staticDescriptor;

        private static final com.sap.aii.proxy.xiruntime.core.GenerationInfo staticGenerationInfo = new com.sap.aii.proxy.xiruntime.core.GenerationInfo("3.0", 1139599640306L);

        private SG01_Type.MetaData metadata = new MetaData(this);

        static {
            com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor descriptor = createNewBaseTypeDescriptor(com.sap.aii.proxy.xiruntime.core.XsdlConstants.XSDL_COMPLEX_TYPE, "http://cbsgmbh.com/xi/EDIFACT", "", 2, 0, SG01_Type.class, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML, null, -1, -1, -1, null);
            staticDescriptor = descriptor;
            descriptorSetElementProperties(descriptor, 0, "UCM", null, null, "unqualified", "http://cbsgmbh.com/xi/EDIFACT:typeUCM", "http://cbsgmbh.com/xi/EDIFACT", false, 1, 1, false, null, "uCM", com.cbsgmbh.xi.eDIFACT.TypeUCM_Type.class, new com.cbsgmbh.xi.eDIFACT.TypeUCM_Type(), new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
            descriptorSetElementProperties(descriptor, 1, "SG02", null, null, "unqualified", null, "http://cbsgmbh.com/xi/EDIFACT", false, 0, 999, false, null, "sG02", SG02_List.class, new SG02_Type(), new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
        }

        protected SG01_Type(com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor descriptor, com.sap.aii.proxy.xiruntime.core.GenerationInfo staticGenerationInfo, int connectionType) {
            super(descriptor, staticGenerationInfo, connectionType);
        }

        public SG01_Type() {
            super(staticDescriptor, staticGenerationInfo, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML);
        }

        public void setUCM(com.cbsgmbh.xi.eDIFACT.TypeUCM_Type uCM) {
            baseTypeData().setElementValue(0, uCM);
        }

        public SG02_Type[] getSG02() {
            SG02_List i$ = (SG02_List) baseTypeData().getElementValue(1);
            if (i$ == null) {
                return null;
            }
            return i$.toArraySG02();
        }

        public void setSG02(SG02_Type[] sG02) {
            baseTypeData().setElementValue(1, new SG02_List());
            SG02_List list$ = get_as_listSG02();
            for (int i$ = 0; i$ < sG02.length; i$++) {
                list$.addSG02(sG02[i$]);
            }
        }

        public MetaData metadataSG01_Type() {
            return metadata;
        }

        public SG02_List get_as_listSG02() {
            return (SG02_List) baseTypeData().getElementValue(1);
        }

        public void setSG02(SG02_List sG02) {
            baseTypeData().setElementValue(1, sG02);
        }

        public void unsetSG02() {
            baseTypeData().deleteElementValue(1);
        }

        public boolean isSetSG02() {
            return baseTypeData().hasElementValue(1);
        }

        public com.cbsgmbh.xi.eDIFACT.TypeUCM_Type getUCM() {
            return (com.cbsgmbh.xi.eDIFACT.TypeUCM_Type) baseTypeData().getElementValue(0);
        }

        public static class MetaData implements java.io.Serializable {

            private SG01_Type parent;

            private static final long serialVersionUID = -386313361L;

            protected MetaData(SG01_Type parent) {
                this.parent = parent;
            }

            public com.sap.aii.proxy.xiruntime.core.ElementMetaData getSG02() {
                return parent.baseTypeMetaData().getElement(1);
            }

            public com.sap.aii.proxy.xiruntime.core.TypeMetaData typeMetadata() {
                return (com.sap.aii.proxy.xiruntime.core.TypeMetaData) parent.baseTypeMetaData();
            }

            public com.sap.aii.proxy.xiruntime.core.ElementMetaData getUCM() {
                return parent.baseTypeMetaData().getElement(0);
            }
        }

        public static class SG02_Type extends com.sap.aii.proxy.xiruntime.core.AbstractType implements java.io.Serializable {

            private static final long serialVersionUID = 662935203L;

            private static final com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor staticDescriptor;

            private static final com.sap.aii.proxy.xiruntime.core.GenerationInfo staticGenerationInfo = new com.sap.aii.proxy.xiruntime.core.GenerationInfo("3.0", 1139599640306L);

            private SG02_Type.MetaData metadata = new MetaData(this);

            static {
                com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor descriptor = createNewBaseTypeDescriptor(com.sap.aii.proxy.xiruntime.core.XsdlConstants.XSDL_COMPLEX_TYPE, "http://cbsgmbh.com/xi/EDIFACT", "", 2, 0, SG02_Type.class, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML, null, -1, -1, -1, null);
                staticDescriptor = descriptor;
                descriptorSetElementProperties(descriptor, 0, "UCS", null, null, "unqualified", "http://cbsgmbh.com/xi/EDIFACT:typeUCS", "http://cbsgmbh.com/xi/EDIFACT", false, 1, 1, false, null, "uCS", com.cbsgmbh.xi.eDIFACT.TypeUCS_Type.class, new com.cbsgmbh.xi.eDIFACT.TypeUCS_Type(), new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
                descriptorSetElementProperties(descriptor, 1, "UCD", null, null, "unqualified", "http://cbsgmbh.com/xi/EDIFACT:typeUCD", "http://cbsgmbh.com/xi/EDIFACT", false, 0, 99, false, null, "uCD", UCD_List.class, new com.cbsgmbh.xi.eDIFACT.TypeUCD_Type(), new java.lang.String[][] {}, null, 0, 0, 0, -1, -1, -1, -1, 0, null);
            }

            protected SG02_Type(com.sap.aii.proxy.xiruntime.core.BaseTypeDescriptor descriptor, com.sap.aii.proxy.xiruntime.core.GenerationInfo staticGenerationInfo, int connectionType) {
                super(descriptor, staticGenerationInfo, connectionType);
            }

            public SG02_Type() {
                super(staticDescriptor, staticGenerationInfo, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML);
            }

            public MetaData metadataSG02_Type() {
                return metadata;
            }

            public void setUCD(com.cbsgmbh.xi.eDIFACT.TypeUCD_Type[] uCD) {
                baseTypeData().setElementValue(1, new UCD_List());
                UCD_List list$ = get_as_listUCD();
                for (int i$ = 0; i$ < uCD.length; i$++) {
                    list$.addUCD(uCD[i$]);
                }
            }

            public void unsetUCD() {
                baseTypeData().deleteElementValue(1);
            }

            public com.cbsgmbh.xi.eDIFACT.TypeUCD_Type[] getUCD() {
                UCD_List i$ = (UCD_List) baseTypeData().getElementValue(1);
                if (i$ == null) {
                    return null;
                }
                return i$.toArrayUCD();
            }

            public void setUCS(com.cbsgmbh.xi.eDIFACT.TypeUCS_Type uCS) {
                baseTypeData().setElementValue(0, uCS);
            }

            public com.cbsgmbh.xi.eDIFACT.TypeUCS_Type getUCS() {
                return (com.cbsgmbh.xi.eDIFACT.TypeUCS_Type) baseTypeData().getElementValue(0);
            }

            public void setUCD(UCD_List uCD) {
                baseTypeData().setElementValue(1, uCD);
            }

            public UCD_List get_as_listUCD() {
                return (UCD_List) baseTypeData().getElementValue(1);
            }

            public boolean isSetUCD() {
                return baseTypeData().hasElementValue(1);
            }

            public static class MetaData implements java.io.Serializable {

                private SG02_Type parent;

                private static final long serialVersionUID = -386313361L;

                protected MetaData(SG02_Type parent) {
                    this.parent = parent;
                }

                public com.sap.aii.proxy.xiruntime.core.ElementMetaData getUCD() {
                    return parent.baseTypeMetaData().getElement(1);
                }

                public com.sap.aii.proxy.xiruntime.core.TypeMetaData typeMetadata() {
                    return (com.sap.aii.proxy.xiruntime.core.TypeMetaData) parent.baseTypeMetaData();
                }

                public com.sap.aii.proxy.xiruntime.core.ElementMetaData getUCS() {
                    return parent.baseTypeMetaData().getElement(0);
                }
            }

            public static class UCD_List extends com.sap.aii.proxy.xiruntime.core.AbstractList implements java.io.Serializable, java.util.List {

                private static final long serialVersionUID = 83830L;

                private static final com.sap.aii.proxy.xiruntime.core.XsdlElementProperties staticElementProperties = createElementProperties("UCD", null, null, "unqualified", "http://cbsgmbh.com/xi/EDIFACT:typeUCD", "http://cbsgmbh.com/xi/EDIFACT", false, 0, 99, false, null, "uCD", com.cbsgmbh.xi.eDIFACT.TypeUCD_Type.class, new com.cbsgmbh.xi.eDIFACT.TypeUCD_Type(), new java.lang.String[][] {}, -1, -1, -1, -1, null);

                public UCD_List() {
                    super(staticElementProperties, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML);
                }

                public void addUCD(int index, com.cbsgmbh.xi.eDIFACT.TypeUCD_Type item) {
                    baseList().add(index, item);
                }

                public boolean containsUCD(com.cbsgmbh.xi.eDIFACT.TypeUCD_Type item) {
                    return baseList().contains(item);
                }

                public int lastIndexOfUCD(com.cbsgmbh.xi.eDIFACT.TypeUCD_Type item) {
                    return baseList().lastIndexOf(item);
                }

                public com.cbsgmbh.xi.eDIFACT.TypeUCD_Type removeUCD(int index) {
                    return (com.cbsgmbh.xi.eDIFACT.TypeUCD_Type) baseList().remove(index);
                }

                public void addUCD(com.cbsgmbh.xi.eDIFACT.TypeUCD_Type item) {
                    baseList().add(item);
                }

                public boolean containsAllUCD(UCD_List item) {
                    return baseList().containsAll(item);
                }

                public void addAllUCD(UCD_List item) {
                    baseList().addAll(item);
                }

                public UCD_List subListUCD(int fromIndex, int toIndex) {
                    return (UCD_List) baseList().subList(fromIndex, toIndex);
                }

                public void addAllUCD(int index, UCD_List item) {
                    baseList().addAll(index, item);
                }

                public com.cbsgmbh.xi.eDIFACT.TypeUCD_Type[] toArrayUCD() {
                    return (com.cbsgmbh.xi.eDIFACT.TypeUCD_Type[]) baseList().toArray(new com.cbsgmbh.xi.eDIFACT.TypeUCD_Type[] {});
                }

                public com.cbsgmbh.xi.eDIFACT.TypeUCD_Type getUCD(int index) {
                    return (com.cbsgmbh.xi.eDIFACT.TypeUCD_Type) baseList().get(index);
                }

                public boolean removeUCD(com.cbsgmbh.xi.eDIFACT.TypeUCD_Type item) {
                    return baseList().remove(item);
                }

                public int indexOfUCD(com.cbsgmbh.xi.eDIFACT.TypeUCD_Type item) {
                    return baseList().indexOf(item);
                }

                public com.cbsgmbh.xi.eDIFACT.TypeUCD_Type setUCD(int index, com.cbsgmbh.xi.eDIFACT.TypeUCD_Type item) {
                    return (com.cbsgmbh.xi.eDIFACT.TypeUCD_Type) baseList().set(index, item);
                }
            }
        }

        public static class SG02_List extends com.sap.aii.proxy.xiruntime.core.AbstractList implements java.io.Serializable, java.util.List {

            private static final long serialVersionUID = 2542422L;

            private static final com.sap.aii.proxy.xiruntime.core.XsdlElementProperties staticElementProperties = createElementProperties("SG02", null, null, "unqualified", null, "http://cbsgmbh.com/xi/EDIFACT", false, 0, 999, false, null, "sG02", SG02_Type.class, new SG02_Type(), new java.lang.String[][] {}, -1, -1, -1, -1, null);

            public SG02_List() {
                super(staticElementProperties, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML);
            }

            public SG02_Type[] toArraySG02() {
                return (SG02_Type[]) baseList().toArray(new SG02_Type[] {});
            }

            public boolean containsSG02(SG02_Type item) {
                return baseList().contains(item);
            }

            public void addSG02(SG02_Type item) {
                baseList().add(item);
            }

            public void addAllSG02(SG02_List item) {
                baseList().addAll(item);
            }

            public int indexOfSG02(SG02_Type item) {
                return baseList().indexOf(item);
            }

            public boolean removeSG02(SG02_Type item) {
                return baseList().remove(item);
            }

            public SG02_Type removeSG02(int index) {
                return (SG02_Type) baseList().remove(index);
            }

            public void addAllSG02(int index, SG02_List item) {
                baseList().addAll(index, item);
            }

            public SG02_Type setSG02(int index, SG02_Type item) {
                return (SG02_Type) baseList().set(index, item);
            }

            public int lastIndexOfSG02(SG02_Type item) {
                return baseList().lastIndexOf(item);
            }

            public SG02_List subListSG02(int fromIndex, int toIndex) {
                return (SG02_List) baseList().subList(fromIndex, toIndex);
            }

            public void addSG02(int index, SG02_Type item) {
                baseList().add(index, item);
            }

            public boolean containsAllSG02(SG02_List item) {
                return baseList().containsAll(item);
            }

            public SG02_Type getSG02(int index) {
                return (SG02_Type) baseList().get(index);
            }
        }
    }

    public static class SG01_List extends com.sap.aii.proxy.xiruntime.core.AbstractList implements java.io.Serializable, java.util.List {

        private static final long serialVersionUID = 2542421L;

        private static final com.sap.aii.proxy.xiruntime.core.XsdlElementProperties staticElementProperties = createElementProperties("SG01", null, null, "unqualified", null, "http://cbsgmbh.com/xi/EDIFACT", false, 0, 999999, false, null, "sG01", SG01_Type.class, new SG01_Type(), new java.lang.String[][] {}, -1, -1, -1, -1, null);

        public SG01_List() {
            super(staticElementProperties, com.sap.aii.proxy.xiruntime.core.FactoryConstants.CONNECTION_TYPE_XML);
        }

        public int lastIndexOfSG01(SG01_Type item) {
            return baseList().lastIndexOf(item);
        }

        public void addSG01(SG01_Type item) {
            baseList().add(item);
        }

        public SG01_Type getSG01(int index) {
            return (SG01_Type) baseList().get(index);
        }

        public int indexOfSG01(SG01_Type item) {
            return baseList().indexOf(item);
        }

        public void addAllSG01(SG01_List item) {
            baseList().addAll(item);
        }

        public SG01_Type[] toArraySG01() {
            return (SG01_Type[]) baseList().toArray(new SG01_Type[] {});
        }

        public SG01_List subListSG01(int fromIndex, int toIndex) {
            return (SG01_List) baseList().subList(fromIndex, toIndex);
        }

        public boolean containsAllSG01(SG01_List item) {
            return baseList().containsAll(item);
        }

        public SG01_Type removeSG01(int index) {
            return (SG01_Type) baseList().remove(index);
        }

        public void addSG01(int index, SG01_Type item) {
            baseList().add(index, item);
        }

        public SG01_Type setSG01(int index, SG01_Type item) {
            return (SG01_Type) baseList().set(index, item);
        }

        public void addAllSG01(int index, SG01_List item) {
            baseList().addAll(index, item);
        }

        public boolean containsSG01(SG01_Type item) {
            return baseList().contains(item);
        }

        public boolean removeSG01(SG01_Type item) {
            return baseList().remove(item);
        }
    }
}
