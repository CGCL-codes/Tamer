package fr.loria.ecoo.wootEngine.test;

import fr.loria.ecoo.clockEngine.Persistent.PersistentClock;
import fr.loria.ecoo.wootEngine.WootEngine;
import junit.framework.TestCase;
import java.io.File;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class WootEngineCase extends TestCase {

    protected String workingDir = "/tmp/testsWoot";

    /**
     * Creates a new WootEngineCase object.
     *
     * @param name DOCUMENT ME!
     */
    public WootEngineCase(String name) {
        super(name);
        if (!new File(this.workingDir).exists()) {
            new File(this.workingDir).mkdirs();
        }
    }

    protected WootEngine createEngine(int id) throws Exception {
        File working = new File(this.workingDir);
        if (!working.exists()) {
            if (!working.mkdir()) {
                throw new RuntimeException("Can't create clocks directory: " + this.workingDir);
            }
        }
        File testsDir = new File(this.workingDir + File.separator + id);
        if (!testsDir.exists()) {
            if (!testsDir.mkdir()) {
                throw new RuntimeException("Can't create clocks directory: " + this.workingDir);
            }
        }
        PersistentClock clock = new PersistentClock(testsDir.toString() + File.separator + "clock");
        WootEngine wootEngine = new WootEngine(id, testsDir.toString(), clock);
        return wootEngine;
    }

    protected void cleanTests(String directory) throws Exception {
        File rootDir = new File(directory);
        if (rootDir.exists()) {
            String[] children = rootDir.list();
            for (String s : children) {
                File toErase = new File(directory, s);
                if (toErase.isDirectory()) {
                    cleanTests(toErase.toString());
                } else {
                    toErase.delete();
                }
            }
            rootDir.delete();
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.out.println("\n******\n* " + this.getName() + "\n******\n");
        this.cleanTests(this.workingDir + File.separator);
    }
}
