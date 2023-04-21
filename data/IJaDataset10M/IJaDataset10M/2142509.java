package org.dozer.functional_tests;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.dozer.vo.ArrayDest;
import org.dozer.vo.ArraySource;
import org.junit.Test;

/**
 * Collections and arrays test
 *
 * @author Vadim Shaigorodskiy
 * @author Hee Tatt Ooi
 */
public class CollectionTest extends AbstractFunctionalTest {

    @Override
    public void setUp() throws Exception {
        mapper = getMapper("arrayMapping.xml");
    }

    /**
   * Test shows how simple array grows, when dest array is not null
   */
    @Test
    public void testArrayGrowConversion() {
        ArraySource sourceBean = new ArraySource();
        String[] sourceArray = sourceBean.getPreInitializedArray();
        sourceArray[0] = "1";
        sourceArray[1] = "2";
        ArrayDest destinationBean = new ArrayDest();
        mapper.map(sourceBean, destinationBean, "array");
        String[] destinationArray = destinationBean.getPreInitializedArray();
        Assert.assertEquals(sourceArray.length + 2, destinationArray.length);
    }

    @Test
    public void testSetValueToNullArray() {
        ArraySource sourceBean = new ArraySource();
        ArrayDest arrayDest = mapper.map(sourceBean, ArrayDest.class, "single");
        Assert.assertEquals(1, arrayDest.getArray().length);
        Assert.assertNull("Element must contain null", arrayDest.getArray()[0]);
    }

    /**
   * Test collection to primitive array mapping
   */
    @Test
    public void testCollectionToPrimitiveArray() {
        ArraySource sourceBean = new ArraySource();
        List<Integer> srcList = new ArrayList<Integer>();
        srcList.add(new Integer(2));
        srcList.add(new Integer(3));
        srcList.add(new Integer(8));
        sourceBean.setListOfIntegers(srcList);
        ArrayDest destBean = mapper.map(sourceBean, ArrayDest.class);
        int[] resultPrimitiveIntArray = destBean.getPrimitiveIntArray();
        for (int i = 0; i < srcList.size(); i++) {
            Integer srcValue = new Integer(srcList.get(i));
            int resultValue = resultPrimitiveIntArray[i];
            assertEquals(srcValue, new Integer(resultValue));
        }
    }

    /**
   * Test primitive array to collection mapping and also test for bidirectionality 
   * in the custom mappings XML file
   */
    @Test
    public void testPrimitiveArrayToCollection() {
        ArrayDest sourceBean = new ArrayDest();
        int[] primitiveIntArray = { 2, 3, 8 };
        sourceBean.setPrimitiveIntArray(primitiveIntArray);
        ArraySource destBean = mapper.map(sourceBean, ArraySource.class);
        List<Integer> resultList = destBean.getListOfIntegers();
        for (int i = 0; i < primitiveIntArray.length; i++) {
            int srcValue = primitiveIntArray[i];
            int resultValue = resultList.get(i);
            assertEquals(srcValue, resultValue);
        }
    }
}
