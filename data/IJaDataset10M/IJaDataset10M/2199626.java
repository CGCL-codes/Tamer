package uk.ac.osswatch.simal.wicket.report;

import java.util.Set;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserApplication;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.doap.ProjectBrowserPage;

/**
 * A page for reporting on the status of a set of projects in the repository.
 * This page provides summary information about the projects supplied.
 */
public class ProjectsSummaryReportPage extends BasePage {

    private static final long serialVersionUID = -4022201031446192100L;

    private int numOfProjects;

    public ProjectsSummaryReportPage() {
        try {
            numOfProjects = UserApplication.getRepository().getAllProjects().size();
            add(new Label("numOfProjects", Integer.toString(numOfProjects)));
            populateReviewDetails();
            populateRepositoryDetails();
            populateHomepageDetails();
            populateMailingListDetails();
            populateStaffDetails();
            populateBugDatabaseDetails();
            populateReleaseDetails();
        } catch (SimalRepositoryException e) {
            UserReportableException error = new UserReportableException("Unable to get repository statistics", ProjectsSummaryReportPage.class, e);
            setResponsePage(new ErrorReportPage(error));
        }
    }

    @SuppressWarnings("unchecked")
    private void populateReviewDetails() throws SimalRepositoryException {
        int numOfProjectsWithReview = SimalRepositoryFactory.getProjectService().getProjectsWithReview().size();
        Link link = new ReviewedProjectsLink("reviewedProjectsLink");
        link.add(new Label("numOfProjectsWithReview", Integer.toString(numOfProjectsWithReview)));
        add(link);
        int numOfProjectsWithoutReview = numOfProjects - numOfProjectsWithReview;
        link = new ProjectsToReviewLink("projectsToReviewLink");
        add(link);
        link.add(new Label("numOfProjectsWithoutReview", Integer.toString(numOfProjectsWithoutReview)));
        Double percentOfProjectsWithReview = Double.valueOf(((double) numOfProjectsWithReview / (double) numOfProjects) * 100);
        add(new Label("percentProjectsWithReview", Math.round(percentOfProjectsWithReview) + "%"));
    }

    @SuppressWarnings("unchecked")
    private void populateRepositoryDetails() throws SimalRepositoryException {
        int numOfProjectsWithRCS = SimalRepositoryFactory.getProjectService().getProjectsWithRCS().size();
        Link link = new ProjectsWithRCSLink("projectsWithRCSLink");
        link.add(new Label("numOfProjectsWithRCS", Integer.toString(numOfProjectsWithRCS)));
        add(link);
        int numOfProjectsWithoutRCS = numOfProjects - numOfProjectsWithRCS;
        link = new ProjectsWithoutRCSLink("projectsWithoutRCSLink");
        add(link);
        link.add(new Label("numOfProjectsWithoutRCS", Integer.toString(numOfProjectsWithoutRCS)));
        Double percentOfProjectsWithRCS = Double.valueOf(((double) numOfProjectsWithRCS / (double) numOfProjects) * 100);
        add(new Label("percentProjectsWithRCS", Math.round(percentOfProjectsWithRCS) + "%"));
        int numOfProjectsWithReview = SimalRepositoryFactory.getProjectService().getProjectsWithReview().size();
        Double reviewedPercentOfProjectsWithRCS = Double.valueOf(((double) numOfProjectsWithRCS / (double) numOfProjectsWithReview) * 100);
        add(new Label("reviewedPercentProjectsWithRCS", Math.round(reviewedPercentOfProjectsWithRCS) + "%"));
    }

    @SuppressWarnings("unchecked")
    private void populateHomepageDetails() throws SimalRepositoryException {
        int numOfProjectsWithHomepage = SimalRepositoryFactory.getProjectService().getProjectsWithHomepage().size();
        Link link = new ProjectsWithHomepageLink("projectsWithHomepageLink");
        link.add(new Label("numOfProjectsWithHomepage", Integer.toString(numOfProjectsWithHomepage)));
        add(link);
        int numOfProjectsWithoutHomepage = numOfProjects - numOfProjectsWithHomepage;
        link = new ProjectsWithoutHomepageLink("projectsWithoutHomepageLink");
        link.add(new Label("numOfProjectsWithoutHomepage", Integer.toString(numOfProjectsWithoutHomepage)));
        add(link);
        Double percentOfProjectsWithHomepage = Double.valueOf(((double) numOfProjectsWithHomepage / (double) numOfProjects) * 100);
        add(new Label("percentProjectsWithHomepage", Math.round(percentOfProjectsWithHomepage) + "%"));
        int numOfProjectsWithReview = SimalRepositoryFactory.getProjectService().getProjectsWithReview().size();
        Double reviewedPercentOfProjectsWith = Double.valueOf(((double) numOfProjectsWithHomepage / (double) numOfProjectsWithReview) * 100);
        add(new Label("reviewedPercentProjectsWithHomepage", Math.round(reviewedPercentOfProjectsWith) + "%"));
    }

    @SuppressWarnings("unchecked")
    private void populateMailingListDetails() throws SimalRepositoryException {
        int numOfProjectsWithMailingList = SimalRepositoryFactory.getProjectService().getProjectsWithMailingList().size();
        Link link = new ProjectsWithMailingListLink("projectsWithMailingListLink");
        link.add(new Label("numOfProjectsWithMailingList", Integer.toString(numOfProjectsWithMailingList)));
        add(link);
        int numOfProjectsWithoutMailingList = numOfProjects - numOfProjectsWithMailingList;
        link = new ProjectsWithoutMailingListLink("projectsWithoutMailingListLink");
        link.add(new Label("numOfProjectsWithoutMailingList", Integer.toString(numOfProjectsWithoutMailingList)));
        add(link);
        Double percentOfProjectsWithMailingList = Double.valueOf(((double) numOfProjectsWithMailingList / (double) numOfProjects) * 100);
        add(new Label("percentProjectsWithMailingList", Math.round(percentOfProjectsWithMailingList) + "%"));
        int numOfProjectsWithReview = SimalRepositoryFactory.getProjectService().getProjectsWithReview().size();
        Double reviewedPercentOfProjectsWith = Double.valueOf(((double) numOfProjectsWithMailingList / (double) numOfProjectsWithReview) * 100);
        add(new Label("reviewedPercentProjectsWithMailingList", Math.round(reviewedPercentOfProjectsWith) + "%"));
    }

    @SuppressWarnings("unchecked")
    private void populateStaffDetails() throws SimalRepositoryException {
        int numOfProjectsWithMaintainer = SimalRepositoryFactory.getProjectService().getProjectsWithMaintainer().size();
        Link link = new ProjectsWithMaintainerLink("projectsWithMaintainerLink");
        link.add(new Label("numOfProjectsWithMaintainer", Integer.toString(numOfProjectsWithMaintainer)));
        add(link);
        int numOfProjectsWithoutMaintainer = numOfProjects - numOfProjectsWithMaintainer;
        link = new ProjectsWithoutMaintainerLink("projectsWithoutMaintainerLink");
        link.add(new Label("numOfProjectsWithoutMaintainer", Integer.toString(numOfProjectsWithoutMaintainer)));
        add(link);
        Double percentOfProjectsWithMaintainer = Double.valueOf(((double) numOfProjectsWithMaintainer / (double) numOfProjects) * 100);
        add(new Label("percentProjectsWithMaintainer", Math.round(percentOfProjectsWithMaintainer) + "%"));
        int numOfProjectsWithReview = SimalRepositoryFactory.getProjectService().getProjectsWithReview().size();
        Double reviewedPercentOfProjectsWith = Double.valueOf(((double) numOfProjectsWithMaintainer / (double) numOfProjectsWithReview) * 100);
        add(new Label("reviewedPercentProjectsWithMaintainer", Math.round(reviewedPercentOfProjectsWith) + "%"));
    }

    @SuppressWarnings("unchecked")
    private void populateBugDatabaseDetails() throws SimalRepositoryException {
        int numOfProjectsWithBugDatabase = SimalRepositoryFactory.getProjectService().getProjectsWithBugDatabase().size();
        Link link = new ProjectsWithBugDatabaseLink("projectsWithBugDatabaseLink");
        link.add(new Label("numOfProjectsWithBugDatabase", Integer.toString(numOfProjectsWithBugDatabase)));
        add(link);
        int numOfProjectsWithoutBugDatabase = numOfProjects - numOfProjectsWithBugDatabase;
        link = new ProjectsWithoutBugDatabaseLink("projectsWithoutBugDatabaseLink");
        link.add(new Label("numOfProjectsWithoutBugDatabase", Integer.toString(numOfProjectsWithoutBugDatabase)));
        add(link);
        Double percentOfProjectsWithBugDatabase = Double.valueOf(((double) numOfProjectsWithBugDatabase / (double) numOfProjects) * 100);
        add(new Label("percentProjectsWithBugDatabase", Math.round(percentOfProjectsWithBugDatabase) + "%"));
        int numOfProjectsWithReview = SimalRepositoryFactory.getProjectService().getProjectsWithReview().size();
        Double reviewedPercentOfProjectsWith = Double.valueOf(((double) numOfProjectsWithBugDatabase / (double) numOfProjectsWithReview) * 100);
        add(new Label("reviewedPercentProjectsWithBugDatabase", Math.round(reviewedPercentOfProjectsWith) + "%"));
    }

    @SuppressWarnings("unchecked")
    private void populateReleaseDetails() throws SimalRepositoryException {
        int numOfProjectsWithRelease = SimalRepositoryFactory.getProjectService().getProjectsWithRelease().size();
        Link link = new ProjectsWithReleasesLink("projectsWithReleasesLink");
        link.add(new Label("numOfProjectsWithRelease", Integer.toString(numOfProjectsWithRelease)));
        add(link);
        int numOfProjectsWithoutRelease = numOfProjects - numOfProjectsWithRelease;
        link = new ProjectsWithoutReleasesLink("projectsWithoutReleasesLink");
        link.add(new Label("numOfProjectsWithoutRelease", Integer.toString(numOfProjectsWithoutRelease)));
        add(link);
        Double percentOfProjectsWithRelease = Double.valueOf(((double) numOfProjectsWithRelease / (double) numOfProjects) * 100);
        add(new Label("percentProjectsWithRelease", Math.round(percentOfProjectsWithRelease) + "%"));
        int numOfProjectsWithReview = SimalRepositoryFactory.getProjectService().getProjectsWithReview().size();
        Double reviewedPercentOfProjectsWith = Double.valueOf(((double) numOfProjectsWithRelease / (double) numOfProjectsWithReview) * 100);
        add(new Label("reviewedPercentProjectsWithRelease", Math.round(reviewedPercentOfProjectsWith) + "%"));
    }

    /**
  *
  */
    private static final class ProjectsWithoutReleasesLink extends Link {

        private static final long serialVersionUID = 1L;

        /**
     * @param id
     */
        private ProjectsWithoutReleasesLink(String id) {
            super(id);
        }

        public void onClick() {
            try {
                Set<IProject> projects = SimalRepositoryFactory.getProjectService().getProjectsWithoutRelease();
                ProjectBrowserPage page = new ProjectBrowserPage(projects);
                setResponsePage(page);
            } catch (SimalRepositoryException e) {
                UserReportableException error = new UserReportableException("Unable to get projects without releases", ProjectsSummaryReportPage.class, e);
                setResponsePage(new ErrorReportPage(error));
            }
        }
    }

    /**
  *
  */
    private static final class ProjectsWithReleasesLink extends Link {

        private static final long serialVersionUID = 1L;

        /**
     * @param id
     */
        private ProjectsWithReleasesLink(String id) {
            super(id);
        }

        public void onClick() {
            try {
                Set<IProject> projects = SimalRepositoryFactory.getProjectService().getProjectsWithRelease();
                ProjectBrowserPage page = new ProjectBrowserPage(projects);
                setResponsePage(page);
            } catch (SimalRepositoryException e) {
                UserReportableException error = new UserReportableException("Unable to get projects without releases", ProjectsSummaryReportPage.class, e);
                setResponsePage(new ErrorReportPage(error));
            }
        }
    }

    /**
  *
  */
    private static final class ProjectsWithoutBugDatabaseLink extends Link {

        private static final long serialVersionUID = 1L;

        /**
     * @param id
     */
        private ProjectsWithoutBugDatabaseLink(String id) {
            super(id);
        }

        public void onClick() {
            try {
                Set<IProject> projects = SimalRepositoryFactory.getProjectService().getProjectsWithoutBugDatabase();
                ProjectBrowserPage page = new ProjectBrowserPage(projects);
                setResponsePage(page);
            } catch (SimalRepositoryException e) {
                UserReportableException error = new UserReportableException("Unable to get projects without bug database", ProjectsSummaryReportPage.class, e);
                setResponsePage(new ErrorReportPage(error));
            }
        }
    }

    /**
  *
  */
    private static final class ProjectsWithBugDatabaseLink extends Link {

        private static final long serialVersionUID = 1L;

        /**
     * @param id
     */
        private ProjectsWithBugDatabaseLink(String id) {
            super(id);
        }

        public void onClick() {
            try {
                Set<IProject> projects = SimalRepositoryFactory.getProjectService().getProjectsWithBugDatabase();
                ProjectBrowserPage page = new ProjectBrowserPage(projects);
                setResponsePage(page);
            } catch (SimalRepositoryException e) {
                UserReportableException error = new UserReportableException("Unable to get projects with bug database", ProjectsSummaryReportPage.class, e);
                setResponsePage(new ErrorReportPage(error));
            }
        }
    }

    /**
  *
  */
    private static final class ProjectsWithoutMaintainerLink extends Link {

        private static final long serialVersionUID = 1L;

        /**
     * @param id
     */
        private ProjectsWithoutMaintainerLink(String id) {
            super(id);
        }

        public void onClick() {
            try {
                Set<IProject> projects = SimalRepositoryFactory.getProjectService().getProjectsWithoutMaintainer();
                ProjectBrowserPage page = new ProjectBrowserPage(projects);
                setResponsePage(page);
            } catch (SimalRepositoryException e) {
                UserReportableException error = new UserReportableException("Unable to get projects without maintainer", ProjectsSummaryReportPage.class, e);
                setResponsePage(new ErrorReportPage(error));
            }
        }
    }

    /**
  *
  */
    private static final class ProjectsWithMaintainerLink extends Link {

        private static final long serialVersionUID = 1L;

        /**
     * @param id
     */
        private ProjectsWithMaintainerLink(String id) {
            super(id);
        }

        public void onClick() {
            try {
                Set<IProject> projects = SimalRepositoryFactory.getProjectService().getProjectsWithMaintainer();
                ProjectBrowserPage page = new ProjectBrowserPage(projects);
                setResponsePage(page);
            } catch (SimalRepositoryException e) {
                UserReportableException error = new UserReportableException("Unable to get projects with maintainer", ProjectsSummaryReportPage.class, e);
                setResponsePage(new ErrorReportPage(error));
            }
        }
    }

    /**
  *
  */
    private static final class ProjectsWithoutMailingListLink extends Link {

        private static final long serialVersionUID = 1L;

        /**
     * @param id
     */
        private ProjectsWithoutMailingListLink(String id) {
            super(id);
        }

        public void onClick() {
            try {
                Set<IProject> projects = SimalRepositoryFactory.getProjectService().getProjectsWithoutMailingList();
                ProjectBrowserPage page = new ProjectBrowserPage(projects);
                setResponsePage(page);
            } catch (SimalRepositoryException e) {
                UserReportableException error = new UserReportableException("Unable to get projects without mailing list", ProjectsSummaryReportPage.class, e);
                setResponsePage(new ErrorReportPage(error));
            }
        }
    }

    /**
  *
  */
    private static final class ProjectsWithMailingListLink extends Link {

        private static final long serialVersionUID = 1L;

        /**
     * @param id
     */
        private ProjectsWithMailingListLink(String id) {
            super(id);
        }

        public void onClick() {
            try {
                Set<IProject> projects = SimalRepositoryFactory.getProjectService().getProjectsWithMailingList();
                ProjectBrowserPage page = new ProjectBrowserPage(projects);
                setResponsePage(page);
            } catch (SimalRepositoryException e) {
                UserReportableException error = new UserReportableException("Unable to get projects with mailing list", ProjectsSummaryReportPage.class, e);
                setResponsePage(new ErrorReportPage(error));
            }
        }
    }

    /**
  *
  */
    private static final class ProjectsWithoutHomepageLink extends Link {

        private static final long serialVersionUID = 1L;

        /**
     * @param id
     */
        private ProjectsWithoutHomepageLink(String id) {
            super(id);
        }

        public void onClick() {
            try {
                Set<IProject> projects = SimalRepositoryFactory.getProjectService().getProjectsWithoutHomepage();
                ProjectBrowserPage page = new ProjectBrowserPage(projects);
                setResponsePage(page);
            } catch (SimalRepositoryException e) {
                UserReportableException error = new UserReportableException("Unable to get projects without homepage", ProjectsSummaryReportPage.class, e);
                setResponsePage(new ErrorReportPage(error));
            }
        }
    }

    /**
  *
  */
    private static final class ProjectsWithHomepageLink extends Link {

        private static final long serialVersionUID = 1L;

        /**
     * @param id
     */
        private ProjectsWithHomepageLink(String id) {
            super(id);
        }

        public void onClick() {
            try {
                Set<IProject> projects = SimalRepositoryFactory.getProjectService().getProjectsWithHomepage();
                ProjectBrowserPage page = new ProjectBrowserPage(projects);
                setResponsePage(page);
            } catch (SimalRepositoryException e) {
                UserReportableException error = new UserReportableException("Unable to get projects with homepage", ProjectsSummaryReportPage.class, e);
                setResponsePage(new ErrorReportPage(error));
            }
        }
    }

    /**
  *
  */
    private static final class ProjectsWithoutRCSLink extends Link {

        private static final long serialVersionUID = 1L;

        /**
     * @param id
     */
        private ProjectsWithoutRCSLink(String id) {
            super(id);
        }

        public void onClick() {
            try {
                Set<IProject> projects = SimalRepositoryFactory.getProjectService().getProjectsWithoutRCS();
                ProjectBrowserPage page = new ProjectBrowserPage(projects);
                setResponsePage(page);
            } catch (SimalRepositoryException e) {
                UserReportableException error = new UserReportableException("Unable to get projects without RCS", ProjectsSummaryReportPage.class, e);
                setResponsePage(new ErrorReportPage(error));
            }
        }
    }

    /**
  *
  */
    private static final class ProjectsWithRCSLink extends Link {

        private static final long serialVersionUID = 1L;

        /**
     * @param id
     */
        private ProjectsWithRCSLink(String id) {
            super(id);
        }

        public void onClick() {
            try {
                Set<IProject> projects = SimalRepositoryFactory.getProjectService().getProjectsWithRCS();
                ProjectBrowserPage page = new ProjectBrowserPage(projects);
                setResponsePage(page);
            } catch (SimalRepositoryException e) {
                UserReportableException error = new UserReportableException("Unable to get projects with RCS", ProjectsSummaryReportPage.class, e);
                setResponsePage(new ErrorReportPage(error));
            }
        }
    }

    /**
  *
  */
    private static final class ProjectsToReviewLink extends Link {

        private static final long serialVersionUID = 1L;

        /**
     * @param id
     */
        private ProjectsToReviewLink(String id) {
            super(id);
        }

        public void onClick() {
            try {
                Set<IProject> projects = SimalRepositoryFactory.getProjectService().getProjectsWithoutReview();
                ProjectBrowserPage page = new ProjectBrowserPage(projects);
                setResponsePage(page);
            } catch (SimalRepositoryException e) {
                UserReportableException error = new UserReportableException("Unable to get projects needing review", ProjectsSummaryReportPage.class, e);
                setResponsePage(new ErrorReportPage(error));
            }
        }
    }

    /**
  *
  */
    private static final class ReviewedProjectsLink extends Link {

        private static final long serialVersionUID = 1L;

        /**
     * @param id
     */
        private ReviewedProjectsLink(String id) {
            super(id);
        }

        public void onClick() {
            try {
                Set<IProject> projects = SimalRepositoryFactory.getProjectService().getProjectsWithReview();
                ProjectBrowserPage page = new ProjectBrowserPage(projects);
                setResponsePage(page);
            } catch (SimalRepositoryException e) {
                UserReportableException error = new UserReportableException("Unable to get projects with review", ProjectsSummaryReportPage.class, e);
                setResponsePage(new ErrorReportPage(error));
            }
        }
    }
}
