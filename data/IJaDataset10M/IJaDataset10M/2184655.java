package eu.more.alarmservice.generated.jaxb.impl;

public class AddServiceToGroupImpl extends eu.more.alarmservice.generated.jaxb.impl.AddServiceToGroupTypeImpl implements eu.more.alarmservice.generated.jaxb.AddServiceToGroup, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, org.soda.jaxb.runtime.UnmarshallableObject, org.soda.jaxb.runtime.XMLSerializable, org.soda.jaxb.runtime.ValidatableObject {

    public static final java.lang.Class version = (eu.more.alarmservice.generated.jaxb.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (eu.more.alarmservice.generated.jaxb.AddServiceToGroup.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://www.ist-more.org/AlarmService/";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "addServiceToGroup";
    }

    public org.soda.jaxb.runtime.UnmarshallingEventHandler createUnmarshaller(org.soda.jaxb.runtime.UnmarshallingContext context) {
        return new eu.more.alarmservice.generated.jaxb.impl.AddServiceToGroupImpl.Unmarshaller(context);
    }

    public void serializeBody(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("http://www.ist-more.org/AlarmService/", "addServiceToGroup");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public void serializeURIs(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (eu.more.alarmservice.generated.jaxb.AddServiceToGroup.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr \'com.sun.msv.grammar.trex.ElementPattern        L " + "\tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv." + "grammar.ElementExp        Z ignoreUndeclaredAttributesL " + "\fcontentModelt  Lcom/sun/msv/grammar/Expression;xr com.sun." + "msv.grammar.ExpressionøèN5~O L epsilonReducibilityt Lj" + "ava/lang/Boolean;L expandedExpq ~ xppp sr com.sun.msv.gra" + "mmar.SequenceExp         xr com.sun.msv.grammar.BinaryExp" + "        L exp1q ~ L exp2q ~ xq ~ ppsq ~ ppsq ~ pps" + "q ~  pp sq ~ ppsr com.sun.msv.grammar.DataExp        L " + "dtt Lorg/relaxng/datatype/Datatype;L exceptq ~ L namet " + "Lcom/sun/msv/util/StringPair;xq ~ ppsr #com.sun.msv.dataty" + "pe.xsd.StringType        Z \risAlwaysValidxr *com.sun.msv." + "datatype.xsd.BuiltinAtomicType         xr %com.sun.msv.dat" + "atype.xsd.ConcreteType         xr \'com.sun.msv.datatype.xs" + "d.XSDatatypeImpl        L \fnamespaceUrit Ljava/lang/Stri" + "ng;L \btypeNameq ~ L \nwhiteSpacet .Lcom/sun/msv/datatype/xsd" + "/WhiteSpaceProcessor;xpt  http://www.w3.org/2001/XMLSchemat " + "stringsr 5com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Pres" + "erve         xr ,com.sun.msv.datatype.xsd.WhiteSpaceProces" + "sor         xpsr 0com.sun.msv.grammar.Expression$NullSetE" + "xpression         xq ~ ppsr com.sun.msv.util.StringPairÐ" + "tjB  L \tlocalNameq ~ L \fnamespaceURIq ~ xpq ~ q ~ s" + "r com.sun.msv.grammar.ChoiceExp         xq ~ \bppsr  com.s" + "un.msv.grammar.AttributeExp        L expq ~ L \tnameClas" + "sq ~ xq ~ sr java.lang.BooleanÍ rÕúî Z valuexp psq ~" + " ppsr \"com.sun.msv.datatype.xsd.QnameType         xq ~ q" + " ~ t QNamesr 5com.sun.msv.datatype.xsd.WhiteSpaceProcessor" + "$Collapse         xq ~ q ~ sq ~  q ~ +q ~ sr #com.sun.m" + "sv.grammar.SimpleNameClass        L \tlocalNameq ~ L \fnam" + "espaceURIq ~ xr com.sun.msv.grammar.NameClass         xp" + "t typet )http://www.w3.org/2001/XMLSchema-instancesr 0com.s" + "un.msv.grammar.Expression$EpsilonExpression         xq ~ " + "sq ~ &psq ~ /t \tgroupNamet %http://www.ist-more.org/AlarmSe" + "rvice/sq ~  pp sq ~ ppq ~ sq ~ \"ppsq ~ $q ~ \'pq ~ (q ~ 1q " + "~ 5sq ~ /t GMSDeviceIDq ~ 9sq ~  pp sq ~ ppq ~ sq ~ \"ppsq" + " ~ $q ~ \'pq ~ (q ~ 1q ~ 5sq ~ /t \fGMSServiceIDq ~ 9sq ~ \"pps" + "q ~ $q ~ \'pq ~ (q ~ 1q ~ 5sq ~ /t addServiceToGroupq ~ 9sr " + "\"com.sun.msv.grammar.ExpressionPool        L \bexpTablet /" + "Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr -com.sun" + ".msv.grammar.ExpressionPool$ClosedHash×jÐNïèí I countB \r" + "streamVersionL parentt $Lcom/sun/msv/grammar/ExpressionPool" + ";xp   \npq ~ #q ~ <q ~ Bq ~ Fq ~ \rq ~ ;q ~ Aq ~ \nq ~ \tq ~ x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends org.soda.jaxb.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(org.soda.jaxb.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(org.soda.jaxb.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return eu.more.alarmservice.generated.jaxb.impl.AddServiceToGroupImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 1:
                        if (("groupName" == ___local) && ("http://www.ist-more.org/AlarmService/" == ___uri)) {
                            spawnHandlerFromEnterElement((((eu.more.alarmservice.generated.jaxb.impl.AddServiceToGroupTypeImpl) eu.more.alarmservice.generated.jaxb.impl.AddServiceToGroupImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        break;
                    case 0:
                        if (("addServiceToGroup" == ___local) && ("http://www.ist-more.org/AlarmService/" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return;
                        }
                        break;
                    case 3:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 2:
                        if (("addServiceToGroup" == ___local) && ("http://www.ist-more.org/AlarmService/" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return;
                        }
                        break;
                    case 3:
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return;
                }
                super.leaveElement(___uri, ___local, ___qname);
                break;
            }
        }

        public void enterAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 3:
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return;
                }
                super.enterAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 3:
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return;
                }
                super.leaveAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void handleText(final java.lang.String value) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                try {
                    switch(state) {
                        case 3:
                            revertToParentFromText(value);
                            return;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }
    }
}
