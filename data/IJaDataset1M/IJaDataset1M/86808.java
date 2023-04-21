package org.springframework.richclient.form.binding.swing;

import javax.swing.JComboBox;
import javax.swing.event.ListDataEvent;
import org.springframework.binding.support.TestLabeledEnum;

public class EnumComboBoxBindingTests extends AbstractBindingTests {

    private EnumComboBoxBinding cbb;

    private JComboBox cb;

    protected String setUpBinding() {
        cbb = new EnumComboBoxBinding(fm, "enumProperty");
        cb = (JComboBox) cbb.getControl();
        return "enumProperty";
    }

    public void testValueModelUpdatesComponent() {
        TestListDataListener tldl = new TestListDataListener();
        cb.getModel().addListDataListener(tldl);
        assertEquals(null, cb.getSelectedItem());
        assertEquals(-1, cb.getSelectedIndex());
        tldl.assertCalls(0);
        vm.setValue(TestLabeledEnum.ONE);
        assertEquals(TestLabeledEnum.ONE, cb.getSelectedItem());
        assertEquals(1, cb.getSelectedIndex());
        tldl.assertEvent(1, ListDataEvent.CONTENTS_CHANGED, -1, -1);
        vm.setValue(TestLabeledEnum.TWO);
        assertEquals(TestLabeledEnum.TWO, cb.getSelectedItem());
        assertEquals(2, cb.getSelectedIndex());
        tldl.assertEvent(2, ListDataEvent.CONTENTS_CHANGED, -1, -1);
        vm.setValue(null);
        assertEquals(null, cb.getSelectedItem());
        assertEquals(-1, cb.getSelectedIndex());
        tldl.assertEvent(3, ListDataEvent.CONTENTS_CHANGED, -1, -1);
        vm.setValue(null);
        tldl.assertCalls(3);
    }

    public void testComponentUpdatesValueModel() {
        cb.setSelectedIndex(1);
        assertEquals(TestLabeledEnum.ONE, vm.getValue());
        cb.setSelectedItem(TestLabeledEnum.TWO);
        assertEquals(TestLabeledEnum.TWO, vm.getValue());
        cb.setSelectedIndex(-1);
        assertEquals(null, vm.getValue());
    }

    public void testComponentTracksEnabledChanges() {
        assertTrue(cb.isEnabled());
        fm.getPropertyMetadata("enumProperty").setEnabled(false);
        assertFalse(cb.isEnabled());
        fm.getPropertyMetadata("enumProperty").setEnabled(true);
        assertTrue(cb.isEnabled());
    }

    public void testComponentTracksReadOnlyChanges() {
        assertTrue(cb.isEnabled());
        fm.getPropertyMetadata("enumProperty").setReadOnly(true);
        assertFalse(cb.isEnabled());
        fm.getPropertyMetadata("enumProperty").setReadOnly(false);
        assertTrue(cb.isEnabled());
    }
}
