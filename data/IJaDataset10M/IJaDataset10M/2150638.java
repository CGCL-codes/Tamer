package pl.edu.mimuw.xqtav.itype;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sf.saxon.Configuration;
import net.sf.saxon.jdom.DocumentWrapper;
import net.sf.saxon.jdom.NodeWrapper;
import net.sf.saxon.om.ArrayIterator;
import net.sf.saxon.om.EmptyIterator;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.value.AtomicValue;
import net.sf.saxon.value.SingletonNode;
import net.sf.saxon.value.Value;
import net.sf.saxon.xpath.XPathException;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.embl.ebi.escience.baclava.DataThing;
import org.embl.ebi.escience.baclava.factory.DataThingFactory;
import org.jdom.Document;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import pl.edu.mimuw.xqtav.exec.ExecException;
import pl.edu.mimuw.xqtav.exec.TLSData;
import pl.edu.mimuw.xqtav.exec.api.TavEngine;

/**
 * @author marchant
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TypeConvert {

    protected static OutputFormat of = new OutputFormat("XML", "UTF-8", true);

    protected static String serializeElementToXmlDoc(Element el) throws IOException {
        StringWriter sw = new StringWriter();
        XMLSerializer xserial = new XMLSerializer(sw, of);
        xserial.serialize(el);
        return sw.toString();
    }

    public static DataThing IType2BDM(IType itv) {
        if (itv == null) {
            return null;
        }
        return itv.toBDM();
    }

    public static IType BDM2IType(Object dt) {
        if (dt == null) {
            return new SingleValue(null);
        }
        if (dt instanceof DataThing) {
            return makeIType(((DataThing) dt).getDataObject());
        } else {
            if (dt instanceof EmptyIterator) {
                return new SingleValue(null);
            }
            throw new ITypeException("Bad type - expected DataThing but found: " + dt.getClass().getName());
        }
    }

    protected static IType makeIType(Object obj) {
        if (obj instanceof List) {
            ArrayList al = new ArrayList();
            for (Iterator it = ((List) obj).iterator(); it.hasNext(); ) {
                al.add(makeIType(it.next()));
            }
            return new ValueList(al);
        }
        if (obj instanceof Set) {
            HashSet hs = new HashSet();
            for (Iterator it = ((Set) obj).iterator(); it.hasNext(); ) {
                hs.add(makeIType(it.next()));
            }
            return new ValueSet(hs);
        }
        return new SingleValue(obj);
    }

    public static IType Saxon2IType(Object obj) {
        try {
            if (obj == null) {
                return new SingleValue(null);
            }
            if (obj instanceof SequenceIterator || obj instanceof List) {
                Item item;
                ArrayList al = new ArrayList();
                if (obj instanceof SequenceIterator) {
                    SequenceIterator si = (SequenceIterator) obj;
                    while ((item = si.next()) != null) {
                        al.add(Saxon2IType(item));
                    }
                } else {
                    Iterator it = ((List) obj).iterator();
                    while (it.hasNext()) {
                        al.add(Saxon2IType(it.next()));
                    }
                }
                if (al.size() == 1) {
                    return (IType) al.get(0);
                }
                return new ValueList(al);
            }
            if (obj instanceof Element) {
                Element elem = (Element) obj;
                if (elem.getTagName().equals("itype:p")) {
                    ArrayList al = new ArrayList();
                    NodeList elems = elem.getChildNodes();
                    if (elems != null) {
                        for (int i = 0; i < elems.getLength(); i++) {
                            al.add(Saxon2IType(elems.item(i)));
                        }
                    }
                    return new ValueList(al);
                }
                if (elem.getTagName().equals("itype:i")) {
                    NodeList elems = elem.getChildNodes();
                    if (elems.getLength() == 0) {
                        return new SingleValue("");
                    }
                    if (elems.getLength() == 1) {
                        return new SingleValue(decodeSingleItem(elem, elems.item(0)));
                    }
                    throw new ExecException("<itype:i> has more than one child (" + elems.getLength() + "). This is not supported yet.");
                }
                throw new ExecException("Unknown element: " + elem);
            }
        } catch (Exception e) {
            throw new ITypeException("Caught exception while converting: " + e);
        } finally {
        }
        System.err.println("*** WARNING - fallback wrap to SingleValue ***");
        return new SingleValue(getDataForSingleItem(obj));
    }

    protected static Object decodeSingleItem(Element itype_i, Object obj) {
        if (itype_i == null) return getDataForSingleItem(obj);
        String smode = itype_i.getAttribute("smode");
        if (smode == null || smode.equals("") || smode.equals("none")) {
            return getDataForSingleItem(obj);
        }
        if (smode.equals("binhex") || smode.equals("dtxml-binhex")) {
            if (!(obj instanceof Text)) {
                throw new ITypeException("Bad data in binhexed element: " + obj.getClass().getName());
            }
            String data = ((Text) obj).getData().trim();
            byte[] buf = BinHexSerializer.hexbin(data, 0, data.length());
            Object retobj = null;
            ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            try {
                if (smode.equals("binhex")) {
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    retobj = ois.readObject();
                    ois.close();
                }
                if (smode.equals("dtxml-binhex")) {
                    retobj = DataThingSerializer.unmarshal(bais);
                }
            } catch (Exception e) {
                throw new ITypeException("Failed to deserialize data using " + smode + " encoding: " + e);
            }
            return retobj;
        }
        throw new ITypeException("Unsupported serialization mode: " + smode);
    }

    /**
	 * convert data item to single object to wrap into itype:i
	 * does not descend recursively (only if needed for proper extraction)
	 * @param obj
	 * @return
	 */
    protected static Object getDataForSingleItem(Object obj) {
        if (obj instanceof Element) {
            try {
                return serializeElementToXmlDoc((Element) obj);
            } catch (Exception e) {
                throw new ITypeException("Failed to serialize argument: " + e);
            }
        }
        if (obj instanceof CDATASection) {
            return ((CDATASection) obj).getData();
        }
        if (obj instanceof Comment) {
            return "";
        }
        if (obj instanceof Text) {
            return ((Text) obj).getData();
        }
        return obj;
    }

    public static Value IType2Saxon(Object itv) {
        if (itv instanceof IType) {
            org.jdom.Element itvelem = ((IType) itv).toJDom();
            Document doc = new Document(itvelem);
            System.err.println("doc = " + doc);
            TavEngine engine = TLSData.getEngine();
            System.err.println("engine = " + engine);
            Configuration config = engine.getConfiuration();
            System.err.println("configuration = " + config);
            DocumentWrapper wrapper = new DocumentWrapper(doc, "baseURI", TLSData.getEngine().getConfiuration());
            System.err.println("wrapper = " + wrapper);
            NodeWrapper nodeWrapper = wrapper.wrap(itvelem);
            System.err.println("node wrapper = " + nodeWrapper);
            return new SingletonNode(nodeWrapper);
        } else {
            throw new ITypeException("Bad type, IType expected, but found: " + itv.getClass().getName());
        }
    }

    public static Value BDM2Saxon(Object dt) {
        System.err.println("TEMP: converting " + dt);
        IType itp = BDM2IType(dt);
        System.err.println("TEMP: itype " + itp);
        Value v = IType2Saxon(itp);
        System.err.println("TEMP: value " + v);
        return v;
    }

    public static Value getXMLfromTAV(Object dt) {
        return BDM2Saxon(dt);
    }

    public static Value getXML(Object dt) {
        return getXMLfromTAV(dt);
    }

    public static DataThing Saxon2BDM(Object obj) {
        return IType2BDM(Saxon2IType(obj));
    }

    public static DataThing getTAVfromXML(Object obj) {
        return Saxon2BDM(obj);
    }

    /** typework - testowanie typow **/
    public static String typeInfo(Object obj) throws XPathException {
        StringBuffer sb = new StringBuffer();
        if (obj == null) return "Null";
        if (obj instanceof Element) {
            return dumpElementInfo((Element) obj);
        }
        if (obj instanceof SequenceIterator) {
            SequenceIterator it = (SequenceIterator) obj;
            sb.append("*** SEQUENCE ITERATOR ***\nElements:\n");
            Item item;
            while ((item = it.next()) != null) {
                sb.append("* ");
                sb.append("\t" + typeInfo(item));
                sb.append("\n");
            }
            sb.append("*** end of SI ***\n");
            return sb.toString();
        }
        if (obj instanceof List) {
            List l = (List) obj;
            sb.append("*** LIST ***\nElements:\n");
            for (Iterator it = l.iterator(); it.hasNext(); ) {
                sb.append("* ");
                ;
                sb.append("\t" + typeInfo(it.next()));
                sb.append("\n");
            }
            sb.append("*** end of LIST ***\n");
            return sb.toString();
        }
        if (obj instanceof ArrayIterator) {
            ArrayIterator ait = (ArrayIterator) obj;
            sb.append("Array Iterator: \n");
            do {
                sb.append("* Element: ");
                sb.append(typeInfo(ait.next()));
            } while (ait.hasNext());
            return sb.toString();
        }
        if (obj instanceof AtomicValue) {
            AtomicValue x = (AtomicValue) obj;
            sb.append("Atomic value of type: " + x.getItemType().toString() + "\n");
            return sb.toString();
        }
        if (obj instanceof DataThing) {
            return "DataThing of type: " + ((DataThing) obj).getSyntacticType();
        }
        if (obj instanceof List) {
            return "List of size" + ((List) obj).size() + ", class: " + obj.getClass().getName() + ", Value: " + obj.toString();
        }
        return "Class: " + obj.getClass().getName() + ", Value: " + obj.toString();
    }

    public static Element getSampleJDomElement() {
        return null;
    }

    public static String dumpElementInfo(Element el) {
        StringBuffer sb = new StringBuffer();
        sb.append("W3C ELEMENT: name [" + el.getTagName() + "]");
        NamedNodeMap attrs = el.getAttributes();
        sb.append("ATTRIBUTE COUNT: " + attrs.getLength());
        sb.append("\n");
        for (int i = 0; i < attrs.getLength(); i++) {
            sb.append("Attr [" + i + "]: ");
            Attr a = (Attr) attrs.item(i);
            sb.append(a.getName() + "=" + a.getValue());
        }
        sb.append("\n");
        return sb.toString();
    }

    public static DataThing makeBDM(Object obj) {
        if (obj == null) return null;
        if (obj instanceof DataThing) {
            return (DataThing) obj;
        } else {
            return DataThingFactory.bake(obj);
        }
    }

    public static DataThing getTAV(Object obj) {
        return makeBDM(obj);
    }
}
