package net.sf.jabref.export.layout.format;

import net.sf.jabref.export.layout.LayoutFormatter;
import net.sf.jabref.AuthorList;

/**
 * <ul>
 * <li>Names are given in order: von last, jr, first.</li>
 * <li>First names will be abbreviated.</li>
 * <li>Individual authors are separated by commas.</li>
 * <li>The 'and' of a list of three or more authors is preceeded by a comma
 * (Oxford comma)</li>
 * </ul>
 * 
 * @author mkovtun
 * @author Christopher Oezbek <oezi@oezi.de>
 * 
 */
public class AuthorLastFirstAbbrOxfordCommas implements LayoutFormatter {

    public String format(String fieldText) {
        return AuthorList.fixAuthor_lastNameFirstCommas(fieldText, true, true);
    }
}
