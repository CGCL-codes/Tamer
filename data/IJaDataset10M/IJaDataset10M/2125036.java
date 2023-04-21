package org.opennms.netmgt.dao.support;

import static org.junit.Assert.*;
import java.util.Collection;
import org.junit.Test;
import org.opennms.netmgt.dao.ExtensionManager;

/**
 * @author brozow
 *
 */
public class DefaultExtensionManagerTest {

    @Test
    public void testRegisterExtension() {
        DefaultExtensionManager mgr = new DefaultExtensionManager();
        mgr.registerExtension(mgr, ExtensionManager.class);
        Collection<ExtensionManager> extensions = mgr.findExtensions(ExtensionManager.class);
        assertEquals(1, extensions.size());
        assertSame(mgr, extensions.iterator().next());
    }

    @Test
    public void testRegisterExtensionUsingActualClass() {
        DefaultExtensionManager mgr = new DefaultExtensionManager();
        mgr.registerExtension(mgr, DefaultExtensionManager.class);
        Collection<DefaultExtensionManager> extensions = mgr.findExtensions(DefaultExtensionManager.class);
        assertEquals(1, extensions.size());
        assertSame(mgr, extensions.iterator().next());
    }

    @Test
    public void testRegisterExtensionUsingMultipleClasses() {
        DefaultExtensionManager mgr = new DefaultExtensionManager();
        mgr.registerExtension(mgr, ExtensionManager.class, DefaultExtensionManager.class);
        Collection<ExtensionManager> extensions = mgr.findExtensions(ExtensionManager.class);
        Collection<DefaultExtensionManager> extensions2 = mgr.findExtensions(DefaultExtensionManager.class);
        assertEquals(1, extensions.size());
        assertSame(mgr, extensions.iterator().next());
        assertEquals(1, extensions2.size());
        assertSame(mgr, extensions2.iterator().next());
    }
}
