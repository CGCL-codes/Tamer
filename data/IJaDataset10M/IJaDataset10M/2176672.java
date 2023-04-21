package ca.huy.ximple;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.TestCase;
import org.xml.sax.SAXException;
import ca.huy.ximple.impl.MapXerializer;

public class TestXimple extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestXimple.class);
    }

    public void testReadWriteXmlMap() {
        HashMap map = new HashMap();
        map.put("aKey", "aValue");
        CompositeObject co = createComposite();
        map.put("aSeller", co);
        map.put(Integer.valueOf(1), Double.valueOf(3.14));
        Ximple ximple = new Ximple();
        MapXerializer mx = new MapXerializer(ximple);
        mx.setOutputImpl(false);
        ximple.addXerializer(java.util.Map.class, mx);
        StringWriter output = new StringWriter();
        try {
            ximple.writeXml(map, output);
            System.out.println(output.toString());
        } catch (XerialException e1) {
            e1.printStackTrace();
            fail(e1.toString());
        } catch (IOException e) {
            fail(e.toString());
        }
        try {
            Map map2 = (Map) ximple.readXml(new StringReader(output.toString()));
            assertTrue(map2.size() == map.size());
            assertTrue(map2.get("aKey").equals(map.get("aKey")));
            assertTrue(map2.get(Integer.valueOf(1)).equals(map.get(Integer.valueOf(1))));
            isEqual((CompositeObject) map2.get("aSeller"), (CompositeObject) map.get("aSeller"));
        } catch (IOException e2) {
            fail(e2.toString());
        } catch (SAXException e2) {
            fail(e2.toString());
        } catch (XerialException e2) {
            e2.printStackTrace();
            fail(e2.toString());
        } catch (ParserConfigurationException e2) {
            fail(e2.toString());
        }
    }

    public void testReadWriteXmlList() {
        List list = new ArrayList();
        for (int i = 0; i < 20; i++) {
            list.add(Integer.valueOf(i));
        }
        Ximple ximple = new Ximple();
        StringWriter output = new StringWriter();
        try {
            ximple.writeXml(list, output);
            System.out.println(output.toString());
        } catch (IOException e) {
            fail(e.toString());
            e.printStackTrace();
        } catch (XerialException e) {
            fail(e.toString());
            e.printStackTrace();
        }
        try {
            List list2 = (List) ximple.readXml(new StringReader(output.toString()));
            assertTrue(list2.equals(list));
        } catch (IOException e1) {
            fail(e1.toString());
            e1.printStackTrace();
        } catch (SAXException e1) {
            fail(e1.toString());
            e1.printStackTrace();
        } catch (XerialException e1) {
            fail(e1.toString());
            e1.printStackTrace();
        } catch (ParserConfigurationException e1) {
            fail(e1.toString());
            e1.printStackTrace();
        }
    }

    public void testReadWriteXmlSet() {
        Set set = new HashSet();
        for (int i = 0; i < 20; i++) {
            set.add(Integer.valueOf(i));
        }
        Ximple ximple = new Ximple();
        StringWriter output = new StringWriter();
        try {
            ximple.writeXml(set, output);
            System.out.println(output.toString());
        } catch (IOException e) {
            fail(e.toString());
            e.printStackTrace();
        } catch (XerialException e) {
            fail(e.toString());
            e.printStackTrace();
        }
        try {
            Set set2 = (Set) ximple.readXml(new StringReader(output.toString()));
            assertTrue(set2.equals(set));
        } catch (IOException e1) {
            fail(e1.toString());
            e1.printStackTrace();
        } catch (SAXException e1) {
            fail(e1.toString());
            e1.printStackTrace();
        } catch (XerialException e1) {
            fail(e1.toString());
            e1.printStackTrace();
        } catch (ParserConfigurationException e1) {
            fail(e1.toString());
            e1.printStackTrace();
        }
    }

    public void testReadWriteXmlObject() {
        CompositeObject co = createComposite();
        Ximple ximple = new Ximple();
        StringWriter output = new StringWriter();
        try {
            ximple.writeXml(co, output);
            System.out.println(output.toString());
        } catch (XerialException e1) {
            e1.printStackTrace();
            fail(e1.toString());
        } catch (IOException e) {
            fail(e.toString());
        }
        try {
            Object o = ximple.readXml(new StringReader(output.toString()));
            assertEqual(co, (CompositeObject) o);
        } catch (Exception e2) {
            e2.printStackTrace();
            fail(e2.toString());
        }
    }

    public void testReadWriteReferences() {
        List list1 = new LinkedList(Arrays.asList(new String[] { "1", "2", "1", "2", "3" }));
        Ximple ximple = new Ximple();
        StringWriter output = new StringWriter();
        try {
            ximple.writeXml(list1, output);
            System.out.println(output.toString());
        } catch (IOException e) {
            fail(e.toString());
            e.printStackTrace();
        } catch (XerialException e) {
            fail(e.toString());
            e.printStackTrace();
        }
        try {
            List list2 = (List) ximple.readXml(new StringReader(output.toString()));
            assertTrue(list2.equals(list1));
        } catch (IOException e1) {
            fail(e1.toString());
            e1.printStackTrace();
        } catch (SAXException e1) {
            fail(e1.toString());
            e1.printStackTrace();
        } catch (XerialException e1) {
            fail(e1.toString());
            e1.printStackTrace();
        } catch (ParserConfigurationException e1) {
            fail(e1.toString());
            e1.printStackTrace();
        }
    }

    public static CompositeObject createComposite() {
        CompositeObject co = new CompositeObject();
        co.setValueOne("Jumbo");
        co.setValueTwo(2);
        Map map = new HashMap();
        map.put("one", Integer.valueOf(1));
        map.put("two-point-two", Float.valueOf(2.2f));
        map.put("true", Boolean.TRUE);
        co.setValueMap(map);
        List list = new ArrayList();
        list.add(Double.valueOf(0.1));
        list.add(Double.valueOf(0.2));
        list.add(Double.valueOf(0.3));
        list.add(Double.valueOf(0.4));
        list.add(Double.valueOf(0.5));
        map.put("list", list);
        return co;
    }

    public static boolean isEqual(CompositeObject c1, CompositeObject c2) {
        if ((c1.getValueOne() != null && !c1.getValueOne().equals(c2.getValueOne())) || (c2.getValueOne() != null && !c2.getValueOne().equals(c1.getValueOne()))) {
            return false;
        }
        if (c1.getValueTwo() != c2.getValueTwo()) {
            return false;
        }
        if ((c1.getValueMap() != null && !c1.getValueMap().equals(c2.getValueMap())) || (c2.getValueMap() != null && !c2.getValueMap().equals(c1.getValueMap()))) {
            return false;
        }
        if ((c1.getValueList() != null && !c1.getValueList().equals(c2.getValueList())) || (c2.getValueList() != null && !c2.getValueList().equals(c1.getValueList()))) {
            return false;
        }
        return true;
    }

    public static void assertEqual(CompositeObject c1, CompositeObject c2) {
        assertEquals(c1.getValueOne(), c2.getValueOne());
        assertEquals(c1.getValueTwo(), c2.getValueTwo());
        assertEquals(c1.getValueMap(), c2.getValueMap());
        assertEquals(c1.getValueList(), c2.getValueList());
    }
}
