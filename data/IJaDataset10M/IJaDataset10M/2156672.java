package vqwiki.svc;

import java.util.Collection;
import java.util.Locale;

/**
 * Stores a list of usernames for each topic page in the VQWiki
 * system, so that an email can be sent to their registered
 * addresses when changes are made to the associated topic page.
 *
 * @author Robert E Brewer
 * @version 0.1
 */
public interface Notify {

    /**
     *
     */
    public void addMember(String userName) throws Exception;

    /**
     *
     */
    public void removeMember(String userName) throws Exception;

    /**
     *
     */
    public boolean isMember(String userName) throws Exception;

    /**
     *
     */
    public Collection getMembers() throws Exception;

    /**
     *
     */
    public boolean sendNotifications(String rootPath, Locale locale) throws Exception;
}
