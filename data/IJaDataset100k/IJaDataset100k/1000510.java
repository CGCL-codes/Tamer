package vbullmin;

import vbullmin.gui.GUI;

/**
 * Main part of the Bot
 * <p>This class is controlling the URL's and getting them.
 * Parser parsing and adding into database to content.</p> 
 * 
 * @author Onur Aslan
 * @version 1.0
 */
public class Bot {

    /**
   * Database reference of the current database
   */
    public DB db;

    /**
   * vBulletin board url
   */
    private String url;

    /**
   * Constructor controlling vbulletin and charset
   * @param db
   */
    public Bot(DB db, String url, String archivePath) {
        this.db = db;
        this.url = url + archivePath;
        Patterns.getPatterns(GUI.urls.pattern);
    }

    /**
   * Checking vBulletin board
   */
    public void check() {
        new Get(url).control();
        GUI.progress.setValue(1);
    }

    /**
   * Parsing forums from /archive/index.php/
   */
    public void forums() {
        String content = new Get(url).toString();
        Parser parser = new Parser(db, content);
        parser.forums();
        GUI.progress.setValue(1);
    }

    /**
   * Looking the forums pagecount<p>
   * All forums ids getting from db.forums function.</p>
   * @see DB#forums()
   */
    public void forumsPageCount() {
        final int[] forums = db.forums();
        for (int i = 0; i < forums.length; i++) {
            String regex = String.format(Patterns.forumsPageCountUrl, forums[i]);
            String content;
            content = new Get(url + regex).toString();
            Parser parser = new Parser(db, content);
            parser.forumPageCount(forums[i]);
            GUI.progress.setValue(i);
        }
    }

    /**
   * Getting topics page counts from all topics
   * after then getting all posts<p>
   * <strong>FIX THIS:</strong> DB.topis () selecting all topics id from database.<br />
   * This can be lack the program.</p>
   * <p>Page number (j) starting from one for all topics</p>
   * @see DB#topics()
   */
    public void posts() {
        int[] topics = db.topics();
        for (int i = 0; i < topics.length; i++) {
            String topicUrl = url + String.format(Patterns.topicsPageCountUrl, topics[i]);
            String content = new Get(topicUrl).toString();
            Parser parser = new Parser(db, content);
            int pageCount = parser.topicPageCount(topics[i]);
            for (int j = 1; j <= pageCount; j++) {
                String regex = String.format(Patterns.postsUrl, topics[i], j);
                content = new Get(url + regex).toString();
                parser = new Parser(db, content);
                int addedPostCount = parser.posts(topics[i]);
                db.postCountInc(topics[i], addedPostCount);
            }
            GUI.progress.setValue(i);
        }
    }

    /**
   * Parsing topics from each forums each page
   */
    public void topics() {
        final int[] forums = db.forums();
        for (int i = 0; i < forums.length; i++) {
            for (int j = 1; j <= db.forumPageCount(forums[i]); j++) {
                final int index = j;
                String content = new Get(url + "f-" + forums[i] + "-p-" + index + ".html").toString();
                Parser parser = new Parser(db, content);
                parser.topics(forums[i]);
            }
            GUI.progress.setValue(i + 1);
        }
    }
}
