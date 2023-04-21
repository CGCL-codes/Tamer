package com.CompPad.OOO;

import com.sun.star.beans.XPropertySet;
import com.sun.star.text.XDependentTextField;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextRange;
import com.sun.star.uno.UnoRuntime;

/**
 * Openoffice representation of error field.  This class is specifically a text field Error.  Could
 * just as well implement other classes such as note error.
 *
 * @author Toby D. Rule
 */
public class OOOError extends OOOElement {

    XDependentTextField xDependentTextField;

    OOODocument oooDocument;

    OOOExpression oooExpression;

    XPropertySet errorFieldMaster;

    private String name;

    private Class[] valueTypes = { java.lang.Error.class, java.lang.Exception.class };

    public OOOError() {
    }

    public OOOError(OOODocument oooDocArg, OOOExpression oooExpArg) {
        xTextDocument = oooDocArg.getTextDocument();
        oooDocument = oooDocArg;
        oooExpression = oooExpArg;
    }

    public OOOError(OOODocument oooDocArg, XDependentTextField xTF) {
        oooDocument = oooDocArg;
        xTextDocument = oooDocArg.getTextDocument();
        xDependentTextField = xTF;
    }

    public void setOOOExpression(OOOExpression oooExpArg) {
        oooExpression = oooExpArg;
    }

    @Override
    public XTextRange getTextRange() {
        return ((XTextContent) UnoRuntime.queryInterface(XTextContent.class, xDependentTextField)).getAnchor();
    }

    @Override
    public String getName() throws Exception {
        try {
            XPropertySet xTFM = xDependentTextField.getTextFieldMaster();
            return (String) xTFM.getPropertyValue("Name");
        } catch (com.sun.star.uno.RuntimeException e) {
            return null;
        }
    }

    public void dispose() {
        if (xDependentTextField != null) {
            xDependentTextField.dispose();
        }
        oooExpression = null;
    }

    @Override
    public void handle(Object arg) throws Exception {
        String message;
        if (java.lang.Error.class.isInstance(arg)) {
            message = (((java.lang.Error) arg).getClass()) + ": " + ((java.lang.Error) arg).getMessage();
        } else if (java.lang.NullPointerException.class.isInstance(arg)) {
            message = ((java.lang.NullPointerException) arg).getClass().getName();
        } else if (java.lang.Exception.class.isInstance(arg)) {
            message = ((java.lang.Exception) arg).getMessage();
        } else {
            message = arg.toString();
        }
        if (xDependentTextField == null) {
            name = oooDocument.newErrorName();
            errorFieldMaster = oooDocument.newFieldMaster(name);
            XDependentTextField xTextField = oooDocument.createField(errorFieldMaster);
            XTextCursor xTextCursor = oooExpression.getTextCursor();
            xTextCursor.goRight((short) 1, false);
            xTextCursor.getText().insertTextContent(xTextCursor, xTextField, false);
        } else {
            errorFieldMaster = xDependentTextField.getTextFieldMaster();
        }
        errorFieldMaster.setPropertyValue("Content", message);
    }

    @Override
    public boolean canHandle(Object e) throws Exception {
        for (Class v : valueTypes) {
            if (v.isInstance(e)) {
                return true;
            }
        }
        return false;
    }

    public void setDependsOn(OOOExpression arg) {
        oooExpression = arg;
    }
}
