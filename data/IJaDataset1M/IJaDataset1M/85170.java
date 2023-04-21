package net.sourceforge.argval.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testConvertToSet_First() {
        String[] textArray = new String[] { "Geweigerd", "Geaccepteerd", "Bekeken", "Getoond", "Geinteresseerd" };
        String text = "|Geweigerd|Geaccepteerd|Bekeken|Getoond|Geinteresseerd";
        Collection<String> coll = StringUtil.convertToSet(text, "|");
        Assert.assertEquals("Both textArray and the Collection instance should have the same number of items.", textArray.length, coll.size());
        for (int index = 0; index < textArray.length; index++) {
            Assert.assertTrue(coll.contains(textArray[index]));
        }
    }

    @Test
    public void testConvertToSet_Second() {
        String[] textArray = new String[] { "geen interesse", "winkel of internet", "aanbod is te duur", "heeft al", "wil opzeggen", "wil meer informatie", "klant belt terug", "wil offerte", "vraag/klacht oplossen", "overig" };
        String text = "geen interesse|winkel of internet|aanbod is te duur|heeft al|wil opzeggen|wil meer informatie|" + "klant belt terug|wil offerte|vraag/klacht oplossen|overig";
        Collection<String> coll = StringUtil.convertToSet(text, "|");
        Assert.assertEquals("Both textArray and the Collection instance should have the same number of items.", textArray.length, coll.size());
        for (int index = 0; index < textArray.length; index++) {
            Assert.assertTrue(coll.contains(textArray[index]));
        }
    }

    @Test
    public void testConvertToList_String() {
        String text = "not dots in this one";
        List<String> convertToList = StringUtil.convertToList(text, ".");
        Assert.assertEquals(text, convertToList.get(0));
    }

    @Test
    public void testRemoveCharacter() {
        Assert.assertEquals("The dashs characters '-' should have been removed.", "ISBN: 0596100949", StringUtil.removeCharacter("ISBN: 0-596-10094-9", '-'));
    }

    @Test
    public void testRemoveCharacters() {
        Assert.assertEquals("The following characters { '-', ':' } should have been removed.", "ISBN 0596100949", StringUtil.removeCharacter("ISBN: 0-596-10094-9", new char[] { '-', ':' }));
    }

    @Test
    public void testCharCount() {
        Assert.assertEquals(0, StringUtil.charCount("This is a text", '.'));
        Assert.assertEquals(3, StringUtil.charCount("This is a text", ' '));
        Assert.assertEquals(2, StringUtil.charCount("*This is a text*", '*'));
    }

    @Test
    public void testGetCharIndexList() {
        Assert.assertEquals(new ArrayList<Integer>(), StringUtil.getCharIndexList("This is a text", '.'));
        List<Integer> indexList = new ArrayList<Integer>();
        indexList.add(new Integer(4));
        indexList.add(new Integer(7));
        indexList.add(new Integer(9));
        Assert.assertEquals(indexList, StringUtil.getCharIndexList("This is a text", ' '));
        indexList.add(new Integer(14));
        Assert.assertEquals(indexList, StringUtil.getCharIndexList("This is a text ", ' '));
        indexList = new ArrayList<Integer>();
        indexList.add(new Integer(0));
        indexList.add(new Integer(5));
        indexList.add(new Integer(8));
        indexList.add(new Integer(10));
        Assert.assertEquals(indexList, StringUtil.getCharIndexList(" This is a text", ' '));
        indexList.add(new Integer(15));
        Assert.assertEquals(indexList, StringUtil.getCharIndexList(" This is a text ", ' '));
        String text = "*This is a text*";
        indexList = new ArrayList<Integer>();
        indexList.add(new Integer(0));
        indexList.add(new Integer(text.length() - 1));
        Assert.assertEquals(indexList, StringUtil.getCharIndexList(text, '*'));
    }
}
