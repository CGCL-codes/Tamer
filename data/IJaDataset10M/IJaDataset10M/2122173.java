package info.bliki.wiki.addon.test.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ChartTest extends AddonTestSupport {

    public ChartTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(ChartTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testChart01() {
        assertEquals("\n" + "<p>test <img border=\"0\" src=\"http://chart.apis.google.com/chart?cht=p3&amp;chd=s:hW&amp;chs=250x100&amp;chl=Hello|World\" alt=\"Yellow line chart\" /> tag.</p>", wikiModel.render("test <chart url=\"cht=p3&chd=s:hW&chs=250x100&chl=Hello|World\" alt=\"Yellow line chart\" /> tag."));
    }

    public void testChart02() {
        assertEquals("\n" + "<p>test <img border=\"0\" src=\"http://chart.apis.google.com/chart?cht=p3&amp;chd=s:hW&amp;chs=250x100&amp;chl=Hello|World\" alt=\"Yellow line chart\" /> tag.</p>", wikiModel.render("test <chart url=\"cht=p3&chd=s:hW&chs=250x100&chl=Hello|World\" alt=\"Yellow line chart\"></chart> tag."));
    }

    public void testChart03() {
        assertEquals("\n" + "<p>test <img border=\"0\" src=\"http://chart.apis.google.com/chart?chd=s:HelloWorld&amp;cht=lc&amp;chs=200x125\" alt=\"Sample chart\" /> tag.</p>", wikiModel.render("test <chart cht=lc chs=200x125 url=\"chd=s:HelloWorld\" alt=\"Sample chart\" /> tag."));
    }

    public void testChart04() {
        assertEquals("\n" + "<p>test <img border=\"0\" src=\"http://chart.apis.google.com/chart?cht=lc&amp;chd=s%3AHelloWorld&amp;chs=200x125\" alt=\"Sample chart\" /> tag.</p>", wikiModel.render("test <chart cht=lc chs=200x125 chd=\"s:HelloWorld\" alt=\"Sample chart\" /> tag."));
    }
}
