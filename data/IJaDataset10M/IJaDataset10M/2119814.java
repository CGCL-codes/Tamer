package org.jaffa.dwr.converters;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.beanutils.DynaClass;
import org.apache.log4j.Logger;
import org.directwebremoting.convert.BeanConverter;
import org.directwebremoting.dwrp.ObjectOutboundVariable;
import org.directwebremoting.dwrp.ParseUtil;
import org.directwebremoting.dwrp.ProtocolConstants;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.TypeHintContext;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Messages;
import org.jaffa.flexfields.FlexBean;
import org.jaffa.flexfields.FlexClass;
import org.jaffa.flexfields.FlexParam;

/**
 * Converter for a FlexBean.
 */
public class FlexBeanConverter extends BeanConverter {

    private static final Logger log = Logger.getLogger(FlexBeanConverter.class);

    @Override
    public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx) throws MarshallException {
        String value = iv.getValue();
        if (value.trim().equals(ProtocolConstants.INBOUND_NULL)) {
            return null;
        }
        if (!value.startsWith(ProtocolConstants.INBOUND_MAP_START)) {
            throw new MarshallException(paramType, Messages.getString("BeanConverter.FormatError", ProtocolConstants.INBOUND_MAP_START));
        }
        if (!value.endsWith(ProtocolConstants.INBOUND_MAP_END)) {
            throw new MarshallException(paramType, Messages.getString("BeanConverter.FormatError", ProtocolConstants.INBOUND_MAP_START));
        }
        value = value.substring(1, value.length() - 1);
        try {
            FlexBean bean;
            if (instanceType != null) {
                bean = (FlexBean) instanceType.newInstance();
            } else {
                bean = (FlexBean) paramType.newInstance();
            }
            Map properties = getPropertyMapFromObject(bean, false, true);
            Map tokens = extractInboundTokens(paramType, value);
            {
                String key = "dynaClass";
                String val = (String) tokens.remove(key);
                Property property = (Property) properties.get(key);
                if (val != null && property != null) {
                    Class propType = FlexClass.class;
                    String[] split = ParseUtil.splitInbound(val);
                    String splitValue = split[LocalUtil.INBOUND_INDEX_VALUE];
                    String splitType = split[LocalUtil.INBOUND_INDEX_TYPE];
                    InboundVariable nested = new InboundVariable(iv.getLookup(), null, splitType, splitValue);
                    TypeHintContext incc = createTypeHintContext(inctx, property);
                    Object output = converterManager.convertInbound(propType, nested, inctx, incc);
                    property.setValue(bean, output);
                    DynaClass flexClass = bean.getDynaClass();
                    if (flexClass != null) {
                        flexClass = FlexClass.instance(flexClass.getName());
                        bean = FlexBean.instance((FlexClass) flexClass);
                    }
                }
            }
            if (instanceType != null) {
                inctx.addConverted(iv, instanceType, bean);
            } else {
                inctx.addConverted(iv, paramType, bean);
            }
            for (Iterator it = tokens.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                Property property = (Property) properties.get(key);
                if (property == null) {
                    Class propType = bean.getDynaClass() != null && bean.getDynaClass().getDynaProperty(key) != null ? bean.getDynaClass().getDynaProperty(key).getType() : String.class;
                    property = new FlexDescriptor(key, propType);
                }
                Class propType = property.getPropertyType();
                String[] split = ParseUtil.splitInbound(val);
                String splitValue = split[LocalUtil.INBOUND_INDEX_VALUE];
                String splitType = split[LocalUtil.INBOUND_INDEX_TYPE];
                InboundVariable nested = new InboundVariable(iv.getLookup(), null, splitType, splitValue);
                TypeHintContext incc = createTypeHintContext(inctx, property);
                Object output = converterManager.convertInbound(propType, nested, inctx, incc);
                property.setValue(bean, output);
            }
            return bean;
        } catch (MarshallException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new MarshallException(paramType, ex);
        }
    }

    @Override
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws MarshallException {
        FlexBean flexBean = (FlexBean) data;
        Map ovs = new TreeMap();
        ObjectOutboundVariable ov = new ObjectOutboundVariable(outctx);
        outctx.put(flexBean, ov);
        try {
            Map properties = getPropertyMapFromObject(flexBean, true, false);
            for (Iterator it = properties.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();
                String name = (String) entry.getKey();
                Property property = (Property) entry.getValue();
                if ("flexParams".equals(name)) {
                    FlexParam[] flexParams = flexBean.getFlexParams();
                    if (flexParams != null) {
                        for (FlexParam flexParam : flexParams) {
                            Object value = flexBean.get(flexParam.getName());
                            if (value != null) {
                                OutboundVariable nested = getConverterManager().convertOutbound(value, outctx);
                                ovs.put(flexParam.getName(), nested);
                            }
                        }
                    }
                } else {
                    Object value = property.getValue(flexBean);
                    if (value != null) {
                        OutboundVariable nested = getConverterManager().convertOutbound(value, outctx);
                        ovs.put(name, nested);
                    }
                }
            }
            if (flexBean != null) {
                String className = flexBean.getClass().getSimpleName();
                OutboundVariable var = getConverterManager().convertOutbound(className, outctx);
                ovs.put("className", var);
            }
        } catch (MarshallException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new MarshallException(flexBean.getClass(), ex);
        }
        ov.init(ovs, getJavascript());
        return ov;
    }

    /** This class is used to encapsulate the setter on the FlexBean class. */
    private static class FlexDescriptor implements Property {

        private String name;

        private Class propertyType;

        private static final Method c_setter;

        static {
            try {
                c_setter = FlexBean.class.getMethod("set", String.class, Object.class);
            } catch (Exception e) {
                String s = "Unable to obtain a handle on the 'public void set(String name, Object value)' method of FlexBean";
                log.fatal(s, e);
                throw new RuntimeException(s, e);
            }
        }

        FlexDescriptor(String name, Class propertyType) {
            this.name = name;
            this.propertyType = propertyType;
        }

        /**
         * Gets the name of this property
         * @return The property name
         */
        public String getName() {
            return name;
        }

        /**
         * What type does this property
         * @return The type of object that will be returned by {@link #getValue(Object)}
         */
        public Class getPropertyType() {
            return propertyType;
        }

        /**
         * Get the value of this property of the passed in java bean
         * @param bean The bean to introspect
         * @return The value assigned to this property of the passed in bean
         * @throws MarshallException If the reflection access fails
         */
        public Object getValue(Object bean) throws MarshallException {
            return ((FlexBean) bean).get(name);
        }

        /**
         * Set the value of this property of the passed in java bean
         * @param bean The bean to introspect
         * @param value The value assigned to this property of the passed in bean
         * @throws MarshallException If the reflection access fails
         */
        public void setValue(Object bean, Object value) throws MarshallException {
            ((FlexBean) bean).set(name, value);
        }

        /**
         * This is a nasty hack - {@link TypeHintContext} needs a {@link Method}.
         * If you are implementing this and not proxying to a {@link PropertyDescriptor}
         * then you can probably return <code>null</code>.
         * We should probably refactor {@link TypeHintContext} to use {@link Property}
         * @return A setter method if one is available, or null otherwise
         */
        public Method getSetter() {
            return c_setter;
        }
    }
}
