package org.opencms.newsletter;

/**
 * Type definition class for email content types.<p>
 * 
 * @author Alexander Kandzior
 * 
 * @version $Revision: 1.4 $
 * 
 * @since 6.2.0
 */
public final class CmsNewsletterContentType {

    /** Content type definition for HTML. */
    public static final CmsNewsletterContentType TYPE_HTML = new CmsNewsletterContentType("html");

    /** Content type definition for plain text. */
    public static final CmsNewsletterContentType TYPE_TEXT = new CmsNewsletterContentType("text");

    /** The name of this type. */
    private String m_typeName;

    /**
     * Creates a new newsletter content type.<p>
     * 
     * @param typeName the type name to use
     */
    private CmsNewsletterContentType(String typeName) {
        m_typeName = typeName;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return m_typeName;
    }
}
