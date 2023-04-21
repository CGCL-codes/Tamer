package com.liferay.portlet.journal.search;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portlet.journal.model.JournalFeed;
import java.util.ArrayList;
import java.util.List;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;

/**
 * <a href="FeedSearch.java.html"><b><i>View Source</i></b></a>
 *
 * @author Raymond Augé
 *
 */
public class FeedSearch extends SearchContainer<JournalFeed> {

    static List<String> headerNames = new ArrayList<String>();

    static {
        headerNames.add("id");
        headerNames.add("description");
    }

    public static final String EMPTY_RESULTS_MESSAGE = "no-feeds-were-found";

    public FeedSearch(RenderRequest renderRequest, PortletURL iteratorURL) {
        super(renderRequest, new FeedDisplayTerms(renderRequest), new FeedSearchTerms(renderRequest), DEFAULT_CUR_PARAM, DEFAULT_DELTA, iteratorURL, headerNames, EMPTY_RESULTS_MESSAGE);
        FeedDisplayTerms displayTerms = (FeedDisplayTerms) getDisplayTerms();
        iteratorURL.setParameter(FeedDisplayTerms.GROUP_ID, String.valueOf(displayTerms.getGroupId()));
        iteratorURL.setParameter(FeedDisplayTerms.FEED_ID, displayTerms.getFeedId());
        iteratorURL.setParameter(FeedDisplayTerms.NAME, displayTerms.getName());
        iteratorURL.setParameter(FeedDisplayTerms.DESCRIPTION, displayTerms.getDescription());
    }
}
