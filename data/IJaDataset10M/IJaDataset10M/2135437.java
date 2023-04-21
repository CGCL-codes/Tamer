package org.omegat.core.data;

/**
 * Storage for translation information, like translation, author, date,
 * comments, etc.
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Martin Fleurke
 */
public class TransEntry {

    public String translation;

    public String comment;

    /**
     * Specifies the date of the last modification of the element.
     */
    public long changeDate;

    /**
     * Change identifier - Specifies the identifier of the user who modified the element last.
     */
    public String changeId;

    public TransEntry(String translation) {
        this.translation = translation;
    }

    /**
     * Creates a new transentry with the properties set to the given values. 
     * @param translation The translation
     * @param changeId The author of the last modification
     * @param changeDate The date (as unix timestamp) of the last modification.
     */
    public TransEntry(String translation, String changeId, long changeDate) {
        this.translation = translation;
        this.changeId = changeId;
        this.changeDate = changeDate;
    }
}
