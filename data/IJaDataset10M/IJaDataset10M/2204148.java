package abbot.script;

import java.awt.Component;
import javax.swing.JTextField;
import java.util.HashMap;
import junit.extensions.abbot.*;

public class PropertyCallTest extends ComponentTestFixture {

    public void testChangeType() throws Throwable {
        ComponentReference ref = new ComponentReference(getResolver(), Component.class, new HashMap());
        getResolver().addComponentReference(ref);
        PropertyCall step = new PCall(getResolver(), getName(), abbot.tester.ComponentTester.class.getName(), "assertFrameShowing", new String[] { "title" });
        assertEquals("Default class should be ComponentTester", abbot.tester.ComponentTester.class.getName(), step.getTargetClassName());
        step.setComponentID(ref.getID());
        assertEquals("Target class should have changed", Component.class.getName(), step.getTargetClassName());
    }

    public void testMethodLookup() throws Throwable {
        String text = "Custom Field";
        JTextField tf = new TextFieldTestClass(text);
        showFrame(tf);
        ComponentReference ref = getResolver().addComponent(tf);
        PropertyCall step = new PCall(getResolver(), getName(), "getText", ref.getID());
        step.run();
    }

    public void testStaticMethodCall() throws Throwable {
        PropertyCall step = new PCall(getResolver(), getName(), getClass().getName(), "staticCall", new String[0]);
        assertEquals("Wrong default class", getClass(), step.getTargetClass());
    }

    public PropertyCallTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        TestHelper.runTests(args, PropertyCallTest.class);
    }

    private class PCall extends PropertyCall {

        public PCall(Resolver r, String d, String m, String id) {
            super(r, d, m, id);
        }

        public PCall(Resolver r, String d, String c, String m, String[] a) {
            super(r, d, c, m, a);
        }
    }

    public static void staticCall() {
    }
}

class TextFieldTestClass extends JTextField {

    public TextFieldTestClass(String contents) {
        super(contents);
    }

    public String getText() {
        return super.getText();
    }
}
