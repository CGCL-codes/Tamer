package eu.more.core.internal.msoa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.zip.Deflater;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.soda.dpws.service.MessagePartInfo;
import org.soda.dpws.util.parser.xpp.XmlPullParserFactory;
import org.soda.dpws.wsdl.OperationInfo;
import org.soda.dpws.wsdl.PortTypeInfo;
import org.soda.dpws.wsdl.WSDLInfo;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import com.sun.xml.bind.util.ListImpl;

/**
 * �SOA Information Factory Containes main functions for msoa interoperability
 * including build a msoa transmission schema, encoding and decoding messages
 * 
 * @author georg
 * 
 */
public class MSOAinfo {

    private static String stringseperator = "|";

    private static byte stringseperatorbyte = "|".getBytes()[0];

    private logger log = new logger();

    /**
	 * Indicator using �soa compression
	 */
    public boolean usemsoa = false;

    /**
	 * WSDLInfoFactory is being created by the java generator and contains the
	 * service to operation mapping
	 */
    public WSDLInfo wsdlinfo;

    /**
	 * Indicates the Name of the WSDL File
	 */
    public String WSDL;

    /**
	 * Types Tree necessary for building �soa encoding schema
	 */
    public TypesTree tt = null;

    /**
	 * Hashmap containing current proxy service mapping
	 */
    public Map<String, MappedMessage> messagemap = new HashMap<String, MappedMessage>();

    /**
	 * Variable to save current bundle. Used by the findMOREClass for loading a
	 * class from bundle of the current service
	 */
    private Bundle cbundle;

    /**
	 * Constructor not needed!
	 */
    public MSOAinfo() {
    }

    /**
	 * Building MSOA Transmission Schema by parsing corresponding WSDL File
	 * contained in the BundleContext. Calls buildencodingschema.
	 * 
	 * @param context
	 *            Context containing current WSDL File
	 * @return True if schema was successfully builded. Else false is returned
	 * @throws IOException
	 * @throws XMLStreamException
	 * @throws URISyntaxException
	 */
    @SuppressWarnings("unchecked")
    public boolean buildschema(BundleContext context, InputStream wsdlin) throws IOException, XMLStreamException, URISyntaxException {
        cbundle = context.getBundle();
        XmlPullParserFactory factory = null;
        XmlPullParser xpp = null;
        ArrayList<String> msglist = new ArrayList<String>();
        int msgcount = 0;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(wsdlin));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                log.msoadebug("Found Tag: " + xpp.getName());
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    log.msoadebug("Start parsing ...");
                } else if (eventType == XmlPullParser.END_DOCUMENT) {
                    log.msoadebug("Finished Parsing! ... Read Count of Messages " + msglist.size());
                    log.msoadebug("Line Number " + xpp.getLineNumber());
                } else if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("message")) {
                        log.msoadebug("Found Message " + xpp.getAttributeValue(0));
                        MappedMessage msg = new MappedMessage();
                        msg.name = xpp.getAttributeValue(0);
                        msg.number = msgcount;
                        msgcount++;
                        log.msoadebug("Mapped Message " + msg.name + " to Number " + msg.number);
                        messagemap.put(msg.name, msg);
                    } else if (xpp.getName().equals("types")) {
                        log.msoadebug("Started Parsing wsdl:Types ...");
                        eventType = xpp.next();
                        while (eventType != XmlPullParser.START_TAG) {
                            eventType = xpp.next();
                            log.msoadebug("Found Tag: " + xpp.getName());
                            log.msoadebug("Element has count of Elements " + xpp.getAttributeCount());
                            log.msoadebug("Element is " + xpp.getAttributeName(0) + " " + xpp.getAttributeValue(0));
                            log.msoadebug("Element is " + xpp.getAttributeName(1) + " " + xpp.getAttributeValue(1));
                        }
                        tt = new TypesTree(xpp);
                        log.msoadebug("Finished Parsing wsdl:Types!");
                    }
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        log.msoadebug("Found Number of Messages " + messagemap.size());
        MappedMessage inmmsg = null;
        MappedMessage outmmsg = null;
        Iterator it = wsdlinfo.getPortTypeInfos().iterator();
        while (it.hasNext()) {
            PortTypeInfo pti = (PortTypeInfo) it.next();
            Iterator nit = pti.getOperations().iterator();
            while (nit.hasNext()) {
                OperationInfo opi = (OperationInfo) nit.next();
                log.msoadebug("\nLooking for Operation: " + opi.getName());
                if (opi.getInputMessage() != null) {
                    if (opi.getInputMessage().getMessageParts().size() == 0) {
                        log.msoadebug("No messageparts defined for input message");
                    }
                    inmmsg = messagemap.get(opi.getInputMessage().getName().getLocalPart());
                    if (inmmsg.oi == null) inmmsg.oi = opi;
                    Iterator init = opi.getInputMessage().getMessageParts().iterator();
                    while (init.hasNext()) {
                        MessagePartInfo mpi = (MessagePartInfo) init.next();
                        if (!(inmmsg.messagePartList.contains(mpi.getSchemaType().getSchemaType()))) inmmsg.messagePartList.add(mpi.getSchemaType().getSchemaType());
                        log.msoadebug("In Message " + inmmsg.name + " uses xsd:Element " + mpi.getSchemaType().getSchemaType().getLocalPart());
                    }
                } else {
                    log.msoadebug(opi.getName() + " has no input message!");
                }
                if (opi.getOutputMessage() != null) {
                    if (opi.getOutputMessage().getMessageParts().size() == 0) log.msoadebug("No messageparts defined for output message");
                    outmmsg = messagemap.get(opi.getOutputMessage().getName().getLocalPart());
                    if (outmmsg.oi == null) outmmsg.oi = opi;
                    Iterator outit = opi.getOutputMessage().getMessageParts().iterator();
                    while (outit.hasNext()) {
                        MessagePartInfo mpi = (MessagePartInfo) outit.next();
                        if (!(outmmsg.messagePartList.contains(mpi.getSchemaType().getSchemaType()))) outmmsg.messagePartList.add(mpi.getSchemaType().getSchemaType());
                        log.msoadebug("Out Message " + outmmsg.name + " uses xsd:Element " + mpi.getSchemaType().getSchemaType().getLocalPart());
                    }
                } else {
                    log.msoadebug(opi.getName() + " has no output message!");
                }
            }
        }
        buildencodingschema();
        return true;
    }

    /**
	 * Hashmap for saving intermediate complex Types before Encoding Element
	 * table was successfully build
	 */
    private HashMap<String, TypesTree> complextypes = new HashMap<String, TypesTree>();

    /**
	 * Hashmap for saving intermediate simple Types before Encoding Element
	 * table was successfully build
	 */
    private HashMap<String, TypesTree> simpletypes = new HashMap<String, TypesTree>();

    /**
	 * Hashmap containing current EncodingElements
	 */
    public HashMap<String, EncodingElement> encodingelements = new HashMap<String, EncodingElement>();

    /**
	 * Build Encoding Schema. Needs to run after buildschema run successfully.
	 */
    public void buildencodingschema() {
        if (tt == null) {
            log.msoadebug("Error finding TypesTree!");
            return;
        }
        log.msoadebug("Building Encoding Schemata");
        LinkedList<TypesTree> elements = new LinkedList<TypesTree>();
        Iterator it = tt.children.iterator();
        while (it.hasNext()) {
            TypesTree parsett = (TypesTree) it.next();
            if (parsett.tagtype.equals("element")) {
                elements.addLast(parsett);
            } else if (parsett.tagtype.equalsIgnoreCase("complexType")) {
                complextypes.put(parsett.getElementValue("name"), parsett);
            } else if (parsett.tagtype.equalsIgnoreCase("simpleType")) {
                simpletypes.put(parsett.getElementValue("name"), parsett);
            }
        }
        log.msoadebug("Hashmaps build!");
        it = elements.iterator();
        while (it.hasNext()) {
            TypesTree parse = (TypesTree) it.next();
            EncodingElement ee = buildEncodingElement(parse);
            encodingelements.put(ee.name, ee);
        }
    }

    /**
	 * Printint Encoding Schema
	 * 
	 * @param h
	 *            Hashmap with EncodingElements as used in MSOAInfo
	 */
    public void printEncodingSchema(HashMap<String, EncodingElement> h) {
        Iterator it = h.keySet().iterator();
        while (it.hasNext()) {
            showEncodingElements(0, encodingelements.get(it.next()));
        }
    }

    /**
	 * Debug Class for printing EncodingElements in Element ee. Function uses
	 * recursiv calls for printing all Encoding Elements beyond given Encoding
	 * Element.
	 * 
	 * @param depth
	 *            Usage only for Recursiv Calls. Should be called with Depth 0
	 * @param ee
	 *            Root Encoding Element to be printed
	 */
    public void showEncodingElements(int depth, EncodingElement ee) {
        String tabs = "";
        for (int i = 0; i < depth; i++) tabs = tabs + "\t";
        System.out.println(tabs + "Encoding Element {" + ee.name + "} is of type {" + ee.type + "} at depth {" + depth + "}");
        System.out.println(tabs + "Occurs - max " + ee.maxoccurs + " min " + ee.minoccurs);
        System.out.println(tabs + "is leaf element:  " + ee.isleafelement);
        System.out.println(tabs + "is child simple:  " + ee.ischildsimple);
        System.out.println(tabs + "is child complex: " + ee.ischildcomplex);
        System.out.println(tabs + "is inner complex: " + ee.isinnercomplex);
        System.out.println(tabs + "Childs:           " + ee.lecount());
        System.out.println(tabs + "sequenced:        " + ee.sequenced);
        System.out.println(tabs + "class:            " + ee.klasse);
        System.out.println("\n");
        Iterator it = ee.linkedelements.iterator();
        while (it.hasNext()) {
            EncodingElement nee = (EncodingElement) it.next();
            showEncodingElements(depth + 1, nee);
        }
    }

    public EncodingElement buildEncodingElement(TypesTree tt) {
        log.msoadebug("Build Element " + tt.getElementValue("name"));
        EncodingElement ee = new EncodingElement();
        if (tt.hasoneChild()) {
            ee.name = tt.getElementValue("name");
            ee.klasse = findMOREClass(makeFirstBig(ee.name) + "Impl.class");
            TypesTree child = tt.children.getFirst();
            if (child.tagtype.equalsIgnoreCase("complextype")) {
                if (child.hasoneChild()) {
                    child = child.children.getFirst();
                    if (child.tagtype.equalsIgnoreCase("sequence")) {
                        ee.sequenced = true;
                        Iterator it = child.children.iterator();
                        while (it.hasNext()) {
                            TypesTree tte = (TypesTree) it.next();
                            if (tte.tagtype.equalsIgnoreCase("element")) ee.linkedelements.addLast(buildEncodingElement(tte));
                        }
                    }
                } else {
                    log.msoadebug("ERROR: Exspected Sequence Tag was not found!");
                }
                return ee;
            } else {
                log.msoadebug("Error parsing Element!");
                return null;
            }
        } else {
            Iterator it = tt.elements.iterator();
            while (it.hasNext()) {
                TypesTreeElement tte = (TypesTreeElement) it.next();
                if (tte.elvaluename.equalsIgnoreCase("name")) {
                    ee.name = tte.elvalue;
                    ee.klasse = findMOREClass(makeFirstBig(ee.name) + "Impl.class");
                } else if (tte.elvaluename.equalsIgnoreCase("type")) {
                    if (tte.elvalue.startsWith("xsd:")) {
                        ee.type = tte.elvalue.substring(4, tte.elvalue.length());
                        ee.isleafelement = true;
                    } else if (tte.elvalue.startsWith("xs:")) {
                        ee.type = tte.elvalue.substring(3, tte.elvalue.length());
                        ee.isleafelement = true;
                    } else if (tte.elvalue.startsWith("tns:")) {
                        if (complextypes.containsKey(tte.elvalue.substring(4, tte.elvalue.length()))) {
                            log.msoadebug("\nFound Complex Type " + tte.elvalue.substring(4, tte.elvalue.length()));
                            ee.linkedelements = buildComplexType(complextypes.get(tte.elvalue.substring(4, tte.elvalue.length())));
                            ee.ischildcomplex = true;
                            log.msoadebug("Finished builing Complex Type " + tte.elvalue.substring(4, tte.elvalue.length()) + "\n");
                        } else if (simpletypes.containsKey(tte.elvalue.substring(4, tte.elvalue.length()))) {
                            log.msoadebug("\nFound Simple Type " + tte.elvalue.substring(4, tte.elvalue.length()));
                            ee.linkedelements = buildSimpleType(simpletypes.get(tte.elvalue.substring(4, tte.elvalue.length())));
                            ee.ischildsimple = true;
                            log.msoadebug("Finished builing Simple Type " + tte.elvalue.substring(4, tte.elvalue.length()) + "\n");
                        }
                    }
                } else if (tte.elvaluename.equalsIgnoreCase("maxoccurs")) {
                    if ((tte.elvalue.equals("unbound")) || (tte.elvalue.equals("-1"))) ee.maxoccurs = "-1"; else ee.maxoccurs = tte.elvalue;
                } else if (tte.elvaluename.equalsIgnoreCase("minoccurs")) {
                    ee.minoccurs = tte.elvalue;
                }
            }
            return ee;
        }
    }

    /**
	 * Builds List of EncodingElements out of Complex Type Description in WSDL
	 * File.
	 * 
	 * @param tt
	 * @return
	 */
    private LinkedList<EncodingElement> buildComplexType(TypesTree tt) {
        LinkedList<EncodingElement> list = new LinkedList<EncodingElement>();
        EncodingElement ee = new EncodingElement();
        if (tt.tagtype.equalsIgnoreCase("sequence")) {
            ee.sequenced = true;
        }
        ee.name = tt.getElementValue("name");
        ee.klasse = findMOREClass(makeFirstBig(ee.name) + "Impl.class");
        if (tt.hasMaxOccurrence()) {
            String value = tt.getElementValue("maxOccurs");
            if (value != null) ee.maxoccurs = (value.equalsIgnoreCase("unbound") ? "-1" : value);
        }
        if (tt.hasMinOccurrence()) {
            String value = tt.getElementValue("minOccurs");
            if (value != null) ee.maxoccurs = (value.equalsIgnoreCase("unbound") ? "-1" : value);
        }
        Iterator it = tt.children.iterator();
        while (it.hasNext()) {
            TypesTree tt2 = (TypesTree) it.next();
            if (tt2.tagtype.equalsIgnoreCase(("sequence"))) {
                ee.sequenced = true;
                Iterator it2 = tt2.children.iterator();
                while (it2.hasNext()) {
                    TypesTree tt3 = (TypesTree) it2.next();
                    if (tt3.tagtype.equalsIgnoreCase("element")) if (tt3.children.size() == 0) {
                        ee.linkedelements.addLast(buildEncodingElement(tt3));
                    } else {
                        LinkedList<EncodingElement> llee = buildComplexType(tt3);
                        Iterator it3 = llee.iterator();
                        while (it3.hasNext()) {
                            ee.linkedelements.add((EncodingElement) it3.next());
                        }
                    }
                }
            } else if (tt2.tagtype.equalsIgnoreCase("element")) {
                ee.linkedelements.addLast(buildEncodingElement(tt2));
            } else if (tt2.tagtype.equalsIgnoreCase("complexType")) {
                log.msoadebug("Found inner Complex Type!");
                ee.isinnercomplex = true;
                ee.klasse = findMOREClass(makeFirstBig(ee.name) + "Impl.class", tt2.getElementValue("name") + "TypeImpl");
                if (tt2.children.size() != 1) {
                    log.msoadebug("Something went terribly wrong!");
                    return null;
                }
                tt2 = tt2.children.get(0);
                if (tt2.tagtype.equalsIgnoreCase("sequence")) {
                    ee.sequenced = true;
                    Iterator it4 = tt2.children.iterator();
                    while (it4.hasNext()) {
                        ee.linkedelements.addLast(buildEncodingElement((TypesTree) it4.next()));
                    }
                }
            }
        }
        list.add(ee);
        return list;
    }

    /**
	 * Build List of EncodingElements out of Simple type Description in WSDL
	 * File.
	 * 
	 * @param tt
	 * @return
	 */
    private LinkedList<EncodingElement> buildSimpleType(TypesTree tt) {
        LinkedList<EncodingElement> list = new LinkedList<EncodingElement>();
        EncodingElement ee = new EncodingElement();
        ee.name = tt.getElementValue("name");
        ee.klasse = findMOREClass(makeFirstBig(ee.name) + ".class");
        Iterator it = tt.children.iterator();
        while (it.hasNext()) {
            TypesTree tt2 = (TypesTree) it.next();
            if (tt2.tagtype.equals("restriction")) {
                ee.type = tt2.getElementValue("base").substring(4, tt2.getElementValue("base").length());
                ee.isleafelement = true;
            }
        }
        list.addLast(ee);
        return list;
    }

    public EncodingElement buildRestriction(TypesTree tt) {
        EncodingElement ee = new EncodingElement();
        ee.name = tt.getElementValue("name");
        return ee;
    }

    /**
	 * Encodes Object as part of message for msoa Transmission
	 */
    public byte[] encode(String message, Object obj) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        MappedMessage mm = messagemap.get(message);
        try {
            stream.write(TypeConversation.toByta(mm.number));
            Iterator it = mm.messagePartList.iterator();
            while (it.hasNext()) {
                QName qname = (QName) it.next();
                EncodingElement ee = encodingelements.get(qname.getLocalPart());
                stream.write(encodeElement(ee, obj));
            }
            return stream.toByteArray();
        } catch (IOException e) {
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (ClassNotFoundException e) {
        }
        return null;
    }

    private ListImpl exampleList = new com.sun.xml.bind.util.ListImpl(new ArrayList<Object>());

    @SuppressWarnings("unchecked")
    public byte[] encodeElement(EncodingElement ee, Object obj) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Iterator it = ee.linkedelements.iterator();
        while (it.hasNext()) {
            EncodingElement child = (EncodingElement) it.next();
            if (child.isleafelement) {
                Object objekt = null;
                if (ee.ischildsimple) {
                    log.msoadebug("Encoding Simple-Type-Leaf Element " + child.name);
                    if (child.normaloccurence()) {
                        objekt = ee.klasse.getDeclaredMethod("getValue").invoke(obj);
                        log.msoadebug("Object is of " + objekt.getClass().getName());
                        encodeRootElement(objekt, stream);
                    } else {
                        log.msoadebug("Should-not-occure-Exception during msoa encoding!");
                    }
                } else {
                    log.msoadebug("Encoding Complex-Type-Leaf Element " + child.name);
                    if (child.normaloccurence()) {
                        objekt = ee.klasse.getDeclaredMethod("get" + reformatName(child.name)).invoke(obj);
                        encodeRootElement(objekt, stream);
                    } else {
                        java.util.List liste = (List<?>) ee.klasse.getDeclaredMethod("get" + makeFirstBig(child.name)).invoke(obj);
                        stream.write(TypeConversation.toByta(liste.size()));
                        log.msoadebug("Found Elements in List " + liste.size());
                        Iterator bit = liste.iterator();
                        while (bit.hasNext()) {
                            Object objekts = bit.next();
                            encodeRootElement(objekts, stream);
                        }
                    }
                }
            } else {
                if (child.ischildcomplex) {
                    if (log.msoadebug) log.msoadebug("Found complex type defined Element " + child.name + " ... father is " + ee.name);
                    Object objekt = ee.klasse.getDeclaredMethod("get" + makeFirstBig(child.name)).invoke(obj);
                    if (log.msoadebug) log.msoadebug("Object is of " + objekt.getClass().getName());
                    if (objekt.getClass().getName().equals("com.sun.xml.bind.util.ListImpl")) {
                        java.util.List liste = (java.util.List) objekt;
                        stream.write(TypeConversation.toByta(liste.size()));
                        if (log.msoadebug) log.msoadebug("Found Elements in List " + liste.size());
                        Iterator bit = liste.iterator();
                        while (bit.hasNext()) {
                            Object objekts = bit.next();
                            stream.write(encodeElement(child, objekts));
                        }
                    } else {
                        stream.write(encodeElement(child, objekt));
                    }
                } else if (child.ischildsimple) {
                    if (log.msoadebug) log.msoadebug("Found simple type defined Element " + child.name + " ... father is " + ee.name);
                    Object objekt = ee.klasse.getDeclaredMethod("get" + makeFirstBig(child.name)).invoke(obj);
                    if (log.msoadebug) log.msoadebug("Object is of " + objekt.getClass().getName());
                    if (objekt.getClass().isInstance(exampleList)) {
                        java.util.List liste = (java.util.List) objekt;
                        stream.write(TypeConversation.toByta(liste.size()));
                        if (log.msoadebug) log.msoadebug("Found Elements in List " + liste.size());
                        Iterator bit = liste.iterator();
                        while (bit.hasNext()) {
                            Object objekts = bit.next();
                            encodeRootElement(objekts, stream);
                        }
                    } else {
                        encodeRootElement(objekt, stream);
                    }
                } else {
                    stream.write(encodeElement(child, obj));
                }
            }
        }
        if (log.msoadebug) log.msoadebug("Returning String Value: " + stream.toString());
        return stream.toByteArray();
    }

    /**
	 * Encodes a Root Element by given Object. Though a root element is a java
	 * generic data type.
	 * 
	 * @param obj
	 * @return Byte array as representation of encoded Object
	 * @throws IOException 
	 */
    public void encodeRootElement(Object obj, ByteArrayOutputStream s) throws IOException {
        if (obj.getClass().getName().equals("java.util.GregorianCalendar")) {
            DateFormat df = new SimpleDateFormat();
            df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.GERMANY);
            String towrite = df.format(((java.util.GregorianCalendar) obj).getTime());
            encodeRootElement(towrite, s);
            return;
        } else if (obj.getClass().getName().equals("java.lang.String")) {
            s.write(TypeConversation.toByta((String) obj));
            s.write(stringseperatorbyte);
            return;
        } else if (obj.getClass().getName().equals("java.lang.Double")) {
            s.write(TypeConversation.toByta((Double) obj));
            return;
        } else if (obj.getClass().getName().equals("java.lang.Float")) {
            s.write(TypeConversation.toByta((Float) obj));
            return;
        } else if (obj.getClass().getName().equals("java.lang.Long")) {
            s.write(TypeConversation.toByta((Long) obj));
            return;
        } else if (obj.getClass().getName().equals("java.lang.Integer")) {
            s.write(TypeConversation.toByta((Integer) obj));
            return;
        } else if (obj.getClass().getName().equals("java.lang.Short")) {
            s.write(TypeConversation.toByta((Short) obj));
            return;
        } else if (obj.getClass().getName().equals("java.lang.Byte")) {
            s.write(TypeConversation.toByta((Byte) obj));
            return;
        } else if (obj.getClass().getName().equals("java.lang.Character")) {
            s.write(TypeConversation.toByta((Character) obj));
            return;
        } else if (obj.getClass().getName().equals("java.lang.Boolean")) {
            s.write(TypeConversation.toByta((Boolean) obj));
            return;
        }
    }

    /**
	 * Decodes a given byte-Stream as part of a msoa message to an array of
	 * objects.
	 */
    public Object[] decode(byte[] input) {
        if (log.msoadebug) log.msoadebug("Decoding mSOA Input Stream! with Queue");
        if (input == null) {
            if (log.msoadebug) log.msoadebug("Error ... recieved empty input stream!");
            return null;
        }
        Queue<Byte> q = new LinkedList<Byte>();
        for (int i = 0; i < input.length; i++) q.add(input[i]);
        int msgnumber = TypeConversation.toInt(pollmultiple(q, 4));
        Iterator it = messagemap.values().iterator();
        MappedMessage choosen = null;
        while (it.hasNext()) {
            MappedMessage itee = (MappedMessage) it.next();
            if (itee.number == msgnumber) {
                choosen = itee;
                break;
            }
        }
        if (choosen == null) {
            if (log.msoadebug) log.msoadebug("Error ... Finding mappedMessage!");
            return null;
        }
        Iterator nit = choosen.messagePartList.iterator();
        Object[] r = new Object[choosen.messagePartList.size()];
        int counter = 0;
        while (nit.hasNext()) {
            QName qname = (QName) nit.next();
            EncodingElement ee = encodingelements.get(qname.getLocalPart());
            try {
                r[counter] = decodeElement(ee, q);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            counter++;
        }
        if (log.msoadebug) log.msoadebug("Decoding mSOA Input Stream completed");
        return r;
    }

    private Object decodeElement(EncodingElement ee, Queue<Byte> q) {
        try {
            return decodeElement(ee, q, null);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Object decodeElement(EncodingElement ee, Queue<Byte> q, Object dclass) throws Exception {
        if (log.msoadebug) log.msoadebug("Decoding EE " + ee.name);
        Object fobj = null;
        Class<?> fklasse = null;
        if (dclass == null) {
            if (log.msoadebug) log.msoadebug("Load Class " + makeFirstBig(ee.name) + "Impl.class");
            fklasse = ee.klasse;
            if (fklasse == null) {
                throw new Exception("Class " + makeFirstBig(ee.name) + "Impl.class not found");
            }
            fobj = fklasse.newInstance();
        } else {
            if (log.msoadebug) log.msoadebug("Found derived Class Object ... using derived Object of " + dclass.getClass().getName());
            fobj = dclass;
            fklasse = ee.klasse;
        }
        Iterator it = ee.linkedelements.iterator();
        while (it.hasNext()) {
            EncodingElement child = (EncodingElement) it.next();
            if (child.isleafelement) {
                if (ee.ischildsimple) {
                    if (log.msoadebug) log.msoadebug("Decoding Simple-Type-Leaf Element " + child.name);
                    Method cchoosen = findMOREMethod("setValue", ee.klasse);
                    if (log.msoadebug) log.msoadebug("Method should return void ... " + cchoosen.getReturnType());
                    Object obj = null;
                    for (int i = 0; i < cchoosen.getParameterTypes().length; i++) {
                        obj = decodeRootElement(q, cchoosen.getParameterTypes()[i]);
                    }
                    findMOREMethod("setValue", fklasse).invoke(fobj, obj);
                } else {
                    if (log.msoadebug) log.msoadebug("Decoding Complex-Type-Leaf Element " + child.name);
                    if (child.normaloccurence()) {
                        Method cchoosen = findMOREMethod("set" + makeFirstBig(child.name), ee.klasse);
                        Object obj = null;
                        for (int i = 0; i < cchoosen.getParameterTypes().length; i++) {
                            obj = decodeRootElement(q, cchoosen.getParameterTypes()[i]);
                        }
                        findMOREMethod("set" + reformatName(child.name), fklasse).invoke(fobj, obj);
                    } else {
                        Method m = findMOREMethod("get" + makeFirstBig(child.name), ee.klasse);
                        int counter = (Integer) decodeRootElement(q, java.lang.Integer.class);
                        if (log.msoadebug) log.msoadebug("There have to be " + counter + " Elements of this class!");
                        Object inob = m.invoke(fobj);
                        for (int i = 0; i < counter; i++) ((java.util.List) inob).add(decodeRootElement(q, child.type));
                    }
                }
            } else {
                if ((child.ischildcomplex) && (child.lecount() == 1)) {
                    if (log.msoadebug) log.msoadebug("Primary Child is " + child.name);
                    if (child.normaloccurence()) {
                        Method m = findMOREMethod("set" + makeFirstBig(child.name), fklasse);
                        child = child.linkedelements.getFirst();
                        if (log.msoadebug) log.msoadebug("Secondary Child is " + child.name);
                        m.invoke(fobj, decodeElement(child, q));
                    } else {
                        Method m = findMOREMethod("get" + makeFirstBig(child.name), fklasse);
                        child = child.linkedelements.getFirst();
                        if (log.msoadebug) log.msoadebug("Secondary Child is " + child.name);
                        int counter = (Integer) decodeRootElement(q, java.lang.Integer.class);
                        if (log.msoadebug) log.msoadebug("There have to be " + counter + " Elements of this class!");
                        Object inob = m.invoke(fobj);
                        for (int i = 0; i < counter; i++) ((java.util.List) inob).add(decodeElement(child, q));
                    }
                } else if (child.ischildsimple) {
                    if (log.msoadebug) log.msoadebug("Primary Child is " + child.name);
                    if (child.normaloccurence()) {
                        Method m = findMOREMethod("set" + makeFirstBig(child.name), fklasse);
                        child = child.linkedelements.getFirst();
                        if (log.msoadebug) log.msoadebug("Secondary Child is " + child.name);
                        m.invoke(fobj, decodeElement(child, q));
                    } else {
                        Method m = findMOREMethod("get" + makeFirstBig(child.name), fklasse);
                        child = child.linkedelements.getFirst();
                        if (log.msoadebug) log.msoadebug("Secondary Child is " + child.name);
                        int counter = (Integer) decodeRootElement(q, java.lang.Integer.class);
                        if (log.msoadebug) log.msoadebug("There have to be " + counter + " Elements of this class!");
                        Object inob = m.invoke(fobj);
                        for (int i = 0; i < counter; i++) ((java.util.List) inob).add(decodeRootElement(q, child.type));
                    }
                } else if ((ee.lecount() == 1) && ee.ischildcomplex && (fklasse.getSuperclass().getName().toLowerCase().contains(child.name.toLowerCase()))) {
                    if (log.msoadebug) log.msoadebug("Found derived Class ... ignoring Tree-Structur!");
                    return decodeElement(child, q, fobj);
                } else if (child.isinnercomplex) {
                    if (log.msoadebug) log.msoadebug("Found inner Complex Type!");
                    if (child.normaloccurence()) {
                        Method m = findMOREMethod("set" + makeFirstBig(child.name), fklasse);
                        Class<?> cklasse = findMOREinnerClass(ee.klasse, child.name + "TypeImpl");
                        Object cobj = cklasse.newInstance();
                        Object obj = null;
                        for (int j = 0; j < child.linkedelements.size(); j++) {
                            EncodingElement tee = child.linkedelements.get(j);
                            if (log.msoadebug) log.msoadebug("Decoding Complex-Type-Leaf Element " + tee.name);
                            Method cchoosen = findMOREMethod("set" + reformatName(tee.name), cklasse);
                            for (int x = 0; x < cchoosen.getParameterTypes().length; x++) {
                                obj = decodeRootElement(q, cchoosen.getParameterTypes()[x]);
                            }
                            cchoosen.invoke(cobj, obj);
                        }
                        m.invoke(fobj, cobj);
                    } else {
                        Method m = findMOREMethod("get" + makeFirstBig(child.name), fklasse);
                        int counter = (Integer) decodeRootElement(q, java.lang.Integer.class);
                        if (log.msoadebug) log.msoadebug("There have to be " + counter + " Elements of this class!");
                        Object inob = m.invoke(fobj);
                        for (int i = 0; i < counter; i++) {
                            Class<?> cklasse = findMOREinnerClass(ee.klasse, child.name + "TypeImpl");
                            Object cobj = cklasse.newInstance();
                            Object obj = null;
                            for (int j = 0; j < child.linkedelements.size(); j++) {
                                EncodingElement tee = child.linkedelements.get(j);
                                if (log.msoadebug) log.msoadebug("Decoding Complex-Type-Leaf Element " + tee.name);
                                Method cchoosen = findMOREMethod("set" + reformatName(tee.name), cklasse);
                                for (int x = 0; x < cchoosen.getParameterTypes().length; x++) {
                                    obj = decodeRootElement(q, cchoosen.getParameterTypes()[x]);
                                }
                                cchoosen.invoke(cobj, obj);
                            }
                            ((java.util.List) inob).add(cobj);
                        }
                    }
                }
            }
        }
        return fobj;
    }

    private static byte[] pollString(Queue<Byte> q) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte b;
        while ((b = q.poll()) != stringseperatorbyte) baos.write(b);
        return baos.toByteArray();
    }

    private byte[] pollmultiple(Queue<Byte> q, int number) {
        if (log.msoadebug) log.msoadebug("Polling Bytes " + number);
        byte[] b = new byte[number];
        for (int i = 0; i < number; i++) {
            b[i] = q.poll();
        }
        return b;
    }

    /**	
	 * Decodes Root Elements. A Root Element therfor is an java generic data
	 * type.
	 * 
	 * @param q
	 * @param klasse
	 * @return Object as an instanz of given class
	 */
    private Object decodeRootElement(Queue<Byte> q, Class<?> klasse) {
        byte[] stream = null;
        if (klasse.getName().equals("java.util.Calendar")) {
            String s = TypeConversation.toString(pollString(q));
            DateFormat df = new SimpleDateFormat();
            df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.GERMANY);
            java.util.Calendar c = Calendar.getInstance();
            try {
                c.setTime(df.parse(s));
            } catch (ParseException e) {
            }
            return c;
        } else if (klasse.getName().equals("java.lang.String")) {
            return TypeConversation.toString(pollString(q));
        } else if ((klasse.getName().equals("java.lang.Double")) || ((klasse.getName().equals("double")))) {
            return TypeConversation.toDouble(pollmultiple(q, 8));
        } else if ((klasse.getName().equals("java.lang.Float")) || ((klasse.getName().equals("float")))) {
            return TypeConversation.toFloat(pollmultiple(q, 4));
        } else if ((klasse.getName().equals("java.lang.Long")) || ((klasse.getName().equals("long")))) {
            return TypeConversation.toLong(pollmultiple(q, 8));
        } else if ((klasse.getName().equals("java.lang.Integer")) || ((klasse.getName().equals("int")))) {
            return TypeConversation.toInt(pollmultiple(q, 4));
        } else if ((klasse.getName().equals("java.lang.Short")) || ((klasse.getName().equals("short")))) {
            return TypeConversation.toShort(pollmultiple(q, 2));
        } else if ((klasse.getName().equals("java.lang.Byte")) || ((klasse.getName().equals("byte")))) {
            return TypeConversation.toByte(new byte[] { q.poll() });
        } else if ((klasse.getName().equals("java.lang.Character")) || ((klasse.getName().equals("char")))) {
            return TypeConversation.toChar(new byte[] { q.poll() });
        } else if ((klasse.getName().equals("java.lang.Boolean")) || ((klasse.getName().equals("boolean")))) {
            return TypeConversation.toBoolean(new byte[] { q.poll() });
        } else return null;
    }

    private Object decodeRootElement(Queue<Byte> q, String klasse) {
        byte[] stream = null;
        if (klasse.equals("java.util.Calendar")) {
            String s = TypeConversation.toString(pollString(q));
            DateFormat df = new SimpleDateFormat();
            df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.GERMANY);
            java.util.Calendar c = Calendar.getInstance();
            try {
                c.setTime(df.parse(s));
            } catch (ParseException e) {
            }
            return c;
        } else if (klasse.equals("java.lang.String")) {
            return TypeConversation.toString(pollString(q));
        } else if ((klasse.equals("java.lang.Double")) || ((klasse.equals("double")))) {
            return TypeConversation.toDouble(pollmultiple(q, 8));
        } else if ((klasse.equals("java.lang.Float")) || ((klasse.equals("float")))) {
            return TypeConversation.toFloat(pollmultiple(q, 4));
        } else if ((klasse.equals("java.lang.Long")) || ((klasse.equals("long")))) {
            return TypeConversation.toLong(pollmultiple(q, 8));
        } else if ((klasse.equals("java.lang.Integer")) || ((klasse.equals("int")))) {
            return TypeConversation.toInt(pollmultiple(q, 4));
        } else if ((klasse.equals("java.lang.Short")) || ((klasse.equals("short")))) {
            return TypeConversation.toShort(pollmultiple(q, 2));
        } else if ((klasse.equals("java.lang.Byte")) || ((klasse.equals("byte")))) {
            return TypeConversation.toByte(new byte[] { q.poll() });
        } else if ((klasse.equals("java.lang.Character")) || ((klasse.equals("char")))) {
            return TypeConversation.toChar(new byte[] { q.poll() });
        } else if ((klasse.equals("java.lang.Boolean")) || ((klasse.equals("boolean")))) {
            return TypeConversation.toBoolean(new byte[] { q.poll() });
        } else return null;
    }

    /**
	 * Reformating String Name to first letter in upper case and the last
	 * letters in lower case.
	 */
    public String reformatName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1, name.length()).toLowerCase();
    }

    /**
	 * Reformating String Name to first letter in upper case. Rest as given.
	 * 
	 * @param name
	 * @return formated String
	 */
    public String makeFirstBig(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
    }

    /**
	 * Method reads given byte array as a string and returns first occurrence of
	 * msoa string split character (in this case "|"). *
	 * 
	 * @param b
	 *            given byte array containing string at beginning.
	 * @return integer value of position, where the first split character
	 *         occurred
	 */
    public int readStringlength(byte[] b) {
        int i = 0;
        byte[] a = "|".getBytes();
        while (b[i] != a[0]) {
            i++;
        }
        return i;
    }

    /**
	 * Method returns sub array of byte array b[], starting with position start
	 * and length count.
	 * 
	 * @param b
	 *            Input byte array
	 * @param start
	 *            Start position of subarray
	 * @param count
	 *            length of subarray
	 * @return Subarray
	 */
    public byte[] subArray(byte[] b, int start, int count) {
        if (b == null) {
            return null;
        } else if (start < 1) {
            return null;
        } else if (start + count - 1 > b.length) {
            return null;
        } else {
            byte[] r = new byte[count];
            for (int c = 0; c < count; c++) r[c] = b[start - 1 + c];
            return r;
        }
    }

    /**
	 * Method returns first count values of given byte array
	 * 
	 * @param b
	 *            Input byte array
	 * @param count
	 *            Length of returning sub array
	 * @return Subarray
	 */
    public byte[] getfirstArray(byte[] b, int count) {
        if (count < b.length) {
            byte[] r = new byte[count];
            for (int i = 0; i < count; i++) r[i] = b[i];
            return r;
        } else if (count == b.length) {
            return b;
        } else return null;
    }

    /**
	 * Method returns last count values of given byte array
	 * 
	 * @param b
	 *            Input byte array
	 * @param count
	 *            Length of returning sub array
	 * @return Subarray
	 */
    public byte[] getlastArray(byte[] b, int count) {
        if (count < b.length) {
            byte[] r = new byte[count];
            int begin = b.length - count;
            for (int i = b.length - count; i < b.length; i++) r[i - begin] = b[i];
            return r;
        } else if (count == b.length) {
            return b;
        } else return null;
    }

    /**
	 * Method compresses given byte array using Java-Buildin Zip compression
	 * algorithm.
	 * 
	 * @param barray
	 *            Input byte array
	 * @return Compressed byte array
	 */
    public byte[] compress(byte[] barray) {
        java.util.zip.Deflater compressor = new Deflater();
        compressor.setLevel(Deflater.BEST_COMPRESSION);
        compressor.setInput(barray);
        compressor.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(barray.length);
        byte[] buf = new byte[1024];
        while (!compressor.finished()) {
            int count = compressor.deflate(buf);
            bos.write(buf, 0, count);
        }
        try {
            bos.close();
        } catch (IOException e) {
        }
        byte[] compressedData = bos.toByteArray();
        return compressedData;
    }

    /**
	 * Capsulating Method for findMOREClass-Method
	 * 
	 * @param name
	 * @return
	 */
    private Class<?> findMOREClass(String name) {
        return findMOREClass(name, null);
    }

    /**
	 * Method returns class by given name. Search is being progressed in given
	 * cbundle.
	 * 
	 * @param name
	 *            Input class name
	 * @return Detected Class
	 * @throws ClassNotFoundException
	 */
    private Class<?> findMOREClass(String name, String subclass) {
        if (log.msoadebug) log.msoadebug("Request to Load Class " + name);
        Class<?> klasse = null;
        Enumeration<URL> enumeration = cbundle.findEntries("", name, true);
        URL url = null;
        URL nurl = null;
        if (enumeration == null) {
            if (log.msoadebug) log.msoadebug("Requested Class not found ... Could not be loaded!");
            return null;
        }
        while (enumeration.hasMoreElements()) {
            nurl = enumeration.nextElement();
            if (url == null) url = nurl;
            if (log.msoadebug) log.msoadebug("Found Element URL " + nurl);
        }
        String loadclass = null;
        if (url.toString().contains("eu/")) loadclass = url.toString().substring(url.toString().indexOf("eu/"), url.toString().length() - 6).replaceAll("/", "."); else if (url.toString().contains("bin/")) loadclass = url.toString().substring(url.toString().indexOf("bin/") + 4, url.toString().length() - 6).replaceAll("/", "."); else if (url.toString().contains("bundleentry")) loadclass = url.getPath().substring(1, url.getPath().length() - 6).replaceAll("/", ".");
        if (log.msoadebug) log.msoadebug("Loading Class " + loadclass);
        try {
            klasse = cbundle.loadClass(loadclass);
        } catch (ClassNotFoundException e) {
            if (log.msoadebug) log.msoadebug("Class " + name + " could not be loaded!");
            return null;
        }
        if (!(subclass == null)) {
            Class[] decklasse = klasse.getDeclaredClasses();
            for (int i = 0; i < decklasse.length; i++) {
                String decName = decklasse[i].getName().substring(decklasse[i].getName().indexOf("$") + 1, decklasse[i].getName().length());
                if (decName.equalsIgnoreCase(subclass)) return decklasse[i];
            }
        }
        return klasse;
    }

    /**
	 * Returning inner Class for inner Complex/Simple Types
	 * 
	 * @param name
	 *            Input class name
	 * @return Detected Class
	 * @throws ClassNotFoundException
	 */
    private Class<?> findMOREinnerClass(Class<?> klasse, String subclass) {
        if (!(subclass == null)) {
            Class[] decklasse = klasse.getDeclaredClasses();
            for (int i = 0; i < decklasse.length; i++) {
                String decName = decklasse[i].getName().substring(decklasse[i].getName().indexOf("$") + 1, decklasse[i].getName().length());
                if (decName.equalsIgnoreCase(subclass)) return decklasse[i];
            }
        }
        return null;
    }

    /**
	 * Method searches the given class for the given method and returns method,
	 * if found
	 * 
	 * @param name
	 *            Input method name
	 * @param klasse
	 *            Input class to browse for method
	 * @return Detected Method
	 */
    private Method findMOREMethod(String name, Class<?> klasse) {
        Method choosen = null;
        Method[] methoden = klasse.getDeclaredMethods();
        for (int i = 0; i < methoden.length; i++) {
            if ((methoden[i].getName().contains(name)) && !(methoden[i].getName().startsWith("_"))) {
                return methoden[i];
            }
        }
        return null;
    }

    private class logger {

        public final boolean msoadebug = false;

        public void msoadebug(String string) {
        }
    }
}
