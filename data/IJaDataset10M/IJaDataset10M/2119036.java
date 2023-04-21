package com.ivata.mask.valueobject;

import java.sql.Timestamp;

/**
 * All value objects which implement timestamp accessors and modifiers should
 * implement this interface. It is used by the hibernate persistence to check
 * whether or no the value object is unsaved.
 *
 * @since ivata masks 1.0 (14-Sep-2005)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public interface TimestampValueObject {

    /**
     * <p>Get the date and time when the value object was created.</p>
     *
     * @return the date and time when the value object was created.
     */
    Timestamp getCreated();

    /**
     * <p>Get the date and time when the value object was last modified.</p>
     *
     * @return the date and time when the value object was last modified.
     * @hibernate.timestamp
     */
    Timestamp getModified();

    /**
     * <copyDoc>Refer to {@link #getCreated}.</copyDoc>
     * @param createdParam The created to set.
     */
    void setCreated(Timestamp createdParam);

    /**
     * <copyDoc>Refer to {@link #getModified}.</copyDoc>
     * @param modifiedParam The modified to set.
     */
    void setModified(Timestamp modifiedParam);
}
