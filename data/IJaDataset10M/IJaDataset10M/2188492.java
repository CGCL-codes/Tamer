package Tests;

import org.junit.Assert;
import org.junit.Test;
import GUI.GUIChairMaintenance.AssignClass;
import Logic.LogicChairMaintenance.Chair.Chair;

public class TestAssignClass {

    @Test
    public void testAssignClass() {
        AssignClass test = new AssignClass("null", null);
        Assert.assertNotNull(test);
    }

    @Test
    public void testTestPrivateMethods() {
        Chair chairTest = new Chair();
        AssignClass test = new AssignClass("3", chairTest);
        Assert.assertEqualsTrue(test.testPrivateMethods());
    }
}
