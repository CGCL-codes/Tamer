package org.springframework.web.servlet.view.json.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import net.sf.sojo.common.ObjectUtil;
import net.sf.sojo.common.PathRecordWalkerInterceptor;
import net.sf.sojo.core.UniqueIdGenerator;
import net.sf.sojo.navigation.PathExecuter;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.servlet.view.json.converter.model.Address;
import org.springframework.web.servlet.view.json.converter.model.Car;
import org.springframework.web.servlet.view.json.converter.model.Customer;
import org.springframework.web.servlet.view.json.converter.model.Node;
import org.springframework.web.servlet.view.json.mock.converter.MockWalkerInterceptor;
import org.springjson.test.TestBase;

public class MapGraphWalkerTest extends TestBase {

    @Test
    public void testInterceptor() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        Assert.assertEquals(0, lvWalker.getInterceptorSize());
        lvWalker.addInterceptor(lvInterceptor);
        Assert.assertEquals(1, lvWalker.getInterceptorSize());
        lvWalker.removeInterceptorByNumber(0);
        Assert.assertEquals(0, lvWalker.getInterceptorSize());
    }

    @Test
    public void testWalkSimpleString() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        String s = "Test-String";
        lvWalker.walk(s);
        Map lvPathes = lvInterceptor.getAllRecordedPathes();
        String lvSearchPath = lvPathes.keySet().iterator().next().toString();
        Assert.assertEquals(0, lvSearchPath.length());
        Object lvResult = PathExecuter.getNestedProperty(s, lvSearchPath);
        Assert.assertEquals(s, lvResult);
    }

    @Test
    public void testWalkSimpleInteger() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        Integer i = new Integer(4711);
        lvWalker.walk(i);
        Map lvPathes = lvInterceptor.getAllRecordedPathes();
        String lvSearchPath = lvPathes.keySet().iterator().next().toString();
        Assert.assertEquals(0, lvSearchPath.length());
        Object lvResult = PathExecuter.getNestedProperty(i, lvSearchPath);
        Assert.assertEquals(i, lvResult);
    }

    @Test
    public void testWalkSimpleDouble() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        Double d = new Double(0.07);
        lvWalker.walk(d);
        Map lvPathes = lvInterceptor.getAllRecordedPathes();
        String lvSearchPath = lvPathes.keySet().iterator().next().toString();
        Assert.assertEquals(0, lvSearchPath.length());
        Object lvResult = PathExecuter.getNestedProperty(d, lvSearchPath);
        Assert.assertEquals(d, lvResult);
    }

    @Test
    public void testWalkSimpleDate() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        Date lvDate = new Date();
        lvWalker.walk(lvDate);
        Map lvPathes = lvInterceptor.getAllRecordedPathes();
        String lvSearchPath = lvPathes.keySet().iterator().next().toString();
        Assert.assertEquals(0, lvSearchPath.length());
        Object lvResult = PathExecuter.getNestedProperty(lvDate, lvSearchPath);
        Assert.assertEquals(lvDate, lvResult);
    }

    @Test
    public void testWalkNull() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        lvWalker.walk(null);
        Map lvPathes = lvInterceptor.getAllRecordedPathes();
        String lvSearchPath = lvPathes.keySet().iterator().next().toString();
        Assert.assertEquals(0, lvSearchPath.length());
        Object lvResult = PathExecuter.getNestedProperty(null, lvSearchPath);
        Assert.assertNull(lvResult);
    }

    @Test
    public void testWalkEmptyList() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        List lvList = new ArrayList();
        lvWalker.walk(lvList);
        Object lvPathValue = PathExecuter.getNestedProperty(lvList, "[]");
        Assert.assertEquals(new ArrayList(), lvPathValue);
    }

    @Test
    public void testWalkList() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        List lvList = new ArrayList();
        lvList.add("aaa");
        lvList.add("bbb");
        lvWalker.walk(lvList);
        Object lvPathValue = PathExecuter.getNestedProperty(lvList, "[0]");
        Assert.assertEquals("aaa", lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(lvList, "[1]");
        Assert.assertEquals("bbb", lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(lvList, "[]");
        Assert.assertEquals(lvList, lvPathValue);
        Assert.assertEquals(3, lvInterceptor.getAllRecordedPathes().size());
    }

    @Test
    public void testWalkObjectArray() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        Date lvDate = new Date();
        Object o[] = new Object[] { "aaa", new Integer(7), lvDate };
        lvWalker.walk(o);
        Assert.assertEquals(4, lvInterceptor.getAllRecordedPathes().size());
        Object lvPathValue = PathExecuter.getNestedProperty(o, "[0]");
        Assert.assertEquals("aaa", lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(o, "[1]");
        Assert.assertEquals(new Integer(7), lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(o, "[2]");
        Assert.assertEquals(lvDate, lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(o, "[]");
        Assert.assertEquals(o, lvPathValue);
    }

    @Test
    public void testWalkStringArray() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        String s[] = new String[] { "aaa", "bbb", "ccc" };
        lvWalker.walk(s);
        Assert.assertEquals(4, lvInterceptor.getAllRecordedPathes().size());
        Object lvPathValue = PathExecuter.getNestedProperty(s, "[0]");
        Assert.assertEquals("aaa", lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(s, "[1]");
        Assert.assertEquals("bbb", lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(s, "[2]");
        Assert.assertEquals("ccc", lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(s, "[]");
        Assert.assertEquals(s, lvPathValue);
    }

    @Test
    public void testWalkEmptyMap() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        Map lvMap = new HashMap();
        lvWalker.walk(lvMap);
        Assert.assertEquals(1, lvInterceptor.getAllRecordedPathes().size());
        Object lvPathValue = PathExecuter.getNestedProperty(lvMap, "()");
        Assert.assertEquals(new Hashtable(), lvPathValue);
    }

    @Test
    public void testWalkEmptyListInMap() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        Map lvMap = new HashMap();
        lvMap.put("empty", new ArrayList());
        lvWalker.walk(lvMap);
        Assert.assertEquals(2, lvInterceptor.getAllRecordedPathes().size());
        Object lvPathValue = PathExecuter.getNestedProperty(lvMap, "(empty)");
        Assert.assertEquals(new ArrayList(), lvPathValue);
    }

    @Test
    public void testWalkEmptyMapInList() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        List lvList = new ArrayList();
        lvList.add(new HashMap());
        lvWalker.walk(lvList);
        Assert.assertEquals(2, lvInterceptor.getAllRecordedPathes().size());
        Object lvPathValue = PathExecuter.getNestedProperty(lvList, "[0]");
        Assert.assertEquals(new HashMap(), lvPathValue);
    }

    @Test
    public void testWalkMap() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        Map lvMap = new HashMap();
        lvMap.put("key1", "val1");
        lvMap.put("key2", "val2");
        lvWalker.walk(lvMap);
        Assert.assertEquals(3, lvInterceptor.getAllRecordedPathes().size());
        Object lvPathValue = PathExecuter.getNestedProperty(lvMap, "(key1)");
        Assert.assertEquals("val1", lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(lvMap, "(key2)");
        Assert.assertEquals("val2", lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(lvMap, "()");
        Assert.assertEquals(lvMap, lvPathValue);
    }

    @Test
    public void testWalkNestedListInMap() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        Map lvMap = new HashMap();
        lvMap.put("key1", "val1");
        lvMap.put("key2", "val2");
        List lvList = new ArrayList();
        lvList.add("aaa");
        lvList.add("bbb");
        lvMap.put("list", lvList);
        lvWalker.walk(lvMap);
        Assert.assertEquals(6, lvInterceptor.getAllRecordedPathes().size());
        Object lvPathValue = PathExecuter.getNestedProperty(lvMap, "(key1)");
        Assert.assertEquals("val1", lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(lvMap, "(key2)");
        Assert.assertEquals("val2", lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(lvMap, "()");
        Assert.assertEquals(lvMap, lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(lvMap, "(list).[]");
        Assert.assertEquals(lvList, lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(lvMap, "(list).[0]");
        Assert.assertEquals("aaa", lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(lvMap, "(list).[1]");
        Assert.assertEquals("bbb", lvPathValue);
    }

    @Test
    public void testWalkNestedMapInList() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        Map lvMap = new HashMap();
        lvMap.put("key1", "val1");
        lvMap.put("key2", "val2");
        List lvList = new ArrayList();
        lvList.add("aaa");
        lvList.add("bbb");
        lvList.add(lvMap);
        lvWalker.walk(lvList);
        Assert.assertEquals(6, lvInterceptor.getAllRecordedPathes().size());
        Object lvPathValue = PathExecuter.getNestedProperty(lvList, "[]");
        Assert.assertEquals(lvList, lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(lvList, "[0]");
        Assert.assertEquals("aaa", lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(lvList, "[1]");
        Assert.assertEquals("bbb", lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(lvList, "[2]");
        Assert.assertEquals(lvMap, lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(lvList, "[2].()");
        Assert.assertEquals(lvMap, lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(lvList, "[2].(key1)");
        Assert.assertEquals("val1", lvPathValue);
        lvPathValue = PathExecuter.getNestedProperty(lvList, "[2].(key2)");
        Assert.assertEquals("val2", lvPathValue);
    }

    @Test
    public void testWalkBeanSimple() throws Exception {
        Node lvNode = new Node("Test-Node");
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvInterceptor.setFilterUniqueIdProperty(true);
        lvWalker.addInterceptor(lvInterceptor);
        lvWalker.walk(lvNode);
        Map lvPathes = lvInterceptor.getAllRecordedPathes();
        Assert.assertNotNull(lvPathes.get("childs[]"));
        Assert.assertNotNull(lvPathes.get("namedChilds()"));
        Assert.assertNotNull(lvPathes.get("class"));
        Assert.assertNotNull(lvPathes.get("name"));
        Assert.assertNull(lvPathes.get(UniqueIdGenerator.UNIQUE_ID_PROPERTY));
        Assert.assertNull(lvPathes.get("parent"));
        Iterator lvIterator = lvPathes.entrySet().iterator();
        while (lvIterator.hasNext()) {
            Map.Entry lvEntry = (Entry) lvIterator.next();
            if (lvEntry.getKey().equals("childs[]")) {
                Object lvResult = PathExecuter.getNestedProperty(lvNode, lvEntry.getKey().toString());
                Assert.assertEquals(new ArrayList(), lvResult);
            } else if (lvEntry.getKey().equals("namedChilds()")) {
                Object lvResult = PathExecuter.getNestedProperty(lvNode, lvEntry.getKey().toString());
                Assert.assertEquals(new Hashtable(), lvResult);
            } else if (lvEntry.getKey().equals("class")) {
                Object lvResult = PathExecuter.getNestedProperty(lvNode, lvEntry.getKey().toString());
                Assert.assertEquals(Node.class, lvResult);
            } else if (lvEntry.getKey().equals("name")) {
                Object lvResult = PathExecuter.getNestedProperty(lvNode, lvEntry.getKey().toString());
                Assert.assertEquals("Test-Node", lvResult);
            } else if (lvEntry.getKey().equals("parent")) {
                Object lvResult = PathExecuter.getNestedProperty(lvNode, lvEntry.getKey().toString());
                Assert.assertNull(lvResult);
            }
        }
    }

    @Test
    public void testWalkBeanSimpleNumberOfPropertiesNode() throws Exception {
        Node lvNode = new Node("Test-Node");
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvInterceptor.setFilterUniqueIdProperty(true);
        lvWalker.addInterceptor(lvInterceptor);
        lvWalker.walk(lvNode);
        Map lvPathes = lvInterceptor.getAllRecordedPathes();
        Map lvNodeMap = (Map) new ObjectUtil().makeSimple(new Node());
        lvNodeMap.remove(UniqueIdGenerator.UNIQUE_ID_PROPERTY);
        Assert.assertEquals(lvPathes.size(), lvNodeMap.size());
        String lvPath = "name";
        Assert.assertNotNull(lvPathes.get(lvPath));
        Object lvResult = PathExecuter.getNestedProperty(lvNode, lvPath);
        Assert.assertEquals("Test-Node", lvResult);
    }

    @Test
    public void testWalkBeanSimpleNumberOfPropertiesCustomer() throws Exception {
        Customer lvCustomer = new Customer("Test-Name");
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvInterceptor.setFilterUniqueIdProperty(true);
        lvWalker.addInterceptor(lvInterceptor);
        lvWalker.walk(lvCustomer);
        Map lvPathes = lvInterceptor.getAllRecordedPathes();
        Map lvCustomerMap = (Map) new ObjectUtil().makeSimple(new Customer());
        lvCustomerMap.remove(UniqueIdGenerator.UNIQUE_ID_PROPERTY);
        Assert.assertEquals(lvPathes.size() + 3, lvCustomerMap.size());
    }

    @Test
    public void testWalkBeanSimpleInList() throws Exception {
        Node lvNode1 = new Node("Test-Node-1");
        lvNode1.getChilds().add("child-1");
        Node lvNode2 = new Node("Test-Node-2");
        lvNode2.getNamedChilds().put("key-1", "value-1");
        List lvList = new ArrayList();
        lvList.add(lvNode1);
        lvList.add(lvNode2);
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvInterceptor.setFilterUniqueIdProperty(true);
        lvWalker.addInterceptor(lvInterceptor);
        lvWalker.walk(lvList);
        Map lvPathes = lvInterceptor.getAllRecordedPathes();
        Map lvNodeMap = (Map) new ObjectUtil().makeSimple(new Node());
        lvNodeMap.remove(UniqueIdGenerator.UNIQUE_ID_PROPERTY);
        Assert.assertEquals(10, lvNodeMap.size() * 2);
        String lvPath = "[0].name";
        Assert.assertNotNull(lvPathes.get(lvPath));
        Object lvResult = PathExecuter.getNestedProperty(lvList, lvPath);
        Assert.assertNotNull(lvResult);
        Assert.assertEquals("Test-Node-1", lvResult);
        lvPath = "[0].childs[0]";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvList, lvPath);
        Assert.assertNotNull(lvResult);
        Assert.assertEquals("child-1", lvResult);
        lvPath = "[1].name";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvList, lvPath);
        Assert.assertNotNull(lvResult);
        Assert.assertEquals("Test-Node-2", lvResult);
        lvPath = "[1].namedChilds(key-1)";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvList, lvPath);
        Assert.assertNotNull(lvResult);
        Assert.assertEquals("value-1", lvResult);
    }

    @Test
    public void testWalkBeanComplex() throws Exception {
        Node lvNode = new Node("Test-Node");
        Node lvNodeParent = new Node("Test-Node-Parent");
        lvNode.setParent(lvNodeParent);
        lvNode.getChilds().add("child-1");
        lvNode.getChilds().add("child-2");
        lvNode.getNamedChilds().put("child-key-1", "child-value-1");
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        lvWalker.walk(lvNode);
        Map lvPathes = lvInterceptor.getAllRecordedPathes();
        String lvPath = "name";
        Assert.assertEquals("Test-Node", lvPathes.get(lvPath));
        Object lvResult = PathExecuter.getNestedProperty(lvNode, lvPath);
        Assert.assertEquals("Test-Node", lvResult);
        lvPath = "childs[0]";
        Assert.assertEquals("child-1", lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvNode, lvPath);
        Assert.assertEquals("child-1", lvResult);
        lvPath = "childs[]";
        Assert.assertEquals(lvNode.getChilds(), lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvNode, lvPath);
        Assert.assertEquals(lvNode.getChilds().size(), ((List) lvResult).size());
        Assert.assertEquals(lvNode.getChilds(), lvResult);
        lvPath = "namedChilds(child-key-1)";
        Assert.assertEquals(lvNode.getNamedChilds().get("child-key-1"), lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvNode, lvPath);
        Assert.assertEquals("child-value-1", lvResult);
        lvResult = PathExecuter.getNestedProperty(lvNode, "namedChilds(key-not-found)");
        Assert.assertNull(lvResult);
    }

    @Test
    public void testWalkBeanComplexWihLongWay() throws Exception {
        Node lvNode = new Node("Test-Node");
        Node lvNodeParent = new Node("Test-Node-Parent");
        lvNode.setParent(lvNodeParent);
        lvNode.getChilds().add("child-1");
        lvNode.getChilds().add("child-2");
        lvNode.getNamedChilds().put("key", "value");
        lvNode.getNamedChilds().put("self", lvNode);
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        lvWalker.walk(lvNode);
        Map lvPathes = lvInterceptor.getAllRecordedPathes();
        String lvPath = "name";
        Assert.assertEquals("Test-Node", lvPathes.get(lvPath));
        Object lvResult = PathExecuter.getNestedProperty(lvNode, lvPath);
        Assert.assertEquals("Test-Node", lvResult);
        lvPath = "namedChilds(key)";
        Assert.assertEquals("value", lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvNode, lvPath);
        Assert.assertEquals("value", lvResult);
        lvPath = "namedChilds(self)";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvNode, lvPath);
        Assert.assertEquals(lvNode, lvResult);
        lvPath = "namedChilds(self).name";
        Assert.assertNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvNode, lvPath);
        Assert.assertEquals("Test-Node", lvResult);
        lvPath = "namedChilds(self).namedChilds(self).name";
        Assert.assertNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvNode, lvPath);
        Assert.assertEquals("Test-Node", lvResult);
        lvPath = "namedChilds(self).namedChilds(self).namedChilds(self).name";
        Assert.assertNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvNode, lvPath);
        Assert.assertEquals("Test-Node", lvResult);
        lvPath = "namedChilds(self).childs[0]";
        Assert.assertNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvNode, lvPath);
        Assert.assertEquals("child-1", lvResult);
        lvPath = "namedChilds(self).childs[1]";
        Assert.assertNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvNode, lvPath);
        Assert.assertEquals("child-2", lvResult);
        lvPath = "namedChilds(self).childs[]";
        Assert.assertNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvNode, lvPath);
        Assert.assertEquals(lvNode.getChilds(), lvResult);
        lvPath = "namedChilds(self).parent.name";
        lvResult = PathExecuter.getNestedProperty(lvNode, lvPath);
        Assert.assertEquals("Test-Node-Parent", lvResult);
        lvPath = "namedChilds(self).namedChilds(self).namedChilds(self).parent.name";
        lvResult = PathExecuter.getNestedProperty(lvNode, lvPath);
        Assert.assertEquals("Test-Node-Parent", lvResult);
    }

    @Test
    public void testLongWayByCustomer() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        Customer lvCustomer = new Customer();
        lvCustomer.setAddresses(new LinkedHashSet());
        lvCustomer.setBirthDate(new Date());
        lvCustomer.setFirstName("First");
        lvCustomer.setLastName("Last");
        lvCustomer.setPartner(new Customer[] { new Customer("NEW"), lvCustomer });
        Address a1 = new Address();
        a1.setCity("city 1");
        a1.setPostcode("12345");
        a1.setCustomer(lvCustomer);
        Address a2 = new Address();
        a2.setCity("city 2");
        a2.setPostcode("98765");
        lvCustomer.getAddresses().add(a1);
        lvCustomer.getAddresses().add(a2);
        lvWalker.walk(lvCustomer);
        Map lvPathes = lvInterceptor.getAllRecordedPathes();
        String lvPath = "firstName";
        Assert.assertNotNull(lvPathes.get(lvPath));
        Object lvResult = PathExecuter.getNestedProperty(lvCustomer, lvPath);
        Assert.assertEquals("First", lvResult);
        lvPath = "lastName";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvCustomer, lvPath);
        Assert.assertEquals("Last", lvResult);
        lvPath = "birthDate";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvCustomer, lvPath);
        Assert.assertEquals(lvCustomer.getBirthDate(), lvResult);
        lvPath = "partner[]";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvCustomer, lvPath);
        Assert.assertEquals(lvCustomer.getPartner().length, ((Customer[]) lvResult).length);
        lvPath = "partner[1]";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvCustomer, lvPath);
        Assert.assertEquals(lvCustomer, lvResult);
        Assert.assertSame(lvCustomer, lvResult);
        lvPath = "partner[0].lastName";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvCustomer, lvPath);
        Assert.assertEquals("NEW", lvResult);
        lvPath = "partner[0].firstName";
        Assert.assertNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvCustomer, lvPath);
        Assert.assertNull(lvResult);
        lvPath = "addresses[]";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvCustomer, lvPath);
        Assert.assertEquals(lvCustomer.getAddresses(), lvResult);
        lvPath = "addresses[0].city";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvCustomer, lvPath);
        Assert.assertEquals("city 1", lvResult);
        lvPath = "addresses[0].customer";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvCustomer, lvPath);
        Assert.assertEquals(lvCustomer, lvResult);
        Assert.assertSame(lvCustomer, lvResult);
        lvPath = "addresses[0].customer.addresses[0].customer";
        Assert.assertNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvCustomer, lvPath);
        Assert.assertEquals(lvCustomer, lvResult);
        Assert.assertSame(lvCustomer, lvResult);
        lvPath = "addresses[1].city";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvCustomer, lvPath);
        Assert.assertEquals("city 2", lvResult);
    }

    @Test
    public void testEmptyArrayByCustomer() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        Customer lvCustomer = new Customer();
        lvCustomer.setLastName("Last-Name");
        lvCustomer.setPartner(new Customer[0]);
        lvWalker.walk(lvCustomer);
        Map lvPathes = lvInterceptor.getAllRecordedPathes();
        String lvPath = "lastName";
        Assert.assertNotNull(lvPathes.get(lvPath));
        Object lvResult = PathExecuter.getNestedProperty(lvCustomer, lvPath);
        Assert.assertEquals("Last-Name", lvResult);
        lvPath = "partner[]";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvCustomer, lvPath);
        Assert.assertEquals(lvCustomer.getPartner(), lvResult);
        lvCustomer.setPartner(null);
        lvPath = "partner[]";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvCustomer, lvPath);
        Assert.assertNull(lvResult);
        lvCustomer.setPartner(new Object[] { new ArrayList() });
        lvPath = "partner[0]";
        lvResult = PathExecuter.getNestedProperty(lvCustomer, lvPath);
        Assert.assertEquals(new ArrayList(), lvResult);
    }

    @Test
    public void testEmptyMapByCustomer() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        Customer lvCustomer = new Customer();
        lvCustomer.setLastName("Last-Name");
        lvCustomer.setPartner(new Object[] { new Hashtable() });
        lvWalker.walk(lvCustomer);
        Map lvPathes = lvInterceptor.getAllRecordedPathes();
        String lvPath = "partner[0].()";
        Assert.assertNotNull(lvPathes.get(lvPath));
        Object lvResult = PathExecuter.getNestedProperty(lvCustomer, lvPath);
        Assert.assertEquals(new HashMap(), lvResult);
    }

    @Test
    public void testBeanCar() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        PathRecordWalkerInterceptor lvInterceptor = new PathRecordWalkerInterceptor();
        lvWalker.addInterceptor(lvInterceptor);
        Car lvCar = new Car("MyCar");
        lvCar.setBuild(new Date());
        lvCar.setProperties(new Properties());
        lvCar.getProperties().put("key", "value");
        lvCar.getProperties().put("self", lvCar);
        lvWalker.walk(lvCar);
        Map lvPathes = lvInterceptor.getAllRecordedPathes();
        String lvPath = "name";
        Assert.assertNotNull(lvPathes.get(lvPath));
        Object lvResult = PathExecuter.getNestedProperty(lvCar, lvPath);
        Assert.assertEquals("MyCar", lvResult);
        lvPath = "build";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvCar, lvPath);
        Assert.assertEquals(lvCar.getBuild(), lvResult);
        lvPath = "properties()";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvCar, lvPath);
        Assert.assertEquals(lvCar.getProperties(), lvResult);
        lvPath = "properties(key)";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvCar, lvPath);
        Assert.assertEquals("value", lvResult);
        lvPath = "properties(self)";
        Assert.assertNotNull(lvPathes.get(lvPath));
        lvResult = PathExecuter.getNestedProperty(lvCar, lvPath);
        Assert.assertEquals(lvCar, lvResult);
        lvPath = "properties(self).name";
        lvResult = PathExecuter.getNestedProperty(lvCar, lvPath);
        Assert.assertEquals("MyCar", lvResult);
    }

    @Test
    public void testSimpleNumberOfRecursion() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        Assert.assertEquals(0, lvWalker.getNumberOfRecursion());
        lvWalker.walk("Test");
        Assert.assertEquals(1, lvWalker.getNumberOfRecursion());
    }

    @Test
    public void testNullNumberOfRecursion() throws Exception {
        MapGraphWalker lvWalker = new MapGraphWalker();
        Assert.assertEquals(0, lvWalker.getNumberOfRecursion());
        lvWalker.walk(null);
        Assert.assertEquals(1, lvWalker.getNumberOfRecursion());
    }

    @Test
    public void testBeanNumberOfRecursion() throws Exception {
        Car lvCar = new Car("MyCar");
        MapGraphWalker lvWalker = new MapGraphWalker();
        Assert.assertEquals(0, lvWalker.getNumberOfRecursion());
        lvWalker.walk(lvCar);
        Assert.assertEquals(5, lvWalker.getNumberOfRecursion());
    }

    @Test
    public void testListNumberOfRecursion() throws Exception {
        List lvList = new ArrayList();
        lvList.add("aa");
        lvList.add(new Long(4711));
        MapGraphWalker lvWalker = new MapGraphWalker();
        Assert.assertEquals(0, lvWalker.getNumberOfRecursion());
        lvWalker.walk(lvList);
        Assert.assertEquals(3, lvWalker.getNumberOfRecursion());
        lvList.add(new Double(47.11));
        lvWalker.walk(lvList);
        Assert.assertEquals(4, lvWalker.getNumberOfRecursion());
    }

    @Test
    public void testWalkInterceptorByList() throws Exception {
        List lvList = new ArrayList();
        lvList.add("aaa");
        lvList.add("bbb");
        lvList.add("ccc");
        MapGraphWalker lvWalker = new MapGraphWalker();
        MockWalkerInterceptor lvInterceptor = new MockWalkerInterceptor("bbb");
        lvWalker.addInterceptor(lvInterceptor);
        lvWalker.walk(lvList);
        Assert.assertNotNull(lvInterceptor);
        Assert.assertEquals("bbb", lvInterceptor.getWhenThisObjectThanCanelWalk());
        lvWalker = new MapGraphWalker();
        lvInterceptor = new MockWalkerInterceptor(lvList);
        lvWalker.addInterceptor(lvInterceptor);
        lvWalker.walk(lvList);
        Assert.assertNotNull(lvInterceptor);
        Assert.assertEquals(lvList, lvInterceptor.getWhenThisObjectThanCanelWalk());
    }

    @Test
    public void testWalkInterceptorByMap() throws Exception {
        Map lvMap = new Hashtable();
        lvMap.put("k-aaa", "v-aaa");
        lvMap.put("k-bbb", "v-bbb");
        MapGraphWalker lvWalker = new MapGraphWalker();
        MockWalkerInterceptor lvInterceptor = new MockWalkerInterceptor(lvMap);
        lvWalker.addInterceptor(lvInterceptor);
        lvWalker.walk(lvMap);
        Assert.assertNotNull(lvInterceptor);
        Assert.assertEquals(lvMap, lvInterceptor.getWhenThisObjectThanCanelWalk());
    }
}
