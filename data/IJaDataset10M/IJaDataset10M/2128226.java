package hambo.messaging;

import java.util.*;
import javax.mail.Message;
import javax.mail.search.SearchTerm;

/**
 * Information about the set of filters a user has for incoming mail.  To get
 * a <code>Filters</code> object, use {@link MailStorage#getFilters}.
 *
 */
public interface Filters {

    FolderType filterMessage(Message msg, SendBuffer msgsender);

    /**
     * Delete a specific filter from the set of filters.  The removal is
     * immediate and permanent.
     * @param filterid the id of the filter to remove.
     */
    void deleteFilter(long filterid);

    /**
     * This method creates / stores a filter.
     * @param name the name of the filter
     * @param folder destination folder for the filter (or null to avoid
     * saving).
     * @param smss a set of phone numbers to send SMS to.
     * @param nsms max number of SMS's for each of smss.
     * @param emails a set of email addresses to redirect to.
     * @param condition what to search for
     */
    void makeFilter(long index, String name, FolderType folder, Vector smss, Vector nsms, Vector emails, SearchTerm condition);

    /**
     * Reorder the filters by moving a specific filter upwards or downwards in
     * the sequence.  The specified filter changes place with the filter above
     * or below.
     * @param index which filter to move.
     * @param moveUp if true move upwards, if false move downwards.
     */
    void reOrder(long index, boolean moveUp);

    /**
     * Get a vector of all {@link Filters.Filter}s for this user, active and not.
     * The returned Filters is basic in the sense that they don't have their
     * data and interval vectors loaded.
     */
    Vector getBasicVect();

    /**
     * Get a specific, fully loaded, filter.
     */
    Filter getFilter(long filterid);

    class Filter {

        public long filterID = -1;

        public String name = null;

        public FolderType folder = null;

        public int maxNumberOfSMS = 0;

        public Vector mobFwdVect = new Vector();

        public Vector emailFwdVect = new Vector();

        public SearchTerm condition;

        public Filter(long filterID, String name, FolderType folder, SearchTerm condition) {
            this.filterID = filterID;
            this.name = name;
            this.folder = folder;
            this.condition = condition;
        }

        public void addMobFwd(String fwd, int max_split) {
            mobFwdVect.add(fwd);
        }

        public void addEmailFwd(String fwd) {
            emailFwdVect.add(fwd);
        }

        public Enumeration getMobFwd() {
            return mobFwdVect.elements();
        }

        public Enumeration getEmailFwd() {
            return emailFwdVect.elements();
        }
    }
}
