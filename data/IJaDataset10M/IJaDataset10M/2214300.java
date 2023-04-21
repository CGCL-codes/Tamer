package org.shams.phonebook.web.pages;

import org.shams.phonebook.web.pages.secure.Home;

/**
 * @author <a href="mailto:m.h.shams@gmail.com">M. H. Shamsi</a>
 * @version 1.0.0
 *          Date Oct 14, 2007
 */
public class Start {

    public Object onActivate() {
        return Home.class;
    }
}
