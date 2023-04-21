package org.dozer.functional_tests;

import org.dozer.vo.ValueObject;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;

/**
 * @author Dmitry Buzdin
 */
public class CopyByReferenceFromMapTest extends AbstractFunctionalTest {

    @Override
    @Before
    public void setUp() throws Exception {
        mapper = getMapper("mapMapping7.xml");
    }

    @Test
    public void testCopyByReferenceFromMap() {
        HashMap<String, ValueObject> hashMap = newInstance(HashMap.class);
        hashMap.put("1", new ValueObject());
        ValueObject destination = newInstance(ValueObject.class);
        mapper.map(hashMap, destination);
        assertNotNull(destination);
        assertNotNull(destination.getValue());
    }
}
