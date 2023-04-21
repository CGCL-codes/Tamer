package com.liferay.portal.mirage.service;

import com.liferay.portal.mirage.aop.ContentFeedInvoker;
import com.liferay.portal.mirage.aop.SearchCriteriaInvoker;
import com.liferay.portal.mirage.model.MirageJournalFeed;
import com.liferay.portlet.journal.model.JournalFeed;
import com.sun.portal.cms.mirage.exception.CMSException;
import com.sun.portal.cms.mirage.model.custom.ContentFeed;
import com.sun.portal.cms.mirage.model.custom.OptionalCriteria;
import com.sun.portal.cms.mirage.model.custom.UpdateCriteria;
import com.sun.portal.cms.mirage.model.search.SearchCriteria;
import com.sun.portal.cms.mirage.service.custom.ContentFeedService;
import java.util.ArrayList;
import java.util.List;

/**
 * <a href="ContentFeedServiceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Karthik Sudarshan
 * @author Brian Wing Shun Chan
 *
 */
public class ContentFeedServiceImpl implements ContentFeedService {

    public void createContentFeed(ContentFeed contentFeed) throws CMSException {
        ContentFeedInvoker contentFeedInvoker = (ContentFeedInvoker) contentFeed;
        contentFeedInvoker.invoke();
    }

    public void deleteContentFeed(ContentFeed contentFeed) throws CMSException {
        ContentFeedInvoker contentFeedInvoker = (ContentFeedInvoker) contentFeed;
        contentFeedInvoker.invoke();
    }

    public ContentFeed getContentFeed(ContentFeed contentFeed, OptionalCriteria optionalCriteria) throws CMSException {
        ContentFeedInvoker contentFeedInvoker = (ContentFeedInvoker) contentFeed;
        contentFeedInvoker.invoke();
        JournalFeed feed = (JournalFeed) contentFeedInvoker.getReturnValue();
        return new MirageJournalFeed(feed);
    }

    public int getContentFeedSearchCount(SearchCriteria searchCriteria) throws CMSException {
        SearchCriteriaInvoker searchCriteriaInvoker = (SearchCriteriaInvoker) searchCriteria;
        searchCriteriaInvoker.invoke();
        Integer i = (Integer) searchCriteriaInvoker.getReturnValue();
        return i.intValue();
    }

    public List<ContentFeed> searchContentFeeds(SearchCriteria searchCriteria) throws CMSException {
        SearchCriteriaInvoker searchCriteriaInvoker = (SearchCriteriaInvoker) searchCriteria;
        searchCriteriaInvoker.invoke();
        List<JournalFeed> feeds = (List<JournalFeed>) searchCriteriaInvoker.getReturnValue();
        List<ContentFeed> contentFeeds = new ArrayList<ContentFeed>(feeds.size());
        for (JournalFeed feed : feeds) {
            contentFeeds.add(new MirageJournalFeed(feed));
        }
        return contentFeeds;
    }

    public void updateContentFeed(ContentFeed contentFeed, UpdateCriteria updateCriteria) throws CMSException {
        ContentFeedInvoker contentFeedInvoker = (ContentFeedInvoker) contentFeed;
        contentFeedInvoker.invoke();
    }
}
