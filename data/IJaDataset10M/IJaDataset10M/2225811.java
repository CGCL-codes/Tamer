package org.mime4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 * 
 *
 * @author Niklas Therning
 * @version $Id: TestUtil.java,v 1.2 2004/10/02 12:41:11 ntherning Exp $
 */
public class TestUtil {

    public static final String[] TEST_MESSAGES = new String[] { "2002_06_12_doublebound", "ak-0696", "bluedot-postcard", "bluedot-simple", "double-bound-with-embedded", "double-bound", "dup-names", "frag", "german", "hdr-fakeout", "multi-2evil", "multi-2gifs", "multi-clen", "multi-digest", "multi-frag", "multi-igor", "multi-igor2", "multi-nested", "multi-nested2", "multi-nested3", "multi-simple", "multi-weirdspace", "re-fwd", "russian", "simple", "uu-junk-target", "uu-junk", "uu-zeegee" };

    public static String readResource(String resource, String charset) throws IOException {
        return IOUtils.toString(readResourceAsStream(resource), charset);
    }

    public static InputStream readResourceAsStream(String resource) throws IOException {
        return new BufferedInputStream(TestUtil.class.getResource(resource).openStream());
    }
}
