package net.sourceforge.jwbf.mediawiki.live;

import static net.sourceforge.jwbf.TestHelper.assumeReachable;
import static net.sourceforge.jwbf.mediawiki.BotFactory.getMediaWikiBot;
import net.sourceforge.jwbf.core.actions.util.ActionException;
import net.sourceforge.jwbf.mediawiki.VersionTestClassVerifier;
import net.sourceforge.jwbf.mediawiki.actions.MediaWiki.Version;
import net.sourceforge.jwbf.mediawiki.actions.misc.GetRendering;
import net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Verifier;

/**
 * 
 * @author Thomas Stock
 * 
 */
public class RenderingTest extends AbstractMediaWikiBotTest {

    @ClassRule
    public static VersionTestClassVerifier classVerifier = new VersionTestClassVerifier(GetRendering.class);

    @Rule
    public Verifier successRegister = classVerifier.getSuccessRegister(this);

    /**
   * 
   * @throws Exception
   *           a
   */
    @Test
    public final void getRenderingWikipediaDe() throws Exception {
        String liveUrl = "http://de.wikipedia.org/w/index.php";
        assumeReachable(liveUrl);
        bot = new MediaWikiBot(liveUrl);
        doTest(bot);
    }

    /**
   * 
   * @throws Exception
   *           a
   */
    @Test(expected = ActionException.class)
    public final void getRenderingPerformManual() throws Exception {
        String liveUrl = "http://de.wikipedia.org/w/index.php";
        assumeReachable(liveUrl);
        bot = new MediaWikiBot(liveUrl);
        GetRendering r = new GetRendering(bot, "bert");
        bot.performAction(r);
    }

    /**
   * 
   * @throws Exception
   *           a
   */
    @Test
    public final void getRenderingMW1x15() throws Exception {
        bot = getMediaWikiBot(Version.MW1_15, true);
        doTest(bot);
        Assert.assertTrue("Wrong Wiki Version " + bot.getVersion(), Version.MW1_15.equals(bot.getVersion()));
    }

    /**
   * 
   * @throws Exception
   *           a
   */
    @Test
    public final void getRenderingMW1x16() throws Exception {
        bot = getMediaWikiBot(Version.MW1_16, true);
        doTest(bot);
        Assert.assertTrue("Wrong Wiki Version " + bot.getVersion(), Version.MW1_16.equals(bot.getVersion()));
    }

    private void doTest(MediaWikiBot bot) throws Exception {
        GetRendering r = new GetRendering(bot, "bert");
        Assert.assertEquals("<p>bert</p>", r.getHtml());
    }
}
