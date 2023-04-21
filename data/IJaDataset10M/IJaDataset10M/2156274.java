package net.sourceforge.blogentis.plugins.pages;

import net.sourceforge.blogentis.plugins.IBlogExtension;

/**
 * Extension that allows pages to be on the fly modified.
 * 
 * @author abas
 */
public interface IPageEditExtension extends IBlogExtension {

    /**
     * Modify a page before it is saved to permanent storage.
     * 
     * @param page
     * @return the modified page to be saved.
     */
    public String pageToStorage(String page);

    /**
     * Modify a page that has been loaded to storage, and prepare it for
     * presentation as HTML.
     * 
     * @param page
     * @return
     */
    public String pageToScreen(String page);
}
