package abbot.editor.editors;

import java.awt.Component;
import javax.swing.*;
import java.util.*;
import junit.extensions.abbot.*;
import abbot.finder.matchers.*;
import abbot.script.*;
import abbot.editor.widgets.*;
import abbot.tester.*;

/** Verify PropertyCallEditor operation. */
public class PropertyCallEditorTest extends ComponentTestFixture {

    private PropertyCall call;

    private PropertyCallEditor editor;

    private JComboBoxTester comboTester;

    private JComboBox id;

    private JComboBox methods;

    private JTextField cname;

    private ArrayEditor args;

    private JComboBox getComponentID(PropertyCallEditor editor) throws Exception {
        return (JComboBox) getFinder().find(editor, new NameMatcher(XMLConstants.TAG_COMPONENT));
    }

    private JTextField getTargetClass(PropertyCallEditor editor) throws Exception {
        return (JTextField) getFinder().find(editor, new NameMatcher(XMLConstants.TAG_CLASS));
    }

    private JComboBox getMethod(PropertyCallEditor editor) throws Exception {
        return (JComboBox) getFinder().find(editor, new NameMatcher(XMLConstants.TAG_METHOD));
    }

    private ArrayEditor getArguments(PropertyCallEditor editor) throws Exception {
        return (ArrayEditor) getFinder().find(editor, new NameMatcher(XMLConstants.TAG_ARGS));
    }

    protected void setUp() throws Exception {
        call = new PropertyCall(getResolver(), null, "java.lang.Object", "wait", new String[] { "5000" }) {
        };
        editor = new PropertyCallEditor(call) {
        };
        comboTester = new JComboBoxTester();
        id = getComponentID(editor);
        methods = getMethod(editor);
        cname = getTargetClass(editor);
        args = getArguments(editor);
    }

    public void testSynchWithComponentID() throws Exception {
        HashMap map = new HashMap();
        ComponentReference ref = new ComponentReference(getResolver(), Component.class, map);
        showFrame(editor);
        int count = methods.getItemCount();
        comboTester.actionSelectItem(id, ref.getID());
        assertEquals("Call target class name not changed", Component.class.getName(), call.getTargetClassName());
        assertEquals("Target class name not updated", Component.class.getName(), cname.getText());
        assertTrue("Method list should have updated", count != methods.getItemCount());
        assertEquals("Argument list not cleared", 0, call.getArguments().length);
        assertEquals("Arguments editor not cleared", 0, args.getValues().length);
    }

    public void testSynchWithTesterMethod() {
        HashMap map = new HashMap();
        ComponentReference ref = new ComponentReference(getResolver(), JTabbedPane.class, map);
        showFrame(editor);
        comboTester.actionSelectItem(id, ref.getID());
        int count = methods.getItemCount();
        comboTester.actionSelectItem(methods, "getTabs");
        assertEquals("Call target class not updated to match tester method", JTabbedPaneTester.class.getName(), call.getTargetClassName());
        assertEquals("Class field not updated to match tester method", JTabbedPaneTester.class.getName(), cname.getText());
        assertEquals("Argument list not updated to include component target", 1, call.getArguments().length);
        assertEquals("Arguments editor not updated to include component target", 1, args.getValues().length);
        assertEquals("Component ID should be cleared", null, call.getComponentID());
        assertEquals("Component ID editor should have no selection", null, id.getSelectedItem());
        assertEquals("Method list should be unchanged", count, methods.getItemCount());
        comboTester.actionSelectItem(methods, "getName");
        assertEquals("Call target class should be the target component class", JTabbedPane.class.getName(), call.getTargetClassName());
        assertEquals("Class field should hold the component class name", JTabbedPane.class.getName(), cname.getText());
        assertEquals("Argument list not updated", 0, call.getArguments().length);
        assertEquals("Arguments editor not updated", 0, args.getValues().length);
        assertEquals("Component ID should be set", ref.getID(), call.getComponentID());
        assertEquals("Component ID editor should have no selection", ref.getID(), id.getSelectedItem());
        assertEquals("Method list should be unchanged", count, methods.getItemCount());
    }

    /** Construct a test case with the given name. */
    public PropertyCallEditorTest(String name) {
        super(name);
    }

    /** Run the default test suite. */
    public static void main(String[] args) {
        TestHelper.runTests(args, PropertyCallEditorTest.class);
    }
}
