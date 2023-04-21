package espider.libs.helliker.id3;

import java.io.IOException;

/**
 *  A common interface for ID3Tag objects so they can easily communicate with
 *  each other.<br/><dl>
 * <dt><b>Version History:</b></dt>
 * <dt>1.2.1 - <small>2002.1023 by gruni</small></dt>
 * <dd>-Made sourcecode compliant to the Sun Coding Conventions</dd>
 * <dt>1.2 - <small>2002.0127 by  helliker</small></dt>
 * <dd>-Added getBytes method.</dd>
 *
 * <dt>1.1 - <small>2002/01/13 by helliker</small></dt>
 * <dd>-Initial version</dd>
 * </dl>
 * @author  Jonathan Hilliker
 * @version 1.2.1
 */
public interface ID3Tag {

    /**
     * Copies information from the ID3Tag parameter and inserts it into
     * this tag.  Previous data will be overwritten.
     *
     * @param tag the tag to copy from
     */
    public void copyFrom(ID3Tag tag);

    /**
     * Saves all data in this tag to the file it is bound to.
     *
     * @exception IOException if an error occurs
     */
    public void writeTag() throws IOException;

    /**
     * Removes this tag from the file it is bound to.
     *
     * @exception IOException if an error occurs
     */
    public void removeTag() throws IOException;

    /**
     * Returns a binary representation of the tag as it would appear in
     * a file.
     *
     * @return a binary representation of the tag
     */
    public byte[] getBytes();
}
