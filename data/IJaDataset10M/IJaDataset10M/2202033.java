package com.google.gdt.eclipse.designer.mobile;

import org.eclipse.wb.internal.core.utils.IOUtils2;
import org.eclipse.wb.tests.designer.tests.DesignerTestCase;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import static org.fest.assertions.Assertions.assertThat;
import org.apache.commons.io.IOUtils;
import java.io.InputStream;

/**
 * Test for {@link Activator}.
 * 
 * @author scheglov_ke
 */
public class ActivatorTest extends DesignerTestCase {

    public void _test_exit() throws Exception {
        System.exit(0);
    }

    /**
   * Test for {@link Activator#getDefault()}.
   */
    public void test_getDefault() throws Exception {
        assertNotNull(Activator.getDefault());
    }

    /**
   * Test for {@link Activator#getResourceString(String)}.
   */
    public void test_getBundleStatic() throws Exception {
        assertEquals("GWT Designer Mobile Support", Activator.getResourceString("%pluginName"));
    }

    /**
   * Test for {@link Activator#getFile(String)}.
   */
    public void test_getFile() throws Exception {
        InputStream file = Activator.getFile("plugin.xml");
        assertNotNull(file);
        try {
            String s = IOUtils2.readString(file);
            assertThat(s.length()).isGreaterThan(512);
        } finally {
            IOUtils.closeQuietly(file);
        }
    }

    /**
   * Test for {@link Activator#getFile(String)}.
   */
    public void test_getFile_bad() throws Exception {
        try {
            Activator.getFile("noSuch.file");
        } catch (Throwable e) {
            String msg = e.getMessage();
            assertThat(msg).contains("noSuch.file").contains("com.google.gdt.eclipse.designer.mobile");
        }
    }

    /**
   * Test for {@link Activator#getImage(String)}.
   */
    public void test_getImage_good() throws Exception {
        Image image = Activator.getImage("device/device.png");
        assertNotNull(image);
    }

    /**
   * Test for {@link Activator#getImage(String)}.
   */
    public void test_getImage_bad() throws Exception {
        try {
            Activator.getImage("noSuch.png");
        } catch (Throwable e) {
            String msg = e.getMessage();
            assertThat(msg).contains("noSuch.png").contains("com.google.gdt.eclipse.designer.mobile");
        }
    }

    /**
   * Test for {@link Activator#getImageDescriptor(String)}.
   */
    public void test_getImageDescription_good() throws Exception {
        ImageDescriptor imageDescriptor = Activator.getImageDescriptor("device/device.png");
        assertNotNull(imageDescriptor);
    }

    /**
   * Test for {@link Activator#getImageDescriptor(String)}.
   */
    public void test_getImageDescription_bad() throws Exception {
        try {
            Activator.getImageDescriptor("noSuch.png");
        } catch (Throwable e) {
            String msg = e.getMessage();
            assertThat(msg).contains("noSuch.png").contains("com.google.gdt.eclipse.designer.mobile");
        }
    }
}
