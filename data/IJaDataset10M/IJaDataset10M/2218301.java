package com.siemens.ct.exi.core;

import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import com.siemens.ct.exi.Constants;
import com.siemens.ct.exi.EXIBodyEncoder;
import com.siemens.ct.exi.EXIFactory;
import com.siemens.ct.exi.EncodingOptions;
import com.siemens.ct.exi.FidelityOptions;
import com.siemens.ct.exi.attributes.AttributeList;
import com.siemens.ct.exi.context.CoderContext;
import com.siemens.ct.exi.context.EncoderContext;
import com.siemens.ct.exi.context.EncoderContextImpl;
import com.siemens.ct.exi.context.EvolvingUriContext;
import com.siemens.ct.exi.context.QNameContext;
import com.siemens.ct.exi.context.UriContext;
import com.siemens.ct.exi.core.container.NamespaceDeclaration;
import com.siemens.ct.exi.datatype.Datatype;
import com.siemens.ct.exi.datatype.strings.StringCoder;
import com.siemens.ct.exi.exceptions.EXIException;
import com.siemens.ct.exi.grammar.EventInformation;
import com.siemens.ct.exi.grammar.event.Attribute;
import com.siemens.ct.exi.grammar.event.AttributeNS;
import com.siemens.ct.exi.grammar.event.DatatypeEvent;
import com.siemens.ct.exi.grammar.event.EventType;
import com.siemens.ct.exi.grammar.event.StartElement;
import com.siemens.ct.exi.grammar.event.StartElementNS;
import com.siemens.ct.exi.grammar.rule.Rule;
import com.siemens.ct.exi.grammar.rule.SchemaInformedFirstStartTagRule;
import com.siemens.ct.exi.grammar.rule.SchemaInformedRule;
import com.siemens.ct.exi.io.channel.EncoderChannel;
import com.siemens.ct.exi.types.BuiltIn;
import com.siemens.ct.exi.types.BuiltInType;
import com.siemens.ct.exi.types.TypeEncoder;
import com.siemens.ct.exi.util.MethodsBag;
import com.siemens.ct.exi.util.xml.QNameUtilities;
import com.siemens.ct.exi.values.QNameValue;
import com.siemens.ct.exi.values.StringValue;
import com.siemens.ct.exi.values.Value;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.8
 */
public abstract class AbstractEXIBodyEncoder extends AbstractEXIBodyCoder implements EXIBodyEncoder {

    protected final EXIHeaderEncoder exiHeader;

    /** prefix of previous start element (relevant for preserving prefixes) */
    protected String sePrefix = null;

    /** Output Channel */
    protected EncoderChannel channel;

    /** Type Encoder (including string encoder etc.) */
    protected TypeEncoder typeEncoder;

    /** Encoding options */
    protected EncodingOptions encodingOptions;

    /** Encoder Context */
    EncoderContext encoderContext;

    protected boolean grammarLearningDisabled;

    public AbstractEXIBodyEncoder(EXIFactory exiFactory) throws EXIException {
        super(exiFactory);
        this.exiHeader = new EXIHeaderEncoder();
    }

    @Override
    protected void initFactoryInformation() throws EXIException {
        super.initFactoryInformation();
        typeEncoder = exiFactory.createTypeEncoder();
        encodingOptions = exiFactory.getEncodingOptions();
        grammarLearningDisabled = exiFactory.isGrammarLearningDisabled();
        this.encoderContext = new EncoderContextImpl(exiFactory.getGrammar().getGrammarContext(), exiFactory.createStringEncoder());
    }

    @Override
    protected void initForEachRun() throws EXIException, IOException {
        super.initForEachRun();
        encoderContext.clear();
    }

    protected CoderContext getCoderContext() {
        return this.encoderContext;
    }

    public void flush() throws IOException {
        channel.flush();
    }

    protected void writeString(String text) throws IOException {
        channel.encodeString(text);
    }

    protected boolean isTypeValid(Datatype datatype, Value value) {
        return typeEncoder.isValid(datatype, value);
    }

    protected abstract void writeValue(QNameContext valueContext) throws IOException;

    protected void encode1stLevelEventCode(int pos) throws IOException {
        int codeLength = getCurrentRule().get1stLevelEventCodeLength(fidelityOptions);
        if (codeLength > 0) {
            channel.encodeNBitUnsignedInteger(pos, codeLength);
        }
    }

    protected void encode2ndLevelEventCode(int pos) throws IOException {
        final Rule currentRule = getCurrentRule();
        channel.encodeNBitUnsignedInteger(currentRule.getNumberOfEvents(), currentRule.get1stLevelEventCodeLength(fidelityOptions));
        int ch2 = currentRule.get2ndLevelCharacteristics(fidelityOptions);
        assert (pos < ch2);
        channel.encodeNBitUnsignedInteger(pos, MethodsBag.getCodingLength(ch2));
    }

    protected void encode3rdLevelEventCode(int pos) throws IOException {
        final Rule currentRule = getCurrentRule();
        channel.encodeNBitUnsignedInteger(currentRule.getNumberOfEvents(), currentRule.get1stLevelEventCodeLength(fidelityOptions));
        int ch2 = currentRule.get2ndLevelCharacteristics(fidelityOptions);
        int ec2 = ch2 > 0 ? ch2 - 1 : 0;
        channel.encodeNBitUnsignedInteger(ec2, MethodsBag.getCodingLength(ch2));
        int ch3 = currentRule.get3rdLevelCharacteristics(fidelityOptions);
        assert (pos < ch3);
        channel.encodeNBitUnsignedInteger(pos, MethodsBag.getCodingLength(ch3));
    }

    public void encodeStartDocument() throws EXIException, IOException {
        if (this.channel == null) {
            throw new EXIException("No valid EXI OutputStream set for encoding. Please use setOutput( ... )");
        }
        initForEachRun();
        EventInformation ei = getCurrentRule().lookForEvent(EventType.START_DOCUMENT);
        if (ei == null) {
            throw new EXIException("No EXI Event found for startDocument");
        }
        updateCurrentRule(ei.next);
    }

    public void encodeEndDocument() throws EXIException, IOException {
        EventInformation ei = getCurrentRule().lookForEvent(EventType.END_DOCUMENT);
        if (ei != null) {
            encode1stLevelEventCode(ei.getEventCode());
        } else {
            throw new EXIException("No EXI Event found for endDocument");
        }
    }

    public void encodeStartElement(QName se) throws EXIException, IOException {
        encodeStartElement(se.getNamespaceURI(), se.getLocalPart(), se.getPrefix());
    }

    public void encodeStartElement(String uri, String localName, String prefix) throws EXIException, IOException {
        sePrefix = prefix;
        EventInformation ei;
        Rule updContextRule;
        StartElement nextSE;
        Rule currentRule = getCurrentRule();
        if ((ei = currentRule.lookForStartElement(uri, localName)) != null) {
            assert (ei.event.isEventType(EventType.START_ELEMENT));
            encode1stLevelEventCode(ei.getEventCode());
            nextSE = (StartElement) ei.event;
            if (preservePrefix) {
                encoderContext.encodeQNamePrefix(nextSE.getQNameContext(), prefix, channel);
            }
            updContextRule = ei.next;
        } else if ((ei = currentRule.lookForStartElementNS(uri)) != null) {
            assert (ei.event.isEventType(EventType.START_ELEMENT_NS));
            encode1stLevelEventCode(ei.getEventCode());
            StartElementNS seNS = (StartElementNS) ei.event;
            EvolvingUriContext uc = encoderContext.getUriContext(seNS.getNamespaceUriID());
            QNameContext qnc = encoderContext.encodeLocalName(localName, uc, channel);
            if (preservePrefix) {
                encoderContext.encodeQNamePrefix(qnc, prefix, channel);
            }
            updContextRule = ei.next;
            nextSE = encoderContext.getGlobalStartElement(qnc);
        } else {
            if ((ei = currentRule.lookForEvent(EventType.START_ELEMENT_GENERIC)) != null) {
                assert (ei.event.isEventType(EventType.START_ELEMENT_GENERIC));
                encode1stLevelEventCode(ei.getEventCode());
                updContextRule = ei.next;
            } else {
                int ecSEundeclared = currentRule.get2ndLevelEventCode(EventType.START_ELEMENT_GENERIC_UNDECLARED, fidelityOptions);
                if (ecSEundeclared == Constants.NOT_FOUND) {
                    throw new EXIException("Unexpected SE {" + uri + "}" + localName + ", " + exiFactory.toString());
                }
                if (limitGrammarLearning()) {
                    currentRule = getCurrentRule();
                    ei = currentRule.lookForEvent(EventType.START_ELEMENT_GENERIC);
                    assert (ei != null);
                    encode1stLevelEventCode(ei.getEventCode());
                    updContextRule = ei.next;
                } else {
                    encode2ndLevelEventCode(ecSEundeclared);
                    updContextRule = currentRule.getElementContentRule();
                }
            }
            QNameContext qnc = encoderContext.encodeQName(uri, localName, channel);
            if (preservePrefix) {
                encoderContext.encodeQNamePrefix(qnc, prefix, channel);
            }
            nextSE = encoderContext.getGlobalStartElement(qnc);
            currentRule.learnStartElement(nextSE);
        }
        pushElement(updContextRule, nextSE);
    }

    private boolean limitGrammarLearning() throws EXIException, IOException {
        if (grammarLearningDisabled && grammar.isSchemaInformed() && !getCurrentRule().isSchemaInformed()) {
            String pfx = null;
            if (this.preservePrefix) {
                EvolvingUriContext euc = encoderContext.getUriContext(3);
                int numberOfPrefixes = euc.getNumberOfPrefixes();
                if (numberOfPrefixes > 0) {
                    pfx = euc.getPrefix(0);
                }
            }
            QNameValue type = new QNameValue(XMLConstants.W3C_XML_SCHEMA_NS_URI, "anyType", pfx);
            this.encodeAttributeXsiType(type, pfx);
            return true;
        } else {
            return false;
        }
    }

    public void encodeNamespaceDeclaration(String uri, String prefix) throws EXIException, IOException {
        declarePrefix(prefix, uri);
        if (preservePrefix) {
            assert (sePrefix != null);
            final Rule currentRule = getCurrentRule();
            int ec2 = currentRule.get2ndLevelEventCode(EventType.NAMESPACE_DECLARATION, fidelityOptions);
            assert (currentRule.get2ndLevelEvent(ec2, fidelityOptions) == EventType.NAMESPACE_DECLARATION);
            encode2ndLevelEventCode(ec2);
            EvolvingUriContext euc = encoderContext.encodeUri(uri, channel);
            encoderContext.encodeNamespacePrefix(euc, prefix, channel);
            channel.encodeBoolean(prefix.equals(sePrefix));
        }
    }

    public void encodeEndElement() throws EXIException, IOException {
        Rule currentRule = getCurrentRule();
        EventInformation ei = currentRule.lookForEvent(EventType.END_ELEMENT);
        if (ei != null) {
            encode1stLevelEventCode(ei.getEventCode());
        } else {
            if ((ei = currentRule.lookForEvent(EventType.CHARACTERS)) != null) {
                BuiltInType bit = ((DatatypeEvent) ei.event).getDatatype().getBuiltInType();
                switch(bit) {
                    case BINARY_BASE64:
                    case BINARY_HEX:
                    case STRING:
                    case LIST:
                    case RCS_STRING:
                        if ((ei = ei.next.lookForEvent(EventType.END_ELEMENT)) != null) {
                            this.encodeCharactersForce(StringCoder.EMPTY_STRING_VALUE);
                        }
                        break;
                    default:
                        ei = null;
                }
            }
            if (ei != null) {
                encode1stLevelEventCode(ei.getEventCode());
            } else {
                int ecEEundeclared = currentRule.get2ndLevelEventCode(EventType.END_ELEMENT_UNDECLARED, fidelityOptions);
                if (ecEEundeclared == Constants.NOT_FOUND) {
                    throw new EXIException("Unexpected EE {" + getElementContext() + ", " + exiFactory.toString());
                } else {
                    if (limitGrammarLearning()) {
                        currentRule = getCurrentRule();
                        ei = currentRule.lookForEvent(EventType.END_ELEMENT);
                        assert (ei != null);
                        encode1stLevelEventCode(ei.getEventCode());
                    } else {
                        encode2ndLevelEventCode(ecEEundeclared);
                        currentRule.learnEndElement();
                    }
                }
            }
        }
        popElement();
    }

    public void encodeAttributeList(AttributeList attributes) throws EXIException, IOException {
        for (int i = 0; i < attributes.getNumberOfNamespaceDeclarations(); i++) {
            NamespaceDeclaration ns = attributes.getNamespaceDeclaration(i);
            this.encodeNamespaceDeclaration(ns.namespaceURI, ns.prefix);
        }
        if (attributes.hasXsiType()) {
            encodeAttributeXsiType(new StringValue(attributes.getXsiTypeRaw()), attributes.getXsiTypePrefix());
        }
        if (attributes.hasXsiNil()) {
            encodeAttributeXsiNil(new StringValue(attributes.getXsiNil()), attributes.getXsiNilPrefix());
        }
        for (int i = 0; i < attributes.getNumberOfAttributes(); i++) {
            encodeAttribute(attributes.getAttributeURI(i), attributes.getAttributeLocalName(i), attributes.getAttributePrefix(i), new StringValue(attributes.getAttributeValue(i)));
        }
    }

    public void encodeAttributeXsiType(Value type, String pfx) throws EXIException, IOException {
        String qnamePrefix;
        String qnameURI;
        String qnameLocalName;
        if (type instanceof QNameValue) {
            QNameValue qv = ((QNameValue) type);
            qnameURI = qv.getNamespaceUri();
            qnamePrefix = qv.getPrefix();
            qnameLocalName = qv.getLocalName();
        } else {
            String sType = type.toString();
            qnamePrefix = QNameUtilities.getPrefixPart(sType);
            qnameURI = getURI(qnamePrefix);
            if (qnameURI == null) {
                qnameURI = XMLConstants.NULL_NS_URI;
                qnameLocalName = sType;
            } else {
                qnameLocalName = QNameUtilities.getLocalPart(sType);
            }
        }
        final Rule currentRule = getCurrentRule();
        int ec2 = currentRule.get2ndLevelEventCode(EventType.ATTRIBUTE_XSI_TYPE, fidelityOptions);
        if (ec2 != Constants.NOT_FOUND) {
            assert (currentRule.get2ndLevelEvent(ec2, fidelityOptions) == EventType.ATTRIBUTE_XSI_TYPE);
            encode2ndLevelEventCode(ec2);
            if (this.preservePrefix) {
                encoderContext.encodeQNamePrefix(encoderContext.getXsiTypeContext(), pfx, channel);
            }
        } else {
            EventInformation ei = currentRule.lookForAttribute(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, Constants.XSI_TYPE);
            if (ei != null) {
                encode1stLevelEventCode(ei.getEventCode());
            } else {
                ei = currentRule.lookForEvent(EventType.ATTRIBUTE_GENERIC);
                if (ei != null) {
                    encode1stLevelEventCode(ei.getEventCode());
                } else {
                    ec2 = currentRule.get2ndLevelEventCode(EventType.ATTRIBUTE_GENERIC_UNDECLARED, fidelityOptions);
                    if (ec2 != Constants.NOT_FOUND) {
                        encode2ndLevelEventCode(ec2);
                        QNameContext qncType = encoderContext.getXsiTypeContext();
                        currentRule.learnAttribute(new Attribute(qncType));
                    } else {
                        throw new EXIException("TypeCast " + type + " not encodable!");
                    }
                }
                QNameContext qncType = encoderContext.getXsiTypeContext();
                encoderContext.encodeQName(qncType.getNamespaceUri(), qncType.getLocalName(), channel);
                if (this.preservePrefix) {
                    encoderContext.encodeQNamePrefix(qncType, pfx, channel);
                }
            }
        }
        QNameContext qncType;
        if (preserveLexicalValues) {
            if (qnamePrefix.length() > 0 && !preservePrefix) {
                throw new EXIException("[EXI] xsi:type='" + type + "' not encodable. Preserve lexicalValues requires prefixes preserved as well!");
            }
            typeEncoder.isValid(BuiltIn.DEFAULT_DATATYPE, type);
            typeEncoder.writeValue(encoderContext, encoderContext.getXsiTypeContext(), channel);
            EvolvingUriContext uc = encoderContext.getUriContext(qnameURI);
            if (uc != null) {
                qncType = uc.getQNameContext(qnameLocalName);
            } else {
                qncType = null;
            }
        } else {
            qncType = encoderContext.encodeQName(qnameURI, qnameLocalName, channel);
            if (preservePrefix) {
                encoderContext.encodeQNamePrefix(qncType, qnamePrefix, channel);
            }
        }
        if (qncType != null && qncType.getTypeGrammar() != null) {
            updateCurrentRule(qncType.getTypeGrammar());
        }
    }

    public void encodeAttributeXsiNil(Value nil, String pfx) throws EXIException, IOException {
        final Rule currentRule = getCurrentRule();
        if (currentRule.isSchemaInformed()) {
            SchemaInformedRule siCurrentRule = (SchemaInformedRule) currentRule;
            if (booleanDatatype.isValid(nil)) {
                if (!preserveLexicalValues && !booleanDatatype.getBoolean() && !this.encodingOptions.isOptionEnabled(EncodingOptions.INCLUDE_INSIGNIFICANT_XSI_NIL)) {
                    return;
                }
                int ec2 = siCurrentRule.get2ndLevelEventCode(EventType.ATTRIBUTE_XSI_NIL, fidelityOptions);
                if (ec2 != Constants.NOT_FOUND) {
                    encode2ndLevelEventCode(ec2);
                    if (this.preservePrefix) {
                        encoderContext.encodeQNamePrefix(encoderContext.getXsiNilContext(), pfx, channel);
                    }
                    if (preserveLexicalValues) {
                        typeEncoder.isValid(booleanDatatype, nil);
                        typeEncoder.writeValue(encoderContext, encoderContext.getXsiTypeContext(), channel);
                    } else {
                        booleanDatatype.writeValue(encoderContext, null, channel);
                    }
                    if (booleanDatatype.getBoolean()) {
                        updateCurrentRule(((SchemaInformedFirstStartTagRule) siCurrentRule).getTypeEmpty());
                    }
                } else {
                    EventInformation ei = currentRule.lookForEvent(EventType.ATTRIBUTE_GENERIC);
                    if (ei != null) {
                        encode1stLevelEventCode(ei.getEventCode());
                        EvolvingUriContext euc = encoderContext.encodeUri(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, channel);
                        encoderContext.encodeLocalName(Constants.XSI_NIL, euc, channel);
                        if (this.preservePrefix) {
                            encoderContext.encodeQNamePrefix(encoderContext.getXsiNilContext(), pfx, channel);
                        }
                        if (preserveLexicalValues) {
                            typeEncoder.isValid(booleanDatatype, nil);
                            typeEncoder.writeValue(encoderContext, encoderContext.getXsiNilContext(), channel);
                        } else {
                            booleanDatatype.writeValue(encoderContext, null, channel);
                        }
                        if (booleanDatatype.getBoolean()) {
                            updateCurrentRule(((SchemaInformedFirstStartTagRule) siCurrentRule).getTypeEmpty());
                        }
                    } else {
                        throw new EXIException("Attribute xsi=nil='" + nil + "' cannot be encoded!");
                    }
                }
            } else {
                encodeSchemaInvalidAttributeEventCode(currentRule.getNumberOfDeclaredAttributes());
                EvolvingUriContext euc = encoderContext.encodeUri(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, channel);
                encoderContext.encodeLocalName(Constants.XSI_NIL, euc, channel);
                if (this.preservePrefix) {
                    encoderContext.encodeQNamePrefix(encoderContext.getXsiNilContext(), pfx, channel);
                }
                Datatype datatype = BuiltIn.DEFAULT_DATATYPE;
                isTypeValid(datatype, nil);
                this.writeValue(encoderContext.getXsiTypeContext());
            }
        } else {
            encodeAttribute(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, Constants.XSI_NIL, pfx, nil);
        }
    }

    protected void encodeSchemaInvalidAttributeEventCode(int eventCode3) throws IOException {
        final Rule currentRule = getCurrentRule();
        int ec2ATdeviated = currentRule.get2ndLevelEventCode(EventType.ATTRIBUTE_INVALID_VALUE, fidelityOptions);
        encode2ndLevelEventCode(ec2ATdeviated);
        channel.encodeNBitUnsignedInteger(eventCode3, MethodsBag.getCodingLength(currentRule.getNumberOfDeclaredAttributes() + 1));
    }

    public void encodeAttribute(QName at, Value value) throws EXIException, IOException {
        encodeAttribute(at.getNamespaceURI(), at.getLocalPart(), at.getPrefix(), value);
    }

    public void encodeAttribute(final String uri, final String localName, String prefix, Value value) throws EXIException, IOException {
        EventInformation ei;
        QNameContext qnc;
        Rule next;
        Rule currentRule = getCurrentRule();
        if ((ei = currentRule.lookForAttribute(uri, localName)) != null) {
            Attribute at = (Attribute) (ei.event);
            qnc = at.getQNameContext();
            if (isTypeValid(at.getDatatype(), value)) {
                encode1stLevelEventCode(ei.getEventCode());
            } else {
                int eventCode3 = ei.getEventCode() - currentRule.getLeastAttributeEventCode();
                encodeSchemaInvalidAttributeEventCode(eventCode3);
                isTypeValid(BuiltIn.DEFAULT_DATATYPE, value);
            }
            next = ei.next;
        } else {
            ei = currentRule.lookForAttributeNS(uri);
            if (ei == null) {
                ei = currentRule.lookForEvent(EventType.ATTRIBUTE_GENERIC);
                if (ei == null) {
                }
            }
            Attribute globalAT;
            if (currentRule.isSchemaInformed() && (globalAT = getGlobalAttribute(uri, localName)) != null) {
                assert (ei != null);
                if (isTypeValid(globalAT.getDatatype(), value)) {
                    if (ei == null) {
                        this.encodeAttributeEventCodeUndeclared(currentRule, localName);
                    } else {
                        encode1stLevelEventCode(ei.getEventCode());
                    }
                } else {
                    encodeSchemaInvalidAttributeEventCode(currentRule.getNumberOfDeclaredAttributes());
                    isTypeValid(BuiltIn.DEFAULT_DATATYPE, value);
                }
                if (ei == null || ei.event.isEventType(EventType.ATTRIBUTE_GENERIC)) {
                    qnc = this.encoderContext.encodeQName(uri, localName, channel);
                    next = ei == null ? currentRule : ei.next;
                } else {
                    AttributeNS atNS = (AttributeNS) ei.event;
                    EvolvingUriContext uc = encoderContext.getUriContext(atNS.getNamespaceUriID());
                    qnc = this.encoderContext.encodeLocalName(localName, uc, channel);
                    next = ei.next;
                }
            } else {
                isTypeValid(BuiltIn.DEFAULT_DATATYPE, value);
                if (ei == null) {
                    if (this.limitGrammarLearning()) {
                        currentRule = this.getCurrentRule();
                        ei = currentRule.lookForEvent(EventType.ATTRIBUTE_GENERIC);
                        assert (ei != null);
                        encode1stLevelEventCode(ei.getEventCode());
                        isTypeValid(BuiltIn.DEFAULT_DATATYPE, value);
                        qnc = this.encoderContext.encodeQName(uri, localName, channel);
                        next = ei.next;
                    } else {
                        qnc = encodeUndeclaredAT(currentRule, uri, localName);
                        next = currentRule;
                    }
                } else {
                    qnc = encodeDeclaredAT(ei, uri, localName);
                    next = ei.next;
                }
            }
        }
        assert (qnc != null);
        if (preservePrefix) {
            this.encoderContext.encodeQNamePrefix(qnc, prefix, channel);
        }
        this.writeValue(qnc);
        assert (next != null);
        updateCurrentRule(next);
    }

    private void encodeAttributeEventCodeUndeclared(Rule currentRule, String localName) throws IOException, EXIException {
        int ecATundeclared = currentRule.get2ndLevelEventCode(EventType.ATTRIBUTE_GENERIC_UNDECLARED, fidelityOptions);
        if (ecATundeclared == Constants.NOT_FOUND) {
            assert (fidelityOptions.isStrict());
            throw new EXIException("Attribute '" + localName + "' cannot be encoded!");
        }
        assert (ecATundeclared != Constants.NOT_FOUND);
        encode2ndLevelEventCode(ecATundeclared);
    }

    private QNameContext encodeDeclaredAT(EventInformation ei, String uri, String localName) throws IOException {
        encode1stLevelEventCode(ei.getEventCode());
        QNameContext qnc;
        if (ei.event.isEventType(EventType.ATTRIBUTE_NS)) {
            AttributeNS atNS = (AttributeNS) ei.event;
            EvolvingUriContext uc = encoderContext.getUriContext(atNS.getNamespaceUriID());
            qnc = this.encoderContext.encodeLocalName(localName, uc, channel);
        } else {
            qnc = this.encoderContext.encodeQName(uri, localName, channel);
        }
        return qnc;
    }

    private QNameContext encodeUndeclaredAT(Rule currentRule, String uri, String localName) throws EXIException, IOException {
        encodeAttributeEventCodeUndeclared(currentRule, localName);
        QNameContext qnc = this.encoderContext.encodeQName(uri, localName, channel);
        currentRule.learnAttribute(new Attribute(qnc));
        return qnc;
    }

    protected Attribute getGlobalAttribute(String uri, String localName) {
        UriContext uc = this.encoderContext.getUriContext(uri);
        if (uc != null) {
            return getGlobalAttribute(uc, localName);
        }
        return null;
    }

    protected Attribute getGlobalAttribute(UriContext uc, String localName) {
        assert (uc != null);
        QNameContext qnc = uc.getQNameContext(localName);
        if (qnc != null) {
            return qnc.getGlobalAttribute();
        }
        return null;
    }

    public void encodeCharacters(Value chars) throws EXIException, IOException {
        if (!preserveLexicalValues) {
            String tchars = chars.toString().trim();
            if (tchars.length() == 0) {
                return;
            }
        }
        encodeCharactersForce(chars);
    }

    public void encodeCharactersForce(Value chars) throws EXIException, IOException {
        Rule currentRule = getCurrentRule();
        EventInformation ei = currentRule.lookForEvent(EventType.CHARACTERS);
        if (ei != null && isTypeValid(((DatatypeEvent) ei.event).getDatatype(), chars)) {
            encode1stLevelEventCode(ei.getEventCode());
            writeValue(getElementContext().qnameContext);
            updateCurrentRule(ei.next);
        } else {
            ei = currentRule.lookForEvent(EventType.CHARACTERS_GENERIC);
            if (ei != null) {
                encode1stLevelEventCode(ei.getEventCode());
                isTypeValid(BuiltIn.DEFAULT_DATATYPE, chars);
                writeValue(getElementContext().qnameContext);
                updateCurrentRule(ei.next);
            } else {
                int ecCHundeclared = currentRule.get2ndLevelEventCode(EventType.CHARACTERS_GENERIC_UNDECLARED, fidelityOptions);
                if (ecCHundeclared == Constants.NOT_FOUND) {
                    if (exiFactory.isFragment()) {
                        throwWarning("Skip CH: '" + chars + "'");
                    } else if (fidelityOptions.isStrict() && chars.toString().trim().length() == 0) {
                        throwWarning("Skip CH: '" + chars + "'");
                    } else {
                        throw new EXIException("Characters '" + chars + "' cannot be encoded!");
                    }
                } else {
                    Rule updContextRule;
                    if (limitGrammarLearning()) {
                        currentRule = getCurrentRule();
                        ei = currentRule.lookForEvent(EventType.CHARACTERS_GENERIC);
                        assert (ei != null);
                        encode1stLevelEventCode(ei.getEventCode());
                        updContextRule = ei.next;
                    } else {
                        encode2ndLevelEventCode(ecCHundeclared);
                        currentRule.learnCharacters();
                        updContextRule = currentRule.getElementContentRule();
                    }
                    isTypeValid(BuiltIn.DEFAULT_DATATYPE, chars);
                    writeValue(getElementContext().qnameContext);
                    updateCurrentRule(updContextRule);
                }
            }
        }
    }

    public void encodeDocType(String name, String publicID, String systemID, String text) throws EXIException, IOException {
        if (fidelityOptions.isFidelityEnabled(FidelityOptions.FEATURE_DTD)) {
            int ec2 = getCurrentRule().get2ndLevelEventCode(EventType.DOC_TYPE, fidelityOptions);
            encode2ndLevelEventCode(ec2);
            writeString(name);
            writeString(publicID);
            writeString(systemID);
            writeString(text);
        }
    }

    public void encodeEntityReference(String name) throws EXIException, IOException {
        if (fidelityOptions.isFidelityEnabled(FidelityOptions.FEATURE_DTD)) {
            final Rule currentRule = getCurrentRule();
            int ec2 = currentRule.get2ndLevelEventCode(EventType.ENTITY_REFERENCE, fidelityOptions);
            encode2ndLevelEventCode(ec2);
            writeString(name);
            updateCurrentRule(currentRule.getElementContentRule());
        }
    }

    public void encodeComment(char[] ch, int start, int length) throws EXIException, IOException {
        if (fidelityOptions.isFidelityEnabled(FidelityOptions.FEATURE_COMMENT)) {
            final Rule currentRule = getCurrentRule();
            int ec3 = currentRule.get3rdLevelEventCode(EventType.COMMENT, fidelityOptions);
            encode3rdLevelEventCode(ec3);
            writeString(new String(ch, start, length));
            updateCurrentRule(currentRule.getElementContentRule());
        }
    }

    public void encodeProcessingInstruction(String target, String data) throws EXIException, IOException {
        if (fidelityOptions.isFidelityEnabled(FidelityOptions.FEATURE_PI)) {
            final Rule currentRule = getCurrentRule();
            int ec3 = currentRule.get3rdLevelEventCode(EventType.PROCESSING_INSTRUCTION, fidelityOptions);
            encode3rdLevelEventCode(ec3);
            writeString(target);
            writeString(data);
            updateCurrentRule(currentRule.getElementContentRule());
        }
    }
}
