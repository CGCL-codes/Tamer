package net.sf.json.test;

import junit.framework.TestCase;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.beanutils.PropertyUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2009-2-4
 * Time: 16:26:13
 * To change this template use File | Settings | File Templates.
 */
public class TestJson extends TestCase {

    public void testarrays() {
        boolean[] boolArray = new boolean[] { true, false, true };
        JSONArray jsonArray = JSONArray.fromObject(boolArray);
        System.out.println(jsonArray);
    }

    public void testcollections() {
        List list = new ArrayList();
        list.add("first");
        list.add("second");
        JSONArray jsonArray = JSONArray.fromObject(list);
        System.out.println(jsonArray);
    }

    public void testMaps() {
        Map map = new HashMap();
        map.put("name", "json");
        map.put("bool", Boolean.TRUE);
        map.put("int", new Integer(1));
        map.put("arr", new String[] { "a", "b" });
        map.put("func", "function(i){ return this.arr[i]; }");
        JSONObject jsonObject = JSONObject.fromObject(map);
        System.out.println(jsonObject);
    }

    public void testBeans() {
        JSONObject jsonObject = JSONObject.fromObject(new MyBean());
        System.out.println(jsonObject);
        String xml = new XMLSerializer().write(jsonObject);
        System.out.println("xml = " + xml);
    }

    public void testDynaBean() throws Exception {
        String json = "{name=\"json\",bool:true,int:1,double:2.2,func:function(a){ return a; },array:[1,2]}";
        JSONObject jsonObject = JSONObject.fromObject(json);
        Object bean = JSONObject.toBean(jsonObject);
        assertEquals(jsonObject.get("name"), PropertyUtils.getProperty(bean, "name"));
        assertEquals(jsonObject.get("bool"), PropertyUtils.getProperty(bean, "bool"));
        assertEquals(jsonObject.get("int"), PropertyUtils.getProperty(bean, "int"));
        assertEquals(jsonObject.get("double"), PropertyUtils.getProperty(bean, "double"));
        assertEquals(jsonObject.get("func"), PropertyUtils.getProperty(bean, "func"));
        List expected = JSONArray.toList(jsonObject.getJSONArray("array"));
        assertEquals(expected, (List) PropertyUtils.getProperty(bean, "array"));
    }

    public void testBean() {
        String json = "{bool:true,integer:1,string:\"json\"}";
        JSONObject jsonObject = JSONObject.fromObject(json);
        MyBean bean = (MyBean) JSONObject.toBean(jsonObject, MyBean.class);
    }

    public void testJson2xml1() {
        JSONObject json = new JSONObject(true);
        String xml = new XMLSerializer().write(json);
        System.out.println("xml = " + xml);
    }

    public void testJson2xml2() {
        JSONObject json = JSONObject.fromObject("{\"name\":\"json\",\"bool\":true,\"int\":1}");
        String xml = new XMLSerializer().write(json);
        System.out.println("xml = " + xml);
    }

    public void testJson2xml3() {
        JSONArray json = JSONArray.fromObject("[1,2,3]");
        String xml = new XMLSerializer().write(json);
        System.out.println("xml = " + xml);
    }

    public void testxml2json() {
        String xml = "<a class=\"array\">  \n" + "  <e type=\"function\" params=\"i,j\">  \n" + "      return matrix[i][j];   \n" + "  </e>  \n" + "</a>  ";
        JSONArray json = (JSONArray) new XMLSerializer().read(xml);
        System.out.println(json);
    }
}
