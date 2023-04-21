package net.sf.jabref.export.layout.format;

import net.sf.jabref.export.layout.LayoutFormatter;
import net.sf.jabref.AuthorList;

/**
 * Duplicate of AuthorLastFirstAbbreviator.
 * 
 * @see AuthorLastFirstAbbreviator
 * 
 * @author Carlos Silla
 */
public class AuthorAbbreviator implements LayoutFormatter {

    public String format(String fieldText) {
        AuthorList list = AuthorList.getAuthorList(fieldText);
        return list.getAuthorsLastFirstAnds(true);
    }
}
