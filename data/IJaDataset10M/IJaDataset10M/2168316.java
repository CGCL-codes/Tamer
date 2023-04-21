package net.sf.jtmt.concurrent.hadoop.querycocanalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * Test for NCSA Log parser.
 * @author Sujit Pal
 * @version $Revision: 51 $
 */
public class NcsaLogParserTest {

    private final Log log = LogFactory.getLog(getClass());

    @Test
    public void testParsing() throws Exception {
        File[] accesslogs = (new File("src/test/resources/access_logs")).listFiles();
        for (File accesslog : accesslogs) {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(accesslog));
            while ((line = reader.readLine()) != null) {
                List<String> tokens = NcsaLogParser.parse(line);
                String url = tokens.get(4);
                if (url.contains("/search")) {
                    printResult(line, tokens);
                }
            }
            reader.close();
        }
    }

    @Test
    public void testParseCommonLogFormatNoCookie() throws Exception {
        String line = "192.168.123.12 - - [19/Oct/2008:19:45:38 -0700] \"" + "GET /search?q1=foo&st=bar HTTP/1.1\" 200 323";
        List<String> tokens = NcsaLogParser.parse(line);
        printResult(line, tokens);
    }

    @Test
    public void testParseCombinedLogFormatWithCookie() throws Exception {
        String line = "192.168.123.12 - - [19/Oct/2008:19:45:38 -0700] \"" + "GET /search?q1=foo&st=bar HTTP/1.1\" 200 323 " + "\"-\" \"Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.8.1.14) " + "Gecko/20080416 Fedora/2.0.0.14-1.fc7 Firefox/2.0.0.14\" " + "\"USER_ID=12345,jsession_id=3BFY342211\"";
        List<String> tokens = NcsaLogParser.parse(line);
        printResult(line, tokens);
    }

    @Test
    public void testParseCustomLogFormat() throws Exception {
        String line = "192.168.123.12 - - [19/Oct/2008:19:45:38 -0700] \"" + "GET /search?q1=foo&st=bar HTTP/1.1\" 200 323 " + "\"-\" 34567 \"Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.8.1.14) " + "Gecko/20080416 Fedora/2.0.0.14-1.fc7 Firefox/2.0.0.14\"";
        List<String> tokens = NcsaLogParser.parse(line);
        printResult(line, tokens);
    }

    private void printResult(String line, List<String> tokens) {
        log.info(">>> " + line);
        log.info("tokens={");
        int i = 0;
        for (String token : tokens) {
            log.info("  [" + i + "] = " + token);
            i++;
        }
        log.info("}");
    }
}
