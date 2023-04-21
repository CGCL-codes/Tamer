package org.surveyforge.core.metadata;

import java.util.ArrayList;
import org.testng.annotations.ExpectedExceptions;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * @author jsegura
 */
public class VariableFamilyTest {

    @Test
    @ExpectedExceptions({ NullPointerException.class })
    public void variableFamilyCreationWithNullIdentifier() {
        new VariableFamily(null);
    }

    @Test
    @ExpectedExceptions({ NullPointerException.class })
    public void variableFamilyCreationWithEmptyIdentifier() {
        new VariableFamily("");
    }

    @Test
    public void variableFamilyCreation() {
        new VariableFamily("VariableFamily");
    }

    @Test
    public void variableFamilyGetIdentifier() {
        String id = "id";
        VariableFamily variableFamily = new VariableFamily(id);
        Assert.assertTrue(variableFamily.getIdentifier().equals(id));
        Assert.assertFalse(variableFamily.getIdentifier().equals("test"));
    }

    @Test
    @ExpectedExceptions({ NullPointerException.class })
    public void variableFamilySetNullDescription() {
        new VariableFamily("variableFamily").setDescription(null);
    }

    @Test
    public void variableFamilyGetDescription() {
        String desc = "desc";
        String empty = "";
        VariableFamily variableFamily = new VariableFamily("id");
        variableFamily.setDescription(desc);
        Assert.assertTrue(variableFamily.getDescription().equals(desc));
        Assert.assertFalse(variableFamily.getDescription().equals("test"));
        variableFamily.setDescription(empty);
        Assert.assertTrue(variableFamily.getDescription().equals(empty));
        Assert.assertFalse(variableFamily.getDescription().equals("test"));
    }

    @Test
    public void variableFamilyGetObjectVariables() {
        Assert.assertEquals(new ArrayList<GlobalVariable>(), new VariableFamily("id").getGlobalVariables());
        VariableFamily family = new VariableFamily("family");
        GlobalVariable gb1 = new GlobalVariable("gb1", family);
        GlobalVariable gb2 = new GlobalVariable("gb2", family);
        gb1.setVariableFamily(family);
        Assert.assertTrue(family.getGlobalVariables().contains(gb1));
        gb2.setVariableFamily(family);
        Assert.assertTrue(family.getGlobalVariables().contains(gb1));
        Assert.assertTrue(family.getGlobalVariables().contains(gb2));
        family.removeGlobalVariable(gb1);
        Assert.assertFalse(family.getGlobalVariables().contains(gb1));
        Assert.assertTrue(family.getGlobalVariables().contains(gb2));
    }

    @Test
    @ExpectedExceptions({ NullPointerException.class })
    public void variableFamilyAddNullGlobalVariable() {
        new VariableFamily("family").addGlobalVariable(null);
    }

    @Test
    @ExpectedExceptions({ NullPointerException.class })
    public void variableFamilyRemoveAddNullGlobalVariable() {
        new VariableFamily("family").removeGlobalVariable(null);
    }
}
