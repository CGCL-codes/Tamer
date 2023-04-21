package flex.messaging.services.messaging;

import java.util.StringTokenizer;
import flex.messaging.services.ServiceException;

/**
 * Represents a message destination subtopic.  You are given instances of Subtopics
 * as arguments to the MessagingAdapter.allowSubscribe and allowSend methods.  These
 * are used to implement your own authorization for the subscribe and send messages
 * to specific subtopics.
 */
public class Subtopic {

    /**
     * Constructor.
     *
     * @exclude
     *
     * @param subtopic The full subtopic string.
     * @param separator The separator for tokenizing a hierarchical subtopic.
     */
    public Subtopic(String subtopic, String separator) {
        this.subtopic = subtopic;
        this.separator = separator;
        if (subtopic.length() == 0) {
            ServiceException se = new ServiceException();
            se.setMessage(10554, new Object[] { subtopic });
            throw se;
        } else if ((separator != null) && (subtopic.indexOf(separator) != -1)) {
            hierarchical = true;
            if (subtopic.startsWith(separator) || subtopic.endsWith(separator) || (subtopic.indexOf(separator + separator) != -1)) {
                ServiceException se = new ServiceException();
                se.setMessage(10554, new Object[] { subtopic });
                throw se;
            }
            StringTokenizer tokenizer = new StringTokenizer(subtopic, separator);
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                if (token.indexOf(SUBTOPIC_WILDCARD) != -1) {
                    if (!token.equals(SUBTOPIC_WILDCARD)) {
                        ServiceException se = new ServiceException();
                        se.setMessage(10554, new Object[] { subtopic });
                        throw se;
                    } else {
                        hasSubtopicWildcard = true;
                    }
                }
            }
        } else if (subtopic.indexOf(SUBTOPIC_WILDCARD) != -1) {
            if (!subtopic.equals(SUBTOPIC_WILDCARD)) {
                ServiceException se = new ServiceException();
                se.setMessage(10554, new Object[] { subtopic });
                throw se;
            } else {
                hasSubtopicWildcard = true;
            }
        }
    }

    /**
     * The wildcard token for hierarchical subtopics.
     */
    public static final String SUBTOPIC_WILDCARD = "*";

    /**
     * The full subtopic value.
     */
    private String subtopic;

    /**
     * The separator used if the subtopic is hierarchical.
     */
    private String separator;

    /**
     * Flag to store whether the subtopic is hierarchical.
     */
    private boolean hierarchical;

    /**
     * Flag to store whether the subtopic contains subtopic wildcards.
     */
    private boolean hasSubtopicWildcard;

    /**
     * Returns true if the subtopic contains a hierarchical subtopic wildcard.
     *
     * @return true if the subtopic contains a hierarchical subtopic wildcard,
     *         otherwise false.
     */
    public boolean containsSubtopicWildcard() {
        return hasSubtopicWildcard;
    }

    /**
     * Override of equals.
     *
     * @param other The object to compare against.
     * @return <code>true</code> if other equals to this; <code>false</code> otherwise;
     */
    public boolean equals(Object other) {
        if (!(other instanceof Subtopic) || (other == null)) return false;
        Subtopic otherSubtopic = (Subtopic) other;
        if (subtopic.equals(otherSubtopic.subtopic) && (separator.equals(otherSubtopic.separator) || (separator == null && otherSubtopic.separator == null))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the separator used to create this Subtopic instance.
     * This value may be <code>null</code>.
     *
     * @return The separator used to create this Subtopic instance.
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * Returns the subtopic value used to create this Subtopic instance.
     *
     * @return The subtopic value used to create this Subtopic instance.
     */
    public String getValue() {
        return subtopic;
    }

    /**
     * Override of hashCode. Hash using the subtopic String rather than the object's address.
     *
     * @return The hashCode.
     */
    public int hashCode() {
        return subtopic.hashCode();
    }

    /**
     * Returns true is the subtopic is hierarchical.
     *
     * @return true if the subtopic is hierarchical, otherwise false.
     */
    public boolean isHierarchical() {
        return hierarchical;
    }

    /**
     * Matches the passed subtopic against this subtopic.
     * If neither subtopic contains a wildcard they must literally match.
     * If one or the other contains a wildcard they may match.
     * "chatrooms.*" will match "chatrooms.lobby" or "chatrooms.us.ca" but will
     * not match "chatrooms" (assuming a subtopic separator of ".").
     * "chatrooms.*.ca" will match "chatrooms.us.ca" but not "chatrooms.us.ma".
     *
     * @param other The other subtopic to match against this subtopic.
     * @return true if they match, otherwise false.
     */
    public boolean matches(Subtopic other) {
        if (!hasSubtopicWildcard && !other.hasSubtopicWildcard) {
            return (subtopic.equals(other.subtopic)) ? true : false;
        } else {
            if (hierarchical && other.hierarchical && !separator.equals(other.separator)) return false;
            StringTokenizer t1 = new StringTokenizer(subtopic, separator);
            StringTokenizer t2 = new StringTokenizer(other.subtopic, other.separator);
            int n = t1.countTokens();
            int difference = n - t2.countTokens();
            String tok1 = null;
            String tok2 = null;
            boolean matchToken;
            while (n-- > 0) {
                tok1 = t1.nextToken();
                if (tok1.equals(SUBTOPIC_WILDCARD)) matchToken = false; else matchToken = true;
                if (t2.hasMoreTokens()) {
                    tok2 = t2.nextToken();
                    if (tok2.equals(SUBTOPIC_WILDCARD)) continue;
                } else {
                    break;
                }
                if (matchToken && !tok1.equals(tok2)) return false;
            }
            if (difference == 0) return true; else if ((difference < 0) && tok1.equals(SUBTOPIC_WILDCARD)) return true; else if ((difference > 0) && tok2.equals(SUBTOPIC_WILDCARD)) return true; else return false;
        }
    }

    /**
     * Override of toString.
     *
     * @return The subtopic string.
     */
    public String toString() {
        return subtopic;
    }
}
