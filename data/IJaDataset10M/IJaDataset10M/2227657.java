package freemind.controller.actions.generated.instance.impl;

public class CloudColorXmlActionImpl extends freemind.controller.actions.generated.instance.impl.CloudColorXmlActionTypeImpl implements freemind.controller.actions.generated.instance.CloudColorXmlAction, com.sun.xml.bind.JAXBObject, com.sun.xml.bind.RIElement, freemind.controller.actions.generated.instance.impl.runtime.UnmarshallableObject, freemind.controller.actions.generated.instance.impl.runtime.XMLSerializable, freemind.controller.actions.generated.instance.impl.runtime.ValidatableObject {

    public static final java.lang.Class version = (freemind.controller.actions.generated.instance.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "cloud_color_xml_action";
    }

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (freemind.controller.actions.generated.instance.CloudColorXmlAction.class);
    }

    public freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingEventHandler createUnmarshaller(freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingContext context) {
        return new freemind.controller.actions.generated.instance.impl.CloudColorXmlActionImpl.Unmarshaller(context);
    }

    public void serializeElementBody(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("", "cloud_color_xml_action");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeElementBody(context);
        context.endElement();
    }

    public void serializeAttributes(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public void serializeAttributeBody(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("", "cloud_color_xml_action");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeElementBody(context);
        context.endElement();
    }

    public void serializeURIs(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (freemind.controller.actions.generated.instance.CloudColorXmlAction.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr \'com.sun.msv.grammar.trex.ElementPattern        L " + "\tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv." + "grammar.ElementExp        Z ignoreUndeclaredAttributesL " + "\fcontentModelt  Lcom/sun/msv/grammar/Expression;xr com.sun." + "msv.grammar.ExpressionøèN5~O I cachedHashCodeL epsilon" + "Reducibilityt Ljava/lang/Boolean;L expandedExpq ~ xpåCôp" + "p sr com.sun.msv.grammar.SequenceExp         xr com.sun." + "msv.grammar.BinaryExp        L exp1q ~ L exp2q ~ xq ~" + " åCéppsr  com.sun.msv.grammar.AttributeExp        L ex" + "pq ~ L \tnameClassq ~ xq ~ J+ppsr com.sun.msv.grammar.D" + "ataExp        L dtt Lorg/relaxng/datatype/Datatype;L e" + "xceptq ~ L namet Lcom/sun/msv/util/StringPair;xq ~ dÞËp" + "psr #com.sun.msv.datatype.xsd.StringType        Z \risAlwa" + "ysValidxr *com.sun.msv.datatype.xsd.BuiltinAtomicType       " + "  xr %com.sun.msv.datatype.xsd.ConcreteType         xr \'" + "com.sun.msv.datatype.xsd.XSDatatypeImpl        L \fnamespa" + "ceUrit Ljava/lang/String;L \btypeNameq ~ L \nwhiteSpacet .Lc" + "om/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt  http://www." + "w3.org/2001/XMLSchemat stringsr 5com.sun.msv.datatype.xsd.W" + "hiteSpaceProcessor$Preserve         xr ,com.sun.msv.dataty" + "pe.xsd.WhiteSpaceProcessor         xpsr 0com.sun.msv.gram" + "mar.Expression$NullSetExpression         xq ~    \nppsr c" + "om.sun.msv.util.StringPairÐtjB  L \tlocalNameq ~ L \fnam" + "espaceURIq ~ xpq ~ q ~ sr #com.sun.msv.grammar.SimpleName" + "Class        L \tlocalNameq ~ L \fnamespaceURIq ~ xr com" + ".sun.msv.grammar.NameClass         xpt nodet  sq ~ \n/¹p" + "pq ~ sq ~  t colorq ~ $sq ~  t cloud_color_xml_actionq ~ " + "$sr \"com.sun.msv.grammar.ExpressionPool        L \bexpTabl" + "et /Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr -com" + ".sun.msv.grammar.ExpressionPool$ClosedHash×jÐNïèí I coun" + "tI \tthresholdL parentq ~ +[ tablet ![Lcom/sun/msv/grammar/" + "Expression;xp      9pur ![Lcom.sun.msv.grammar.Expression;Ö" + "8DÃ]­§\n  xp   ¿pppppppppppppppq ~ \tpppppppppppppppppppppppp" + "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp" + "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp" + "ppppppppppppppppppppppppppppppp"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends freemind.controller.actions.generated.instance.impl.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return freemind.controller.actions.generated.instance.impl.CloudColorXmlActionImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 3:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                    case 0:
                        if (("cloud_color_xml_action" == ___local) && ("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return;
                        }
                        break;
                    case 1:
                        attIdx = context.getAttribute("", "color");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
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
                    case 3:
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return;
                    case 2:
                        if (("cloud_color_xml_action" == ___local) && ("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return;
                        }
                        break;
                    case 1:
                        attIdx = context.getAttribute("", "color");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return;
                        }
                        break;
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
                    case 1:
                        if (("color" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((freemind.controller.actions.generated.instance.impl.CloudColorXmlActionTypeImpl) freemind.controller.actions.generated.instance.impl.CloudColorXmlActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                            return;
                        }
                        break;
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
                    case 1:
                        attIdx = context.getAttribute("", "color");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return;
                        }
                        break;
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
                        case 1:
                            attIdx = context.getAttribute("", "color");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return;
                            }
                            break;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }
    }
}
