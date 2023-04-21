package net.sf.mareco.web.gui;

import net.sf.mareco.aaam.gui.LoginPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class UnsignedInHeader extends ApplicationHeader {

    private static final long serialVersionUID = -2126313672847803404L;

    public UnsignedInHeader() {
        super();
        add(new BookmarkablePageLink("signInLink", LoginPage.class));
    }
}
