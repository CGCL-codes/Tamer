package net.sf.fmj.test.compat.sun;

import javax.media.*;
import javax.media.format.*;
import javax.media.renderer.*;
import junit.framework.*;
import net.sf.fmj.utility.*;
import com.lti.utils.*;
import com.sun.media.renderer.video.*;

/**
 * 
 * @author Ken Larson
 * 
 */
public class AWTRendererTest extends TestCase {

    public void testAWTRenderer() {
        VideoRenderer r = new AWTRenderer();
        assertEquals(r.getName(), "AWT Renderer");
        assertTrue(r.getBounds() == null);
        assertTrue(r.getComponent() != null);
        Object[] controls = r.getControls();
        assertEquals(controls.length, 1);
        assertEquals(controls[0], r);
        final Format[] supportedInputFormats;
        if (OSUtils.isMacOSX() && !ClasspathChecker.checkManagerImplementation()) {
            supportedInputFormats = new Format[] { new RGBFormat(null, -1, Format.intArray, -1.0f, 32, 0xff0000, 0xff00, 0xff, 1, -1, 0, -1), new RGBFormat(null, -1, Format.intArray, -1.0f, 32, 0xff0000, 0xff00, 0xff, 1, -1, 0, -1) };
        } else {
            supportedInputFormats = new Format[] { new RGBFormat(null, -1, Format.intArray, -1.0f, 32, 0xff0000, 0xff00, 0xff, 1, -1, 0, -1), new RGBFormat(null, -1, Format.intArray, -1.0f, 32, 0xff, 0xff00, 0xff0000, 1, -1, 0, -1) };
        }
        final Format[] formats = r.getSupportedInputFormats();
        assertEquals(formats.length, supportedInputFormats.length);
        for (int i = 0; i < formats.length; ++i) {
            Format format = formats[i];
            assertEquals(format, supportedInputFormats[i]);
        }
    }
}
