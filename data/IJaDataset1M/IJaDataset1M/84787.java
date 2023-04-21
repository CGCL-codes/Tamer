package net.sf.iqser.plugin.web.base.swisspost;

import java.util.Properties;
import java.util.regex.Pattern;

public class SwissPostHTMLCrawlerTest extends SwissPostCrawlerBase {

    String regexLinkFilter = "\\S+admin.ch" + "((\\S+/e/\\S+\\.html\\S*)|" + "(\\S+/en/\\S+\\.html\\S*)|" + "(\\S+\\.html\\S+lang=en\\S*))";

    String regexItemFilter = "(\\S+\\.html\\S+lang=en$)|" + "(\\S+\\.html\\S+lang=en\\S+id=\\d{5}$)|" + "(\\S+/en/\\S+\\.html$)|" + "(\\S+/en/\\S+\\.html\\S+id=\\d{5}$)|" + "(\\S+/e/\\S+\\.html$)|" + "(\\S+/e/\\S+\\.html\\S+id=\\d{5}$)";

    String regexLinkExcludeFilter = "\\S+(form|print|download)\\S+";

    @Override
    protected void updateInitParams(Properties initParams) {
        initParams.setProperty("start-server", "http://www.admin.ch");
        initParams.setProperty("start-path", "?lang=en");
        initParams.setProperty("link-filter", regexLinkFilter);
        initParams.setProperty("item-filter", regexItemFilter);
        initParams.setProperty("link-exclude-filter", regexLinkExcludeFilter);
        initParams.setProperty("maxdepth-filter", "1");
    }

    @Override
    public void testDoSynchonization() {
        super.testDoSynchonization();
    }

    public void testRegex() {
        Pattern pattern = Pattern.compile(regexLinkFilter);
        String[] examples = new String[] { "http://www.admin.ch/org/polit/index.html?lang=en", "http://www.admin.ch/index.html?lang=en", "http://www.fdfa.admin.ch/index.html?lang=en", "http://www.admin.ch/index.html?p1=v1&lang=en", "http://www.admin.ch/index.html?p1=v1&lang=en&p2=v2", "http://www.admin.ch/ch/e/rs/6.html", "http://www.admin.ch/ch/en/rs/142_31/index.html" };
        for (String example : examples) {
            java.util.regex.Matcher m = pattern.matcher(example);
            assertTrue(example, m.matches());
        }
        String[] negExamples = new String[] { "http://www.admin.ch/index.html?p1=v1", "http://www.admin.ch/index.html?lang=de", "http://www.admin.ch/index.html?p1=v1&lang=de", "http://www.admin.ch/index.html?p1=v1&lang=de&p2=v2", "http://www.admin.ch/ch/d/rs/6.html", "http://www.other.ch/index.html", "http://www.sippo.ch/internet/osec/en/footer/copyright.html", "http://www.ch.ch/abstimmungen_und_wahlen/01253/01265/index.html?lang=en" };
        for (String negExample : negExamples) {
            java.util.regex.Matcher m = pattern.matcher(negExample);
            assertFalse(negExample, m.matches());
        }
    }
}
