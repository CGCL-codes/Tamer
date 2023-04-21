package freeguide.plugins.program.freeguide.migration;

import freeguide.common.lib.general.FileHelper;
import freeguide.plugins.program.freeguide.FreeGuide;
import java.io.File;
import java.util.prefs.BackingStoreException;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class Migrate0_10_6To0_10_7 extends MigrationProcessBase {

    /**
     * Creates a new Migrate0_10_5To0_10_6 object.
     *
     * @param nodeName DOCUMENT ME!
     *
     * @throws BackingStoreException DOCUMENT ME!
     */
    public Migrate0_10_6To0_10_7(final String nodeName) throws BackingStoreException {
        super(nodeName);
    }

    /**
     * DOCUMENT_ME!
     *
     * @throws Exception DOCUMENT_ME!
     */
    public void migrate() throws Exception {
        FreeGuide.log.info("Upgrading preferences 0.10.6 -> 0.10.7");
        boolean isWindows = System.getProperty("os.name").startsWith("Windows");
        if (isWindows) {
            final File xmltvDir = new File(FreeGuide.config.workingDirectory, "xmltv");
            FileHelper.deleteDir(xmltvDir);
        }
        moveNode("");
        getAndRemoveKey("version");
        putKey("version", "0.10.7");
    }
}
