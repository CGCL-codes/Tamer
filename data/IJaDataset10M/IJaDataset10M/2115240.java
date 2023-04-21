package org.xmlvm.iphone;

import org.xmlvm.XMLVMSkeletonOnly;

@XMLVMSkeletonOnly
public class UITextFieldDelegate extends NSObject {

    public boolean textFieldShouldBeginEditing(UITextField textField) {
        return true;
    }

    public void textFieldDidBeginEditing(UITextField textField) {
    }

    public boolean textFieldShouldEndEditing(UITextField textField) {
        return true;
    }

    public void textFieldDidEndEditing(UITextField textField) {
    }

    public boolean textFieldShouldChangeCharactersInRange(UITextField textField, NSRange range, String replacementString) {
        return true;
    }

    public boolean textFieldShouldClear(UITextField textField) {
        return true;
    }

    public boolean textFieldShouldReturn(UITextField textField) {
        return true;
    }
}
