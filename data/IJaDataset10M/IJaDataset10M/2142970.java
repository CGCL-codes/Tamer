package org.pustefixframework.editor.common.dom;

import java.util.Collection;

/**
 * Pages are the objects which are directly visible to the end-user. One page
 * can exist in several variants, whereas the variant name is used as a suffix
 * to the page name, seperated from the page name by two colons ("::").
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public interface Page extends Comparable<Page> {

    /**
     * Returns page name (not including variant name)
     * 
     * @return Name of the page as seen by the end-user
     */
    String getName();

    /**
     * Returns the variant being used for this page. If no variant is being used
     * for this page (default), null is returned.
     * 
     * @return Variant or <code>null</code> if no variant is selected for this
     *         Page
     */
    Variant getVariant();

    /**
     * Returns page name including variant name
     * 
     * @return Name of the page including variant as used internally
     */
    String getFullName();

    /**
     * Returns the XSL target which is used to generate the output of this Page
     * 
     * @return Target of type TARGET_XSL representing the stylesheet being used
     *         to render this Page
     */
    Target getPageTarget();

    /**
     * Returns a list of themes being active for this Page. The themes are
     * returned in order of preference - preferred themes are returned before
     * less preferred themes.
     * 
     * @return Themes being used by this Page
     */
    ThemeList getThemes();

    /**
     * Returns project this Page belongs to
     * 
     * @return Project this Page is part of
     */
    Project getProject();

    /**
     * Returns a list of all pages which are childs of this page in navigation.
     * 
     * @return All child pages
     */
    Collection<Page> getSubPages();

    /**
     * Returns whether there is at least one child page
     * 
     * @return Whether child pages are existing
     */
    boolean hasSubPages();

    /**
     * Registers this page for an update. This can be used to recalculate
     * dependencies after something has changed. The update is not processed
     * immediately but asynchronously.
     * 
     * @see #update()
     */
    void registerForUpdate();

    /**
     * Regenerates the page. This is useful to recalculate the dependencies
     * after some part being used by this page has been changed.
     * The regeneration is done synchronously.
     * 
     *  @see #registerForUpdate()
     */
    void update();
}
