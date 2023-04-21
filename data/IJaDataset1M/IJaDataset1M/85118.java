package jps.datastore;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author Radosław Kowalczyk
 */
public class SBAStore {

    public static Map<Long, SBAObject> objectMap = new HashMap<Long, SBAObject>();

    public static Long entryOID = new Long(0);

    public static Long rootOID = new Long(0);

    public static Map<Long, Object> parsedObjectMap = new HashMap<Long, Object>();

    public Long addObject(Object object) throws IllegalArgumentException, IllegalAccessException {
        return parseElement(object, object.getClass().getSimpleName());
    }

    public String printStructure() {
        String result = "\n";
        Iterator it = objectMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            result += pairs.getValue().toString() + "\n";
        }
        return result;
    }

    public Long loadXML(String xmlFileName) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        builder.setValidation(false);
        builder.setIgnoringElementContentWhitespace(false);
        Document doc = builder.build(new File(xmlFileName));
        Element root = doc.getRootElement();
        return parseElement(root);
    }

    private SimpleObject getProperObject(String name, String rawValue) {
        try {
            Integer value = Integer.parseInt(rawValue);
            IntegerObject so = new IntegerObject();
            so.setName(name);
            so.setValue(value);
            return so;
        } catch (NumberFormatException e1) {
            try {
                Double value = Double.parseDouble(rawValue);
                DoubleObject so = new DoubleObject();
                so.setName(name);
                so.setValue(value);
                return so;
            } catch (NumberFormatException e2) {
                try {
                    String doubleValue = rawValue.replace(",", ".");
                    Double value = Double.parseDouble(doubleValue);
                    DoubleObject so = new DoubleObject();
                    so.setName(name);
                    so.setValue(value);
                    return so;
                } catch (NumberFormatException e3) {
                    try {
                        Boolean value = Boolean.parseBoolean(rawValue);
                        if (!value) {
                            if (!rawValue.equals("false")) {
                                throw new NumberFormatException();
                            }
                        }
                        BooleanObject so = new BooleanObject();
                        so.setName(name);
                        so.setValue(value);
                        return so;
                    } catch (NumberFormatException e4) {
                        String value = rawValue;
                        StringObject so = new StringObject();
                        so.setName(name);
                        so.setValue(value);
                        return so;
                    }
                }
            }
        }
    }

    private Long parseElement(Object object, String name) throws IllegalArgumentException, IllegalAccessException {
        Class mainClass = object.getClass();
        if (mainClass.equals(String.class)) {
            StringObject io = new StringObject();
            io.setName(name);
            io.setValue((String) object);
            return io.getOID();
        } else if (mainClass.equals(Integer.class)) {
            IntegerObject io = new IntegerObject();
            io.setName(name);
            io.setValue((Integer) object);
            return io.getOID();
        } else if (mainClass.equals(Boolean.class)) {
            BooleanObject io = new BooleanObject();
            io.setName(name);
            io.setValue((Boolean) object);
            return io.getOID();
        } else if (mainClass.equals(Double.class)) {
            DoubleObject io = new DoubleObject();
            io.setName(name);
            io.setValue((Double) object);
            return io.getOID();
        } else if (mainClass.equals(ArrayList.class)) {
            ComplexObject io = new ComplexObject();
            io.setName(name);
            List<Long> childOIDs = new ArrayList<Long>();
            ArrayList col = (ArrayList) object;
            Iterator i = col.iterator();
            Integer j = 0;
            while (i.hasNext()) {
                Object tempObj = i.next();
                childOIDs.add(parseElement(tempObj, (j++).toString()));
            }
            io.setChildOIDs(childOIDs);
            return io.getOID();
        } else {
            ComplexObject io = new ComplexObject();
            io.setName(name);
            List<Long> childOIDs = new ArrayList<Long>();
            for (Field field : mainClass.getFields()) {
                Object o = field.get(object);
                childOIDs.add(parseElement(o, field.getName()));
            }
            io.setChildOIDs(childOIDs);
            return io.getOID();
        }
    }

    private Long parseElement(Element el) {
        String name = el.getName();
        String value = el.getValue();
        boolean isSimple = el.getChildren().isEmpty();
        if (isSimple) {
            SimpleObject so = getProperObject(name, value);
            return so.getOID();
        } else {
            ComplexObject co = new ComplexObject();
            co.setName(name);
            List<Long> childOIDs = new ArrayList<Long>();
            List childEls = el.getChildren();
            for (Object elObj : childEls) {
                if (elObj instanceof Element) {
                    Element childEl = (Element) elObj;
                    Long childOID = parseElement(childEl);
                    childOIDs.add(childOID);
                }
            }
            co.setChildOIDs(childOIDs);
            return co.getOID();
        }
    }

    public static Long generateOID() {
        return entryOID++;
    }

    private List<Long> addObject(Collection c) throws IllegalArgumentException, IllegalAccessException {
        List<Long> childOIDs = new ArrayList<Long>();
        for (Object o : c) {
            childOIDs.add(addObject(o));
        }
        return childOIDs;
    }

    public SBAObject getObject(Long oid) {
        return objectMap.get(oid);
    }

    public ComplexObject getRoot() {
        return (ComplexObject) objectMap.get(rootOID);
    }
}
