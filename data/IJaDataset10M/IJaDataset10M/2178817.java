package com.vmladenov.objects.mainobjects;

import java.util.LinkedList;

/**
 * User: Ventsislav Mladenov - Invincible
 * Date: 2007-1-1
 * Time: 23:13:29
 */
public class PageList extends LinkedList<Page> {

    /**
	 * Create new instance of List of Page.
	 */
    public PageList() {
    }

    /**
	 * Find page by Name
	 *
	 * @param PageName Page's name.
	 * @return Page's object
	 */
    public Page getPage(String PageName) {
        Page page = null;
        for (Page pages : this) {
            if (pages.getPageName().equalsIgnoreCase(PageName)) {
                page = pages;
            }
        }
        return page;
    }
}
