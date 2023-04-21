package com.director.core.json.impl.gson;

import com.director.core.IncludeReturnDataStrategy;
import com.director.core.annotation.DirectReturnIncludeStrategy;
import com.director.test.ResultObject;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Simone Ricciardi
 * @version 1.0, 11/01/2011
 */
public class IncludeReturnDataStrategyTest {

    @Test
    public void testBuildSerializationMap() {
        IncludeReturnDataStrategy strategy = new IncludeReturnDataStrategy(new DirectReturnIncludeStrategy() {

            @Override
            public String[] fieldPatterns() {
                return new String[] { "prop1", "prop2", "multiple.prop1" };
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return DirectReturnIncludeStrategy.class;
            }
        });
        ResultObject result = new ResultObject("prop", 2, new ResultObject("nestedProp", 1));
        ResultObject multipleItem1 = new ResultObject("multipleProp1", 3);
        ResultObject multipleItem2 = new ResultObject("multipleProp2", 4);
        result.addMultiple(multipleItem1);
        result.addMultiple(multipleItem2);
        Map<Object, List<String>> serializationMap = strategy.buildSerializationMap(result);
        assertNotNull(serializationMap);
        assertEquals(3, serializationMap.size());
        List<String> rootFieldList = serializationMap.get(result);
        assertNotNull(rootFieldList);
        assertEquals(3, rootFieldList.size());
        for (String field : rootFieldList) {
            assertTrue(Arrays.asList("prop1", "prop2", "multiple").contains(field));
        }
        List<String> multipleItem1FieldList = serializationMap.get(multipleItem1);
        assertNotNull(multipleItem1FieldList);
        assertEquals(1, multipleItem1FieldList.size());
        assertEquals("prop1", multipleItem1FieldList.get(0));
        List<String> multipleItem2FieldList = serializationMap.get(multipleItem2);
        assertNotNull(multipleItem2FieldList);
        assertEquals(1, multipleItem2FieldList.size());
        assertEquals("prop1", multipleItem2FieldList.get(0));
    }
}
