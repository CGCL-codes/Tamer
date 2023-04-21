package org.vqwiki;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vqwiki.lex.DefaultParser;
import org.vqwiki.persistence.PersistenceHandler;

/**
 * This class represents a topic in the wiki with all information pertaining to it like revisions and acl's.
 */
public class Topic {

    /** Name of this topic */
    protected String name;

    protected String contents;

    /** Author of this topic */
    protected String author;

    /** Last modification date of this topic */
    protected Date lastRevisionDate;

    /** revision number of this topic */
    protected int revision;

    protected List acls;

    private static Logger logger = LoggerFactory.getLogger(Topic.class);

    /**
     * Creates a new Topic object.
     *
     * @param name The name of this topic
     *
     * @throws Exception If the topic cannot be retrieved
     */
    public Topic(String name) throws Exception {
        this.name = name;
        this.contents = "Temporary dummy contents";
        this.author = null;
        this.lastRevisionDate = null;
        this.revision = -1;
    }

    /**
     * Populate the current instance of the Topic class from the persistence sub-system using a specified topic name.
     *
     * @param name - The name of the topic to be loaded.
     * @param vwiki - The name of the virtual wiki to which the topic belongs.
     */
    public void loadTopic(PersistenceHandler ph, String vwiki, String name) throws Exception {
        if (name == null || name.equals("")) {
            logger.debug("Attempted to load a topic from persistence mechanism with empty topic name.");
        }
        if (vwiki == null || vwiki.equals("")) {
            logger.debug("Attempted to load a topic from persistence mechanism with empty virtual wiki name.");
        }
        try {
            this.contents = ph.read(vwiki, name);
        } catch (Exception e) {
            logger.debug("PROBLEMS reading content!");
        }
    }

    /**
     * Populate the current instance of the Topic class from the persistence sub-system.
     *
     */
    public void loadTopic(PersistenceHandler ph, String virtualwiki) throws Exception {
        loadTopic(ph, virtualwiki, this.name);
    }

    /**
     * This method should return a rendered version of the topic's content using whatever
     * rendering system is in place.
     *
     * @return A <code>String</code> object containing the rendered topic.
     */
    public String getRenderedContent(HttpServletRequest request) {
        DefaultParser parser = new DefaultParser();
        String buff = null;
        try {
            buff = parser.parseHTML(this.contents, "default");
        } catch (Exception e) {
            logger.error("unable to render content", e);
        }
        return buff;
    }

    /**
     * This method should return the raw content of the topic.
     *
     * @return A <code>String</code> object containing the raw topic content.
     */
    public String getRawContent(HttpServletRequest request) {
        logger.debug("Getting content for topic: " + this.name);
        return this.contents;
    }

    /**
     * Returns the <code>Topic</code>'s name.
     *
     * @return A <code>String</code> object.
     */
    public String getName() {
        return this.name;
    }
}
