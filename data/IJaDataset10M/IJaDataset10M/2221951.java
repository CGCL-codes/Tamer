package uk.ac.osswatch.simal.wicket.report;

import org.apache.wicket.Page;
import org.apache.wicket.util.tester.ITestPageSource;
import org.junit.Before;
import org.junit.Test;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.TestBase;

public class TestProjectsSummaryReport extends TestBase {

    @SuppressWarnings("serial")
    @Before
    public void initTester() throws SimalRepositoryException {
        tester.startPage(new ITestPageSource() {

            public Page getTestPage() {
                return new ProjectsSummaryReportPage();
            }
        });
        tester.assertRenderedPage(ProjectsSummaryReportPage.class);
    }

    @Test
    public void testProjectFigures() {
        tester.assertVisible("numOfProjects");
        tester.assertLabel("numOfProjects", "9");
    }

    @Test
    public void testRepositoryFigures() {
        tester.assertVisible("projectsWithRCSLink:numOfProjectsWithRCS");
        tester.assertLabel("projectsWithRCSLink:numOfProjectsWithRCS", "2");
        tester.assertVisible("projectsWithoutRCSLink:numOfProjectsWithoutRCS");
        tester.assertLabel("projectsWithoutRCSLink:numOfProjectsWithoutRCS", "7");
        tester.assertVisible("percentProjectsWithRCS");
        tester.assertLabel("percentProjectsWithRCS", "22%");
    }

    @Test
    public void testHomepageFigures() {
        tester.assertVisible("projectsWithHomepageLink:numOfProjectsWithHomepage");
        tester.assertLabel("projectsWithHomepageLink:numOfProjectsWithHomepage", "8");
        tester.assertVisible("projectsWithoutHomepageLink:numOfProjectsWithoutHomepage");
        tester.assertLabel("projectsWithoutHomepageLink:numOfProjectsWithoutHomepage", "1");
        tester.assertVisible("percentProjectsWithHomepage");
        tester.assertLabel("percentProjectsWithHomepage", "89%");
    }

    @Test
    public void testMailingListFigures() {
        tester.assertVisible("projectsWithMailingListLink:numOfProjectsWithMailingList");
        tester.assertLabel("projectsWithMailingListLink:numOfProjectsWithMailingList", "4");
        tester.assertVisible("projectsWithoutMailingListLink:numOfProjectsWithoutMailingList");
        tester.assertLabel("projectsWithoutMailingListLink:numOfProjectsWithoutMailingList", "5");
        tester.assertVisible("percentProjectsWithMailingList");
        tester.assertLabel("percentProjectsWithMailingList", "44%");
    }

    @Test
    public void testMaintainerFigures() {
        tester.assertVisible("projectsWithMaintainerLink:numOfProjectsWithMaintainer");
        tester.assertLabel("projectsWithMaintainerLink:numOfProjectsWithMaintainer", "7");
        tester.assertVisible("projectsWithoutMaintainerLink:numOfProjectsWithoutMaintainer");
        tester.assertLabel("projectsWithoutMaintainerLink:numOfProjectsWithoutMaintainer", "2");
        tester.assertVisible("percentProjectsWithMaintainer");
        tester.assertLabel("percentProjectsWithMaintainer", "78%");
    }

    @Test
    public void testBugDatabaseFigures() {
        tester.assertVisible("projectsWithBugDatabaseLink:numOfProjectsWithBugDatabase");
        tester.assertLabel("projectsWithBugDatabaseLink:numOfProjectsWithBugDatabase", "4");
        tester.assertVisible("projectsWithoutBugDatabaseLink:numOfProjectsWithoutBugDatabase");
        tester.assertLabel("projectsWithoutBugDatabaseLink:numOfProjectsWithoutBugDatabase", "5");
        tester.assertVisible("percentProjectsWithBugDatabase");
        tester.assertLabel("percentProjectsWithBugDatabase", "44%");
    }
}
