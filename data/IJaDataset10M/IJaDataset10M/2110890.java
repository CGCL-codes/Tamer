package KFrameWork.FieldValidators;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.Toolkit;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;
import KFrameWork.Base.*;

public class currencyFieldValidatorClass1 extends DefaultStyledDocument {

    int maxNumbers;

    String fieldName;

    /** Creates new limitedNumbersDocumentClass */
    public currencyFieldValidatorClass1(int maxNumbersParam, String fieldNameParam) {
        maxNumbers = maxNumbersParam;
        fieldName = fieldNameParam;
    }

    @Override
    public void insertString(int offset, String data, AttributeSet attributeSet) throws BadLocationException {
        if ((getLength() + data.length()) > maxNumbers) {
            if (data.length() == 1) {
                Toolkit.getDefaultToolkit().beep();
            } else {
                KMetaUtilsClass.showErrorMessageFromText1(null, "*** Could not insert data in " + fieldName + maxNumbers + " ***" + " Text is too long for number field.");
            }
            return;
        }
        super.insertString(offset, data, attributeSet);
    }
}
