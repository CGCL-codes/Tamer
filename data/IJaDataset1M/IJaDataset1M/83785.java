package org.simpleframework.http.validate;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.simpleframework.http.core.PerformanceUtil;
import org.simpleframework.util.buffer.Buffer;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * <analyser bodyLength='1024'>
 *    <expect name='Content-Type'>text/plain</expect>
 * </analyser>
 * 
 * @author niall
 *
 */
@Root
public class Analyser {

    @ElementList(inline = true)
    private List<Expect> expect;

    @Attribute(required = false)
    private String bodyHash;

    @Attribute
    private int bodyLength;

    public void analyse(Response response, boolean debug) throws Exception {
        InputStream stream = response.getInputStream();
        String statusLine = response.getStatusLine();
        Map<String, String> header = response.getHeader();
        int length = stream.available();
        if (!statusLine.startsWith("HTTP")) {
            throw new IllegalStateException("Header does not start with HTTP version");
        }
        for (Expect entry : expect) {
            entry.check(header);
        }
        if (length != bodyLength) {
            throw new IllegalStateException("Body length '" + length + "' does not match expectation '" + bodyLength + "'");
        }
        if (bodyHash != null) {
            byte[] md5HashArray = PerformanceUtil.getMD5Hash(stream);
            String md5HashString = PerformanceUtil.toHexString(md5HashArray);
            if (md5HashArray.equals(bodyHash)) {
                throw new Exception("Expected body MD5 hash '" + bodyHash + "' does not match computed hash '" + md5HashString + "'");
            }
            if (debug) {
                System.out.printf("MD5: hash ok as %s for length %s%n", md5HashString, length);
            }
        }
    }

    @Root
    private static class Expect {

        @Attribute
        private String name;

        @Text
        private String value;

        public void check(Map<String, String> response) {
            String text = response.get(name);
            if (value == null) {
                throw new IllegalStateException("Response does not contain header '" + name + "'");
            }
            if (!value.equals(text)) {
                throw new IllegalStateException("Response header value '" + text + "' does not match expectation '" + value + "'");
            }
        }
    }
}
