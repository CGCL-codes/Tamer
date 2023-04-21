package javax.microedition.lcdui;

import com.sun.midp.i3test.TestCase;
import com.sun.midp.util.Baton;
import com.sun.midp.util.LcduiTestMIDlet;
import com.sun.midp.util.LiveTracer;
import com.sun.midp.util.LiveTraceListener;
import com.sun.midp.util.SerialCallback;

/**
 * Regression tests for CR 6254765. There are actually two bugs mentioned 
 * there. Details are provided for each test case.
 */
public class Test6254765 extends TestCase {

    Display dpy;

    /**
     * Checks all of the itemLFs of the given form to see if their layouts are 
     * valid. Returns true if all are valid, false if any one is invalid.
     */
    boolean checkValidLayout(Form form) {
        FormLFImpl formLF = (FormLFImpl) form.formLF;
        ItemLFImpl itemLF;
        boolean anyInvalid = false;
        for (int ii = 0; ii < formLF.numOfLFs; ii++) {
            itemLF = formLF.itemLFs[ii];
            boolean thisInvalid = itemLF.actualBoundsInvalid[0] || itemLF.actualBoundsInvalid[1] || itemLF.actualBoundsInvalid[2] || itemLF.actualBoundsInvalid[3];
            anyInvalid = anyInvalid || thisInvalid;
        }
        return !anyInvalid;
    }

    /**
     * Regression test for the main CR described in CR 6254765.  This is a
     * race condition between modifying a Form's contents (for example, with
     * append) while the Form is in the process of becoming current.
     */
    void testScreenChangeAppend() {
        StringItem items[] = new StringItem[10];
        final Baton baton = new Baton();
        for (int ii = 0; ii < items.length; ii++) {
            items[ii] = new StringItem(null, Integer.toString(ii % 10));
        }
        Form form = new Form("Test Form");
        dpy.liveTracer.add(Display.LTR_SCREENCHANGE_AFTERSHOW, new LiveTraceListener() {

            public void call(String tag) {
                baton.pass();
            }
        });
        dpy.setCurrent(form);
        for (int ii = 0; ii < items.length; ii++) {
            form.append(items[ii]);
            if (ii == 3) {
                baton.start();
            }
        }
        baton.finish();
        new SerialCallback(dpy).invokeAndWait();
        assertTrue("layout must be valid", checkValidLayout(form));
        dpy.liveTracer.clear();
    }

    /**
     * This is a regression test for another CR that is also mentioned in 
     * 6254765, which occurs when the form is in an inconsistent state such as 
     * what arose from the initial CR, but which can also arise for other 
     * reasons.
     * 
     * This case is as follows: traverseIndex == -1 and itemTraverse == false,
     * indicating no initial focus; and getNextInteractiveItem returns a value
     * >= 0 indicating that there is a focusable item on screen.
     *
     * The uTraverse() code assumes that if getNextInteractiveItem returns
     * some nonnegative value, there must be a currently focused item (that
     * is, traverseIndex is also nonnegative). However, this is not always the
     * case. This can occur if the form initially has no visible focusable
     * items, and the app adds a focusable item, which triggers an invalidate,
     * and then the user traverses before the invalidate can be processed. 
     * This test simulates that case.
     */
    void testTraversalInconsistency() {
        Form form = new Form("Test Form 2");
        FormLFImpl formLF = (FormLFImpl) form.formLF;
        final Baton baton = new Baton();
        SerialCallback scb = new SerialCallback(dpy);
        Item item = new Gauge(null, true, 1, 0);
        form.append("String 1");
        dpy.setCurrent(form);
        scb.invokeAndWait();
        dpy.callSerially(new Runnable() {

            public void run() {
                baton.pass();
            }
        });
        baton.start();
        form.insert(0, item);
        formLF.uTraverse(Canvas.DOWN);
        baton.finish();
        scb.invokeAndWait();
        assertEquals("item 0 should be focused", 0, formLF.traverseIndex);
    }

    public void runTests() throws Throwable {
        if (!LcduiTestMIDlet.invoke()) {
            throw new RuntimeException("can't start LcduiTestMIDlet");
        }
        try {
            dpy = LcduiTestMIDlet.getDisplay();
            declare("testScreenChangeAppend");
            testScreenChangeAppend();
            declare("testTraversalInconsistency");
            testTraversalInconsistency();
        } finally {
            LcduiTestMIDlet.cleanup();
        }
    }
}
