package com.jspx.sioc.type;

import com.jspx.utils.StringUtil;
import com.jspx.utils.BeanUtil;
import com.jspx.utils.ClassUtil;
import com.jspx.sioc.util.TypeUtil;
import com.jspx.sioc.util.AnnotationUtil;
import com.jspx.sioc.tag.BeanElement;
import com.jspx.sioc.tag.ListElement;
import com.jspx.sioc.tag.MapElement;
import com.jspx.sioc.tag.ArrayElement;
import com.jspx.sioc.BeanFactory;
import com.jspx.sioc.Sioc;
import com.jspx.jtxml.impl.TxElement;
import java.util.Map;
import java.util.List;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User:chenYuan (mail:cayurain@21cn.com)
 * Date: 2007-4-1
 * Time: 3:22:11
 */
public class BeanXmlType extends TypeSerializer {

    public String getTypeString() {
        return "bean";
    }

    private String namespace = "global";

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    private BeanFactory beanFactory = null;

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object getTypeObject() throws Exception {
        BeanElement beanElement = new BeanElement();
        beanElement.setElementString((String) value);
        String className = beanElement.getClassName();
        if (StringUtil.isNULL(className)) return null;
        Object result = ClassUtil.newInstance(className);
        if (result == null) return null;
        Map<String, Object> paramMap = TypeUtil.getPropertyValue(beanElement.getPropertyElements(), namespace);
        List<TxElement> listElements = beanElement.getListElements();
        for (TxElement element : listElements) {
            ListElement alist = (ListElement) element;
            paramMap.put(alist.getName(), TypeUtil.getListValue(alist, namespace, beanFactory));
        }
        listElements.clear();
        List<TxElement> mapElements = beanElement.getMapElements();
        for (TxElement element : mapElements) {
            MapElement amap = (MapElement) element;
            paramMap.put(amap.getName(), TypeUtil.getMapValue(amap, namespace, beanFactory));
        }
        mapElements.clear();
        List<TxElement> arrayElements = beanElement.getArrayElements();
        for (TxElement element : arrayElements) {
            ArrayElement aArray = (ArrayElement) element;
            paramMap.put(aArray.getName(), TypeUtil.getArrayValue(aArray, namespace, beanFactory));
        }
        arrayElements.clear();
        for (String name : paramMap.keySet()) {
            if (name == null) continue;
            Object pValue = paramMap.get(name);
            if (pValue instanceof String) {
                String stmp = (String) pValue;
                if (stmp.startsWith(Sioc.siocLoad)) {
                    stmp = stmp.substring(Sioc.siocLoad.length());
                    String beanName = stmp.substring(0, stmp.indexOf(Sioc.siocFen));
                    if (beanFactory != null) {
                        pValue = beanFactory.getBean(beanName, namespace);
                    }
                }
            }
            BeanUtil.setProperty(result, name, pValue);
        }
        if (!StringUtil.isNULL(beanElement.getCreate())) {
            result = BeanUtil.getProperty(result, beanElement.getCreate(), null);
        }
        result = AnnotationUtil.setRef(result, beanFactory, namespace);
        try {
            BeanUtil.invoke(result, beanElement.getInitMethod());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getXMLString() throws Exception {
        if (value == null) return "";
        StringBuffer sb = new StringBuffer();
        sb.append("<bean name=\"").append(name).append("\" class=\"").append(value.getClass().getName()).append("\">\r\n");
        Method[] methods = BeanUtil.getDeclaredReturnMethods(value.getClass());
        for (Method method : methods) {
            String methodName = BeanUtil.getCallMethodName(method);
            Object object = BeanUtil.getProperty(value, methodName, null);
            sb.append(TypeUtil.getTypeSerializer(methodName, object));
        }
        sb.append("</bean>");
        return sb.toString();
    }
}
