package freemind.controller.actions.generated.instance.impl;

public class PasteNodeActionTypeImpl extends freemind.controller.actions.generated.instance.impl.NodeActionImpl implements freemind.controller.actions.generated.instance.PasteNodeActionType, com.sun.xml.bind.JAXBObject, freemind.controller.actions.generated.instance.impl.runtime.UnmarshallableObject, freemind.controller.actions.generated.instance.impl.runtime.XMLSerializable, freemind.controller.actions.generated.instance.impl.runtime.ValidatableObject {

    protected freemind.controller.actions.generated.instance.TransferableContentType _TransferableContent;

    protected boolean has_AsSibling;

    protected boolean _AsSibling;

    protected boolean has_IsLeft;

    protected boolean _IsLeft;

    public static final java.lang.Class version = (freemind.controller.actions.generated.instance.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (freemind.controller.actions.generated.instance.PasteNodeActionType.class);
    }

    public freemind.controller.actions.generated.instance.TransferableContentType getTransferableContent() {
        return _TransferableContent;
    }

    public void setTransferableContent(freemind.controller.actions.generated.instance.TransferableContentType value) {
        _TransferableContent = value;
    }

    public boolean isAsSibling() {
        return _AsSibling;
    }

    public void setAsSibling(boolean value) {
        _AsSibling = value;
        has_AsSibling = true;
    }

    public boolean isIsLeft() {
        return _IsLeft;
    }

    public void setIsLeft(boolean value) {
        _IsLeft = value;
        has_IsLeft = true;
    }

    public freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingEventHandler createUnmarshaller(freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingContext context) {
        return new freemind.controller.actions.generated.instance.impl.PasteNodeActionTypeImpl.Unmarshaller(context);
    }

    public void serializeElementBody(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        super.serializeElementBody(context);
        if (_TransferableContent instanceof javax.xml.bind.Element) {
            context.childAsElementBody(((com.sun.xml.bind.JAXBObject) _TransferableContent));
        } else {
            context.startElement("", "transferable_content");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _TransferableContent));
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _TransferableContent));
            context.endAttributes();
            context.childAsElementBody(((com.sun.xml.bind.JAXBObject) _TransferableContent));
            context.endElement();
        }
    }

    public void serializeAttributes(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startAttribute("", "asSibling");
        try {
            context.text(javax.xml.bind.DatatypeConverter.printBoolean(((boolean) _AsSibling)));
        } catch (java.lang.Exception e) {
            freemind.controller.actions.generated.instance.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        context.startAttribute("", "isLeft");
        try {
            context.text(javax.xml.bind.DatatypeConverter.printBoolean(((boolean) _IsLeft)));
        } catch (java.lang.Exception e) {
            freemind.controller.actions.generated.instance.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        super.serializeAttributes(context);
    }

    public void serializeAttributeBody(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        super.serializeAttributeBody(context);
        if (_TransferableContent instanceof javax.xml.bind.Element) {
            context.childAsAttributeBody(((com.sun.xml.bind.JAXBObject) _TransferableContent));
        } else {
            context.startElement("", "transferable_content");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _TransferableContent));
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _TransferableContent));
            context.endAttributes();
            context.childAsElementBody(((com.sun.xml.bind.JAXBObject) _TransferableContent));
            context.endElement();
        }
    }

    public void serializeURIs(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        super.serializeURIs(context);
    }

    public java.lang.Class getPrimaryInterface() {
        return (freemind.controller.actions.generated.instance.PasteNodeActionType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr com.sun.msv.grammar.SequenceExp         xr com.su" + "n.msv.grammar.BinaryExp        L exp1t  Lcom/sun/msv/gra" + "mmar/Expression;L exp2q ~ xr com.sun.msv.grammar.Expressi" + "onøèN5~O I cachedHashCodeL epsilonReducibilityt Ljava" + "/lang/Boolean;L expandedExpq ~ xpÂVÞppsq ~  o\fÜppsq ~  " + "Ò]ppsr  com.sun.msv.grammar.AttributeExp        L expq " + "~ L \tnameClasst Lcom/sun/msv/grammar/NameClass;xq ~ J+p" + "psr com.sun.msv.grammar.DataExp        L dtt Lorg/rela" + "xng/datatype/Datatype;L exceptq ~ L namet Lcom/sun/msv/u" + "til/StringPair;xq ~ dÞËppsr #com.sun.msv.datatype.xsd.Stri" + "ngType        Z \risAlwaysValidxr *com.sun.msv.datatype.xs" + "d.BuiltinAtomicType         xr %com.sun.msv.datatype.xsd.C" + "oncreteType         xr \'com.sun.msv.datatype.xsd.XSDatatyp" + "eImpl        L \fnamespaceUrit Ljava/lang/String;L \btypeN" + "ameq ~ L \nwhiteSpacet .Lcom/sun/msv/datatype/xsd/WhiteSpace" + "Processor;xpt  http://www.w3.org/2001/XMLSchemat stringsr 5" + "com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve       " + "  xr ,com.sun.msv.datatype.xsd.WhiteSpaceProcessor       " + "  xpsr 0com.sun.msv.grammar.Expression$NullSetExpression  " + "       xq ~    \nppsr com.sun.msv.util.StringPairÐtjB " + " L \tlocalNameq ~ L \fnamespaceURIq ~ xpq ~ q ~ sr #com." + "sun.msv.grammar.SimpleNameClass        L \tlocalNameq ~ L" + " \fnamespaceURIq ~ xr com.sun.msv.grammar.NameClass       " + "  xpt nodet  sr com.sun.msv.grammar.ChoiceExp         x" + "q ~  -ppsr \'com.sun.msv.grammar.trex.ElementPattern      " + "  L \tnameClassq ~ \txr com.sun.msv.grammar.ElementExp    " + "    Z ignoreUndeclaredAttributesL \fcontentModelq ~ xq ~" + "  D pp sq ~ & D pp sq ~ $ D ppsr  com.sun.msv.grammar.On" + "eOrMoreExp         xr com.sun.msv.grammar.UnaryExp       " + " L expq ~ xq ~  D zsr java.lang.BooleanÍ rÕúî Z " + "valuexp psq ~ \b D wq ~ /psr 2com.sun.msv.grammar.Expression$" + "AnyStringExpression         xq ~    \bsq ~ .psr  com.sun." + "msv.grammar.AnyNameClass         xq ~  sr 0com.sun.msv.gra" + "mmar.Expression$EpsilonExpression         xq ~    \tq ~ 3q" + " ~ 7sq ~ t Ffreemind.controller.actions.generated.instance." + "TransferableContentTypet +http://java.sun.com/jaxb/xjc/dummy" + "-elementssq ~ t transferable_contentq ~ #sq ~ & D pp sq ~" + " $ D ppsq ~ + D zq ~ /psq ~ \b D wq ~ /pq ~ 2q ~ 5q ~ 7sq ~ " + "t Bfreemind.controller.actions.generated.instance.Transfera" + "bleContentq ~ :sq ~ \b÷zppsq ~  Qï^ppsr $com.sun.msv.datat" + "ype.xsd.BooleanType         xq ~ q ~ t booleansr 5com.s" + "un.msv.datatype.xsd.WhiteSpaceProcessor$Collapse         x" + "q ~ q ~ sq ~ q ~ Gq ~ sq ~ t \tasSiblingq ~ #sq ~ \bSIýp" + "pq ~ Dsq ~ t isLeftq ~ #sr \"com.sun.msv.grammar.Expression" + "Pool        L \bexpTablet /Lcom/sun/msv/grammar/Expression" + "Pool$ClosedHash;xpsr -com.sun.msv.grammar.ExpressionPool$Clo" + "sedHash×jÐNïèí I countI \tthresholdL parentq ~ Q[ table" + "t ![Lcom/sun/msv/grammar/Expression;xp   \b   9pur ![Lcom.sun" + ".msv.grammar.Expression;Ö8DÃ]­§\n  xp   ¿ppppppppppppppppppp" + "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp" + "pppppppppppppppppppppppppppppppq ~ pppppppppppppppppppppppp" + "pppppppppppppppppppppppq ~ -q ~ ?pppppppppq ~ *q ~ >pppq ~ " + "ppq ~ ppppq ~ %pppppppp"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends freemind.controller.actions.generated.instance.impl.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingContext context) {
            super(context, "-----------");
        }

        protected Unmarshaller(freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return freemind.controller.actions.generated.instance.impl.PasteNodeActionTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 8:
                        attIdx = context.getAttribute("", "TransferableAsHtml");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return;
                        }
                        attIdx = context.getAttribute("", "TransferableAsRTF");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return;
                        }
                        attIdx = context.getAttribute("", "TransferableAsPlainText");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return;
                        }
                        attIdx = context.getAttribute("", "TransferableAsDrop");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return;
                        }
                        attIdx = context.getAttribute("", "Transferable");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("TransferableAsFileList" == ___local) && ("" == ___uri)) {
                            _TransferableContent = ((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl) spawnChildFromEnterElement((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl.class), 9, ___uri, ___local, ___qname, __atts));
                            return;
                        }
                        _TransferableContent = ((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl) spawnChildFromEnterElement((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl.class), 9, ___uri, ___local, ___qname, __atts));
                        return;
                    case 7:
                        if (("transferable_content" == ___local) && ("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 8;
                            return;
                        }
                        break;
                    case 10:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                    case 6:
                        attIdx = context.getAttribute("", "node");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return;
                        }
                        break;
                    case 0:
                        attIdx = context.getAttribute("", "asSibling");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText0(v);
                            state = 3;
                            continue outer;
                        }
                        break;
                    case 3:
                        attIdx = context.getAttribute("", "isLeft");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 6;
                            continue outer;
                        }
                        break;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        private void eatText0(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _AsSibling = javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_AsSibling = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText1(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _IsLeft = javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_IsLeft = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 8:
                        attIdx = context.getAttribute("", "TransferableAsHtml");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return;
                        }
                        attIdx = context.getAttribute("", "TransferableAsRTF");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return;
                        }
                        attIdx = context.getAttribute("", "TransferableAsPlainText");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return;
                        }
                        attIdx = context.getAttribute("", "TransferableAsDrop");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return;
                        }
                        attIdx = context.getAttribute("", "Transferable");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return;
                        }
                        _TransferableContent = ((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl) spawnChildFromLeaveElement((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl.class), 9, ___uri, ___local, ___qname));
                        return;
                    case 10:
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return;
                    case 6:
                        attIdx = context.getAttribute("", "node");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return;
                        }
                        break;
                    case 0:
                        attIdx = context.getAttribute("", "asSibling");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText0(v);
                            state = 3;
                            continue outer;
                        }
                        break;
                    case 3:
                        attIdx = context.getAttribute("", "isLeft");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 6;
                            continue outer;
                        }
                        break;
                    case 9:
                        if (("transferable_content" == ___local) && ("" == ___uri)) {
                            context.popAttributes();
                            state = 10;
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
                    case 8:
                        if (("TransferableAsHtml" == ___local) && ("" == ___uri)) {
                            _TransferableContent = ((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl) spawnChildFromEnterAttribute((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl.class), 9, ___uri, ___local, ___qname));
                            return;
                        }
                        if (("TransferableAsRTF" == ___local) && ("" == ___uri)) {
                            _TransferableContent = ((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl) spawnChildFromEnterAttribute((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl.class), 9, ___uri, ___local, ___qname));
                            return;
                        }
                        if (("TransferableAsPlainText" == ___local) && ("" == ___uri)) {
                            _TransferableContent = ((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl) spawnChildFromEnterAttribute((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl.class), 9, ___uri, ___local, ___qname));
                            return;
                        }
                        if (("TransferableAsDrop" == ___local) && ("" == ___uri)) {
                            _TransferableContent = ((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl) spawnChildFromEnterAttribute((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl.class), 9, ___uri, ___local, ___qname));
                            return;
                        }
                        if (("Transferable" == ___local) && ("" == ___uri)) {
                            _TransferableContent = ((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl) spawnChildFromEnterAttribute((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl.class), 9, ___uri, ___local, ___qname));
                            return;
                        }
                        _TransferableContent = ((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl) spawnChildFromEnterAttribute((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl.class), 9, ___uri, ___local, ___qname));
                        return;
                    case 10:
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return;
                    case 6:
                        if (("node" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((freemind.controller.actions.generated.instance.impl.NodeActionImpl) freemind.controller.actions.generated.instance.impl.PasteNodeActionTypeImpl.this).new Unmarshaller(context)), 7, ___uri, ___local, ___qname);
                            return;
                        }
                        break;
                    case 0:
                        if (("asSibling" == ___local) && ("" == ___uri)) {
                            state = 1;
                            return;
                        }
                        break;
                    case 3:
                        if (("isLeft" == ___local) && ("" == ___uri)) {
                            state = 4;
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
                    case 8:
                        attIdx = context.getAttribute("", "TransferableAsHtml");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return;
                        }
                        attIdx = context.getAttribute("", "TransferableAsRTF");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return;
                        }
                        attIdx = context.getAttribute("", "TransferableAsPlainText");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return;
                        }
                        attIdx = context.getAttribute("", "TransferableAsDrop");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return;
                        }
                        attIdx = context.getAttribute("", "Transferable");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return;
                        }
                        _TransferableContent = ((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl) spawnChildFromLeaveAttribute((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl.class), 9, ___uri, ___local, ___qname));
                        return;
                    case 10:
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return;
                    case 6:
                        attIdx = context.getAttribute("", "node");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return;
                        }
                        break;
                    case 2:
                        if (("asSibling" == ___local) && ("" == ___uri)) {
                            state = 3;
                            return;
                        }
                        break;
                    case 5:
                        if (("isLeft" == ___local) && ("" == ___uri)) {
                            state = 6;
                            return;
                        }
                        break;
                    case 0:
                        attIdx = context.getAttribute("", "asSibling");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText0(v);
                            state = 3;
                            continue outer;
                        }
                        break;
                    case 3:
                        attIdx = context.getAttribute("", "isLeft");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 6;
                            continue outer;
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
                        case 8:
                            attIdx = context.getAttribute("", "TransferableAsHtml");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return;
                            }
                            attIdx = context.getAttribute("", "TransferableAsRTF");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return;
                            }
                            attIdx = context.getAttribute("", "TransferableAsPlainText");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return;
                            }
                            attIdx = context.getAttribute("", "TransferableAsDrop");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return;
                            }
                            attIdx = context.getAttribute("", "Transferable");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return;
                            }
                            _TransferableContent = ((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl) spawnChildFromText((freemind.controller.actions.generated.instance.impl.TransferableContentTypeImpl.class), 9, value));
                            return;
                        case 4:
                            eatText1(value);
                            state = 5;
                            return;
                        case 10:
                            revertToParentFromText(value);
                            return;
                        case 6:
                            attIdx = context.getAttribute("", "node");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return;
                            }
                            break;
                        case 1:
                            eatText0(value);
                            state = 2;
                            return;
                        case 0:
                            attIdx = context.getAttribute("", "asSibling");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText0(v);
                                state = 3;
                                continue outer;
                            }
                            break;
                        case 3:
                            attIdx = context.getAttribute("", "isLeft");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 6;
                                continue outer;
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
