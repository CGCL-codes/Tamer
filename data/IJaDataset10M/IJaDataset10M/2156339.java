package net.sourceforge.jwbf.live;

import net.sourceforge.jwbf.LiveTestFather;
import net.sourceforge.jwbf.bots.MediaWikiBot;
import net.sourceforge.jwbf.contentRep.mw.Siteinfo;
import net.sourceforge.jwbf.contentRep.mw.Version;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Thomas Stock
 *
 */
public class SiteinfoTest extends LiveTestFather {

    private MediaWikiBot bot = null;

    /**
	 * Setup log4j.
	 * @throws Exception a
	 */
    @BeforeClass
    public static void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("test4log4j.properties", 60 * 1000);
    }

    public final void siteInfoWikipediaDe() throws Exception {
        bot = new MediaWikiBot("http://de.wikipedia.org/w/index.php");
        Siteinfo is = bot.getSiteinfo();
        System.out.println(is);
    }

    /**
	 * Test get siteinfo on a MW.
	 * @throws Exception a
	 */
    @Test
    public final void siteInfoMW1_09() throws Exception {
        bot = new MediaWikiBot(getValue("wikiMW1_09_url"));
        Assert.assertEquals(bot.getVersion(), Version.MW1_09);
    }

    /**
	 * Test get siteinfo on a MW.
	 * @throws Exception a
	 */
    @Test
    public final void siteInfoMW1_10() throws Exception {
        bot = new MediaWikiBot(getValue("wikiMW1_10_url"));
        Assert.assertEquals(bot.getVersion(), Version.MW1_10);
    }

    /**
	 * Test get siteinfo on a MW.
	 * @throws Exception a
	 */
    @Test
    public final void siteInfoMW1_11() throws Exception {
        bot = new MediaWikiBot(getValue("wikiMW1_11_url"));
        Assert.assertEquals(bot.getVersion(), Version.MW1_11);
    }

    /**
	 * Test get siteinfo on a MW.
	 * @throws Exception a
	 */
    @Test
    public final void siteInfoMW1_12() throws Exception {
        bot = new MediaWikiBot(getValue("wikiMW1_12_url"));
        Assert.assertEquals(bot.getVersion(), Version.MW1_12);
    }

    /**
	 * Test get siteinfo on a MW.
	 * @throws Exception a
	 */
    @Test
    public final void siteInfoMW1_13() throws Exception {
        bot = new MediaWikiBot(getValue("wikiMW1_13_url"));
        Assert.assertEquals(bot.getVersion(), Version.MW1_13);
    }
}
