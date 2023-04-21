package org.authorsite.mailarchive.model;

/**
 * Abstraction of any textual content constituting an email address.
 * 
 * <p><code>TextContent</code> is intended to support all text content associated
 * with an email message. This includes the full headers, stored in case they are needed,
 * but not for full searching, the main message body, in the case where the message
 * has not been broken down in the original into mime parts, and any textual attachments.</p>
 * 
 * <p>The precise role played is determined by the <code>TextContentRole</code>
 * type safe enumeration. Implementations must take care to ensure the logical
 * integrity of the records</p>
 * 
 * @see org.authorsite.mailarchive.model.TextContentRole
 * @author jejking
 * @version $Revision: 1.3 $
 */
public interface TextContent extends MessageContent {

    public String getContent();

    public void setContent(String newContent);

    public TextContentRole getRole();

    public void setRole(TextContentRole newRole);
}
