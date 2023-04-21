package org.docflower.serializationutils;

import java.lang.reflect.*;
import java.util.Map;
import javax.xml.namespace.QName;
import org.docflower.consts.DocFlowerXmlConsts;
import org.docflower.server.utils.DocFlowerServerActivator;
import org.w3c.dom.Element;

public class DeserializerUtils {

    public static final String PROTOCOL_SEPARATOR = "://";

    public static String getClassNameFromQName(QName qname) {
        String location = qname.getNamespaceURI();
        location = trimProtocol(location);
        int endStorageIdIndex = location.indexOf("/") + 1;
        return location.replace('/', '.').substring(endStorageIdIndex);
    }

    public static String trimProtocol(String location) {
        int protocolLength = location.indexOf(PROTOCOL_SEPARATOR);
        return location.substring(protocolLength + PROTOCOL_SEPARATOR.length());
    }

    public static Class<?> getClassByXsiType(String xsiType, Element element) {
        String location = element.lookupNamespaceURI(xsiType.substring(0, xsiType.indexOf(':')));
        location = trimProtocol(location);
        String packageName = location.substring(location.indexOf('/') + 1).replace('/', '.');
        String className = packageName + "." + xsiType.substring(xsiType.indexOf(':') + 1);
        ClassLoader loader = DocFlowerServerActivator.getDefault().getServerClassLoader();
        try {
            return loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getClassByQName(QName qname) {
        ClassLoader loader = DocFlowerServerActivator.getDefault().getServerClassLoader();
        String className = DeserializerUtils.getClassNameFromQName(qname) + "." + qname.getLocalPart();
        try {
            return loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Field getFieldByFieldName(Object obj, String fieldName) {
        try {
            Field field = SerializerUtils.getFieldFromClass(obj.getClass(), fieldName);
            return field;
        } catch (SecurityException e) {
        }
        return null;
    }

    private static Field getFieldByFieldName(Class<?> cl, String fieldName) {
        try {
            Field field = cl.getDeclaredField(fieldName);
            return field;
        } catch (SecurityException e) {
        } catch (NoSuchFieldException e) {
        }
        return null;
    }

    public static Class<?> getClassForElement(Element objectElement, String className) {
        Class<?> resultClass = null;
        if (objectElement instanceof Element) {
            String derivedTypeName = (objectElement).getAttributeNS(DocFlowerXmlConsts.NS_XML_INSTANCE, DocFlowerXmlConsts.NS_XML_INSTANCE_ATTR_TYPE);
            if (className != null && className.length() > 0) {
                ClassLoader loader = DocFlowerServerActivator.getDefault().getServerClassLoader();
                try {
                    resultClass = loader.loadClass(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (derivedTypeName != null && derivedTypeName.length() > 0) {
                resultClass = DeserializerUtils.getClassByXsiType(derivedTypeName, objectElement);
            } else {
                resultClass = DeserializerUtils.getClassByQName(new QName(objectElement.getNamespaceURI(), objectElement.getLocalName()));
            }
        }
        return resultClass;
    }

    public static String getClassNameForField(Object obj, String fieldName) {
        Field field = getFieldByFieldName(obj, fieldName);
        if (field.getGenericType() instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) field.getGenericType();
            Type actualType = pt.getActualTypeArguments()[0];
            String result = actualType.toString();
            result = result.replace("class ", "");
            return result;
        } else {
            return field.getType().getName();
        }
    }

    public static String getClassNameForFieldByGetter(Method getter, String fieldName) {
        Field field = getFieldByFieldName(getter.getDeclaringClass(), fieldName);
        if (field.getGenericType() instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) field.getGenericType();
            Type actualType = pt.getActualTypeArguments()[0];
            String result = actualType.toString();
            result = result.replace("class ", "");
            return result;
        } else {
            return field.getType().getName();
        }
    }

    public static Field getFieldByName(String fieldName, Method getter) {
        Field result = getFieldByFieldName(getter.getDeclaringClass(), fieldName);
        return result;
    }

    public static Object getInstanceOfField(Object obj, Method getter) {
        Object result = null;
        try {
            result = getter.invoke(obj, (Object[]) null);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Class<?> getFieldTypeByMethod(String fieldName, Map<String, MethodsContainer> objectMethods) {
        if (objectMethods.get(fieldName) != null) {
            return objectMethods.get(fieldName).getGetter().getReturnType();
        }
        return null;
    }

    public static void setInstanceOfField(Object obj, Method setter, Object value) {
        try {
            setter.invoke(obj, value);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
