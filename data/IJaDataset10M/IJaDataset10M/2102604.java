package com.web.robot.impl;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import com.web.robot.interfaces.BookmarkProcessor;
import com.web.robot.interfaces.BookmarkService;
import com.web.robot.interfaces.PageDownloader;
import com.web.robot.interfaces.RssParser;
import com.web.robot.model.Bookmark;

/**
 * Main implementation of bookmark processing module. Input is list of URLs for
 * RSS with bookmarks (adapted for http://del.icio.us). Each RSS is downloaded,
 * saved on disc and then parsed to extract bookmark information. Finally,
 * bookmarks are saved to the database, ignoring repeated URLs.
 * 
 * @author avasiljeva
 */
public class MainBookmarkProcessor implements BookmarkProcessor {

    private final Logger log = Logger.getLogger(MainBookmarkProcessor.class);

    private List<String> urlList;

    private PageDownloader pageDownloader;

    private RssParser rssParser;

    private BookmarkService bookmarkService;

    /**
	 * Each URL from the input list is processed as follows: - page is
	 * downloaded and saved on disc - file is parsed and list of bookmarks is
	 * extracted Finally, bookmarks from all sources are joined in one list.
	 */
    public List<Bookmark> loadBookmarks() {
        log.info("Starting to load bookmarks");
        List<Bookmark> bookmarks = new LinkedList<Bookmark>();
        for (String url : urlList) {
            log.debug("Starting to process URL [" + url + "]");
            String fileName = pageDownloader.downloadPage(url);
            log.debug("Page downloaded and saved to file [" + fileName + "]");
            String mainResourceName = url.replaceAll("/rss", "");
            List<Bookmark> result = rssParser.extractBookmarks(fileName, mainResourceName);
            log.debug("[" + result.size() + "] bookmarks extracted from [" + fileName + "]");
            bookmarks.addAll(result);
            File file = new File(fileName);
            file.delete();
        }
        log.info("Totally [" + bookmarks.size() + "] bookmarks loaded");
        return bookmarks;
    }

    /**
	 * Bookmarks are saved to the database, ignoring only those with URLs
	 * already presented. To determine if bookmark is already present, first all
	 * existing bookmarks are loaded from DB.
	 * 
	 * @param bookmarks -
	 *            list of bookmark objects
	 */
    public void processBookmarks(List<Bookmark> bookmarks) {
        log.info("Starting to process bookmarks");
        List<Bookmark> existingBookmarks = bookmarkService.findAll();
        List<String> existingURLs = new LinkedList<String>();
        for (Bookmark b : existingBookmarks) {
            existingURLs.add(b.getUrl());
        }
        int saved = 0;
        int ignored = 0;
        for (Bookmark bookmark : bookmarks) {
            if (!existingURLs.contains(bookmark.getUrl())) {
                bookmarkService.save(bookmark);
                saved++;
                log.debug("[Bookmark saved] " + bookmark.toString());
            } else {
                ignored++;
                log.debug("[Bookmark already exists] " + bookmark.toString());
            }
        }
        log.info("Bookmarks processed: [" + saved + "] saved, [" + ignored + "] ignored");
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public PageDownloader getPageDownloader() {
        return pageDownloader;
    }

    public void setPageDownloader(PageDownloader pageDownloader) {
        this.pageDownloader = pageDownloader;
    }

    public RssParser getRssParser() {
        return rssParser;
    }

    public void setRssParser(RssParser rssParser) {
        this.rssParser = rssParser;
    }

    public BookmarkService getBookmarkService() {
        return bookmarkService;
    }

    public void setBookmarkService(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }
}
