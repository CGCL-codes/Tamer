package net.sf.dozer.functional_tests;

import net.sf.dozer.util.mapping.DataObjectInstantiator;
import net.sf.dozer.util.mapping.NoProxyDataObjectInstantiator;
import net.sf.dozer.util.mapping.vo.AnotherTestObject;
import net.sf.dozer.util.mapping.vo.AnotherTestObjectPrime;
import net.sf.dozer.util.mapping.vo.SimpleObj;
import net.sf.dozer.util.mapping.vo.SimpleObjPrime;
import net.sf.dozer.util.mapping.vo.TestObject;
import net.sf.dozer.util.mapping.vo.TestObjectPrime;

public class TrimStringsTest extends AbstractMapperTest {

    protected void setUp() throws Exception {
        super.setUp();
        mapper = getMapper(new String[] { "trimStringsMapping.xml" });
    }

    public void testTrimStrings_Global() {
        AnotherTestObject src = (AnotherTestObject) newInstance(AnotherTestObject.class);
        src.setField3("      valueNeedingTrimmed       ");
        src.setField4("      anotherValueNeedingTrimmed       ");
        src.setField5("  127 ");
        AnotherTestObjectPrime dest = (AnotherTestObjectPrime) mapper.map(src, AnotherTestObjectPrime.class);
        assertEquals("valueNeedingTrimmed", dest.getField3());
        assertEquals("anotherValueNeedingTrimmed", dest.getTo().getOne());
        assertEquals("field 5 not trimmed", Integer.valueOf("127"), dest.getField5());
    }

    public void testTrimStrings_ClassMapLevel() {
        TestObject src = (TestObject) newInstance(TestObject.class);
        String value = "    shouldNotBeNeedingTrimmed     ";
        src.setOne(value);
        TestObjectPrime dest = (TestObjectPrime) mapper.map(src, TestObjectPrime.class);
        assertEquals(value, dest.getOnePrime());
    }

    public void testTrimStrings_ImplicitMapping() {
        SimpleObj src = (SimpleObj) newInstance(SimpleObj.class);
        src.setField1("      valueNeedingTrimmed       ");
        SimpleObjPrime dest = (SimpleObjPrime) mapper.map(src, SimpleObjPrime.class);
        assertEquals("valueNeedingTrimmed", dest.getField1());
    }

    protected DataObjectInstantiator getDataObjectInstantiator() {
        return NoProxyDataObjectInstantiator.INSTANCE;
    }
}
