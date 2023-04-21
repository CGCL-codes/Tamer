package com.technoetic.xplanner.forms.test;

import junit.framework.TestCase;
import org.apache.struts.action.ActionErrors;
import com.technoetic.xplanner.forms.ReorderStoriesForm;

public class TestReorderStoriesForm extends TestCase {

    private ReorderStoriesForm reorderStoriesForm;

    protected void setUp() throws Exception {
        super.setUp();
        reorderStoriesForm = new ReorderStoriesForm();
    }

    public void testGetOrderNoAsInt() throws Exception {
        reorderStoriesForm.setOrderNo(0, "2.3");
        assertEquals(2, reorderStoriesForm.getOrderNoAsInt(0));
    }

    public void testValidate() throws Exception {
        reorderStoriesForm.setOrderNo(0, "x");
        ActionErrors actionErrors = reorderStoriesForm.validate(null, null);
        assertEquals(1, actionErrors.size());
    }
}
