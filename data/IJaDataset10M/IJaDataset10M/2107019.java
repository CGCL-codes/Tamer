package simpleorm.simpleweb.eg.database;

import simpleorm.simpleweb.core.WPage;
import simpleorm.simpleweb.core.WPagelet;
import simpleorm.simpleweb.eg.dbute.WCreateDB;
import simpleorm.core.SConstants;

/**
 * The scratch init form that creates the database.
 */
public class WDumpRawPage extends WPage implements SConstants {

    public final WDumpRawPage.WInitPagelet pagelet = new WDumpRawPage.WInitPagelet(this);

    public static class WInitPagelet extends WPagelet {

        public WInitPagelet(WDumpRawPage page) {
            super(page, "init");
        }

        protected void onPreMaybeSubmitted() throws Exception {
            WCreateDB.dumpRaw(getPage(), "RSA_USER");
        }
    }
}
