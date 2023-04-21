package net.sf.buildbox.releasator;

import java.util.concurrent.Callable;
import net.sf.buildbox.args.api.ArgsCommand;
import net.sf.buildbox.releasator.ng.ScmAdapter;
import net.sf.buildbox.releasator.ng.ScmAdapterManager;

/**
 * README
 * releasator - a tool to produce reproducible project releases
 *
 * Subcommands
 * - prepare - the tag-making part only
 * - upload - build from a tag and upload (output artifacts, website, details) to target destination
 * - release - all in one
 *
 * Global flags
 * --pretend  commits+pushes against local clone, uploads to a local filesystem or designated trash target
 *
 * Output:
 * - info file:
 *   - tag url
 *   - download url
 *   - artifact ids
 *   - system info snapshot (jdk version, maven version, env vars, ...)
 *
 * - files:
 *   - output artifacts
 *   - all logs
 *   - repository listing (with sha1)
 *   - website
 *
 */
public class SpringMain {

    public static void main(String[] args) throws Exception {
        final Callable<Integer> command = new DemoCmd();
        if (command instanceof UsesSpringBeans) {
            ((UsesSpringBeans) command).useSpringBeans(SpringBeans.INSTANCE);
        }
        final int exitCode = command.call();
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }
}

class DemoCmd implements ArgsCommand, UsesSpringBeans {

    public ScmAdapterManager scmAdapterManager;

    public void useSpringBeans(SpringBeans springBeans) {
        scmAdapterManager = springBeans.getScmAdapterManager();
    }

    public Integer call() throws Exception {
        System.out.println("scmAdapterManager = " + scmAdapterManager);
        final ScmAdapter scm = scmAdapterManager.create("scm:svn:https://buildbox.svn.sourceforge.net/svnroot/buildbox/trunk/tools/releasator");
        final String fullTagName = scm.getFullTagName("net.sf.buildbox", "releasator", "2.0.0");
        System.out.println("fullTagName = " + fullTagName);
        final String tagCheckoutCommandHint = scm.getTagCheckoutCommandHint(fullTagName);
        System.out.println("tagCheckoutCommandHint = " + tagCheckoutCommandHint);
        scm.checkout();
        return 0;
    }
}
