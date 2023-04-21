package net.laubenberger.bogatyr.helper.launcher;

import static org.junit.Assert.fail;
import java.net.URI;
import net.laubenberger.bogatyr.helper.HelperString;
import net.laubenberger.bogatyr.misc.Constants;
import net.laubenberger.bogatyr.misc.exception.RuntimeExceptionIsEmpty;
import net.laubenberger.bogatyr.misc.exception.RuntimeExceptionIsNull;
import org.junit.Test;

/**
 * JUnit test for {@link LauncherBrowser}
 * 
 * @author Stefan Laubenberger
 * @version 20101119
 */
public class LauncherBrowserTest {

    @Test
    public void testPassBrowse() {
        try {
            LauncherBrowser.browse(Constants.BOGATYR.getUrl().toURI());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        try {
            LauncherBrowser.browse("code.google.com/p/bogatyr/");
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testFailBrowse() {
        try {
            LauncherBrowser.browse((URI) null);
            fail("uri is null");
        } catch (RuntimeExceptionIsNull ex) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        try {
            LauncherBrowser.browse((String) null);
            fail("url is null");
        } catch (RuntimeExceptionIsNull ex) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        try {
            LauncherBrowser.browse(HelperString.EMPTY_STRING);
            fail("url is empty");
        } catch (RuntimeExceptionIsEmpty ex) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
}
