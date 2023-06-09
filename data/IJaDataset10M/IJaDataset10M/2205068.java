package com.google.checkout.schema._2.impl;

public class MerchantCalculationCallbackElementImpl extends com.google.checkout.schema._2.impl.MerchantCalculationCallbackImpl implements com.google.checkout.schema._2.MerchantCalculationCallbackElement, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, com.google.checkout.schema._2.impl.runtime.UnmarshallableObject, com.google.checkout.schema._2.impl.runtime.XMLSerializable, com.google.checkout.schema._2.impl.runtime.ValidatableObject {

    public static final java.lang.Class version = (com.google.checkout.schema._2.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (com.google.checkout.schema._2.MerchantCalculationCallbackElement.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://checkout.google.com/schema/2";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "merchant-calculation-callback";
    }

    public com.google.checkout.schema._2.impl.runtime.UnmarshallingEventHandler createUnmarshaller(com.google.checkout.schema._2.impl.runtime.UnmarshallingContext context) {
        return new com.google.checkout.schema._2.impl.MerchantCalculationCallbackElementImpl.Unmarshaller(context);
    }

    public void serializeBody(com.google.checkout.schema._2.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("http://checkout.google.com/schema/2", "merchant-calculation-callback");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(com.google.checkout.schema._2.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public void serializeURIs(com.google.checkout.schema._2.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (com.google.checkout.schema._2.MerchantCalculationCallbackElement.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr \'com.sun.msv.grammar.trex.ElementPattern        L " + "\tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv." + "grammar.ElementExp        Z ignoreUndeclaredAttributesL " + "\fcontentModelt  Lcom/sun/msv/grammar/Expression;xr com.sun." + "msv.grammar.ExpressionøèN5~O L epsilonReducibilityt Lj" + "ava/lang/Boolean;L expandedExpq ~ xppp sr com.sun.msv.gra" + "mmar.SequenceExp         xr com.sun.msv.grammar.BinaryExp" + "        L exp1q ~ L exp2q ~ xq ~ ppsq ~ ppsr !com.s" + "un.msv.grammar.InterleaveExp         xq ~ \bppsq ~ ppsq ~ " + "ppsr com.sun.msv.grammar.ChoiceExp         xq ~ \bppsq ~ " + " sr java.lang.BooleanÍ rÕúî Z valuexp p sq ~ ppsr co" + "m.sun.msv.grammar.DataExp        L dtt Lorg/relaxng/dat" + "atype/Datatype;L exceptq ~ L namet Lcom/sun/msv/util/Str" + "ingPair;xq ~ ppsr !com.sun.msv.datatype.xsd.LongType       " + "  xr +com.sun.msv.datatype.xsd.IntegerDerivedTypeñ]&6k¾" + " L \nbaseFacetst )Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;x" + "r *com.sun.msv.datatype.xsd.BuiltinAtomicType         xr %" + "com.sun.msv.datatype.xsd.ConcreteType         xr \'com.sun." + "msv.datatype.xsd.XSDatatypeImpl        L \fnamespaceUrit " + "Ljava/lang/String;L \btypeNameq ~ L \nwhiteSpacet .Lcom/sun/m" + "sv/datatype/xsd/WhiteSpaceProcessor;xpt  http://www.w3.org/2" + "001/XMLSchemat longsr 5com.sun.msv.datatype.xsd.WhiteSpaceP" + "rocessor$Collapse         xr ,com.sun.msv.datatype.xsd.Whi" + "teSpaceProcessor         xpsr *com.sun.msv.datatype.xsd.Ma" + "xInclusiveFacet         xr #com.sun.msv.datatype.xsd.Range" + "Facet        L \nlimitValuet Ljava/lang/Object;xr 9com.su" + "n.msv.datatype.xsd.DataTypeWithValueConstraintFacet\"§RoÊÇT" + "  xr *com.sun.msv.datatype.xsd.DataTypeWithFacet        Z" + " \fisFacetFixedZ needValueCheckFlagL \bbaseTypeq ~ L \fconcre" + "teTypet \'Lcom/sun/msv/datatype/xsd/ConcreteType;L \tfacetName" + "q ~ xq ~ ppq ~ & sr *com.sun.msv.datatype.xsd.MinInclusiv" + "eFacet         xq ~ (ppq ~ &  sr $com.sun.msv.datatype.xsd" + ".IntegerType         xq ~ q ~ \"t integerq ~ &sr ,com.sun" + ".msv.datatype.xsd.FractionDigitsFacet        I scalexr ;" + "com.sun.msv.datatype.xsd.DataTypeWithLexicalConstraintFacetT" + ">zbê  xq ~ +ppq ~ & sr #com.sun.msv.datatype.xsd.Number" + "Type         xq ~ q ~ \"t decimalq ~ &q ~ 7t fractionDig" + "its    q ~ 1t \fminInclusivesr java.lang.Long;äÌ#ß J v" + "aluexr java.lang.Number¬à  xp       q ~ 1t \fmaxIncl" + "usivesq ~ ;ÿÿÿÿÿÿÿsr 0com.sun.msv.grammar.Expression$NullSe" + "tExpression         xq ~ ppsr com.sun.msv.util.StringPai" + "rÐtjB  L \tlocalNameq ~ L \fnamespaceURIq ~ xpq ~ #q ~ " + "\"sq ~ ppsr  com.sun.msv.grammar.AttributeExp        L e" + "xpq ~ L \tnameClassq ~ xq ~ q ~ psq ~ ppsr \"com.sun.msv." + "datatype.xsd.QnameType         xq ~ q ~ \"t QNameq ~ &q ~" + " Asq ~ Bq ~ Jq ~ \"sr #com.sun.msv.grammar.SimpleNameClass   " + "     L \tlocalNameq ~ L \fnamespaceURIq ~ xr com.sun.msv" + ".grammar.NameClass         xpt typet )http://www.w3.org/2" + "001/XMLSchema-instancesr 0com.sun.msv.grammar.Expression$Eps" + "ilonExpression         xq ~ sq ~ q ~ Rsq ~ Lt \bbuyer-id" + "t #http://checkout.google.com/schema/2q ~ Rsq ~  pp sq ~ pp" + "sq ~ ppsr #com.sun.msv.datatype.xsd.StringType        Z " + "\risAlwaysValidxq ~ q ~ \"t stringsr 5com.sun.msv.datatype.x" + "sd.WhiteSpaceProcessor$Preserve         xq ~ %q ~ Asq ~ B" + "q ~ \\q ~ \"sq ~ ppsq ~ Eq ~ pq ~ Gq ~ Nq ~ Rsq ~ Lt buyer-" + "languageq ~ Vsq ~  pp sq ~ ppsq ~  pp sq ~ ppsr  com.sun.m" + "sv.grammar.OneOrMoreExp         xr com.sun.msv.grammar.Un" + "aryExp        L expq ~ xq ~ q ~ psq ~ Eq ~ psr 2com." + "sun.msv.grammar.Expression$AnyStringExpression         xq " + "~ q ~ Sq ~ msr  com.sun.msv.grammar.AnyNameClass         " + "xq ~ Mq ~ Rsq ~ Lt \'com.google.checkout.schema._2.Calculatet" + " +http://java.sun.com/jaxb/xjc/dummy-elementssq ~ ppsq ~ Eq" + " ~ pq ~ Gq ~ Nq ~ Rsq ~ Lt \tcalculateq ~ Vsq ~  pp sq ~ pp" + "sq ~  pp sq ~ ppsq ~ hq ~ psq ~ Eq ~ pq ~ mq ~ oq ~ Rsq ~" + " Lt *com.google.checkout.schema._2.ShoppingCartq ~ rsq ~ pp" + "sq ~ Eq ~ pq ~ Gq ~ Nq ~ Rsq ~ Lt \rshopping-cartq ~ Vsq ~ E" + "ppq ~ Ysq ~ Lt \rserial-numbert  sq ~ ppsq ~ Eq ~ pq ~ Gq ~" + " Nq ~ Rsq ~ Lt merchant-calculation-callbackq ~ Vsr \"com.su" + "n.msv.grammar.ExpressionPool        L \bexpTablet /Lcom/su" + "n/msv/grammar/ExpressionPool$ClosedHash;xpsr -com.sun.msv.gr" + "ammar.ExpressionPool$ClosedHash×jÐNïèí I countB \rstreamV" + "ersionL parentt $Lcom/sun/msv/grammar/ExpressionPool;xp   " + "pq ~ eq ~ xq ~ q ~ q ~ Xq ~ q ~ Dq ~ `q ~ sq ~ q ~ q ~" + " \fq ~ jq ~ {q ~ \nq ~ \tq ~ gq ~ zq ~ \rx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends com.google.checkout.schema._2.impl.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(com.google.checkout.schema._2.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(com.google.checkout.schema._2.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return com.google.checkout.schema._2.impl.MerchantCalculationCallbackElementImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 1:
                        attIdx = context.getAttribute("", "serial-number");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return;
                        }
                        break;
                    case 3:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                    case 0:
                        if (("merchant-calculation-callback" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
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
                        if (("merchant-calculation-callback" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return;
                        }
                        break;
                    case 1:
                        attIdx = context.getAttribute("", "serial-number");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
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
                    case 1:
                        if (("serial-number" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((com.google.checkout.schema._2.impl.MerchantCalculationCallbackImpl) com.google.checkout.schema._2.impl.MerchantCalculationCallbackElementImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                            return;
                        }
                        break;
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
                    case 1:
                        attIdx = context.getAttribute("", "serial-number");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return;
                        }
                        break;
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
                        case 1:
                            attIdx = context.getAttribute("", "serial-number");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return;
                            }
                            break;
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
