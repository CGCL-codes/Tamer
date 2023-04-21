package com.c2b2.ipoint.model.test;

import com.c2b2.ipoint.model.*;
import junit.framework.TestCase;

public class FormTestCase extends TestCase {

    public FormTestCase(String sTestName) {
        super(sTestName);
    }

    public void testCreate() throws PersistentModelException {
        String name = "name";
        String className = "className";
        String jsp = "JSP";
        int height = 100;
        int width = 200;
        String title = "title";
        Form form = Form.createForm(name, className, jsp, height, width, title);
        assertNotNull(form);
        ;
        assertEquals(className, form.getClassName());
        assertEquals(name, form.getName());
        assertEquals(jsp, form.getJspFile());
        assertEquals(height, form.getHeight());
        assertEquals(width, form.getWidth());
        assertEquals(title, form.getTitle());
        assertEquals(form, Form.getForm(name));
    }

    public void testInvalidCreate() throws PersistentModelException {
        String name = "name2";
        String className = "className";
        String jsp = "JSP";
        int height = 100;
        int width = 200;
        String title = "title";
        Form form = Form.createForm(name, className, jsp, height, width, title);
        assertNotNull(form);
        ;
        try {
            Form.createForm(name, className, jsp, height, width, title);
            fail("An exception should be thrown as a duplicate name was used");
        } catch (Exception ex) {
        }
    }

    public void testDelete() throws PersistentModelException {
        String name = "name3";
        String className = "className";
        String jsp = "JSP";
        int height = 100;
        int width = 200;
        String title = "title";
        Form form = Form.createForm(name, className, jsp, height, width, title);
        assertNotNull(form);
        ;
        form.delete();
        try {
            Form.getForm(name);
            fail("An exception should be thrown as a duplicate name was used");
        } catch (Exception ex) {
        }
    }
}
