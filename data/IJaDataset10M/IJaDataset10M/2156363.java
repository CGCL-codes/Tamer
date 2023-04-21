package org.blojsom.extension.atomapi;

import org.blojsom.blog.Blog;
import org.blojsom.blog.BlogEntry;
import org.blojsom.blog.BlogUser;
import org.blojsom.util.BlojsomConstants;
import org.blojsom.util.BlojsomUtils;
import org.intabulas.sandler.AtomConstants;
import org.intabulas.sandler.SyndicationFactory;
import org.intabulas.sandler.authentication.DigestUtilities;
import org.intabulas.sandler.elements.Content;
import org.intabulas.sandler.elements.Entry;
import org.intabulas.sandler.elements.Link;
import org.intabulas.sandler.elements.impl.ContentImpl;
import org.intabulas.sandler.elements.impl.EntryImpl;
import org.intabulas.sandler.elements.impl.LinkImpl;
import java.util.Date;

/**
 * AtomUtils
 *
 * @author Mark Lussier
 * @version $Id: AtomUtils.java,v 1.20 2006-01-04 16:24:06 czarneckid Exp $
 * @since blojsom 2.0
 */
public class AtomUtils implements AtomAPIConstants {

    /**
     * Generate a NONCE value based on the the current blog
     *
     * @param user BlogUser instance for the particular blog
     * @return String that is a SHA digest (in hex) of the NONCE value
     *         todo Optimize the format we gen the nonce from
     */
    public static String generateNextNonce(BlogUser user) {
        String nonce = BlojsomUtils.getISO8601Date(new Date()) + ":" + user.getId() + ":" + user.getBlog().getBlogDescription();
        return DigestUtilities.digestString(nonce);
    }

    /**
     * Generate an Atom Entry object from a Blojsom BlogEntry object
     *
     * @param blog      Blog instance
     * @param user      BlogUser instance
     * @param blogentry BlogEntry to convert
     * @param servletPath URL path to Atom API servlet
     * @return Entry object populated from the BlogEntry
     */
    public static Entry fromBlogEntry(Blog blog, BlogUser user, BlogEntry blogentry, String servletPath) {
        Entry result = new EntryImpl();
        result.setTitle(blogentry.getEscapedTitle());
        result.setSummary(blogentry.getEscapedTitle());
        result.setCreated(blogentry.getDate());
        result.setIssued(blogentry.getDate());
        result.setModified(new Date(blogentry.getLastModified()));
        result.setId("tag:" + blog.getBlogOwnerEmail() + "," + blogentry.getDateAsFormat("yyyy-MM-dd") + ":" + blogentry.getId());
        Link link = new LinkImpl();
        link.setType(CONTENTTYPE_HTML);
        link.setRelationship(AtomConstants.Rel.ALTERNATE);
        link.setHref(blogentry.getEscapedLink());
        result.addLink(link);
        Link editlink = new LinkImpl();
        editlink.setType(CONTENTTYPE_ATOM);
        editlink.setRelationship(AtomConstants.Rel.SERVICE_EDIT);
        editlink.setHref(blog.getBlogBaseURL() + servletPath + blogentry.getId());
        result.addLink(editlink);
        result.setAuthor(SyndicationFactory.createPerson(blog.getBlogOwner(), blog.getBlogOwnerEmail(), blog.getBlogURL()));
        Content content = new ContentImpl();
        content.setMimeType(CONTENTTYPE_HTML);
        content.setMode(AtomConstants.Mode.ESCAPED);
        content.setBody(blogentry.getEscapedDescription());
        result.addContent(content);
        return result;
    }

    /**
     * Generates a slim Entry object (tile and id only) from a BlogEntry object
     *
     * @param blog           Blog instance
     * @param user           BlogUser instance
     * @param blogentry      BlogEntry to convert
     * @param servletMapping Servlet mapping for the Atom API
     * @return Entry object populated from the BlogEntry
     */
    public static Entry fromBlogEntrySearch(Blog blog, BlogUser user, BlogEntry blogentry, String servletMapping) {
        Entry result = new EntryImpl();
        result.setTitle(blogentry.getEscapedTitle());
        result.setId(blog.getBlogBaseURL() + servletMapping + user.getId() + blogentry.getId());
        return result;
    }

    /**
     * Extract the AtomAPI Method off the end of the SOAPActionHeader
     *
     * @param soapaction the contents of the SOAPAction header
     * @return the method
     */
    public static String getSOAPActionMethod(String soapaction) {
        String result = null;
        int idx = soapaction.lastIndexOf('/');
        if (idx != -1) {
            result = soapaction.substring(idx);
        }
        return result;
    }
}
