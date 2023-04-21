package org.fpse.forum.experts.exchange;

import org.fpse.download.Downloader;
import org.fpse.forum.Forum;
import org.fpse.forum.ForumConfiguration;

/**
 * Created on Dec 17, 2006 12:37:26 AM by Ajay
 */
public class ExpertsExchangeForum extends Forum {

    private Downloader m_downloader;

    public ExpertsExchangeForum(String name, String downloadLocation, ForumConfiguration config) {
        super(name, downloadLocation, config);
    }

    public Downloader getDownloader() {
        return m_downloader;
    }

    public void setDownloader(Downloader downloader) {
        m_downloader = downloader;
    }
}
