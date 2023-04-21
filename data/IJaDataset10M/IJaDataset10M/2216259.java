package com.liferay.portal.mirage.model;

import com.liferay.portlet.journal.model.JournalFeed;
import com.sun.portal.cms.mirage.model.custom.ContentFeed;

/**
 * <a href="JournalContentFeed.java.html"><b><i>View Source</i></b></a>
 *
 * @author Karthik Sudarshan
 *
 */
public class JournalContentFeed extends ContentFeed {

    public class CreationAttributes {

        public CreationAttributes(boolean autoCreateId) {
            _autoCreateId = autoCreateId;
        }

        public boolean isAutoCreateId() {
            return _autoCreateId;
        }

        private boolean _autoCreateId;
    }

    public JournalContentFeed(JournalFeed feed) {
        _feed = feed;
    }

    public JournalFeed getFeed() {
        return _feed;
    }

    public void setFeed(JournalFeed feed) {
        _feed = feed;
    }

    public JournalContentFeed.CreationAttributes getCreationAttributes() {
        return _creationAttributes;
    }

    public void setCreationAttributes(JournalContentFeed.CreationAttributes creationAttributes) {
        _creationAttributes = creationAttributes;
    }

    private JournalFeed _feed;

    private CreationAttributes _creationAttributes;
}
