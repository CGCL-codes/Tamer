package org.realteam.iframework.json;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.ezmorph.Morpher;
import net.sf.ezmorph.MorpherRegistry;
import net.sf.ezmorph.bean.BeanMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;
import org.realteam.iframework.json.beans.BeanA;
import org.realteam.iframework.json.beans.MyBean2;
import org.realteam.iframework.json.beans.Person;

/**
 * Json-lib can transform JSONObjects to either a DynaBean or an specific bean
 * class. When using DynaBean all arrays are converted to Lists, when using an
 * specific bean class the transformation will use type conversion if necessary
 * on array properties.
 */
public class JSON2BeanTest {

    /**
	 * Convert to DynaBean:
	 */
    @Test
    public void JSON2DynaBean() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String json = "{name=\"json\",bool:true,int:1,double:2.2,func:function(a){ return a; },array:[1,2]}";
        JSONObject jsonObject = JSONObject.fromObject(json);
        Object bean = JSONObject.toBean(jsonObject);
        assertEquals(jsonObject.get("name"), PropertyUtils.getProperty(bean, "name"));
        assertEquals(jsonObject.get("bool"), PropertyUtils.getProperty(bean, "bool"));
        assertEquals(jsonObject.get("int"), PropertyUtils.getProperty(bean, "int"));
        assertEquals(jsonObject.get("double"), PropertyUtils.getProperty(bean, "double"));
        assertEquals(jsonObject.get("func"), PropertyUtils.getProperty(bean, "func"));
        List expected = JSONArray.toList(jsonObject.getJSONArray("array"));
        assertArrayEquals(expected.toArray(), ((List) PropertyUtils.getProperty(bean, "array")).toArray());
    }

    /**
	 * Convert to Bean:
	 */
    @Test
    public void JSON2Bean() {
        String json = "{bool:true,integer:1,string:\"json\"}";
        JSONObject jsonObject = JSONObject.fromObject(json);
        BeanA bean = (BeanA) JSONObject.toBean(jsonObject, BeanA.class);
        assertEquals(jsonObject.get("bool"), Boolean.valueOf(bean.isBool()));
        assertEquals(jsonObject.get("integer"), new Integer(bean.getInteger()));
        assertEquals(jsonObject.get("string"), bean.getString());
    }

    @Test
    public void JSON2Bean2() {
        String json = "{'data':[{'name':'Wallace'},{'name':'Grommit'}]}";
        Map classMap = new HashMap();
        classMap.put("data", Person.class);
        MyBean2 bean = (MyBean2) JSONObject.toBean(JSONObject.fromObject(json), MyBean2.class, classMap);
        System.out.println(((Person) bean.getData().get(0)).getName());
    }

    @Test
    public void JSON2Bean3() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String json = "{'data':[{'name':'Wallace'},{'name':'Grommit'}]}";
        MyBean2 bean = (MyBean2) JSONObject.toBean(JSONObject.fromObject(json), MyBean2.class);
        System.out.println(PropertyUtils.getProperty(bean.getData().get(0), "name"));
        MorpherRegistry morpherRegistry = JSONUtils.getMorpherRegistry();
        Morpher dynaMorpher = new BeanMorpher(Person.class, morpherRegistry);
        morpherRegistry.registerMorpher(dynaMorpher);
        List output = new ArrayList();
        for (Iterator i = bean.getData().iterator(); i.hasNext(); ) {
            output.add(morpherRegistry.morph(Person.class, i.next()));
        }
        bean.setData(output);
        System.out.println(((Person) bean.getData().get(0)).getName());
    }
}
