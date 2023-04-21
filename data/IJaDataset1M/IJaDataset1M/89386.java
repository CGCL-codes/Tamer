package com.ems.common.util;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 
 * @author Chiknin
 *
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();

    public static <T> T toBeanFromJson(JsonObject jsonObj, Class<T> beanClass) {
        return gson.fromJson(jsonObj, beanClass);
    }

    public static <T> List<T> toBeanFromJson(JsonArray jsonArray, Class<T> beanClass) {
        List<T> beans = new ArrayList<T>();
        for (JsonElement jsonEl : jsonArray) {
            beans.add(gson.fromJson(jsonEl, beanClass));
        }
        return beans;
    }

    public static <T> T toBeanFromJsonFirst(JsonArray jsonArray, Class<T> beanClass) {
        List<T> beans = toBeanFromJson(jsonArray, beanClass);
        if (beans.size() > 0) {
            return beans.get(0);
        }
        return null;
    }

    public static String toJsonFormBean(Object bean) {
        return gson.toJson(bean);
    }

    public static void copyBeanToMap(Object beanObj, Map<String, Object> map) {
        BeanWrapper bean = new BeanWrapperImpl(beanObj);
        PropertyDescriptor[] pds = bean.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            String name = pd.getName();
            if ("class".equals(name)) {
                continue;
            }
            Object value = bean.getPropertyValue(name);
            map.put(name, value);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T toBeanFromMap(Map<String, String> map, Class<T> beanClass) {
        if (map.size() <= 0) {
            return null;
        }
        BeanWrapper bean = new BeanWrapperImpl(beanClass);
        bean.registerCustomEditor(Date.class, new CustomDateEditor());
        bean.setPropertyValues(map);
        return (T) bean.getWrappedInstance();
    }

    static class CustomDateEditor extends PropertyEditorSupport {

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            this.setValue(DateUtil.parse(text));
        }

        @Override
        public String getAsText() {
            Date date = (Date) this.getValue();
            String text = DateUtil.formatFull(date);
            return text != null ? text : "";
        }
    }

    static class Bean {

        private String a;

        private String b;

        private Integer c;

        private Date d1;

        private Date d2;

        private Date d3;

        public Date getD3() {
            return d3;
        }

        public void setD3(Date d3) {
            this.d3 = d3;
        }

        public Date getD1() {
            return d1;
        }

        public void setD1(Date d1) {
            this.d1 = d1;
        }

        public Date getD2() {
            return d2;
        }

        public void setD2(Date d2) {
            this.d2 = d2;
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        public Integer getC() {
            return c;
        }

        public void setC(Integer c) {
            this.c = c;
        }
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("a", "a1");
        map.put("b", "b1");
        map.put("c", "1");
        map.put("d1", "2001-10-10");
        map.put("d2", "21:01:12");
        map.put("d3", "2001-10-10 21:01:12");
        Bean bean = BeanUtils.toBeanFromMap(map, Bean.class);
        bean.setA(null);
        System.out.println(ToStringBuilder.reflectionToString(bean));
        Map<String, Object> map1 = new HashMap<String, Object>();
        BeanUtils.copyBeanToMap(bean, map1);
        System.out.println(map1);
        System.out.println(toJsonFormBean(bean));
        System.out.println(toJsonFormBean(map1));
    }
}
