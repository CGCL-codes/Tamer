package com.lubq.lm.bestwiz.order.ui.view.imp;

import org.eclipse.swt.custom.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import java.io.*;
import java.lang.reflect.*;

public class StyledTextContentSpec implements TextChangeListener {

    static String contentClassName;

    static int failCount = 0;

    static int errorCount = 0;

    Class contentClass = null;

    StyledTextContent contentInstance = null;

    int verify = 0;

    Method currentMethod = null;

    boolean failed = false;

    StyledText widget = null;

    Shell shell = null;

    public StyledTextContentSpec() {
    }

    public void assertDo(String message, boolean condition) {
        System.out.print("\t" + currentMethod.getName() + " " + message);
        if (!condition) fail(message); else System.out.println(" passed");
    }

    public void fail(String message) {
        failed = true;
        System.out.println(" FAILED");
        failCount++;
    }

    public StyledTextContent getContentInstance() {
        contentInstance.setText("");
        widget.setContent(contentInstance);
        return contentInstance;
    }

    public static String getTestText() {
        return "This is the first line.\r\n" + "This is the second line.\r\n" + "This is the third line.\r\n" + "This is the fourth line.\r\n" + "This is the fifth line.\r\n" + "\r\n" + "This is the first line again.\r\n" + "This is the second line again.\r\n" + "This is the third line again.\r\n" + "This is the fourth line again.\r\n" + "This is the fifth line again.\r\n" + "\r\n" + "This is the first line once again.\r\n" + "This is the second line once again.\r\n" + "This is the third line once again.\r\n" + "This is the fourth line once again.\r\n" + "This is the fifth line once again.";
    }

    public static void main(String[] args) {
        StyledTextContentSpec spec = new StyledTextContentSpec();
        if (args.length > 0) {
            contentClassName = args[0];
        } else {
            MessageBox box = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR);
            box.setMessage("Content class must be specified as an execution argument.");
            box.open();
            return;
        }
        spec.run();
        System.out.println();
        System.out.println(failCount + " TEST FAILURES.");
        System.out.println(errorCount + " UNEXPECTED ERRORS.");
    }

    public void run() {
        if (contentClassName.equals("")) {
            MessageBox box = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR);
            box.setMessage("Content class must be specified as an execution argument.");
            box.open();
            return;
        }
        if (contentClass == null) {
            try {
                contentClass = Class.forName(contentClassName);
            } catch (ClassNotFoundException e) {
                MessageBox box = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR);
                box.setMessage("Content class:\n" + contentClassName + "\nnot found");
                box.open();
                return;
            }
        }
        try {
            contentInstance = (StyledTextContent) contentClass.newInstance();
        } catch (IllegalAccessException e) {
            MessageBox box = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR);
            box.setMessage("Unable to access content class:\n" + contentClassName);
            box.open();
            return;
        } catch (InstantiationException e) {
            MessageBox box = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR);
            box.setMessage("Unable to instantiate content class:\n" + contentClassName);
            box.open();
            return;
        }
        Class clazz;
        clazz = this.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            setUp();
            currentMethod = methods[i];
            failed = false;
            try {
                if (currentMethod.getName().startsWith("test_")) {
                    System.out.println();
                    System.out.println(currentMethod.getName() + "...");
                    currentMethod.invoke(this, new Object[0]);
                    if (!failed) {
                        System.out.println("PASSED.");
                    } else {
                        System.out.println("FAILED");
                    }
                }
            } catch (InvocationTargetException ex) {
                System.out.println("\t" + currentMethod.getName() + " ERROR ==> " + ex.getTargetException().toString());
                System.out.println("FAILED");
                errorCount++;
            } catch (Exception ex) {
                System.out.println("\t" + currentMethod.getName() + " ERROR ==> " + ex.toString());
                System.out.println("FAILED");
                errorCount++;
            }
            if (verify != 0) {
                verify = 0;
                contentInstance.removeTextChangeListener(this);
            }
            tearDown();
        }
    }

    public void textSet(TextChangedEvent event) {
    }

    public void textChanged(TextChangedEvent event) {
    }

    public void textChanging(TextChangingEvent event) {
        switch(verify) {
            case 1:
                {
                    assertDo(":1a:", event.replaceLineCount == 0);
                    assertDo(":1b:", event.newLineCount == 1);
                    break;
                }
            case 2:
                {
                    assertDo(":2a:", event.replaceLineCount == 2);
                    assertDo(":2b:", event.newLineCount == 0);
                    break;
                }
            case 3:
                {
                    assertDo(":3a:", event.replaceLineCount == 0);
                    assertDo(":3b:", event.newLineCount == 2);
                    break;
                }
            case 4:
                {
                    assertDo(":4:", false);
                    break;
                }
            case 5:
                {
                    assertDo(":5a:", event.replaceLineCount == 0);
                    assertDo(":5b:", event.newLineCount == 1);
                    break;
                }
            case 6:
                {
                    assertDo(":6a:", event.replaceLineCount == 1);
                    assertDo(":6b:", event.newLineCount == 0);
                    break;
                }
            case 8:
                {
                    assertDo(":8a:", event.replaceLineCount == 1);
                    assertDo(":8b:", event.newLineCount == 0);
                    break;
                }
            case 9:
                {
                    assertDo(":9a:", event.replaceLineCount == 1);
                    assertDo(":9b:", event.newLineCount == 0);
                    break;
                }
            case 10:
                {
                    assertDo(":10:", false);
                    break;
                }
            case 11:
                {
                    assertDo(":11:", false);
                    break;
                }
            case 12:
                {
                    assertDo(":12a:", event.replaceLineCount == 0);
                    assertDo(":12b:", event.newLineCount == 1);
                    break;
                }
            case 13:
                {
                    assertDo(":13a:", event.replaceLineCount == 0);
                    assertDo(":13b:", event.newLineCount == 1);
                    break;
                }
            case 14:
                {
                    assertDo(":14:", false);
                    break;
                }
            case 15:
                {
                    assertDo(":15a:", event.replaceLineCount == 1);
                    assertDo(":15b:", event.newLineCount == 2);
                    break;
                }
            case 16:
                {
                    assertDo(":16:", false);
                    break;
                }
            case 17:
                {
                    assertDo(":17:", false);
                    break;
                }
            case 18:
                {
                    assertDo(":18a:", event.replaceLineCount == 0);
                    assertDo(":18b:", event.newLineCount == 2);
                    break;
                }
            case 19:
                {
                    assertDo(":19a:", event.replaceLineCount == 0);
                    assertDo(":19b:", event.newLineCount == 3);
                    break;
                }
            case 20:
                {
                    assertDo(":20:", false);
                    break;
                }
        }
    }

    public void test_Insert() {
        StyledTextContent content = getContentInstance();
        String newText;
        content.setText("This\nis a test\r");
        content.replaceTextRange(0, 0, "test\n ");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":1a:", newText.equals("test\n This\nis a test\r"));
        assertDo(":1b:", content.getLineCount() == 4);
        assertDo(":1c:", content.getLine(0).equals("test"));
        assertDo(":1d:", content.getLine(1).equals(" This"));
        assertDo(":1e:", content.getLine(2).equals("is a test"));
        assertDo(":1f:", content.getLine(3).equals(""));
        content.setText("This\nis a test\r");
        content.replaceTextRange(5, 0, "*** ");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":2a:", newText.equals("This\n*** is a test\r"));
        assertDo(":2b:", content.getLineCount() == 3);
        assertDo(":2c:", content.getLine(0).equals("This"));
        assertDo(":2d:", content.getLine(1).equals("*** is a test"));
        assertDo(":2e:", content.getLine(2).equals(""));
        content.setText("Line 1\r\nLine 2");
        content.replaceTextRange(0, 0, "\r");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":3a:", newText.equals("\rLine 1\r\nLine 2"));
        assertDo(":3b:", content.getLineCount() == 3);
        assertDo(":3c:", content.getLine(0).equals(""));
        assertDo(":3d:", content.getLine(1).equals("Line 1"));
        assertDo(":3e:", content.getLine(2).equals("Line 2"));
        content.replaceTextRange(9, 0, "\r");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":3f:", newText.equals("\rLine 1\r\n\rLine 2"));
        assertDo(":3g:", content.getLineCount() == 4);
        assertDo(":3h:", content.getLine(0).equals(""));
        assertDo(":3i:", content.getLine(1).equals("Line 1"));
        assertDo(":3j:", content.getLine(2).equals(""));
        assertDo(":3k:", content.getLine(3).equals("Line 2"));
        content.setText("This\nis a test\r");
        content.replaceTextRange(0, 0, "\n");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":4a:", newText.equals("\nThis\nis a test\r"));
        assertDo(":4b:", content.getLineCount() == 4);
        assertDo(":4c:", content.getLine(0).equals(""));
        assertDo(":4d:", content.getLine(1).equals("This"));
        assertDo(":4e:", content.getLine(2).equals("is a test"));
        assertDo(":4f:", content.getLine(3).equals(""));
        content.setText("This\nis a test\r");
        content.replaceTextRange(7, 0, "\r\nnewLine");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":5a:", newText.equals("This\nis\r\nnewLine a test\r"));
        assertDo(":5b:", content.getLineCount() == 4);
        assertDo(":5c:", content.getLine(0).equals("This"));
        assertDo(":5d:", content.getLine(1).equals("is"));
        assertDo(":5e:", content.getLine(2).equals("newLine a test"));
        assertDo(":5f:", content.getLine(3).equals(""));
        content.setText("");
        content.replaceTextRange(0, 0, "This\nis\r\nnewLine a test\r");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":6a:", newText.equals("This\nis\r\nnewLine a test\r"));
        assertDo(":6b:", content.getLineCount() == 4);
        assertDo(":6c:", content.getLine(0).equals("This"));
        assertDo(":6d:", content.getLine(1).equals("is"));
        assertDo(":6e:", content.getLine(2).equals("newLine a test"));
        assertDo(":6f:", content.getLine(3).equals(""));
        content.setText("This");
        content.replaceTextRange(4, 0, "\n ");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":7a:", newText.equals("This\n "));
        assertDo(":7b:", content.getLineCount() == 2);
        assertDo(":7c:", content.getLine(0).equals("This"));
        assertDo(":7d:", content.getLine(1).equals(" "));
        content.setText("This\n");
        content.replaceTextRange(5, 0, "\n");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":7e:", newText.equals("This\n\n"));
        assertDo(":7f:", content.getLineCount() == 3);
        assertDo(":7g:", content.getLine(0).equals("This"));
        assertDo(":7h:", content.getLine(1).equals(""));
        assertDo(":7i:", content.getLine(2).equals(""));
        content.setText("This");
        content.replaceTextRange(0, 0, "\n");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":8a:", newText.equals("\nThis"));
        assertDo(":8b:", content.getLineCount() == 2);
        assertDo(":8c:", content.getLine(0).equals(""));
        assertDo(":8d:", content.getLine(1).equals("This"));
        content.setText("This\nis a test\r");
        content.replaceTextRange(5, 0, "*** ");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":9a:", newText.equals("This\n*** is a test\r"));
        assertDo(":9b:", content.getLineCount() == 3);
        assertDo(":9c:", content.getLine(0).equals("This"));
        assertDo(":9d:", content.getLine(1).equals("*** is a test"));
        assertDo(":9e:", content.getLine(2).equals(""));
        content.setText("This\n");
        content.replaceTextRange(5, 0, "line");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":10a:", newText.equals("This\nline"));
        assertDo(":10b:", content.getLineCount() == 2);
        assertDo(":10c:", content.getLine(0).equals("This"));
        assertDo(":10d:", content.getLine(1).equals("line"));
        assertDo(":10e:", content.getLineAtOffset(8) == 1);
        assertDo(":10f:", content.getLineAtOffset(9) == 1);
        content.setText("This\n");
        content.replaceTextRange(0, 0, "line\n");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":11a:", newText.equals("line\nThis\n"));
        assertDo(":11b:", content.getLineCount() == 3);
        assertDo(":11c:", content.getLine(0).equals("line"));
        assertDo(":11d:", content.getLine(1).equals("This"));
        assertDo(":11e:", content.getLineAtOffset(5) == 1);
        content.setText("Line 1\r\nLine 2\r\nLine 3");
        content.replaceTextRange(0, 0, "\r");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":12a:", newText.equals("\rLine 1\r\nLine 2\r\nLine 3"));
        assertDo(":12b:", content.getLineCount() == 4);
        assertDo(":12c:", content.getLine(0).equals(""));
        assertDo(":12d:", content.getLine(1).equals("Line 1"));
        assertDo(":12e:", content.getLine(2).equals("Line 2"));
        assertDo(":12f:", content.getLine(3).equals("Line 3"));
        content.setText("Line 1\nLine 2\nLine 3");
        content.replaceTextRange(7, 0, "Line1a\nLine1b\n");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":13a:", newText.equals("Line 1\nLine1a\nLine1b\nLine 2\nLine 3"));
        assertDo(":13b:", content.getLineCount() == 5);
        assertDo(":13c:", content.getLine(0).equals("Line 1"));
        assertDo(":13d:", content.getLine(1).equals("Line1a"));
        assertDo(":13e:", content.getLine(2).equals("Line1b"));
        assertDo(":13f:", content.getLine(3).equals("Line 2"));
        assertDo(":13g:", content.getLine(4).equals("Line 3"));
        content.setText("Line 1\nLine 2\nLine 3");
        content.replaceTextRange(11, 0, "l1a");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":14a:", newText.equals("Line 1\nLinel1a 2\nLine 3"));
        assertDo(":14b:", content.getLineCount() == 3);
        assertDo(":14c:", content.getLine(0).equals("Line 1"));
        assertDo(":14d:", content.getLine(1).equals("Linel1a 2"));
        assertDo(":14e:", content.getLine(2).equals("Line 3"));
        content.setText("Line 1\nLine 2 is a very long line that spans many words\nLine 3");
        content.replaceTextRange(19, 0, "very, very, ");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":15a:", newText.equals("Line 1\nLine 2 is a very, very, very long line that spans many words\nLine 3"));
        assertDo(":15b:", content.getLineCount() == 3);
        assertDo(":15c:", content.getLine(0).equals("Line 1"));
        assertDo(":15d:", content.getLine(1).equals("Line 2 is a very, very, very long line that spans many words"));
        assertDo(":15e:", content.getLine(2).equals("Line 3"));
    }

    public void test_Empty() {
        StyledTextContent content = getContentInstance();
        assertDo(":1a:", content.getLineCount() == 1);
        assertDo(":1b:", content.getLine(0).equals(""));
        content.setText("test");
        content.replaceTextRange(0, 4, "");
        assertDo(":2a:", content.getLineCount() == 1);
        assertDo(":2b:", content.getLine(0).equals(""));
    }

    public void test_Line_Conversion() {
        StyledTextContent content = getContentInstance();
        content.setText("This\nis a test\rrepeat\nend\r");
        assertDo(":1a:", content.getLineCount() == 5);
        assertDo(":1b:", content.getLine(0).equals("This"));
        assertDo(":1c:", content.getOffsetAtLine(0) == 0);
        assertDo(":1d:", content.getLine(1).equals("is a test"));
        assertDo(":1e:", content.getLineAtOffset(4) == 0);
        assertDo(":1f:", content.getOffsetAtLine(1) == 5);
        assertDo(":1g:", content.getLine(2).equals("repeat"));
        assertDo(":1h:", content.getOffsetAtLine(2) == 15);
        assertDo(":1i:", content.getLine(3).equals("end"));
        assertDo(":1j:", content.getOffsetAtLine(3) == 22);
        assertDo(":1k:", content.getLine(4).equals(""));
        assertDo(":1l:", content.getOffsetAtLine(4) == 26);
        content.setText("This\r\nis a test");
        assertDo(":2a:", content.getLineCount() == 2);
        assertDo(":2b:", content.getLine(1).equals("is a test"));
        assertDo(":2c:", content.getLineAtOffset(4) == 0);
        assertDo(":2d:", content.getLineAtOffset(5) == 0);
        content.setText("This\r\nis a test\r");
        assertDo(":3a:", content.getLineCount() == 3);
        assertDo(":3b:", content.getLine(1).equals("is a test"));
        assertDo(":3c:", content.getLineAtOffset(15) == 1);
        content.setText("\r\n");
        assertDo(":4a:", content.getLineCount() == 2);
        assertDo(":4b:", content.getLine(0).equals(""));
        assertDo(":4c:", content.getLine(1).equals(""));
        assertDo(":4d:", content.getLineAtOffset(0) == 0);
        assertDo(":4e:", content.getLineAtOffset(1) == 0);
        assertDo(":4f:", content.getLineAtOffset(2) == 1);
        content.setText("\r\n\n\r\r\n");
        assertDo(":5a:", content.getLineCount() == 5);
        assertDo(":5b:", content.getLine(0).equals(""));
        assertDo(":5c:", content.getOffsetAtLine(0) == 0);
        assertDo(":5d:", content.getLine(1).equals(""));
        assertDo(":5e:", content.getOffsetAtLine(1) == 2);
        assertDo(":5f:", content.getLine(2).equals(""));
        assertDo(":5g:", content.getOffsetAtLine(2) == 3);
        assertDo(":5h:", content.getLine(3).equals(""));
        assertDo(":5i:", content.getOffsetAtLine(3) == 4);
        assertDo(":5j:", content.getLine(4).equals(""));
        assertDo(":5k:", content.getOffsetAtLine(4) == 6);
        content.setText("test\r\rtest2\r\r");
        assertDo(":6a:", content.getLineCount() == 5);
        assertDo(":6b:", content.getLine(0).equals("test"));
        assertDo(":6c:", content.getOffsetAtLine(0) == 0);
        assertDo(":6d:", content.getLine(1).equals(""));
        assertDo(":6e:", content.getOffsetAtLine(1) == 5);
        assertDo(":6f:", content.getLine(2).equals("test2"));
        assertDo(":6g:", content.getOffsetAtLine(2) == 6);
        assertDo(":6h:", content.getLine(3).equals(""));
        assertDo(":6i:", content.getOffsetAtLine(3) == 12);
        assertDo(":6j:", content.getLine(4).equals(""));
        assertDo(":6k:", content.getOffsetAtLine(4) == 13);
    }

    public void test_Offset_To_Line() {
        StyledTextContent content = getContentInstance();
        content.setText("This\nis a test\rrepeat\nend\r");
        assertDo(":1a:", content.getLineAtOffset(0) == 0);
        assertDo(":1b:", content.getLineAtOffset(3) == 0);
        assertDo(":1c:", content.getLineAtOffset(4) == 0);
        assertDo(":1d:", content.getLineAtOffset(25) == 3);
        assertDo(":1e:", content.getLineAtOffset(26) == 4);
        content.setText("This\r\nis a test");
        assertDo(":2a:", content.getLineAtOffset(5) == 0);
        assertDo(":2b:", content.getLineAtOffset(6) == 1);
        assertDo(":2c:", content.getLineAtOffset(10) == 1);
        content.setText("\r\n");
        assertDo(":3a:", content.getLineAtOffset(0) == 0);
        assertDo(":3b:", content.getLineAtOffset(1) == 0);
        assertDo(":3c:", content.getLineAtOffset(2) == 1);
        content.setText("\r\n\n\r\r\n");
        assertDo(":4a:", content.getLineAtOffset(0) == 0);
        assertDo(":4b:", content.getLineAtOffset(1) == 0);
        assertDo(":4c:", content.getLineAtOffset(2) == 1);
        assertDo(":4d:", content.getLineAtOffset(3) == 2);
        assertDo(":4e:", content.getLineAtOffset(4) == 3);
        assertDo(":4f:", content.getLineAtOffset(5) == 3);
        assertDo(":4g:", content.getLineAtOffset(6) == 4);
        content.setText("\r\n\r\n");
        assertDo(":5a:", content.getLineAtOffset(0) == 0);
        assertDo(":5b:", content.getLineAtOffset(1) == 0);
        assertDo(":5c:", content.getLineAtOffset(2) == 1);
        assertDo(":5d:", content.getLineAtOffset(3) == 1);
        assertDo(":5e:", content.getLineAtOffset(4) == 2);
        content.setText("\r\r\r\n\r\n");
        assertDo(":6a:", content.getLineAtOffset(0) == 0);
        assertDo(":6b:", content.getLineAtOffset(1) == 1);
        assertDo(":6c:", content.getLineAtOffset(2) == 2);
        assertDo(":6d:", content.getLineAtOffset(4) == 3);
        content.setText("");
        assertDo(":7a:", content.getLineAtOffset(0) == 0);
        content = getContentInstance();
        assertDo(":8a:", content.getLineAtOffset(0) == 0);
    }

    public void test_Line_To_Offset() {
        StyledTextContent content = getContentInstance();
        content.setText("This\nis a test\rrepeat\nend\r");
        assertDo(":1a:", content.getOffsetAtLine(0) == 0);
        assertDo(":1b:", content.getOffsetAtLine(1) == 5);
        assertDo(":1c:", content.getOffsetAtLine(2) == 15);
        assertDo(":1d:", content.getOffsetAtLine(3) == 22);
        assertDo(":1e:", content.getOffsetAtLine(4) == 26);
        content.setText("This\r\nis a test");
        assertDo(":2a:", content.getOffsetAtLine(0) == 0);
        assertDo(":2b:", content.getOffsetAtLine(1) == 6);
        content.setText("\r\n");
        assertDo(":3a:", content.getOffsetAtLine(0) == 0);
        assertDo(":3b:", content.getOffsetAtLine(1) == 2);
        content.setText("\r\n\n\r\r\n");
        assertDo(":4a:", content.getOffsetAtLine(0) == 0);
        assertDo(":4b:", content.getOffsetAtLine(1) == 2);
        assertDo(":4c:", content.getOffsetAtLine(2) == 3);
        assertDo(":4d:", content.getOffsetAtLine(3) == 4);
        assertDo(":4e:", content.getOffsetAtLine(4) == 6);
        content.setText("\r\ntest\r\n");
        assertDo(":5a:", content.getOffsetAtLine(0) == 0);
        assertDo(":5b:", content.getOffsetAtLine(1) == 2);
        assertDo(":5c:", content.getOffsetAtLine(2) == 8);
    }

    public void test_Delete() {
        StyledTextContent content = getContentInstance();
        String newText;
        content.setText("This\nis a test\r");
        content.replaceTextRange(6, 2, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":1a:", newText.equals("This\nia test\r"));
        assertDo(":1b:", content.getLine(0).equals("This"));
        assertDo(":1c:", content.getLine(1).equals("ia test"));
        content.setText("This\nis a test\r");
        content.replaceTextRange(5, 9, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":2a:", newText.equals("This\n\r"));
        assertDo(":2b:", content.getLineCount() == 3);
        assertDo(":2c:", content.getLine(0).equals("This"));
        assertDo(":2d:", content.getLine(1).equals(""));
        assertDo(":2e:", content.getLine(2).equals(""));
        content.setText("This\nis a test\nline 3\nline 4");
        content.replaceTextRange(21, 7, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":3a:", newText.equals("This\nis a test\nline 3"));
        assertDo(":3b:", content.getLineCount() == 3);
        assertDo(":3c:", content.getLine(0).equals("This"));
        assertDo(":3d:", content.getLine(1).equals("is a test"));
        assertDo(":3e:", content.getLine(2).equals("line 3"));
        content.setText("This\nis a test\nline 3\nline 4");
        content.replaceTextRange(0, 5, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":4a:", newText.equals("is a test\nline 3\nline 4"));
        assertDo(":4b:", content.getLineCount() == 3);
        assertDo(":4c:", content.getLine(0).equals("is a test"));
        assertDo(":4d:", content.getLine(1).equals("line 3"));
        assertDo(":4e:", content.getLine(2).equals("line 4"));
        content.replaceTextRange(16, 7, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":4f:", newText.equals("is a test\nline 3"));
        assertDo(":4g:", content.getLine(0).equals("is a test"));
        assertDo(":4h:", content.getLine(1).equals("line 3"));
        content.replaceTextRange(9, 7, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":4i:", newText.equals("is a test"));
        assertDo(":4j:", content.getLine(0).equals("is a test"));
        content.replaceTextRange(1, 8, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":4k:", newText.equals("i"));
        assertDo(":4l:", content.getLine(0).equals("i"));
        content.replaceTextRange(0, 1, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":4m:", newText.equals(""));
        assertDo(":4n:", content.getLine(0).equals(""));
        content.setText("This\nis a test\r");
        content.replaceTextRange(5, 9, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":5a:", newText.equals("This\n\r"));
        assertDo(":5b:", content.getLineCount() == 3);
        assertDo(":5c:", content.getLine(0).equals("This"));
        assertDo(":5d:", content.getLine(1).equals(""));
        assertDo(":5e:", content.getLine(2).equals(""));
        content.setText("L1\r\nL2\r\nL3\r\nL4\r\n");
        content.replaceTextRange(4, 8, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":6a:", newText.equals("L1\r\nL4\r\n"));
        assertDo(":6b:", content.getLineCount() == 3);
        assertDo(":6c:", content.getLine(0).equals("L1"));
        assertDo(":6d:", content.getLine(1).equals("L4"));
        assertDo(":6e:", content.getLine(2).equals(""));
        content.setText("\nL1\r\nL2");
        content.replaceTextRange(0, 1, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":7a:", newText.equals("L1\r\nL2"));
        assertDo(":7b:", content.getLineCount() == 2);
        assertDo(":7c:", content.getLine(0).equals("L1"));
        assertDo(":7d:", content.getLine(1).equals("L2"));
        content.setText("\nL1\r\nL2\r\n");
        content.replaceTextRange(7, 2, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":8a:", newText.equals("\nL1\r\nL2"));
        assertDo(":8b:", content.getLineCount() == 3);
        assertDo(":8c:", content.getLine(0).equals(""));
        assertDo(":8d:", content.getLine(1).equals("L1"));
        assertDo(":8e:", content.getLine(2).equals("L2"));
        content.setText("\nLine 1\nLine 2\n");
        content.replaceTextRange(0, 7, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":9a:", newText.equals("\nLine 2\n"));
        assertDo(":9b:", content.getLineCount() == 3);
        assertDo(":9c:", content.getLine(0).equals(""));
        assertDo(":9d:", content.getLine(1).equals("Line 2"));
        assertDo(":9e:", content.getLine(2).equals(""));
        content.setText("Line 1\nLine 2\n");
        content.replaceTextRange(6, 8, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":10a:", newText.equals("Line 1"));
        assertDo(":10b:", content.getLineCount() == 1);
        assertDo(":10c:", content.getLine(0).equals("Line 1"));
        content.setText("Line one is short\r\nLine 2 is a longer line\r\nLine 3\n");
        content.replaceTextRange(12, 17, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":11a:", newText.equals("Line one is a longer line\r\nLine 3\n"));
        assertDo(":11b:", content.getLineCount() == 3);
        assertDo(":11c:", content.getLine(0).equals("Line one is a longer line"));
        assertDo(":11d:", content.getLine(1).equals("Line 3"));
        assertDo(":11e:", content.getLine(2).equals(""));
    }

    public void test_Replace() {
        StyledTextContent content = getContentInstance();
        String newText;
        content.setText("This\nis a test\r");
        content.replaceTextRange(5, 4, "a");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":1a:", newText.equals("This\na test\r"));
        assertDo(":1b:", content.getLineCount() == 3);
        assertDo(":1c:", content.getLine(0).equals("This"));
        assertDo(":1d:", content.getLine(1).equals("a test"));
        assertDo(":1e:", content.getLine(2).equals(""));
        content.setText("This\nis a test\r");
        content.replaceTextRange(5, 2, "was");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":2a:", newText.equals("This\nwas a test\r"));
        assertDo(":2b:", content.getLineCount() == 3);
        assertDo(":2c:", content.getLine(0).equals("This"));
        assertDo(":2d:", content.getLine(1).equals("was a test"));
        assertDo(":2e:", content.getLine(2).equals(""));
        content.setText("This is a test\r");
        content.replaceTextRange(5, 2, "was");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":3a:", newText.equals("This was a test\r"));
        assertDo(":3b:", content.getLineCount() == 2);
        assertDo(":3c:", content.getLine(0).equals("This was a test"));
        assertDo(":3d:", content.getLineAtOffset(15) == 0);
        content.setText("Line 1\nLine 2\nLine 3");
        content.replaceTextRange(0, 7, "La\nLb\n");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":4a:", newText.equals("La\nLb\nLine 2\nLine 3"));
        assertDo(":4b:", content.getLine(0).equals("La"));
        assertDo(":4c:", content.getLine(1).equals("Lb"));
        assertDo(":4d:", content.getLine(2).equals("Line 2"));
        assertDo(":4e:", content.getLine(3).equals("Line 3"));
        content.setText(getTestText());
        newText = content.getTextRange(0, content.getCharCount());
        int start = content.getOffsetAtLine(6);
        int end = content.getOffsetAtLine(11);
        content.replaceTextRange(start, end - start, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":5a:", content.getLineCount() == 12);
        assertDo(":5a:", content.getLine(5).equals(""));
        assertDo(":5a:", content.getLine(6).equals(""));
        start = content.getOffsetAtLine(7);
        content.replaceTextRange(start, content.getCharCount() - start, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":5a:", content.getLineCount() == 8);
        assertDo(":5a:", content.getLine(5).equals(""));
        assertDo(":5a:", content.getLine(6).equals(""));
        assertDo(":5a:", content.getLine(7).equals(""));
    }

    public void test_Special_Cases() {
        String newText;
        StyledTextContent content = getContentInstance();
        assertDo(":0a:", content.getLineCount() == 1);
        assertDo(":0b:", content.getOffsetAtLine(0) == 0);
        content.setText("This is the input/output text component.");
        content.replaceTextRange(0, 0, "\n");
        assertDo(":1a:", content.getLine(0).equals(""));
        content.replaceTextRange(1, 0, "\n");
        assertDo(":1b:", content.getLine(0).equals(""));
        content.replaceTextRange(2, 0, "\n");
        assertDo(":1c:", content.getLine(0).equals(""));
        content.replaceTextRange(3, 0, "\n");
        assertDo(":1d:", content.getLine(0).equals(""));
        content.replaceTextRange(4, 0, "\n");
        assertDo(":1e:", content.getLine(0).equals(""));
        content.replaceTextRange(5, 0, "\n");
        assertDo(":1f:", content.getLine(0).equals(""));
        content.replaceTextRange(6, 0, "\n");
        assertDo(":1g:", content.getLine(0).equals(""));
        content.replaceTextRange(7, 0, "\n");
        assertDo(":1h:", content.getLine(0).equals(""));
        content.setText("This is the input/output text component.");
        content.replaceTextRange(0, 0, "\n");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":2a:", newText.equals("\nThis is the input/output text component."));
        assertDo(":2b:", content.getLine(0).equals(""));
        assertDo(":2c:", content.getLine(1).equals("This is the input/output text component."));
        content.replaceTextRange(1, 0, "\n");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":2d:", newText.equals("\n\nThis is the input/output text component."));
        assertDo(":2e:", content.getLine(0).equals(""));
        assertDo(":2f:", content.getLine(1).equals(""));
        assertDo(":2g:", content.getLine(2).equals("This is the input/output text component."));
        content.replaceTextRange(2, 0, "\n");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":3a:", newText.equals("\n\n\nThis is the input/output text component."));
        assertDo(":3b:", content.getLine(0).equals(""));
        assertDo(":3c:", content.getLine(1).equals(""));
        assertDo(":3d:", content.getLine(2).equals(""));
        assertDo(":3e:", content.getLine(3).equals("This is the input/output text component."));
        content.replaceTextRange(3, 0, "\n");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":3f:", newText.equals("\n\n\n\nThis is the input/output text component."));
        assertDo(":3g:", content.getLine(0).equals(""));
        assertDo(":3h:", content.getLine(1).equals(""));
        assertDo(":3i:", content.getLine(2).equals(""));
        assertDo(":3j:", content.getLine(3).equals(""));
        assertDo(":3k:", content.getLine(4).equals("This is the input/output text component."));
        content.replaceTextRange(3, 1, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":4a:", newText.equals("\n\n\nThis is the input/output text component."));
        assertDo(":4b:", content.getLine(0).equals(""));
        assertDo(":4c:", content.getLine(1).equals(""));
        assertDo(":4d:", content.getLine(2).equals(""));
        assertDo(":4e:", content.getLine(3).equals("This is the input/output text component."));
        content.replaceTextRange(2, 1, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":4f:", newText.equals("\n\nThis is the input/output text component."));
        assertDo(":4g:", content.getLine(0).equals(""));
        assertDo(":4h:", content.getLine(1).equals(""));
        assertDo(":4i:", content.getLine(2).equals("This is the input/output text component."));
        content.replaceTextRange(2, 0, "a");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":5a:", newText.equals("\n\naThis is the input/output text component."));
        assertDo(":5b:", content.getLine(0).equals(""));
        assertDo(":5c:", content.getLine(1).equals(""));
        assertDo(":5d:", content.getLine(2).equals("aThis is the input/output text component."));
        content.setText("abc\r\ndef");
        content.replaceTextRange(1, 1, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":6a:", newText.equals("ac\r\ndef"));
        assertDo(":6b:", content.getLineCount() == 2);
        assertDo(":6c:", content.getLine(0).equals("ac"));
        assertDo(":6d:", content.getLine(1).equals("def"));
        content.replaceTextRange(1, 1, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":6e:", newText.equals("a\r\ndef"));
        assertDo(":6f:", content.getLineCount() == 2);
        assertDo(":6g:", content.getLine(0).equals("a"));
        assertDo(":6h:", content.getLine(1).equals("def"));
        content.replaceTextRange(1, 2, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":6i:", newText.equals("adef"));
        assertDo(":6j:", content.getLineCount() == 1);
        assertDo(":6k:", content.getLine(0).equals("adef"));
        content.replaceTextRange(1, 1, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":6l:", newText.equals("aef"));
        assertDo(":6m:", content.getLineCount() == 1);
        assertDo(":6n:", content.getLine(0).equals("aef"));
        content.replaceTextRange(1, 1, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":6o:", newText.equals("af"));
        assertDo(":6p:", content.getLineCount() == 1);
        assertDo(":6q:", content.getLine(0).equals("af"));
        content.setText("abc");
        content.replaceTextRange(0, 1, "1");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":7a:", content.getLineCount() == 1);
        assertDo(":7b:", newText.equals("1bc"));
        assertDo(":7c:", content.getLine(0).equals("1bc"));
        content.replaceTextRange(0, 0, "\n");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":7d:", newText.equals("\n1bc"));
        assertDo(":7e:", content.getLineCount() == 2);
        assertDo(":7f:", content.getLine(0).equals(""));
        assertDo(":7g:", content.getLine(1).equals("1bc"));
        content = getContentInstance();
        content.replaceTextRange(0, 0, "a");
        content.setText("package test;\n/* Line 1\n * Line 2\n */\npublic class SimpleClass {\n}");
        content.replaceTextRange(14, 23, "\t/*Line 1\n\t * Line 2\n\t */");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":8a:", newText.equals("package test;\n\t/*Line 1\n\t * Line 2\n\t */\npublic class SimpleClass {\n}"));
        assertDo(":8b:", content.getLineCount() == 6);
        assertDo(":8c:", content.getLine(0).equals("package test;"));
        assertDo(":8d:", content.getLine(1).equals("\t/*Line 1"));
        assertDo(":8e:", content.getLine(2).equals("\t * Line 2"));
        assertDo(":8f:", content.getLine(3).equals("\t */"));
        assertDo(":8g:", content.getLine(4).equals("public class SimpleClass {"));
        assertDo(":8h:", content.getLine(5).equals("}"));
    }

    public void test_Text_Changed_Event() {
        StyledTextContent content = getContentInstance();
        content.addTextChangeListener(this);
        verify = 1;
        content.setText("testing");
        content.replaceTextRange(0, 0, "\n");
        verify = 2;
        content.setText("\n\n");
        content.replaceTextRange(0, 2, "a");
        verify = 3;
        content.setText("a");
        content.replaceTextRange(0, 1, "\n\n");
        verify = 5;
        content.setText("Line 1\r\nLine 2");
        content.replaceTextRange(0, 0, "\r");
        verify = 6;
        content.setText("This\nis a test\nline 3\nline 4");
        content.replaceTextRange(21, 7, "");
        verify = 7;
        content.setText("This\nis a test\r");
        content.replaceTextRange(5, 9, "");
        verify = 8;
        content.setText("\nL1\r\nL2\r\n");
        content.replaceTextRange(7, 2, "");
        verify = 9;
        content.setText("L1\r\n");
        content.replaceTextRange(2, 2, "test");
        verify = 12;
        content.setText("L1\r");
        content.replaceTextRange(3, 0, "\n");
        verify = 13;
        content.setText("L1\n");
        content.replaceTextRange(2, 0, "\r");
        verify = 15;
        content.setText("L1\r\n");
        content.replaceTextRange(2, 2, "test\n\n");
        verify = 18;
        content.setText("L1\r");
        content.replaceTextRange(3, 0, "\ntest\r\n");
        verify = 19;
        content.setText("L1\n");
        content.replaceTextRange(2, 0, "test\r\r\r");
        verify = 0;
        content.removeTextChangeListener(this);
    }

    public void test_Delimiter_Special_Cases() {
        StyledTextContent content = getContentInstance();
        String newText;
        content.setText("\nL1\r\nL2\r\n");
        content.replaceTextRange(7, 2, "");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":1:", newText.equals("\nL1\r\nL2"));
        content.setText("L1\r\n");
        content.replaceTextRange(2, 2, "test\n\n");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":2:", newText.equals("L1test\n\n"));
        content.setText("L1\n");
        content.replaceTextRange(2, 0, "test\r\r\r");
        newText = content.getTextRange(0, content.getCharCount());
        assertDo(":3:", newText.equals("L1test\r\r\r\n"));
    }

    protected void setUp() {
        shell = new Shell();
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        shell.setSize(500, 300);
        shell.setLayout(layout);
        widget = new StyledText(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData spec = new GridData();
        spec.horizontalAlignment = spec.FILL;
        spec.grabExcessHorizontalSpace = true;
        spec.verticalAlignment = spec.FILL;
        spec.grabExcessVerticalSpace = true;
        widget.setLayoutData(spec);
        shell.open();
    }

    protected void tearDown() {
        if (shell != null && !shell.isDisposed()) shell.dispose();
        shell = null;
    }
}
