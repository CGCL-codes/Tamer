package org.jaffa.tools.patternmetaengine.domain.impl;

public class AppBuilderImpl extends org.jaffa.tools.patternmetaengine.domain.impl.AppBuilderTypeImpl implements org.jaffa.tools.patternmetaengine.domain.AppBuilder, com.sun.xml.bind.RIElement, com.sun.xml.bind.unmarshaller.UnmarshallableObject, com.sun.xml.bind.serializer.XMLSerializable, com.sun.xml.bind.validator.ValidatableObject {

    private static final com.sun.msv.grammar.Grammar schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize("¬í sr \'com.sun.msv.grammar.trex.ElementPattern        L \tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv.grammar.ElementExp        Z ignoreUndeclaredAttributesL \fcontentModelt  Lcom/sun/msv/grammar/Expression;xr com.sun.msv.grammar.ExpressionøèN5~O I cachedHashCodeL epsilonReducibilityt Ljava/lang/Boolean;L expandedExpq ~ xpopp sr com.sun.msv.grammar.SequenceExp         xr com.sun.msv.grammar.BinaryExp        L exp1q ~ L exp2q ~ xq ~ dppsq ~  þNeppsq ~  ©ppsq ~   TÄÈpp sr com.sun.msv.grammar.DataExp        L dtt Lorg/relaxng/datatype/Datatype;L exceptq ~ L namet Lcom/sun/msv/util/StringPair;xq ~  TÄ½ppsr #com.sun.msv.datatype.xsd.StringType        Z \risAlwaysValidxr *com.sun.msv.datatype.xsd.BuiltinAtomicType         xr %com.sun.msv.datatype.xsd.ConcreteType         xr \'com.sun.msv.datatype.xsd.XSDatatypeImpl        L \fnamespaceUrit Ljava/lang/String;L \btypeNameq ~ L \nwhiteSpacet .Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt  http://www.w3.org/2001/XMLSchemat stringsr .com.sun.msv.datatype.xsd.WhiteSpaceProcessor$1JMoIÛ¤G  xr ,com.sun.msv.datatype.xsd.WhiteSpaceProcessor         xpsr 0com.sun.msv.grammar.Expression$NullSetExpression         xq ~    \nppsr com.sun.msv.util.StringPairÐtjB  L \tlocalNameq ~ L \fnamespaceURIq ~ xpq ~ q ~ sr #com.sun.msv.grammar.SimpleNameClass        L \tlocalNameq ~ L \fnamespaceURIq ~ xr com.sun.msv.grammar.NameClass         xpt \bapp-namet  sq ~   TÄÈpp q ~ sq ~ !t package-prefixq ~ %sr  com.sun.msv.grammar.OneOrMoreExp         xr com.sun.msv.grammar.UnaryExp        L expq ~ xq ~  TÄËppsq ~   TÄÈpp q ~ sq ~ !t domain-object-pathq ~ %sq ~ )>úppsq ~  >÷pp sq ~  >ìpp sr com.sun.msv.grammar.ChoiceExp         xq ~ \b>áppsq ~ )>Ösr java.lang.BooleanÍ rÕúî Z valuexp psr  com.sun.msv.grammar.AttributeExp        L expq ~ L \tnameClassq ~ xq ~ >Óq ~ 6psr 2com.sun.msv.grammar.Expression$AnyStringExpression         xq ~    \bsq ~ 5q ~ :sr  com.sun.msv.grammar.AnyNameClass         xq ~ \"sr 0com.sun.msv.grammar.Expression$EpsilonExpression         xq ~    \tq ~ ;psq ~ !t /org.jaffa.tools.patternmetaengine.domain.Modulet +http://java.sun.com/jaxb/xjc/dummy-elementssq ~ !t moduleq ~ %sq ~ !t app-builderq ~ %sr \"com.sun.msv.grammar.ExpressionPool        L \bexpTablet /Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr -com.sun.msv.grammar.ExpressionPool$ClosedHash×jÐNïèí I countI \tthresholdL parentq ~ H[ tablet ![Lcom/sun/msv/grammar/Expression;xp      9pur ![Lcom.sun.msv.grammar.Expression;Ö8DÃ]­§\n  xp   ¿ppq ~ /pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppq ~ \nppppppq ~ \tppppppppppq ~ ppppppppppppq ~ 4ppppppppppq ~ +q ~ 3ppppppppppppppppppppp");

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "app-builder";
    }

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return org.jaffa.tools.patternmetaengine.domain.AppBuilder.class;
    }

    public com.sun.xml.bind.unmarshaller.ContentHandlerEx getUnmarshaller(com.sun.xml.bind.unmarshaller.UnmarshallingContext context) {
        return new org.jaffa.tools.patternmetaengine.domain.impl.AppBuilderImpl.Unmarshaller(context);
    }

    public java.lang.Class getPrimaryInterfaceClass() {
        return PRIMARY_INTERFACE_CLASS();
    }

    public void serializeElements(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("", "app-builder");
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
        return (org.jaffa.tools.patternmetaengine.domain.AppBuilder.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends com.sun.xml.bind.unmarshaller.ContentHandlerEx {

        public Unmarshaller(com.sun.xml.bind.unmarshaller.UnmarshallingContext context) {
            super(context, "----");
        }

        protected com.sun.xml.bind.unmarshaller.UnmarshallableObject owner() {
            return org.jaffa.tools.patternmetaengine.domain.impl.AppBuilderImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, org.xml.sax.Attributes __atts) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 0:
                    if (("" == ___uri) && ("app-builder" == ___local)) {
                        context.pushAttributes(__atts);
                        state = 1;
                        return;
                    }
                    break;
                case 3:
                    revertToParentFromEnterElement(___uri, ___local, __atts);
                    return;
                case 1:
                    if (("" == ___uri) && ("app-name" == ___local)) {
                        spawnSuperClassFromEnterElement((((org.jaffa.tools.patternmetaengine.domain.impl.AppBuilderTypeImpl) org.jaffa.tools.patternmetaengine.domain.impl.AppBuilderImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, __atts);
                        return;
                    }
                    break;
            }
            super.enterElement(___uri, ___local, __atts);
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 2:
                    if (("" == ___uri) && ("app-builder" == ___local)) {
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
