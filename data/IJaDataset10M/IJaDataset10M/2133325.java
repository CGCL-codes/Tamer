package net.taylor.xml;

import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import net.taylor.lang.ReflectionUtil;
import org.apache.commons.betwixt.AttributeDescriptor;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.expression.Context;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.DefaultObjectStringConverter;
import org.apache.commons.betwixt.strategy.TypeBindingStrategy;
import org.apache.commons.betwixt.strategy.ValueSuppressionStrategy;
import org.apache.commons.lang.StringUtils;
import org.hibernate.proxy.LazyInitializer;
import org.xml.sax.InputSource;

public class BetwixtUtil {

    public static void exportXml(Object object, OutputStream os) {
        exportXml(object, os, null);
    }

    public static void exportXml(Object object, OutputStream os, ValueSuppressionStrategy strategy) {
        try {
            os.write("<?xml version='1.0' ?>".getBytes());
            BeanWriter beanWriter = getBeanWriter(os, strategy);
            beanWriter.write(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static BeanWriter getBeanWriter(OutputStream os, ValueSuppressionStrategy strategy) {
        try {
            BeanWriter beanWriter = new BeanWriter(os);
            beanWriter.enablePrettyPrint();
            List<InputSource> mappings = getBetwixtConfig();
            for (InputSource inputSource : mappings) {
                beanWriter.getXMLIntrospector().register(inputSource);
            }
            beanWriter.getXMLIntrospector().getConfiguration().setTypeBindingStrategy(new CustomTypeBindingStrategy());
            beanWriter.getBindingConfiguration().setObjectStringConverter(new CustomObjectStringConverter());
            if (strategy != null) {
                beanWriter.getBindingConfiguration().setValueSuppressionStrategy(strategy);
            } else {
                beanWriter.getBindingConfiguration().setValueSuppressionStrategy(new TransientSuppressionStrategy());
            }
            return beanWriter;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Object importXml(String xml, Class clazz) {
        StringReader xmlReader = new StringReader(xml);
        BeanReader beanReader = new BeanReader();
        Object o;
        try {
            beanReader.getXMLIntrospector().getConfiguration().setTypeBindingStrategy(new CustomTypeBindingStrategy());
            beanReader.getBindingConfiguration().setObjectStringConverter(new CustomObjectStringConverter());
            List<InputSource> mappings = getBetwixtConfig();
            for (InputSource inputSource : mappings) {
                beanReader.registerMultiMapping(inputSource);
            }
            beanReader.registerBeanClass(ArrayList.class);
            if (clazz != null) {
                beanReader.registerBeanClass(clazz);
            }
            o = beanReader.parse(xmlReader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return o;
    }

    protected static List<InputSource> getBetwixtConfig() {
        try {
            List<InputSource> list = new ArrayList<InputSource>();
            Enumeration<URL> mappings = Thread.currentThread().getContextClassLoader().getResources("betwixt-config.xml");
            while (mappings.hasMoreElements()) {
                URL url = (URL) mappings.nextElement();
                InputSource is = new InputSource(url.openStream());
                if (is != null) {
                    list.add(is);
                }
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class CustomTypeBindingStrategy extends TypeBindingStrategy {

        public TypeBindingStrategy.BindingType bindingType(Class type) {
            TypeBindingStrategy.BindingType bindingType = null;
            if (Enum.class.isAssignableFrom(type)) {
                bindingType = TypeBindingStrategy.BindingType.PRIMITIVE;
            } else {
                bindingType = TypeBindingStrategy.DEFAULT.bindingType(type);
            }
            return bindingType;
        }
    }

    public static class CustomObjectStringConverter extends DefaultObjectStringConverter {

        public String objectToString(Object object, Class type, Context context) {
            String value = null;
            if (object instanceof Enum) {
                value = ((Enum) object).name();
            } else {
                value = super.objectToString(object, type, context);
            }
            return value;
        }

        public Object stringToObject(String value, Class type, Context context) {
            Object object = null;
            if (Enum.class.isAssignableFrom(type)) {
                if (!StringUtils.isEmpty(value)) {
                    object = Enum.valueOf(type, value);
                }
            } else {
                object = super.stringToObject(value, type, context);
            }
            return object;
        }
    }

    public static class TransientSuppressionStrategy extends ValueSuppressionStrategy {

        @Override
        public boolean suppressAttribute(AttributeDescriptor attributeDescriptor, String value) {
            return StringUtils.isEmpty(value);
        }

        public boolean suppressElement(ElementDescriptor element, String namespaceUri, String localName, String qualifiedName, Object value) {
            if (value instanceof LazyInitializer) {
                return true;
            }
            Method method = getMethod(value, qualifiedName);
            if (method != null && method.isAnnotationPresent(Transient.class)) {
                return true;
            }
            return false;
        }

        protected Method getMethod(Object value, String qualifiedName) {
            Method method = ReflectionUtil.findMethodByName(value.getClass(), "get" + StringUtils.capitalize(qualifiedName));
            return method;
        }
    }

    public static class NamedSuppressionStrategy extends TransientSuppressionStrategy {

        private String names = "";

        public NamedSuppressionStrategy() {
        }

        /**
		 * @param names
		 *            delimited set of property names to suppress
		 */
        public NamedSuppressionStrategy(String names) {
            this.names = names;
        }

        public boolean suppressElement(ElementDescriptor element, String namespaceUri, String localName, String qualifiedName, Object value) {
            boolean result = super.suppressElement(element, namespaceUri, localName, qualifiedName, value);
            if (result) {
                return true;
            }
            if (names.contains(value.getClass().getName() + "." + qualifiedName)) {
                return true;
            }
            return false;
        }
    }

    public static class EmptySuppressionStrategy extends NamedSuppressionStrategy {

        public EmptySuppressionStrategy() {
            super();
        }

        public EmptySuppressionStrategy(String names) {
            super(names);
        }

        public boolean suppressElement(ElementDescriptor element, String namespaceUri, String localName, String qualifiedName, Object value) {
            boolean result = super.suppressElement(element, namespaceUri, localName, qualifiedName, value);
            if (result) {
                return true;
            }
            Method method = getMethod(value, qualifiedName);
            if (method == null) {
                return false;
            }
            Object temp = ReflectionUtil.invoke(method, value, new Object[] {});
            if (temp == null) {
                return true;
            } else if (temp instanceof Collection) {
                return ((Collection) temp).size() == 0;
            }
            return false;
        }
    }

    public static class OneToManySuppressionStrategy extends EmptySuppressionStrategy {

        public OneToManySuppressionStrategy() {
            super();
        }

        public OneToManySuppressionStrategy(String names) {
            super(names);
        }

        public boolean suppressElement(ElementDescriptor element, String namespaceUri, String localName, String qualifiedName, Object value) {
            boolean result = super.suppressElement(element, namespaceUri, localName, qualifiedName, value);
            if (result) {
                return true;
            }
            Method method = getMethod(value, qualifiedName);
            if (method == null) {
                return false;
            }
            return method.isAnnotationPresent(OneToMany.class);
        }
    }
}
