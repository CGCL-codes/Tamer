package org.wikipediacleaner.gui.swing.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.wikipediacleaner.api.constants.EnumWikipedia;
import org.wikipediacleaner.gui.swing.basic.Utilities;

/**
 * An action listener for viewing page. 
 */
@SuppressWarnings("serial")
public class PageViewAction extends AbstractAction {

    private final String title;

    private final EnumWikipedia wiki;

    private final boolean redirect;

    private final String action;

    public PageViewAction(String url) {
        this(url, null, false, null);
    }

    public PageViewAction(String title, EnumWikipedia wiki) {
        this(title, wiki, false, null);
    }

    public PageViewAction(String title, EnumWikipedia wiki, boolean redirect) {
        this(title, wiki, redirect, null);
    }

    public PageViewAction(String title, EnumWikipedia wiki, String action) {
        this(title, wiki, false, action);
    }

    private PageViewAction(String title, EnumWikipedia wiki, boolean redirect, String action) {
        this.title = title;
        this.wiki = wiki;
        this.redirect = redirect;
        this.action = action;
    }

    public void actionPerformed(@SuppressWarnings("unused") ActionEvent e) {
        if (action != null) {
            Utilities.browseURL(wiki, title, action);
        } else if (wiki != null) {
            Utilities.browseURL(wiki, title, redirect);
        } else {
            Utilities.browseURL(title);
        }
    }
}
