package vqwiki.svc;

import java.util.Collection;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import vqwiki.Change;

public interface ChangeLog {

    /**
     * Logs a change of a topic in the persistence.
     * @param change      The change made in the wiki.
     * @param request     the servletrequest to get some request specific config.
     * @throws Exception  Throws exception if something goes wrong.
     */
    public void logChange(Change change, HttpServletRequest request) throws Exception;

    /**
     * Removes some changes along with the topic name. This happens if someone
     * purges topics from the vqwiki.
     * @param virtualWiki the virtual wiki to delete the topics.
     * @param cl          a collection of the topics to be deleted.
     * @throws Exception
     */
    public void removeChanges(String virtualwiki, Collection cl) throws Exception;

    /**
     * Gets the recent changes from the persistence.
     * @param virtualWiki the virtual wiki to get the recent changes.
     * @param d           the date of the recent changes.
     * @return            A collection of changes
     * @throws Exception
     * @see vqwiki.Change
     */
    public Collection getChanges(String virtualWiki, Date d) throws Exception;
}
