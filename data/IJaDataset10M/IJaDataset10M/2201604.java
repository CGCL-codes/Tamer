package com.kwoksys.framework.parsers.wiki;

import com.kwoksys.framework.parsers.wiki.generic.MatchingTags;
import com.kwoksys.framework.parsers.wiki.generic.SingleTag;
import com.kwoksys.framework.parsers.wiki.twiki.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TwikiParser
 */
public class TwikiParser extends Parser {

    public static List<Tag> list = new ArrayList();

    private List verbaimList = new ArrayList();

    static {
        Tag tag = new VerbatimStripTag("<verbatim>((.|[\r\n])*?)</verbatim>");
        tag.setExample("<verbatim><code here></verbatim>");
        list.add(tag);
        tag = new SingleTag("^(----)");
        tag.setExample("----");
        tag.setTag("<hr/>");
        list.add(tag);
        tag = new MatchingTags("^---\\+\\+\\+(.+)");
        tag.setExample("---+++Heading 3");
        tag.setTags("<h3>", "</h3>");
        list.add(tag);
        tag = new MatchingTags("^---\\+\\+(.+)");
        tag.setExample("---++Heading 1");
        tag.setTags("<h1>", "</h1>");
        list.add(tag);
        tag = new MatchingTags("^---\\+(.+)");
        tag.setExample("---+Heading 1");
        tag.setTags("<h1>", "</h1>");
        list.add(tag);
        tag = new SingleTag("^$");
        tag.setTag("\n<p>");
        list.add(tag);
        tag = new NoSpaceMatchingTags("\\__(.+?)\\__");
        tag.setExample("__Bold italic__");
        tag.setTags("<strong><em>", "</em></strong>");
        list.add(tag);
        tag = new NoSpaceMatchingTags("\\*(.+?)\\*");
        tag.setExample("*Bold*");
        tag.setTags("<b>", "</b>");
        list.add(tag);
        tag = new NoSpaceMatchingTags("\\_(.+?)\\_");
        tag.setExample("_Italic_");
        tag.setTags("<i>", "</i>");
        list.add(tag);
        tag = new NoSpaceMatchingTags("\\==(.+?)\\==");
        tag.setExample("==Bold fixed==");
        tag.setTags("<code><b>", "</b></code>");
        list.add(tag);
        tag = new NoSpaceMatchingTags("\\=(.+?)\\=");
        tag.setExample("=Fixed font=");
        tag.setTags("<code>", "</code>");
        list.add(tag);
        tag = new ExternalLabelLinkTag("(^|.)(\\[\\[((https://|http://|ftp://)(.+?))\\]\\[(.+?)\\]\\])");
        tag.setExample("[[http://gnu.org][GNU]]");
        list.add(tag);
        tag = new WikiWordTag("(^|[ !])(([A-Z]+)([a-z]+)([A-Z]+)(\\w+))([^\\w])");
        tag.setExample("WebStatistics");
        list.add(tag);
        tag = new VerbatimRestoreTag("&lt;verbatim&gt;([0-9]+)&lt;/verbatim&gt;");
        list.add(tag);
    }

    public String parseHtml(String content) {
        StringBuffer sb = new StringBuffer(content);
        for (Tag tag : list) {
            sb = tag.parseContent(sb, this);
        }
        return sb.toString();
    }

    public List getVerbaimList() {
        return verbaimList;
    }
}
