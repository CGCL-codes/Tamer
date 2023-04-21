package edu.hawaii.ics.csdl.jupiter.model.reviewissue;

/**
 * Provides status information. Clients can instantiate this with a key and its ordinal number.
 * The ordinal number is used for the comparison of this instances.
 *
 * @author Takuya Yamashita
 * @version $Id: Status.java 82 2008-02-22 09:34:57Z jsakuda $
 */
public class Status implements Comparable<Status> {

    /** The key of the status. */
    private String key;

    /** Assigns an ordinal to this key. */
    private int ordinal;

    /**
   * Constructor for the Status object. Sets the key string and its ordinal number.
   *
   * @param key the key of the code review.
   * @param ordinal the ordinal of the key.
   */
    public Status(String key, int ordinal) {
        this.key = key;
        this.ordinal = ordinal;
    }

    /**
   * Gets the key for the status value.
   *
   * @return the key for the status value
   */
    public String getKey() {
        return this.key;
    }

    /**
   * Compare this object by the ordinal number.
   *
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
    public int compareTo(Status status) {
        return ordinal - status.ordinal;
    }
}
