package com.googlecode.protobuf.format;

import static com.googlecode.protobuf.format.util.TextUtils.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.Message;
import com.google.protobuf.UnknownFieldSet;

/**
 * <p>
 * (c) 2011 Neustar, Inc. All Rights Reserved.
 *
 * @author jeffrey.damick@neustar.biz Jeffrey Damick
 *         Based on the original code by:
 * @author eliran.bivas@gmail.com Eliran Bivas
 * @author aantonov@orbitz.com Alex Antonov
 *         <p/>
 * @author wenboz@google.com Wenbo Zhu
 * @author kenton@google.com Kenton Varda
 */
public class XmlJavaxFormat extends ProtobufFormatter {

    private static final String MESSAGE_ELEMENT = "message";

    private static final String EXTENSION_ELEMENT = "extension";

    private static final String EXTENSION_TYPE = "type";

    private static final String UNKNOWN_FIELD_ELEMENT = "unknown-field";

    private static final String UNKNOWN_FIELD_INDEX = "index";

    private XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();

    private XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();

    /**
     * Outputs a Smile representation of the Protocol Message supplied into the parameter output.
     * (This representation is the new version of the classic "ProtocolPrinter" output from the
     * original Protocol Buffer system)
     */
    public void print(final Message message, OutputStream output, Charset cs) throws IOException {
        try {
            XMLStreamWriter generator = createGenerator(output);
            print(message, generator);
            generator.writeEndDocument();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    /**
     * Outputs a Smile representation of the Protocol Message supplied into the parameter output.
     * (This representation is the new version of the classic "ProtocolPrinter" output from the
     * original Protocol Buffer system)
     */
    public void print(Message message, XMLStreamWriter generator) throws IOException {
        try {
            final String messageName = message.getDescriptorForType().getName();
            generator.writeStartElement(messageName);
            printMessage(message, generator);
            generator.writeEndElement();
            generator.flush();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    /**
     * Outputs a Smile representation of {@code fields} to {@code output}.
     */
    public void print(final UnknownFieldSet fields, OutputStream output, Charset cs) throws IOException {
        try {
            XMLStreamWriter generator = createGenerator(output);
            generator.writeStartElement(MESSAGE_ELEMENT);
            printUnknownFields(fields, generator);
            generator.writeEndElement();
            generator.close();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    /**
     * Parse a text-format message from {@code input} and merge the contents into {@code builder}.
     * Extensions will be recognized if they are registered in {@code extensionRegistry}.
     * @throws IOException 
     */
    public void merge(InputStream input, Charset cs, ExtensionRegistry extensionRegistry, Message.Builder builder) throws IOException {
        XMLEventReader parser;
        try {
            parser = xmlInputFactory.createXMLEventReader(input);
            merge(parser, extensionRegistry, builder);
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    /**
     * Parse a text-format message from {@code input} and merge the contents into {@code builder}.
     * Extensions will be recognized if they are registered in {@code extensionRegistry}.
     * @throws IOException 
     */
    public void merge(XMLEventReader parser, ExtensionRegistry extensionRegistry, Message.Builder builder) throws IOException {
        try {
            XMLEvent messageElement = parser.nextTag();
            if (messageElement.isStartElement()) {
                final String messageName = messageElement.asStartElement().getName().getLocalPart();
                assert builder.getDescriptorForType().getName().equals(messageName);
                while (parser.hasNext() && !parser.peek().isEndDocument()) {
                    XMLEvent event = parser.nextTag();
                    if (event.isStartElement()) {
                        mergeField(parser, event, extensionRegistry, builder);
                        XMLEvent endElement = parser.nextTag();
                        assert endElement.isEndElement();
                    } else if (event.isEndElement()) {
                        break;
                    } else {
                        throw new RuntimeException("Expecting the end of the stream, but there seems to be more data!  Check the input for a valid XML format.");
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    protected XMLStreamWriter createGenerator(OutputStream output) throws XMLStreamException {
        XMLStreamWriter generator;
        generator = xmlOutputFactory.createXMLStreamWriter(output);
        generator.writeStartDocument();
        return generator;
    }

    protected void printMessage(Message message, XMLStreamWriter generator) throws XMLStreamException, IOException {
        for (Iterator<Map.Entry<FieldDescriptor, Object>> iter = message.getAllFields().entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<FieldDescriptor, Object> field = iter.next();
            printField(field.getKey(), field.getValue(), generator);
        }
        printUnknownFields(message.getUnknownFields(), generator);
    }

    public void printField(FieldDescriptor field, Object value, XMLStreamWriter generator) throws IOException {
        try {
            if (field.isRepeated()) {
                for (Iterator<?> iter = ((List<?>) value).iterator(); iter.hasNext(); ) {
                    printSingleField(field, iter.next(), generator);
                }
            } else {
                printSingleField(field, value, generator);
            }
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    private void printSingleField(FieldDescriptor field, Object value, XMLStreamWriter generator) throws XMLStreamException, IOException {
        if (field.isExtension()) {
            generator.writeStartElement(EXTENSION_ELEMENT);
            if (field.getContainingType().getOptions().getMessageSetWireFormat() && (field.getType() == FieldDescriptor.Type.MESSAGE) && (field.isOptional()) && (field.getExtensionScope() == field.getMessageType())) {
                generator.writeAttribute(EXTENSION_TYPE, field.getMessageType().getFullName());
            } else {
                generator.writeAttribute(EXTENSION_TYPE, field.getFullName());
            }
        } else {
            if (field.getType() == FieldDescriptor.Type.GROUP) {
                generator.writeStartElement(field.getMessageType().getName());
            } else {
                generator.writeStartElement(field.getName());
            }
        }
        printFieldValue(field, value, generator);
        generator.writeEndElement();
    }

    private void printFieldValue(FieldDescriptor field, Object value, XMLStreamWriter generator) throws XMLStreamException, IOException {
        switch(field.getType()) {
            case INT32:
            case INT64:
            case SINT32:
            case SINT64:
            case SFIXED32:
            case SFIXED64:
            case FLOAT:
            case DOUBLE:
            case BOOL:
            case STRING:
                generator.writeCharacters(value.toString());
                break;
            case UINT32:
            case FIXED32:
                generator.writeCharacters(unsignedToString((Integer) value));
                break;
            case UINT64:
            case FIXED64:
                generator.writeCharacters(unsignedToString((Long) value));
                break;
            case BYTES:
                {
                    generator.writeCharacters(escapeBytes((ByteString) value));
                    break;
                }
            case ENUM:
                {
                    generator.writeCharacters(((EnumValueDescriptor) value).getName());
                    break;
                }
            case MESSAGE:
            case GROUP:
                printMessage((Message) value, generator);
                break;
        }
    }

    protected void printUnknownFields(UnknownFieldSet unknownFields, XMLStreamWriter generator) throws XMLStreamException {
        for (Map.Entry<Integer, UnknownFieldSet.Field> entry : unknownFields.asMap().entrySet()) {
            UnknownFieldSet.Field field = entry.getValue();
            final String key = entry.getKey().toString();
            for (long value : field.getVarintList()) {
                printUnknownField(key, unsignedToString(value), generator);
            }
            for (int value : field.getFixed32List()) {
                printUnknownField(key, String.format((Locale) null, "0x%08x", value), generator);
            }
            for (long value : field.getFixed64List()) {
                printUnknownField(key, String.format((Locale) null, "0x%016x", value), generator);
            }
            for (ByteString value : field.getLengthDelimitedList()) {
                printUnknownField(key, escapeBytes(value), generator);
            }
            for (UnknownFieldSet value : field.getGroupList()) {
                generator.writeStartElement(UNKNOWN_FIELD_ELEMENT);
                generator.writeAttribute(UNKNOWN_FIELD_INDEX, key);
                printUnknownFields(value, generator);
                generator.writeEndElement();
            }
        }
    }

    private void printUnknownField(String fieldKey, String fieldValue, XMLStreamWriter generator) throws XMLStreamException {
        generator.writeStartElement(UNKNOWN_FIELD_ELEMENT);
        generator.writeAttribute(UNKNOWN_FIELD_INDEX, fieldKey);
        generator.writeCharacters(fieldValue);
        generator.writeEndElement();
    }

    /**
     * Parse a single field from {@code parser} and merge it into {@code builder}. If a ',' is
     * detected after the field ends, the next field will be parsed automatically
     * @throws XMLStreamException  
     */
    protected void mergeField(XMLEventReader parser, XMLEvent fieldEvent, ExtensionRegistry extensionRegistry, Message.Builder builder) throws XMLStreamException {
        FieldDescriptor field = null;
        Descriptor type = builder.getDescriptorForType();
        boolean unknown = false;
        ExtensionRegistry.ExtensionInfo extension = null;
        String fieldName = fieldEvent.asStartElement().getName().getLocalPart();
        XMLEvent event = parser.nextEvent();
        if (event != null) {
            if (fieldName.equalsIgnoreCase(EXTENSION_ELEMENT)) {
                String extensionName = fieldEvent.asStartElement().getAttributeByName(new QName(EXTENSION_TYPE)).getValue();
                extension = extensionRegistry.findExtensionByName(extensionName);
                if (extension == null) {
                    throw new RuntimeException("Extension \"" + fieldName + "\" not found in the ExtensionRegistry.");
                } else if (extension.descriptor.getContainingType() != type) {
                    throw new RuntimeException("Extension \"" + fieldName + "\" does not extend message type \"" + type.getFullName() + "\".");
                }
                field = extension.descriptor;
            } else {
                field = type.findFieldByName(fieldName);
                if (field == null) {
                    String lowerName = fieldName.toLowerCase(Locale.US);
                    field = type.findFieldByName(lowerName);
                    if ((field != null) && (field.getType() != FieldDescriptor.Type.GROUP)) {
                        field = null;
                    }
                }
                if ((field != null) && (field.getType() == FieldDescriptor.Type.GROUP) && !field.getMessageType().getName().equals(fieldName) && !field.getMessageType().getFullName().equalsIgnoreCase(fieldName)) {
                    field = null;
                }
                if (fieldName.equalsIgnoreCase(UNKNOWN_FIELD_ELEMENT)) {
                    String index = fieldEvent.asStartElement().getAttributeByName(new QName(UNKNOWN_FIELD_INDEX)).getValue();
                    if (index != null) {
                        fieldName = index;
                    }
                }
                if (field == null && isDigits(fieldName)) {
                    field = type.findFieldByNumber(Integer.parseInt(fieldName));
                    unknown = true;
                }
                if (field == null) {
                    UnknownFieldSet.Builder unknownsBuilder = UnknownFieldSet.newBuilder();
                    handleMissingField(fieldName, parser, event, extensionRegistry, unknownsBuilder);
                    builder.setUnknownFields(unknownsBuilder.build());
                }
            }
        }
        if (field != null) {
            Object result = null;
            if (event.isCharacters()) {
                result = handlePrimitive(parser, field, event.asCharacters().getData());
            } else if ((event.isStartElement() || event.isEndElement()) && field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
                result = handleObject(parser, event, extensionRegistry, builder, field, extension);
            }
            if (result != null) {
                if (field.isRepeated()) {
                    builder.addRepeatedField(field, result);
                } else {
                    builder.setField(field, result);
                }
            }
        } else if (!unknown) {
            assert event.isCharacters();
        }
    }

    private void handleMissingField(String fieldName, XMLEventReader parser, XMLEvent event, ExtensionRegistry extensionRegistry, UnknownFieldSet.Builder builder) throws XMLStreamException {
        if (event.isStartElement()) {
            int depth = 1;
            while (parser.hasNext()) {
                XMLEvent nextEvent = parser.nextEvent();
                if (nextEvent.isEndElement()) {
                    depth--;
                    if (depth <= 0 && parser.peek().isEndElement()) {
                        break;
                    }
                } else if (nextEvent.isStartElement()) {
                    depth++;
                }
            }
        } else if (event.isCharacters()) {
        }
    }

    private Object handlePrimitive(XMLEventReader parser, FieldDescriptor field, String value) {
        Object result = null;
        if (value == null) return result;
        switch(field.getType()) {
            case INT32:
            case SINT32:
            case SFIXED32:
                result = parseInt32(value);
                break;
            case INT64:
            case SINT64:
            case SFIXED64:
                result = parseInt64(value);
                break;
            case UINT32:
            case FIXED32:
                result = parseUInt32(value);
                break;
            case UINT64:
            case FIXED64:
                result = parseUInt64(value);
                break;
            case FLOAT:
                result = parseFloat(value);
                break;
            case DOUBLE:
                result = parseDouble(value);
                break;
            case BOOL:
                result = parseBoolean(value);
                break;
            case STRING:
                result = value;
                break;
            case BYTES:
                result = unescapeBytes(value);
                break;
            case ENUM:
                {
                    EnumDescriptor enumType = field.getEnumType();
                    if (isDigits(value)) {
                        int number = parseInt32(value);
                        result = enumType.findValueByNumber(number);
                        if (result == null) {
                            throw new RuntimeException("Enum type \"" + enumType.getFullName() + "\" has no value with number " + number + ".");
                        }
                    } else {
                        String id = value;
                        result = enumType.findValueByName(id);
                        if (result == null) {
                            throw new RuntimeException("Enum type \"" + enumType.getFullName() + "\" has no value named \"" + id + "\".");
                        }
                    }
                    break;
                }
            case MESSAGE:
            case GROUP:
                throw new RuntimeException("Can't get here.");
        }
        return result;
    }

    private Message.Builder createSubBuilder(Message.Builder builder, FieldDescriptor field, ExtensionRegistry.ExtensionInfo extension) {
        Message.Builder subBuilder;
        if (extension == null) {
            subBuilder = builder.newBuilderForField(field);
        } else {
            subBuilder = extension.defaultInstance.newBuilderForType();
        }
        return subBuilder;
    }

    private Object handleObject(XMLEventReader parser, XMLEvent startEvent, ExtensionRegistry extensionRegistry, Message.Builder builder, FieldDescriptor field, ExtensionRegistry.ExtensionInfo extension) throws XMLStreamException {
        Message.Builder subBuilder = createSubBuilder(builder, field, extension);
        XMLEvent event = startEvent;
        int depth = 0;
        do {
            if (event.isStartElement()) {
                depth++;
                mergeField(parser, event, extensionRegistry, subBuilder);
                XMLEvent nextEvent = parser.nextTag();
                if (nextEvent.isEndElement()) {
                    depth--;
                    if (depth <= 0 && parser.peek().isEndElement()) {
                        break;
                    }
                } else if (nextEvent.isStartElement()) {
                    depth++;
                }
            } else {
                break;
            }
        } while (parser.hasNext() && (event = parser.nextTag()) != null);
        return subBuilder.build();
    }
}
