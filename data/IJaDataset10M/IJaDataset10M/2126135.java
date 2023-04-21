package org.springframework.binding.form.support;

import junit.framework.TestCase;
import org.springframework.binding.form.PropertyMetadata;
import org.springframework.binding.support.TestPropertyChangeListener;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.config.DefaultApplicationLifecycleAdvisor;

/**
 * 
 * @author  Oliver Hutchison
 */
public class PropertyMetadataImplTests extends TestCase {

    public final void setUp() {
        Application.load(null);
        new Application(new DefaultApplicationLifecycleAdvisor());
    }

    public void testDirtyChangeTrackingOnValueChange() {
        FormModelMediatingValueModel vm = new FormModelMediatingValueModel(new ValueHolder("v1"));
        DefaultFormModel fm = new DefaultFormModel(new Object());
        PropertyMetadataImpl m = new PropertyMetadataImpl(fm, vm, Object.class, false);
        TestPropertyChangeListener pcl = new TestPropertyChangeListener(PropertyMetadata.DIRTY_PROPERTY);
        m.addPropertyChangeListener(PropertyMetadata.DIRTY_PROPERTY, pcl);
        assertTrue(!m.isDirty());
        assertEquals(0, pcl.eventCount());
        vm.setValue("v1");
        assertEquals(0, pcl.eventCount());
        assertTrue(!m.isDirty());
        vm.setValue("v2");
        assertEquals(1, pcl.eventCount());
        assertTrue(m.isDirty());
        vm.setValue("v3");
        assertEquals(1, pcl.eventCount());
        assertTrue(m.isDirty());
        vm.setValue("v1");
        assertEquals(2, pcl.eventCount());
        assertTrue(!m.isDirty());
        vm.setValue(null);
        assertEquals(3, pcl.eventCount());
        assertTrue(m.isDirty());
        vm.clearDirty();
        assertEquals(4, pcl.eventCount());
        assertTrue(!m.isDirty());
        vm.setValue(null);
        assertEquals(4, pcl.eventCount());
        assertTrue(!m.isDirty());
    }

    public void testEnabledChange() {
        FormModelMediatingValueModel vm = new FormModelMediatingValueModel(new ValueHolder("v1"));
        DefaultFormModel fm = new DefaultFormModel(new Object());
        PropertyMetadataImpl m = new PropertyMetadataImpl(fm, vm, Object.class, false);
        TestPropertyChangeListener pcl = new TestPropertyChangeListener(PropertyMetadata.ENABLED_PROPERTY);
        m.addPropertyChangeListener(PropertyMetadata.ENABLED_PROPERTY, pcl);
        assertEquals(0, pcl.eventCount());
        assertTrue(m.isEnabled());
        m.setEnabled(true);
        assertEquals(0, pcl.eventCount());
        assertTrue(m.isEnabled());
        m.setEnabled(false);
        assertEquals(1, pcl.eventCount());
        assertTrue(!m.isEnabled());
        fm.setEnabled(false);
        assertEquals(1, pcl.eventCount());
        assertTrue(!m.isEnabled());
        m.setEnabled(true);
        assertEquals(1, pcl.eventCount());
        assertTrue(!m.isEnabled());
        fm.setEnabled(true);
        assertEquals(2, pcl.eventCount());
        assertTrue(m.isEnabled());
    }
}
