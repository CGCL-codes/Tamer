package com.intel.gpe.services.jms.workflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;
import javax.xml.xpath.XPathFunctionResolver;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import com.intel.gpe.client2.common.requests.utils.FileUtils;
import com.intel.gpe.util.filesets.FilePathUtils;
import com.intel.util.xml.JXMLUtil;
import com.intel.util.xml.Namespaces;
import com.intel.util.xml.Slice;

/**
 * An implementation of {@link javax.xml.xpath.XPathFunctionResolver}
 * 
 * @author Alexander Lukichev
 * @version $Id: BPELFunctionResolver.java,v 1.10 2006/09/28 08:30:55 mlukichev Exp $
 */
public class BPELFunctionResolver implements XPathFunctionResolver {

    private XPathFunctionResolver base;

    private Map functions;

    private IScope scope;

    private Document basedoc;

    public static final String NAMESPACE = "http://schemas.xmlsoap.org/ws/2003/03/business-process/";

    public static final String WES_NAMESPACE = "http://gpe.intel.com/workflow";

    public BPELFunctionResolver(XPathFunctionResolver base, IScope scope, Document basedoc) {
        this.base = base;
        this.scope = scope;
        this.basedoc = basedoc;
        functions = new HashMap();
        functions.put(new QName(NAMESPACE, "getVariableData"), new GetVariableData(basedoc));
        functions.put(new QName(WES_NAMESPACE, "getArrayElement"), new GetArrayElement(basedoc));
        functions.put(new QName(WES_NAMESPACE, "buildElement"), new BuildElement(basedoc));
        functions.put(new QName(WES_NAMESPACE, "insertElement"), new InsertArrayElement(basedoc));
        functions.put(new QName(WES_NAMESPACE, "lastIndexOf"), new LastIndexOf(basedoc));
        functions.put(new QName(WES_NAMESPACE, "matchPath"), new MatchPath(basedoc));
    }

    public XPathFunction resolveFunction(QName name, int arity) {
        XPathFunction result = (XPathFunction) functions.get(name);
        if (result != null) {
            return result;
        } else {
            return base.resolveFunction(name, arity);
        }
    }

    private class GetVariableData implements XPathFunction {

        private Document basedoc;

        public GetVariableData(Document basedoc) {
            this.basedoc = basedoc;
        }

        public Object evaluate(List args) throws XPathFunctionException {
            String name = (String) args.get(0);
            String part = (String) args.get(1);
            try {
                Node node = scope.getVariablePart(name, part);
                if (node instanceof Element) {
                    if (basedoc.getDocumentElement() != null) {
                        basedoc.removeChild(basedoc.getDocumentElement());
                    }
                    basedoc.appendChild(basedoc.importNode(node, true));
                    return basedoc.getChildNodes();
                } else if (node instanceof Text) {
                    String text = ((Text) node).getData();
                    try {
                        return new Double(text);
                    } catch (Exception e) {
                        return text;
                    }
                } else {
                    throw new XPathFunctionException("Unknown object");
                }
            } catch (NoSuchDataException e) {
                throw new XPathFunctionException(e);
            }
        }
    }

    private class GetArrayElement implements XPathFunction {

        private Document basedoc;

        public GetArrayElement(Document basedoc) {
            this.basedoc = basedoc;
        }

        public Object evaluate(List args) throws XPathFunctionException {
            String name = (String) args.get(0);
            String part = (String) args.get(1);
            String itname = (String) args.get(2);
            String itpart = (String) args.get(3);
            try {
                Node itnode = scope.getVariablePart(itname, itpart);
                String itvalue = itnode.getNodeValue();
                int it = Integer.parseInt(itvalue);
                Element array = (Element) scope.getVariablePart(name, part);
                array = (Element) basedoc.importNode(array, true);
                basedoc.appendChild(array);
                NodeList elements = array.getChildNodes();
                return new Slice(it - 1, it - 1, elements);
            } catch (Exception e) {
                throw new XPathFunctionException(e);
            }
        }
    }

    private class BuildElement implements XPathFunction {

        private Document baseDoc;

        public BuildElement(Document baseDoc) {
            this.baseDoc = baseDoc;
        }

        private String getStringValue(Object val) {
            if (val instanceof Double) {
                return String.valueOf(((Double) val).intValue());
            } else if (val instanceof String) {
                return (String) val;
            } else if (val instanceof Integer) {
                return String.valueOf(((Integer) val).intValue());
            } else if (val instanceof List) {
                return getStringValue(((List) val).get(0));
            } else return val.toString();
        }

        public Object evaluate(List args) throws XPathFunctionException {
            String template = (String) args.get(0);
            Map<String, String> values = new HashMap<String, String>();
            int i = 1;
            while (i < args.size() - 1) {
                String key = getStringValue(args.get(i));
                Object val = args.get(i + 1);
                String value = getStringValue(val);
                values.put(key, value);
                i += 2;
            }
            Document fake = new DocumentImpl();
            try {
                Namespaces namespaces = new Namespaces();
                Node node = JXMLUtil.createNode(fake, namespaces, template, values);
                if (basedoc.getDocumentElement() != null) {
                    basedoc.removeChild(basedoc.getDocumentElement());
                }
                basedoc.appendChild(basedoc.importNode(node, true));
                return basedoc.getChildNodes();
            } catch (Exception e) {
                throw new XPathFunctionException(e);
            }
        }
    }

    private class InsertArrayElement implements XPathFunction {

        private Document baseDoc;

        public InsertArrayElement(Document baseDoc) {
            this.baseDoc = baseDoc;
        }

        public Object evaluate(List args) throws XPathFunctionException {
            List nodeArray = (List) args.get(0);
            List node = (List) args.get(1);
            try {
                Element item = (Element) node.get(0);
                Element array = (Element) nodeArray.get(0);
                array.appendChild(item);
                if (basedoc.getDocumentElement() != null) {
                    basedoc.removeChild(basedoc.getDocumentElement());
                }
                basedoc.appendChild(basedoc.importNode(array, true));
                return basedoc.getChildNodes();
            } catch (Exception e) {
                throw new XPathFunctionException(e);
            }
        }
    }

    private class LastIndexOf implements XPathFunction {

        private Document baseDoc;

        public LastIndexOf(Document baseDoc) {
            this.baseDoc = baseDoc;
        }

        private String getStringValue(Object val) {
            if (val instanceof Double) {
                return String.valueOf(((Double) val).intValue());
            } else if (val instanceof String) {
                return (String) val;
            } else if (val instanceof Integer) {
                return String.valueOf(((Integer) val).intValue());
            } else if (val instanceof List) {
                return getStringValue(((List) val).get(0));
            } else if (val instanceof String) {
                return (String) val;
            } else if (val instanceof Text) {
                return ((Text) val).getData();
            } else return val.toString();
        }

        public Object evaluate(List args) throws XPathFunctionException {
            try {
                String string = getStringValue(args.get(0));
                String pattern = getStringValue(args.get(1));
                int res = string.lastIndexOf(pattern);
                return new Integer(res);
            } catch (Exception e) {
                throw new XPathFunctionException(e);
            }
        }
    }

    private class MatchPath implements XPathFunction {

        private Document baseDoc;

        public MatchPath(Document baseDoc) {
            this.baseDoc = baseDoc;
        }

        private String getStringValue(Object val) {
            if (val instanceof String) {
                return (String) val;
            } else if (val instanceof List) {
                return getStringValue(((List) val).get(0));
            } else return val.toString();
        }

        public Object evaluate(List args) throws XPathFunctionException {
            try {
                String pattern = getStringValue(args.get(0));
                String path = getStringValue(args.get(1));
                return FilePathUtils.matchPath(pattern, path, true);
            } catch (Exception e) {
                throw new XPathFunctionException(e);
            }
        }
    }
}
