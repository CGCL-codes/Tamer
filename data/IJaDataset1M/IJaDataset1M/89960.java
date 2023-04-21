package net.sf.lavalamp;

import java.io.IOException;
import net.sf.lavalamp.site.BuildSite;
import net.sf.lavalamp.site.Console;
import net.sf.lavalamp.site.LoginRequiredException;

public class BuildsChecker {

    private LavaLogger logger = new LavaLogger();

    public void check(Builds builds, Console console) throws IOException, LoginRequiredException {
        for (Build build : builds.getBuilds()) {
            logger.debug("checking " + build.getIdentity());
            BuildSite site = build.getBuildSite();
            try {
                check(build, site.wasLastBuildSuccessful(build));
            } catch (LoginRequiredException e) {
                site.setCredentials(console);
                site.login();
                check(build, site.wasLastBuildSuccessful(build));
            }
        }
    }

    private void check(Build build, boolean successful) {
        if (successful) {
            logger.debug("ok");
            build.setSuccessful(true);
        } else {
            logger.debug("failed");
            build.setSuccessful(false);
        }
    }
}
