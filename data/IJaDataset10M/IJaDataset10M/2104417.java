package org.jaffa.tools.loadtest.domain.impl;

public class LoadTestingImpl extends org.jaffa.tools.loadtest.domain.impl.LoadTestingTypeImpl implements org.jaffa.tools.loadtest.domain.LoadTesting, com.sun.xml.bind.RIElement, com.sun.xml.bind.unmarshaller.UnmarshallableObject, com.sun.xml.bind.serializer.XMLSerializable, com.sun.xml.bind.validator.ValidatableObject {

    private static final com.sun.msv.grammar.Grammar schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize("¬í sr \'com.sun.msv.grammar.trex.ElementPattern        L \tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv.grammar.ElementExp        Z ignoreUndeclaredAttributesL \fcontentModelt  Lcom/sun/msv/grammar/Expression;xr com.sun.msv.grammar.ExpressionøèN5~O I cachedHashCodeL epsilonReducibilityt Ljava/lang/Boolean;L expandedExpq ~ xp )ãpp sr  com.sun.msv.grammar.OneOrMoreExp         xr com.sun.msv.grammar.UnaryExp        L expq ~ xq ~  )ãppsq ~   )ãpp sq ~   )ãypp sr com.sun.msv.grammar.ChoiceExp         xr com.sun.msv.grammar.BinaryExp        L exp1q ~ L exp2q ~ xq ~  )ãnppsq ~  )ãcsr java.lang.BooleanÍ rÕúî Z valuexp psr  com.sun.msv.grammar.AttributeExp        L expq ~ L \tnameClassq ~ xq ~  )ã`q ~ psr 2com.sun.msv.grammar.Expression$AnyStringExpression         xq ~    \bsq ~ q ~ sr  com.sun.msv.grammar.AnyNameClass         xr com.sun.msv.grammar.NameClass         xpsr 0com.sun.msv.grammar.Expression$EpsilonExpression         xq ~    \tq ~ psr #com.sun.msv.grammar.SimpleNameClass        L \tlocalNamet Ljava/lang/String;L \fnamespaceURIq ~ xq ~ t \'org.jaffa.tools.loadtest.domain.TestSett +http://java.sun.com/jaxb/xjc/dummy-elementssq ~ t \btest-sett  sq ~ t \fload-testingq ~ #sr \"com.sun.msv.grammar.ExpressionPool        L \bexpTablet /Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr -com.sun.msv.grammar.ExpressionPool$ClosedHash×jÐNïèí I countI \tthresholdL parentq ~ \'[ tablet ![Lcom/sun/msv/grammar/Expression;xp      9pur ![Lcom.sun.msv.grammar.Expression;Ö8DÃ]­§\n  xp   ¿pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppq ~ ppppppppppq ~ ppppppppppppppppppppppppq ~ \tppppppppppppppppppp");

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "load-testing";
    }

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return org.jaffa.tools.loadtest.domain.LoadTesting.class;
    }

    public com.sun.xml.bind.unmarshaller.ContentHandlerEx getUnmarshaller(com.sun.xml.bind.unmarshaller.UnmarshallingContext context) {
        return new org.jaffa.tools.loadtest.domain.impl.LoadTestingImpl.Unmarshaller(context);
    }

    public java.lang.Class getPrimaryInterfaceClass() {
        return PRIMARY_INTERFACE_CLASS();
    }

    public void serializeElements(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("", "load-testing");
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeElements(context);
        context.endElement();
    }

    public void serializeAttributes(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public void serializeAttributeBodies(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (org.jaffa.tools.loadtest.domain.LoadTesting.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends com.sun.xml.bind.unmarshaller.ContentHandlerEx {

        public Unmarshaller(com.sun.xml.bind.unmarshaller.UnmarshallingContext context) {
            super(context, "----");
        }

        protected com.sun.xml.bind.unmarshaller.UnmarshallableObject owner() {
            return org.jaffa.tools.loadtest.domain.impl.LoadTestingImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, org.xml.sax.Attributes __atts) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 1:
                    if (("" == ___uri) && ("test-set" == ___local)) {
                        spawnSuperClassFromEnterElement((((org.jaffa.tools.loadtest.domain.impl.LoadTestingTypeImpl) org.jaffa.tools.loadtest.domain.impl.LoadTestingImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, __atts);
                        return;
                    }
                    break;
                case 0:
                    if (("" == ___uri) && ("load-testing" == ___local)) {
                        context.pushAttributes(__atts);
                        state = 1;
                        return;
                    }
                    break;
                case 3:
                    revertToParentFromEnterElement(___uri, ___local, __atts);
                    return;
            }
            super.enterElement(___uri, ___local, __atts);
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 2:
                    if (("" == ___uri) && ("load-testing" == ___local)) {
                        context.popAttributes();
                        state = 3;
                        return;
                    }
                    break;
                case 3:
                    revertToParentFromLeaveElement(___uri, ___local);
                    return;
            }
            super.leaveElement(___uri, ___local);
        }

        public void enterAttribute(java.lang.String ___uri, java.lang.String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 3:
                    revertToParentFromEnterAttribute(___uri, ___local);
                    return;
            }
            super.enterAttribute(___uri, ___local);
        }

        public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 3:
                    revertToParentFromLeaveAttribute(___uri, ___local);
                    return;
            }
            super.leaveAttribute(___uri, ___local);
        }

        public void text(java.lang.String value) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            try {
                switch(state) {
                    case 3:
                        revertToParentFromText(value);
                        return;
                }
            } catch (java.lang.RuntimeException e) {
                handleUnexpectedTextException(value, e);
            }
        }

        public void leaveChild(int nextState) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(nextState) {
                case 2:
                    state = 2;
                    return;
            }
            super.leaveChild(nextState);
        }
    }
}
