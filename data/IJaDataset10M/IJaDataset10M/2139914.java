package eu.more.configservice.generated.jaxb.impl;

public class GetPropertyResponse1TypeImpl implements eu.more.configservice.generated.jaxb.GetPropertyResponse1Type, com.sun.xml.bind.JAXBObject, org.soda.jaxb.runtime.UnmarshallableObject, org.soda.jaxb.runtime.XMLSerializable, org.soda.jaxb.runtime.ValidatableObject {

    protected eu.more.configservice.generated.jaxb.KeyValuePair _Property;

    public static final java.lang.Class version = (eu.more.configservice.generated.jaxb.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (eu.more.configservice.generated.jaxb.GetPropertyResponse1Type.class);
    }

    public eu.more.configservice.generated.jaxb.KeyValuePair getProperty() {
        return _Property;
    }

    public void setProperty(eu.more.configservice.generated.jaxb.KeyValuePair value) {
        _Property = value;
    }

    public org.soda.jaxb.runtime.UnmarshallingEventHandler createUnmarshaller(org.soda.jaxb.runtime.UnmarshallingContext context) {
        return new eu.more.configservice.generated.jaxb.impl.GetPropertyResponse1TypeImpl.Unmarshaller(context);
    }

    public void serializeBody(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("http://www.ist-more.org/ConfigService/", "property");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Property), "Property");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Property), "Property");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _Property), "Property");
        context.endElement();
    }

    public void serializeAttributes(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public void serializeURIs(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (eu.more.configservice.generated.jaxb.GetPropertyResponse1Type.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr \'com.sun.msv.grammar.trex.ElementPattern        L " + "\tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv." + "grammar.ElementExp        Z ignoreUndeclaredAttributesL " + "\fcontentModelt  Lcom/sun/msv/grammar/Expression;xr com.sun." + "msv.grammar.ExpressionøèN5~O L epsilonReducibilityt Lj" + "ava/lang/Boolean;L expandedExpq ~ xppp sr com.sun.msv.gra" + "mmar.SequenceExp         xr com.sun.msv.grammar.BinaryExp" + "        L exp1q ~ L exp2q ~ xq ~ ppsq ~  pp sr com." + "sun.msv.grammar.ChoiceExp         xq ~ \bppsr  com.sun.msv." + "grammar.OneOrMoreExp         xr com.sun.msv.grammar.Unary" + "Exp        L expq ~ xq ~ sr java.lang.BooleanÍ rÕúî" + " Z valuexp psr  com.sun.msv.grammar.AttributeExp       " + " L expq ~ L \tnameClassq ~ xq ~ q ~ psr 2com.sun.msv.gr" + "ammar.Expression$AnyStringExpression         xq ~ sq ~ " + "psr  com.sun.msv.grammar.AnyNameClass         xr com.sun." + "msv.grammar.NameClass         xpsr 0com.sun.msv.grammar.Ex" + "pression$EpsilonExpression         xq ~ q ~ psr #com.sun" + ".msv.grammar.SimpleNameClass        L \tlocalNamet Ljava/" + "lang/String;L \fnamespaceURIq ~ xq ~ t 1eu.more.configservi" + "ce.generated.jaxb.KeyValuePairt +http://java.sun.com/jaxb/xj" + "c/dummy-elementssq ~ ppsq ~ q ~ psr com.sun.msv.grammar." + "DataExp        L dtt Lorg/relaxng/datatype/Datatype;L " + "exceptq ~ L namet Lcom/sun/msv/util/StringPair;xq ~ ppsr" + " \"com.sun.msv.datatype.xsd.QnameType         xr *com.sun.m" + "sv.datatype.xsd.BuiltinAtomicType         xr %com.sun.msv." + "datatype.xsd.ConcreteType         xr \'com.sun.msv.datatype" + ".xsd.XSDatatypeImpl        L \fnamespaceUriq ~ L \btypeNam" + "eq ~ L \nwhiteSpacet .Lcom/sun/msv/datatype/xsd/WhiteSpacePr" + "ocessor;xpt  http://www.w3.org/2001/XMLSchemat QNamesr 5com" + ".sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse        " + " xr ,com.sun.msv.datatype.xsd.WhiteSpaceProcessor         " + "xpsr 0com.sun.msv.grammar.Expression$NullSetExpression      " + "   xq ~ ppsr com.sun.msv.util.StringPairÐtjB  L \tlo" + "calNameq ~ L \fnamespaceURIq ~ xpq ~ .q ~ -sq ~ t typet )" + "http://www.w3.org/2001/XMLSchema-instanceq ~ sq ~ t \bprope" + "rtyt &http://www.ist-more.org/ConfigService/sr \"com.sun.msv." + "grammar.ExpressionPool        L \bexpTablet /Lcom/sun/msv/" + "grammar/ExpressionPool$ClosedHash;xpsr -com.sun.msv.grammar." + "ExpressionPool$ClosedHash×jÐNïèí I countB \rstreamVersion" + "L parentt $Lcom/sun/msv/grammar/ExpressionPool;xp   pq ~ " + "\tq ~ \fq ~ q ~ !x"));
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
            return eu.more.configservice.generated.jaxb.impl.GetPropertyResponse1TypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 1:
                        if (("key" == ___local) && ("http://www.ist-more.org/ConfigService/" == ___uri)) {
                            _Property = ((eu.more.configservice.generated.jaxb.impl.KeyValuePairImpl) spawnChildFromEnterElement((eu.more.configservice.generated.jaxb.impl.KeyValuePairImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return;
                        }
                        break;
                    case 3:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                    case 0:
                        if (("property" == ___local) && ("http://www.ist-more.org/ConfigService/" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return;
                        }
                        break;
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
                        if (("property" == ___local) && ("http://www.ist-more.org/ConfigService/" == ___uri)) {
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
