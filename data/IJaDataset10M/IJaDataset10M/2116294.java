package uk.co.christhomson.reflection.builders;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.jdom.Element;
import uk.co.christhomson.coherence.viewer.exception.CacheException;

public class XmlObjectBuilder {

    private Class<?> cls = null;

    private Object obj = null;

    public XmlObjectBuilder(Class<?> cls, Object obj) throws CacheException {
        this.cls = cls;
        this.obj = obj;
        if (cls == null) {
            throw new CacheException("Invalid class name specified");
        }
        if (cls.isPrimitive()) {
            this.cls = ClassBuilder.convertToObject(cls);
        }
    }

    public XmlObjectBuilder(String className) throws ClassNotFoundException, CacheException {
        this(className, null);
    }

    public XmlObjectBuilder(String className, Object obj) throws ClassNotFoundException, CacheException {
        this(ClassBuilder.convertToObject(ClassBuilder.getClassFromName(className)), obj);
    }

    public Element createXmlObject() throws IllegalArgumentException, IllegalAccessException {
        Set<Class<?>> parentClasses = new HashSet<Class<?>>();
        Element elem = processClass(cls, null, parentClasses, obj, true);
        return elem;
    }

    private Element processClass(Class<?> cls, String name, Set<Class<?>> parentClasses, Object obj, boolean isRoot) throws IllegalArgumentException, IllegalAccessException {
        System.out.println(cls + "-" + obj);
        Element elem = new Element("Class");
        if (name != null) {
            elem.setAttribute("name", name);
        }
        elem.setAttribute("type", cls.getName());
        elem.setAttribute("shorttype", ClassBuilder.getShortType(cls.getName()));
        Set<Class<?>> subParentClasses = new HashSet<Class<?>>(parentClasses);
        subParentClasses.add(cls);
        if (cls.isEnum()) {
            elem.setAttribute("isenum", "true");
            if (obj != null) {
                elem.setAttribute("value", obj.toString());
            }
            Object[] constants = cls.getEnumConstants();
            for (Object constant : constants) {
                Element option = new Element("Option");
                option.setText(constant.toString());
                elem.addContent(option);
            }
        } else if (cls.equals(String.class)) {
            Element fieldElem = createField("value", String.class.getName(), obj);
            if (fieldElem != null) {
                elem.addContent(fieldElem);
            }
        } else if (cls.equals(Date.class)) {
            if (obj != null) {
                elem.setAttribute("value", formatDate((Date) obj));
            }
        } else if (!((cls.equals(Date.class) || cls.equals(String.class)) && isRoot)) {
            for (Field field : cls.getDeclaredFields()) {
                if ((field.getModifiers() & Modifier.STATIC) == 0) {
                    Object subObj = null;
                    if (obj != null) {
                        field.setAccessible(true);
                        subObj = field.get(obj);
                    }
                    Element fieldElem = processField(field, subParentClasses, subObj);
                    if (fieldElem != null) {
                        elem.addContent(fieldElem);
                    }
                }
            }
        }
        return elem;
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private Element processField(Field field, Set<Class<?>> parentClasses, Object obj) throws IllegalArgumentException, IllegalAccessException {
        Class<?> fieldType = field.getType();
        if (!parentClasses.contains(fieldType)) {
            if (fieldType.equals(int.class) || fieldType.equals(Integer.class) || fieldType.equals(double.class) || fieldType.equals(Double.class) || fieldType.equals(String.class) || fieldType.equals(long.class) || fieldType.equals(Long.class) || fieldType.equals(boolean.class) || fieldType.equals(Boolean.class) || fieldType.equals(short.class) || fieldType.equals(Short.class)) {
                return createField(field.getName(), fieldType.getName(), obj);
            } else if (fieldType.isArray()) {
                Class<?> arrayType = fieldType.getComponentType();
                if (arrayType.equals(char.class)) {
                    if (obj != null) {
                        return createField(field.getName(), arrayType.getName() + "[]", new String((char[]) obj));
                    } else {
                        return createField(field.getName(), arrayType.getName() + "[]", obj);
                    }
                } else {
                    return createField(field.getName(), arrayType.getName() + "[]", obj);
                }
            } else {
                return processClass(field.getType(), field.getName(), parentClasses, obj, false);
            }
        } else {
            return null;
        }
    }

    private Element createField(String name, String type, Object obj) {
        Element elem = new Element("Field");
        if (name != null) {
            elem.setAttribute("name", name);
        }
        elem.setAttribute("type", type);
        elem.setAttribute("shorttype", ClassBuilder.getShortType(type));
        if (obj != null) elem.setAttribute("value", obj.toString());
        return elem;
    }
}
