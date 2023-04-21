package com.listsorter.test;

import java.util.List;
import java.util.Locale;
import com.listsorter.OrderType;
import com.listsorter.SortUtil;

/**
 * 
 * @author tamerhur
 * 
 */
public class Test {

    public static void main(String[] args) {
        Locale locale = new Locale("tr", "TR");
        System.out.println("------------------------ DEFAULT DOUBLE TEST ------------------------");
        List<DoubleItem> doubleItems = DoubleItem.getItemList();
        SortUtil.sort(doubleItems);
        for (DoubleItem doubleItem : doubleItems) {
            System.out.println(doubleItem.value);
        }
        System.out.println("------------------------ DESCENDING DOUBLE TEST ------------------------");
        doubleItems = DoubleItem.getItemList();
        SortUtil.sort(doubleItems, OrderType.DESCENDING);
        for (DoubleItem doubleItem : doubleItems) {
            System.out.println(doubleItem.value);
        }
        System.out.println("------------------------ CUSTOM METHOD DOUBLE TEST ------------------------");
        doubleItems = DoubleItem.getItemList();
        SortUtil.sort(doubleItems, new String[] { "customOrder" });
        for (DoubleItem doubleItem : doubleItems) {
            System.out.println(doubleItem.value);
        }
        System.out.println("------------------------ CUSTOM METHOD2 DOUBLE TEST ------------------------");
        doubleItems = DoubleItem.getItemList();
        SortUtil.sort(doubleItems, new String[] { "customOrder2" });
        for (DoubleItem doubleItem : doubleItems) {
            System.out.println(doubleItem.value);
        }
        System.out.println("------------------------ CUSTOM DESCENDING METHOD DOUBLE TEST ------------------------");
        doubleItems = DoubleItem.getItemList();
        SortUtil.sort(doubleItems, new String[] { "customOrder" }, OrderType.DESCENDING);
        for (DoubleItem doubleItem : doubleItems) {
            System.out.println(doubleItem.value);
        }
        System.out.println("**************************************************************************************");
        System.out.println("------------------------ DEFAULT INTEGER TEST ------------------------");
        List<IntegerItem> integerItems = IntegerItem.getItemList();
        SortUtil.sort(integerItems);
        for (IntegerItem val : integerItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ DESCENDING INTEGER TEST ------------------------");
        integerItems = IntegerItem.getItemList();
        SortUtil.sort(integerItems, OrderType.DESCENDING);
        for (IntegerItem val : integerItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ CUSTOM METHOD INTEGER TEST ------------------------");
        integerItems = IntegerItem.getItemList();
        SortUtil.sort(integerItems, new String[] { "customOrder" });
        for (IntegerItem val : integerItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ CUSTOM METHOD2 INTEGER TEST ------------------------");
        integerItems = IntegerItem.getItemList();
        SortUtil.sort(integerItems, new String[] { "customOrder2" });
        for (IntegerItem val : integerItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ CUSTOM DESCENDING METHOD INTEGER TEST ------------------------");
        integerItems = IntegerItem.getItemList();
        SortUtil.sort(integerItems, new String[] { "customOrder" }, OrderType.DESCENDING);
        for (IntegerItem val : integerItems) {
            System.out.println(val.value);
        }
        System.out.println("**************************************************************************************");
        System.out.println("------------------------ DEFAULT LONG TEST ------------------------");
        List<LongItem> longItems = LongItem.getItemList();
        SortUtil.sort(longItems);
        for (LongItem val : longItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ DESCENDING LONG TEST ------------------------");
        longItems = LongItem.getItemList();
        SortUtil.sort(longItems, OrderType.DESCENDING);
        for (LongItem val : longItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ CUSTOM LONG TEST ------------------------");
        longItems = LongItem.getItemList();
        SortUtil.sort(longItems, new String[] { "customOrder" });
        for (LongItem val : longItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ CUSTOM METHOD2 LONG TEST ------------------------");
        longItems = LongItem.getItemList();
        SortUtil.sort(longItems, new String[] { "customOrder2" });
        for (LongItem val : longItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ CUSTOM DESCENDING METHOD LONG TEST ------------------------");
        longItems = LongItem.getItemList();
        SortUtil.sort(longItems, new String[] { "customOrder" }, OrderType.DESCENDING);
        for (LongItem val : longItems) {
            System.out.println(val.value);
        }
        System.out.println("**************************************************************************************");
        System.out.println("------------------------ DEFAULT FLOAT TEST ------------------------");
        List<FloatItem> floatItems = FloatItem.getItemList();
        SortUtil.sort(floatItems);
        for (FloatItem val : floatItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ DESCENDING FLOAT TEST ------------------------");
        floatItems = FloatItem.getItemList();
        SortUtil.sort(floatItems, OrderType.DESCENDING);
        for (FloatItem val : floatItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ CUSTOM FLOAT TEST ------------------------");
        floatItems = FloatItem.getItemList();
        SortUtil.sort(floatItems, new String[] { "customOrder" });
        for (FloatItem val : floatItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ CUSTOM METHOD2 FLOAT TEST ------------------------");
        floatItems = FloatItem.getItemList();
        SortUtil.sort(floatItems, new String[] { "customOrder2" });
        for (FloatItem val : floatItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ CUSTOM DESCENDING METHOD FLOAT TEST ------------------------");
        floatItems = FloatItem.getItemList();
        SortUtil.sort(floatItems, new String[] { "customOrder" }, OrderType.DESCENDING);
        for (FloatItem val : floatItems) {
            System.out.println(val.value);
        }
        System.out.println("**************************************************************************************");
        System.out.println("------------------------ DEFAULT STRING TEST ------------------------");
        List<StringItem> stringItems = StringItem.getItemList();
        SortUtil.sort(stringItems);
        for (StringItem val : stringItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ LOCALE STRING TEST ------------------------");
        stringItems = StringItem.getItemList();
        SortUtil.sort(stringItems, locale);
        for (StringItem val : stringItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ DESCENDING STRING TEST ------------------------");
        stringItems = StringItem.getItemList();
        SortUtil.sort(stringItems, OrderType.DESCENDING);
        for (StringItem val : stringItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ DESCENDING LOCAL STRING TEST ------------------------");
        stringItems = StringItem.getItemList();
        SortUtil.sort(stringItems, OrderType.DESCENDING, locale);
        for (StringItem val : stringItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ CUSTOM STRING TEST ------------------------");
        stringItems = StringItem.getItemList();
        SortUtil.sort(stringItems, new String[] { "customOrder" });
        for (StringItem val : stringItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ CUSTOM STRING LOCAL TEST ------------------------");
        stringItems = StringItem.getItemList();
        SortUtil.sort(stringItems, new String[] { "customOrder" }, locale);
        for (StringItem val : stringItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ CUSTOM DESCENDING METHOD STRING TEST ------------------------");
        stringItems = StringItem.getItemList();
        SortUtil.sort(stringItems, new String[] { "customOrder" }, OrderType.DESCENDING);
        for (StringItem val : stringItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ CUSTOM DESCENDING LOCAL METHOD STRING TEST ------------------------");
        stringItems = StringItem.getItemList();
        SortUtil.sort(stringItems, new String[] { "customOrder" }, OrderType.DESCENDING, locale);
        for (StringItem val : stringItems) {
            System.out.println(val.value);
        }
        System.out.println("**************************************************************************************");
        System.out.println("------------------------ DEFAULT DATE TEST ------------------------");
        List<DateItem> dateItems = DateItem.getItemList();
        SortUtil.sort(dateItems);
        for (DateItem val : dateItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ DESCENDING DATE TEST ------------------------");
        dateItems = DateItem.getItemList();
        SortUtil.sort(dateItems, OrderType.DESCENDING);
        for (DateItem val : dateItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ CUSTOM DATE TEST ------------------------");
        dateItems = DateItem.getItemList();
        SortUtil.sort(dateItems, new String[] { "customOrder" });
        for (DateItem val : dateItems) {
            System.out.println(val.value);
        }
        System.out.println("------------------------ CUSTOM DESCENDING DATE METHOD TEST ------------------------");
        dateItems = DateItem.getItemList();
        SortUtil.sort(dateItems, new String[] { "customOrder" }, OrderType.DESCENDING);
        for (DateItem val : dateItems) {
            System.out.println(val.value);
        }
    }
}
