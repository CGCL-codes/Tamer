package org.t2framework.commons.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.t2framework.commons.ut.BaseTestCase;

public class ForeachTest extends BaseTestCase {

    public void test1() throws Exception {
        String[] array = new String[] { "a", "b", "c" };
        Foreach.each(array, new Foreach.EasyArrayItem<String>() {

            @Override
            public void each(String target, int index, String[] array) {
                teeNotNull(target);
            }
        });
        Integer[] nums = new Integer[] { 1, 2, 3 };
        Foreach.each(nums, new Foreach.EasyArrayItem<Integer>() {

            @Override
            public void each(Integer target, int index, Integer[] array) {
                teeNotNull(target);
            }
        });
        List<BigDecimal> list = Arrays.asList(new BigDecimal(1), new BigDecimal(2), new BigDecimal(3));
        Foreach.each(list, new Foreach.EasyIterableItem<BigDecimal>() {

            @Override
            public void each(BigDecimal target, int index, Iterable<BigDecimal> all) {
                teeNotNull(target);
            }
        });
        Map<String, String> map = new HashMap<String, String>() {

            private static final long serialVersionUID = 1L;

            {
                put("a", "A");
                put("b", "B");
                put("c", "C");
            }
        };
        Foreach.each(map, new Foreach.EasyMapItem<String, String>() {

            @Override
            public void each(Entry<String, String> target, int index, Iterable<Entry<String, String>> all) {
                teeNotNull(target.getKey());
                teeNotNull(target.getValue());
            }
        });
    }

    public void test2() throws Exception {
        String[] array = new String[] { "a", "b", "c" };
        try {
            Foreach.each(array, new Foreach.ArrayItem<String, Throwable>() {

                @Override
                public void each(String target, int index, String[] array) throws Throwable {
                    teeNotNull(target);
                }
            });
        } catch (Throwable e) {
            fail();
        }
        Integer[] nums = new Integer[] { 1, 2, 3 };
        try {
            Foreach.each(nums, new Foreach.ArrayItem<Integer, Throwable>() {

                @Override
                public void each(Integer target, int index, Integer[] array) throws Throwable {
                    teeNotNull(target);
                }
            });
        } catch (Throwable e) {
            fail();
        }
        List<BigDecimal> list = Arrays.asList(new BigDecimal(1), new BigDecimal(2), new BigDecimal(3));
        try {
            Foreach.each(list, new Foreach.IterableItem<BigDecimal, Throwable>() {

                @Override
                public void each(BigDecimal target, int index, Iterable<BigDecimal> all) throws Throwable {
                    teeNotNull(target);
                }
            });
        } catch (Throwable e) {
            fail();
        }
        Map<String, String> map = new HashMap<String, String>() {

            private static final long serialVersionUID = 1L;

            {
                put("a", "A");
                put("b", "B");
                put("c", "C");
            }
        };
        try {
            Foreach.each(map, new Foreach.MapItem<String, String, Throwable>() {

                @Override
                public void each(Entry<String, String> target, int index, Iterable<Entry<String, String>> all) throws Throwable {
                    teeNotNull(target.getKey());
                    teeNotNull(target.getValue());
                }
            });
        } catch (Throwable e) {
            fail();
        }
    }
}
