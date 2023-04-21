package net.sf.iqser.plugin.web.base.swisspost;

import java.util.Properties;
import java.util.regex.Pattern;

/**
 * 
 * Links that will lead to PDFs
 * http://www.admin.ch/ch/d/as/2011/
 * http://www.admin.ch/ch/d/sr/sr.html
 * 
 * http://www.admin.ch/ch/e/rs/1.html
 * http://www.admin.ch/ch/e/rs/142_31/index.html
 * http://www.admin.ch/ch/e/rs/6.html
 * 
 * Page with PDF documents:
 * http://www.admin.ch/ch/d/ff/2011/index0_37.html
 */
public class SwissPostPDFCrawlerTest extends SwissPostCrawlerBase {

    String simplifyRegex = "\\S+\\.admin.ch\\S+";

    String regex = "\\S+admin.ch" + "((\\S+/e/\\S+\\.html\\S*)|" + "(\\S+/en/\\S+\\.html\\S*)|" + "(\\S+\\.html\\S+lang=en\\S*)|" + "(\\S+\\.pdf)|" + "(\\S+\\.PDF))|";

    @Override
    protected void updateInitParams(Properties initParams) {
        initParams.setProperty("start-server", "http://www.admin.ch");
        initParams.setProperty("start-path", "/ch/e/rs/c814_012.html");
        initParams.setProperty("link-filter", regex);
        initParams.setProperty("item-filter", "(\\S+\\.pdf$)||(\\S+\\.PDF$)");
        initParams.setProperty("maxdepth-filter", "1");
    }

    @Override
    public void testDoSynchonization() {
        super.testDoSynchonization();
    }

    public void testRegex() {
        Pattern pattern = Pattern.compile(regex);
        String[] examples = new String[] { "http://www.admin.ch/org/polit/index.html?lang=en", "http://www.admin.ch/index.html?lang=en", "http://www.admin.ch/index.html?p1=v1&lang=en", "http://www.admin.ch/index.html?p1=v1&lang=en&p2=v2", "http://www.admin.ch/index.pdf", "http://www.admin.ch/index.PDF", "http://www.admin.ch/ch/e/rs/6.html", "http://www.admin.ch/ch/e/rs/142_31/index.html", "http://www.admin.ch/ch/e/rs/8/814.012.en.pdf" };
        for (String example : examples) {
            java.util.regex.Matcher m = pattern.matcher(example);
            assertTrue(example, m.matches());
        }
        String[] negExamples = new String[] { "http://www.admin.ch/index.html?p1=v1", "http://www.admin.ch/index.html?lang=de", "http://www.admin.ch/index.html?p1=v1&lang=de", "http://www.admin.ch/index.html?p1=v1&lang=de&p2=v2", "http://www.admin.ch/ch/d/rs/6.html", "http://www.fdfa.admin.ch/index.html?p1=v1", "http://www.fdfa.admin.ch/index.html?lang=de", "http://www.fdfa.admin.ch/index.html?p1=v1&lang=de", "http://www.fdfa.admin.ch/index.html?p1=v1&lang=de&p2=v2", "http://www.fdfa.admin.ch/ch/d/rs/6.html" };
        for (String negExample : negExamples) {
            java.util.regex.Matcher m = pattern.matcher(negExample);
            assertFalse(negExample, m.matches());
        }
    }
}
