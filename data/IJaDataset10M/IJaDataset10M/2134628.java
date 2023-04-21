package org.vramework.mvc.markuptags;

/**
 * @author thomas.mahringer
 */
public class HtmlTag extends CompiletimeTag {

    public HtmlTag(String name, String fullTag, String contents, int beginPos, int endPos, String viewName) {
        super(name, fullTag, contents, beginPos, endPos, viewName, false);
    }
}
