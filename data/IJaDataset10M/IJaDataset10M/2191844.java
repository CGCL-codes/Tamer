package com.google.code.appengine.awt;

import javax.accessibility.AccessibleState;
import com.google.code.appengine.awt.TextField;
import com.google.code.appengine.awt.TextField.AccessibleAWTTextField;
import junit.framework.TestCase;

public class AccessibleAWTTextFieldTest extends TestCase {

    TextField textField;

    AccessibleAWTTextField aTextField;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        textField = new TextField();
        aTextField = textField.new AccessibleAWTTextField();
        assertTrue(textField.getAccessibleContext() instanceof AccessibleAWTTextField);
    }

    public void testGetAccessibleStateSet() {
        AccessibleState state = AccessibleState.SINGLE_LINE;
        assertTrue("text field is a single-line text", aTextField.getAccessibleStateSet().contains(state));
    }
}
