package org.torweg.pulse.site.content.admin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.jdom.Content;
import org.jdom.Element;
import org.torweg.pulse.bundle.Result;
import org.torweg.pulse.site.content.AbstractBasicContent;
import org.torweg.pulse.site.content.AbstractBasicVariant;
import org.torweg.pulse.site.content.Attachment;

/**
 * the result for the {@code AbstractBasicContentEditor}.
 * 
 * @author Daniel Dietz
 * @version $Revision: 1425 $
 */
public class AbstractBasicContentEditorResult implements Result {

    /**
	 * the {@code AbstractBasicContent} of the result.
	 */
    private AbstractBasicContent content;

    /**
	 * The variant of the result.
	 */
    private AbstractBasicVariant<?> variant;

    /**
	 * a map of attributes to be added to the result.
	 */
    private final Map<String, String> attributes = new HashMap<String, String>();

    /**
	 * the {@code Content}s to be added to the result.
	 */
    private final Set<Content> contents = new HashSet<Content>();

    /**
	 * the mode of the result.
	 * 
	 * <p>
	 * used as flag for xsl:choose:<br/>
	 * <ul type="disc">
	 * <li>mode=1: with ajax.AbstractBasicContent.Attachments.xsl will use js
	 * that builds up file-browser-panel for attachments-editor.</li>
	 * <li>mode=2: with ajax.AbstractBasicContent.Attachments.xsl will use
	 * html/js that builds up attachments-editor for a attachment of a content.</li>
	 * </ul>
	 * </p>
	 */
    private Integer mode;

    /**
	 * the attachment of the result.
	 */
    private Attachment attachment;

    /**
	 * Flag indicating whether to de-serialise time-zones for the result.
	 */
    private boolean addTimeZones = false;

    /**
	 * @return the result as {@code Element}.
	 * 
	 * @see org.torweg.pulse.bundle.JDOMable#deserializeToJDOM()
	 */
    public Element deserializeToJDOM() {
        Element result = new Element("result").setAttribute("class", this.getClass().getCanonicalName());
        if (this.content != null) {
            result.addContent(this.content.deserializeToJDOM());
        }
        if (this.variant != null) {
            result.addContent(this.variant.deserializeToJDOM());
        }
        for (Map.Entry<String, String> entry : this.attributes.entrySet()) {
            result.setAttribute(entry.getKey(), entry.getValue());
        }
        for (Content c : this.contents) {
            result.addContent(c.detach());
        }
        if (this.attachment != null) {
            result.addContent(this.attachment.deserializeToJDOM());
        }
        if (this.mode != null) {
            result.setAttribute("mode", this.mode.toString());
        }
        if (this.addTimeZones) {
            Element timeZones = new Element("time-zones");
            for (String tz : TimeZone.getAvailableIDs()) {
                timeZones.addContent(new Element("time-zone").setText(tz));
            }
            result.addContent(timeZones);
        }
        return result;
    }

    /**
	 * sets the content of the result.
	 * 
	 * @param c
	 *            the {@code AbstractBasicContent} to be set
	 */
    public final void setContent(final AbstractBasicContent c) {
        this.content = c;
    }

    /**
	 * sets the variant of the result.
	 * 
	 * @param v
	 *            the {@code AbstractBasicVariant} to be set
	 */
    public final void setVariant(final AbstractBasicVariant<?> v) {
        this.variant = v;
    }

    /**
	 * adds a attribute to the root-container of this result.
	 * 
	 * @param attributeName
	 *            the name of the attribute
	 * @param attributeValue
	 *            the value of the attribute
	 */
    public final void addAttribute(final String attributeName, final String attributeValue) {
        this.attributes.put(attributeName, attributeValue);
    }

    /**
	 * adds a {@code Content} of this result.
	 * 
	 * @param jDomContent
	 *            the {@code Content} to add
	 */
    public final void addContent(final Content jDomContent) {
        this.contents.add(jDomContent);
    }

    /**
	 * sets the mode for the result.
	 * 
	 * @param i
	 *            the mode to set
	 */
    public final void setMode(final int i) {
        this.mode = i;
    }

    /**
	 * sets the {@code Attachement} for the result.
	 * 
	 * @param att
	 *            the {@code Attachment} to set
	 */
    public final void setAttachment(final Attachment att) {
        this.attachment = att;
    }

    /**
	 * Flag indicating whether to de-serialise time-zones for the result.
	 */
    public final void addTimeZones() {
        this.addTimeZones = true;
    }
}
