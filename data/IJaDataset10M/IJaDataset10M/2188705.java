package metso.paradigma.core.dictionaries.test;

import org.junit.Assert;
import org.junit.Test;
import metso.paradigma.core.dictionaries.Dictionary;
import metso.paradigma.core.dictionaries.DictionaryHelper;

public class TestDictionaryHelper {

    @Test
    public void testGetDictionary() {
        DictionaryHelper.setBasePath("test");
        Dictionary dictionary = DictionaryHelper.getDictionary("reparti");
        Assert.assertNotNull(dictionary);
        Assert.assertEquals("reparti", dictionary.getId());
        Assert.assertEquals("Tipologie di reparto", dictionary.getName());
        Assert.assertNotNull(dictionary.values());
        Assert.assertEquals("Degenze ordinarie", dictionary.getValue("ordinarie"));
        Assert.assertEquals("Day Surgery", dictionary.getValue("daysurgery"));
        Dictionary dictionary2 = DictionaryHelper.getDictionary("qualifiche");
        Assert.assertNotNull(dictionary2);
        Assert.assertEquals("qualifiche", dictionary2.getId());
        Assert.assertEquals("Qualifiche abilitanti", dictionary2.getName());
        Assert.assertNotNull(dictionary2.values());
        Assert.assertEquals("Caposala", dictionary2.getValue("001"));
        Dictionary dictionary3 = DictionaryHelper.getDictionary("inquadramento");
        Assert.assertNotNull(dictionary3);
        Assert.assertEquals("inquadramento", dictionary3.getId());
        Assert.assertEquals("Inquadramento contrattuale", dictionary3.getName());
        Assert.assertNotNull(dictionary3.values());
        Assert.assertEquals("D(D1-D6))", dictionary3.getValue("I001"));
    }
}
