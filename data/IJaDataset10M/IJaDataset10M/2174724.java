package org.jaffa.dwr.converters;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.directwebremoting.convert.BeanConverter;
import org.directwebremoting.dwrp.ObjectOutboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.extend.Property;

/**
 * Convert a Javascript associative array into a JavaBean
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class NotNullBeanConverter extends BeanConverter {

    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws MarshallException {
        Map ovs = new TreeMap();
        ObjectOutboundVariable ov = new ObjectOutboundVariable(outctx);
        outctx.put(data, ov);
        try {
            Map properties = getPropertyMapFromObject(data, true, false);
            for (Iterator it = properties.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();
                String name = (String) entry.getKey();
                Property property = (Property) entry.getValue();
                Object value = property.getValue(data);
                if (value != null) {
                    OutboundVariable nested = getConverterManager().convertOutbound(value, outctx);
                    ovs.put(name, nested);
                }
            }
            if (data != null) {
                String className = data.getClass().getSimpleName();
                OutboundVariable var = getConverterManager().convertOutbound(className, outctx);
                ovs.put("className", var);
            }
        } catch (MarshallException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new MarshallException(data.getClass(), ex);
        }
        ov.init(ovs, getJavascript());
        return ov;
    }
}
