package com.sproketsoft.domlet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import junit.framework.TestCase;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: DanH
 * Date: Aug 1, 2005
 * Time: 7:17:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestTokenizer extends TestCase {

    private static final Logger log = LogManager.getLogger(TestTokenizer.class);

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testTokenizer() {
        String s1 = "1234/";
        if (s1.charAt(s1.length() - 1) == '/') {
            s1 = s1.substring(0, s1.length() - 1);
        }
        log.debug(s1);
        String s = "src=\"http://www.jetbrains.com/idea/download/../../img/logo_bw.gif\" width=\"124\" height=\"44\" alt=\"JetBrains logo\"";
        StringTokenizer st = new StringTokenizer(s);
        int tokenCount = st.countTokens();
        int currentToken = 0;
        log.debug("tokens: " + tokenCount);
        while (st.hasMoreTokens()) {
            String c = st.nextToken();
            String key, value;
            int n = c.indexOf("=");
            if (n > -1) {
                key = c.substring(0, n);
                value = c.substring(n + 1);
                while ((value.startsWith("\"") && !value.endsWith("\"")) || (value.startsWith("'") && !value.endsWith("'"))) {
                    c = st.nextToken();
                    currentToken++;
                    value += " " + c;
                    if (currentToken > tokenCount) {
                        break;
                    }
                }
            } else {
                key = c;
                value = "true";
            }
            log.debug(c);
            log.debug("key :" + key + " value: " + value);
        }
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
