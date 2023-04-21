package net.sourceforge.basher.internal.impl;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.ops4j.gaderian.ApplicationRuntimeException;
import org.ops4j.gaderian.Location;
import org.ops4j.gaderian.internal.Module;
import org.easymock.MockControl;

/**
 * @author Johan Lindquist
 * @version 1.0
 */
public class TestRegularExpressionObjectProvider extends TestCase {

    public void testValid() {
        MockControl moduleControl = MockControl.createControl(Module.class);
        Module module = (Module) moduleControl.getMock();
        MockControl locationControl = MockControl.createControl(Location.class);
        Location location = (Location) locationControl.getMock();
        MockControl logControl = MockControl.createControl(Log.class);
        Log log = (Log) logControl.getMock();
        log.debug("Compiling pattern '.*'");
        moduleControl.replay();
        locationControl.replay();
        logControl.replay();
        RegularExpressionObjectProvider regularExpressionObjectProvider = new RegularExpressionObjectProvider();
        regularExpressionObjectProvider.setLog(log);
        Pattern pattern = (Pattern) regularExpressionObjectProvider.provideObject(module, String.class, ".*", location);
        assertNotNull("Unexpected null recevied", pattern);
        assertTrue("Pattern does not match", pattern.matcher("Test").matches());
        moduleControl.verify();
        locationControl.verify();
        logControl.verify();
        moduleControl.reset();
        locationControl.reset();
        logControl.reset();
        log.debug("Compiling pattern 'HEHE.*'");
        moduleControl.replay();
        locationControl.replay();
        logControl.replay();
        pattern = (Pattern) regularExpressionObjectProvider.provideObject(module, String.class, "HEHE.*", location);
        assertNotNull("Unexpected null recevied", pattern);
        assertTrue("Pattern does not match", pattern.matcher("HEHE-Test").matches());
        assertFalse("Pattern does not match", pattern.matcher("AHEHE-Test").matches());
        moduleControl.verify();
        locationControl.verify();
        logControl.verify();
    }

    public void testInvalid() {
        MockControl moduleControl = MockControl.createControl(Module.class);
        Module module = (Module) moduleControl.getMock();
        MockControl locationControl = MockControl.createControl(Location.class);
        Location location = (Location) locationControl.getMock();
        MockControl logControl = MockControl.createControl(Log.class);
        Log log = (Log) logControl.getMock();
        log.debug("Compiling pattern '/fd/\\.[*'");
        location.getLineNumber();
        locationControl.setReturnValue(1);
        moduleControl.replay();
        locationControl.replay();
        logControl.replay();
        RegularExpressionObjectProvider regularExpressionObjectProvider = new RegularExpressionObjectProvider();
        regularExpressionObjectProvider.setLog(log);
        try {
            Pattern pattern = (Pattern) regularExpressionObjectProvider.provideObject(module, String.class, "/fd/\\.[*", location);
            fail("could create invalid pattern");
        } catch (ApplicationRuntimeException e) {
            assertEquals("Invalid line number repoerted", 1, e.getLocation().getLineNumber());
            assertTrue("Invalid cause", e.getCause() instanceof PatternSyntaxException);
        }
        moduleControl.verify();
        locationControl.verify();
        logControl.verify();
    }

    public void testUnknownException() {
        MockControl moduleControl = MockControl.createControl(Module.class);
        Module module = (Module) moduleControl.getMock();
        MockControl locationControl = MockControl.createControl(Location.class);
        Location location = (Location) locationControl.getMock();
        MockControl logControl = MockControl.createControl(Log.class);
        Log log = (Log) logControl.getMock();
        log.debug("Compiling pattern 'null'");
        location.getLineNumber();
        locationControl.setReturnValue(1);
        moduleControl.replay();
        locationControl.replay();
        logControl.replay();
        RegularExpressionObjectProvider regularExpressionObjectProvider = new RegularExpressionObjectProvider();
        regularExpressionObjectProvider.setLog(log);
        try {
            Pattern pattern = (Pattern) regularExpressionObjectProvider.provideObject(module, String.class, null, location);
            fail("could create invalid pattern");
        } catch (ApplicationRuntimeException e) {
            assertEquals("Invalid line number repoerted", 1, e.getLocation().getLineNumber());
            assertTrue("Invalid cause", e.getCause() instanceof NullPointerException);
        }
        moduleControl.verify();
        locationControl.verify();
        logControl.verify();
    }
}
