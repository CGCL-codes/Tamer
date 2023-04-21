package org.bing.zion.filter.serializer;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple object serializer, impl by reflect.
 * 
 * @author chenbin
 * 
 */
public class SimpleReflectEncoder extends SimpleTypeCodec {

    public void encode(Object obj, Writer out) throws IOException {
        Map<Object, Integer> exists = new HashMap<Object, Integer>();
        this.encode(null, obj, new SimpleXmlWriter(out), exists, 0);
    }

    @SuppressWarnings("unchecked")
    private void encode(String fieldName, Object fieldValue, SimpleXmlWriter out, Map<Object, Integer> exists, int beginId) throws IOException {
        beginId++;
        if (fieldValue == null) {
            out.addNull();
            return;
        } else if (super.isSimpleType(fieldValue)) {
            if (fieldName != null) {
                super.simpleFieldEncode(fieldName, fieldValue, out);
            } else {
                super.simpleObjectEncode(fieldValue, out);
            }
        } else if (fieldValue.getClass().isArray()) {
            out.beginElement("a");
            out.addNotNullAttribute("n", fieldName);
            out.addAttribute("c", super.fineSimpleTypeName(fieldValue.getClass().getComponentType()));
            out.addAttribute("s", Array.getLength(fieldValue));
            out.endAttribute();
            int len = Array.getLength(fieldValue);
            for (int i = 0; i < len; i++) {
                Object value = Array.get(fieldValue, i);
                encode(null, value, out, exists, beginId++);
            }
            out.endElement("a");
        } else if (fieldValue instanceof List) {
            out.beginElement("l");
            out.addNotNullAttribute("n", fieldName);
            List list = (List) fieldValue;
            int len = list.size();
            out.addAttribute("s", len);
            out.endAttribute();
            for (int i = 0; i < len; i++) {
                Object value = list.get(i);
                encode(null, value, out, exists, beginId++);
            }
            out.endElement("l");
        } else {
            if (exists.containsKey(fieldValue)) {
                int refId = exists.get(fieldValue);
                out.beginElement("o");
                out.addAttribute("ref", refId);
                out.addNotNullAttribute("n", fieldName);
                out.fastEndElement();
                return;
            }
            ObjectReflectHelper refHelper = ObjectReflectHelper.instance(fieldValue.getClass());
            exists.put(fieldValue, beginId);
            out.beginElement("o");
            out.addAttribute("id", beginId);
            out.addNotNullAttribute("n", fieldName);
            out.addAttribute("c", fieldValue.getClass().getName());
            out.endAttribute();
            Field[] fds = refHelper.getTotalFields();
            for (int i = 0, l = fds.length; i < l; i++) {
                Object fval = refHelper.getValue(fieldValue, fds[i]);
                if (fval != null) {
                    String fname = fds[i].getName();
                    if (super.isSimpleType(fval)) {
                        super.simpleFieldEncode(fname, fval, out);
                    } else {
                        encode(fname, fval, out, exists, beginId);
                    }
                }
            }
            out.endElement("o");
        }
    }

    public String write(Object obj) throws IOException {
        StringWriter sw = new StringWriter(100);
        this.encode(obj, sw);
        return sw.toString();
    }
}
