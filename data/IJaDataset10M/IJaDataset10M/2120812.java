package org.encuestame.core.service.imp;

import org.encuestame.core.service.ServiceOperations;

/**
 * Interface to Application Bean Service.
 * @author Picado, Juan juanATencuestame.org
 * @since 11/05/2009 14:35:21
 * @version $Id$
 */
public interface IApplicationServices extends ServiceOperations {

    /**
     * Getter.
     * @return {@link SecurityOperations}
     */
    SecurityOperations getSecurityService();

    /**
     * Setter.
     * @param securityService {@link SecurityOperations}
     */
    void setSecurityService(SecurityOperations securityService);

    /**
     * @return the surveyService
     */
    ISurveyService getSurveyService();

    /**
     * @param surveyService the surveyService to set
     */
    void setSurveyService(ISurveyService surveyService);

    /**
     * @return the pollService
     */
    IPollService getPollService();

    /**
     * @param pollService the pollService to set
     */
    void setPollService(IPollService pollService);

    /**
     * @return the tweetPollService
     */
    ITweetPollService getTweetPollService();

    /**
     * @param tweetPollService the tweetPollService to set
     */
    void setTweetPollService(ITweetPollService tweetPollService);

    /**
     * @param projectService the projectService to set
     */
    void setProjectService(final IProjectService projectService);

    /**
     * @return the projectService
     */
    IProjectService getProjectService();

    /**
     * @param locationService the locationService to set
     */
    void setLocationService(final GeoLocationSupport locationService);

    /**
     * @return the locationService
     */
    GeoLocationSupport getLocationService();

    /**
     *
     * @return
     */
    IFrontEndService getFrontEndService();

    /**
     * @param frontEndService the frontEndService to set
     */
    void setFrontEndService(final IFrontEndService frontEndService);

    /**
     *
     * @return
     */
    IPictureService getPictureService();

    /**
     *
     * @param pictureService
     */
    void setPictureService(IPictureService pictureService);

    /**
     *
     * @return
     */
    SearchServiceOperations getSearchService();

    /**
     *
     * @param searchService
     */
    void setSearchService(SearchServiceOperations searchService);

    /**
     * @return the dashboard service.
     */
    IDashboardService getDashboardService();

    /**
     *
     * @param dashboardService
     */
    void setDashboardService(final IDashboardService dashboardService);

    /**
     *
     * @return
     */
    StreamOperations getStreamOperations();

    /**
     *
     * @param streamOperations
     */
    void setStreamOperations(final StreamOperations streamOperations);

    /**
     *
     * @return
     */
    ICommentService getCommentService();

    /**
     * @param commentService the commentService to set
     */
    void setCommentService(final ICommentService commentService);
}
