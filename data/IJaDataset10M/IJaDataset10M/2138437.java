package net.webassembletool.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import junit.framework.TestCase;
import net.webassembletool.output.Output;
import net.webassembletool.resource.Resource;
import org.apache.http.impl.cookie.DateUtils;
import org.junit.Assert;

/**
 * Tests for RFC2616 handling.
 * 
 * @author Nicolas Richeton
 * 
 */
public class Rfc2616Test extends TestCase {

    /**
	 * Ensure max-date is correctly used (in seconds). See
	 * https://sourceforge.net/apps/mantisbt/webassembletool/view.php?id=17
	 */
    public void test_getExpiration() {
        final Date now = new Date();
        Resource r = new Resource() {

            @Override
            public void render(Output output) throws IOException {
            }

            @Override
            public void release() {
            }

            @Override
            public int getStatusCode() {
                return 200;
            }

            @Override
            public String getHeader(String name) {
                if (name.equals("Cache-control")) {
                    return "max-age=60";
                }
                if (name.equals("Date")) {
                    return DateUtils.formatDate(now);
                }
                return null;
            }

            @Override
            public Collection<String> getHeaders(String name) {
                String value = getHeader(name);
                if (value != null) {
                    return Collections.singleton(value);
                } else {
                    return null;
                }
            }

            @Override
            public Collection<String> getHeaderNames() {
                return null;
            }

            @Override
            public String getStatusMessage() {
                return "OK";
            }
        };
        Date d = Rfc2616.getExpiration(r);
        assertTrue(d.after(now));
        long distance = (d.getTime() - now.getTime());
        assertTrue(distance > 59000 && distance < 61000);
    }

    public void test_getDate() {
        Resource resourceWithoutDate = new Resource() {

            @Override
            public void render(Output output) throws IOException {
            }

            @Override
            public void release() {
            }

            @Override
            public int getStatusCode() {
                return 0;
            }

            @Override
            public String getHeader(String name) {
                return null;
            }

            @Override
            public Collection<String> getHeaders(String name) {
                return null;
            }

            @Override
            public Collection<String> getHeaderNames() {
                return null;
            }

            @Override
            public String getStatusMessage() {
                return null;
            }
        };
        Date date = Rfc2616.getDate(resourceWithoutDate);
        Assert.assertNotNull(date);
    }
}
