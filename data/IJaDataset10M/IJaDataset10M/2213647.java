package com.flaptor.wizard.ui;

import com.flaptor.wizard.Action;
import com.flaptor.wizard.Page;
import com.flaptor.wizard.PageElement;

/**
 * Interface for the UI of a wizard
 * 
 * @author Martin Massera
 */
public interface UI {

    /**
     * execute a page
     * @param page the page to be shown
     * @return the action to be done
     */
    Action doPage(Page page, boolean withBack);

    /**
     * for updating an element in the UI
     * @param element the element to be updated
     */
    public void elementUpdated(PageElement element);
}
