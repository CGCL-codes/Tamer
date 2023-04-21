package net.sf.test.unit;

import java.io.StringReader;
import java.net.URL;
import net.sf.jukebox.conf.Configuration;
import net.sf.jukebox.conf.ConfigurationFactory;
import net.sf.jukebox.service.ActiveService;

/**
 * Test case for <a
 * href="http://sourceforge.net/tracker/index.php?func=detail&aid=589983&group_id=3021&atid=103021">bug
 * #589983"</a>.
 *
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim
 * Tkachenko</a> 2002
 * @version $Id: Bug589983.java,v 1.2 2007-06-14 04:32:23 vtt Exp $
 */
public class Bug589983 extends ActiveService {

    /**
     * Test configuration to use.
     */
    public static final String testCf = "a.b.c.1=A\n" + "a.b.c.2=B\n" + "a.b.c.3=C\n";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void startup() {
    }

    /**
     * Run the test case.
     *
     * @exception Throwable if anything goes wrong.
     */
    @Override
    protected void execute() throws Throwable {
        ConfigurationFactory cff = new ConfigurationFactory();
        Configuration source = cff.getConfiguration(new StringReader(testCf));
        logger.info("Source config: " + source);
        URL tempURL = new URL("file:/tmp/cf");
        cff.storeText(source, tempURL);
        Configuration target = cff.getConfiguration(tempURL);
        logger.info("Target config: " + target);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void shutdown() {
    }
}
