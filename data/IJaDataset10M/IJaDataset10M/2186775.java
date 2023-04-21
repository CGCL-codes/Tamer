package net.sourceforge.blogentis.plugins.base;

import net.sourceforge.blogentis.turbine.BlogRunData;
import net.sourceforge.blogentis.utils.AbsoluteLinkURL;

/**
 * This interface defines a linked item in the blogs UI. Only the target link
 * and the description are relevant, the caller will know what to do with them.
 * 
 * @author abas
 */
public interface ILinkTo {

    /**
     * Get this links label. TODO: provide i18n support?
     * 
     * @return the label for this link.
     */
    public String getLabel();

    /**
     * Get the linked page that this link should lead to.
     * 
     * @param data
     *            the RunData of the request.
     * @param link
     *            an (possibly modified) link.
     * @return
     */
    public AbsoluteLinkURL getLink(BlogRunData data, AbsoluteLinkURL link);

    /**
     * Check if this link's target is the same as the current request.
     * 
     * @param data
     *            The current request
     * @return true if the current request shows the current page.
     */
    public boolean isCurrentPage(BlogRunData data);

    /**
     * Check if this links target is accessible with the current requests
     * credentials.
     * 
     * @param data
     *            The current request
     * @return true if the target page is viewable with the current credentials.
     */
    public boolean isAuthorized(BlogRunData data);
}
