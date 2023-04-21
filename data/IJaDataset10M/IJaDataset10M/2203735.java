package info.bliki.wiki.addon.test.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PlotTest extends AddonTestSupport {

    public PlotTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(PlotTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testSampe01() {
        assertEquals("\n" + "<p>test <div id=\"plotter\">\n" + "<a href=\"#\" id=\"showp\" onclick=\"$(\'plot\').show();$(\'hidep\').show();$(\'showp\').hide();\" />Show Plot</a> \n" + "<a href=\"#\" style=\"display: none;\" id=\"hidep\" onclick=\"$(\'plot\').hide();$(\'hidep\').hide();$(\'showp\').show();\" />Hide Plot</a><br />\n" + "<applet id=\"plot\" style=\"display: none;\" code=\"FuncPlotter\" width=\"910\" height=\"530\" codebase=\"../static/lib\" archive=\"funcplotter.jar,meparser.jar\">\n" + " <param name=\"app.startup.xInterval\" value=\"-20, 20\"/>\n" + " <param name=\"app.startup.yInterval\" value=\"-5, 5\"/>\n" + " <param name=\"app.startup.function0\" value=\"Sin[x]\"/>\n" + "</applet></div> tag.</p>", wikiModel.render("test <plot xrange=\"-20, 20\" yrange=\"-5, 5\" function=\"Sin[x]\" /> tag."));
    }
}
