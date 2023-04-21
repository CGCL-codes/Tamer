package de.is24.nexus.yum.plugin.integration;

import static java.util.concurrent.TimeUnit.SECONDS;
import org.junit.Test;
import org.sonatype.nexus.integrationtests.TestContainer;

public class CreateNewRpmRepositoryIT extends AbstractNexusTestBase {

    private static final String DUMMY_ARTIFACT = "dummy-rpm";

    private static final String GROUP_ID = "de.is24.test";

    private static final String ARTIFACT_VERSION_1 = "1.0";

    private static final String ARTIFACT_VERSION_2 = "2.0";

    private static final String NEW_REPO_ID = "next-try";

    static {
        TestContainer.getInstance().getTestContext().setSecureTest(true);
    }

    @Test
    public void shouldCreateRepoRpmsForNewRpmsInNewRepository() throws Exception {
        givenTestRepository(NEW_REPO_ID);
        wait(5, SECONDS);
        deployRpmToRepo(DUMMY_ARTIFACT, GROUP_ID, ARTIFACT_VERSION_1, NEW_REPO_ID);
        deployRpmToRepo(DUMMY_ARTIFACT, GROUP_ID, ARTIFACT_VERSION_2, NEW_REPO_ID);
        wait(5, SECONDS);
        executeGet("/yum/repo/is24-rel-" + NEW_REPO_ID + "-" + ARTIFACT_VERSION_1 + "-repo-1-1.noarch.rpm");
        executeGet("/yum/repo/is24-rel-" + NEW_REPO_ID + "-" + ARTIFACT_VERSION_2 + "-repo-1-1.noarch.rpm");
    }
}
