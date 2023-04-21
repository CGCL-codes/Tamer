package com.google.code.ptrends.entities;

import org.junit.Test;
import junit.framework.Assert;

public class ItemValueCollectionTest {

    @Test
    public void testContainsAndGettingItems() {
        ItemValueCollection ivc = new ItemValueCollection();
        ItemValueRecord ivr;
        ivr = new ItemValueRecord();
        ivr.setParametrID(1);
        ivr.setValue("first");
        ivc.add(ivr);
        ivr = new ItemValueRecord();
        ivr.setParametrID(2);
        ivr.setValue("second");
        ivc.add(ivr);
        ivr = new ItemValueRecord();
        ivr.setParametrID(3);
        ivr.setValue("third");
        ivc.add(ivr);
        Assert.assertTrue(ivc.haveParameter(1));
        Assert.assertTrue(ivc.haveParameter(2));
        Assert.assertTrue(ivc.haveParameter(3));
        Assert.assertFalse(ivc.haveParameter(0));
        ivr = ivc.getItemValueRecord(0);
        Assert.assertNull(ivr);
        ivr = ivc.getItemValueRecord(1);
        Assert.assertEquals("first", ivr.getValue());
    }

    @Test
    public void testEquals() {
        ItemValueCollection ivc = new ItemValueCollection();
        ItemValueRecord ivr;
        ivr = new ItemValueRecord();
        ivr.setParametrID(1);
        ivr.setValue("first");
        ivc.add(ivr);
        ivr = new ItemValueRecord();
        ivr.setParametrID(2);
        ivr.setValue("second");
        ivc.add(ivr);
        ivr = new ItemValueRecord();
        ivr.setParametrID(3);
        ivr.setValue("third");
        ivc.add(ivr);
        ItemValueCollection ivc2 = new ItemValueCollection();
        ivr = new ItemValueRecord();
        ivr.setParametrID(2);
        ivr.setValue("second");
        ivc2.add(ivr);
        ivr = new ItemValueRecord();
        ivr.setParametrID(3);
        ivr.setValue("third");
        ivc2.add(ivr);
        ivr = new ItemValueRecord();
        ivr.setParametrID(1);
        ivr.setValue("first");
        ivc2.add(ivr);
        Assert.assertTrue(ivc.equals(ivc2));
    }
}
