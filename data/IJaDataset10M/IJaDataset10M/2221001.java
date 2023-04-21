package com.kwoksys.framework.parsers.wiki.twiki;

import com.kwoksys.framework.parsers.wiki.Tag;
import com.kwoksys.framework.parsers.wiki.Parser;
import com.kwoksys.framework.parsers.wiki.TwikiParser;
import com.kwoksys.framework.util.HtmlUtils;
import com.kwoksys.framework.util.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

/**
 * VerbatimRestoreTag
 */
public class VerbatimRestoreTag extends Tag {

    public VerbatimRestoreTag(String regex) {
        super(regex);
    }

    public StringBuffer parseContent(StringBuffer content, Parser parser) {
        List verbatimList = ((TwikiParser) parser).getVerbaimList();
        StringBuffer sb = new StringBuffer();
        Matcher m = Pattern.compile(getRegex(), Pattern.MULTILINE).matcher(content);
        while (m.find()) {
            String replace = "<pre>" + HtmlUtils.encode((String) verbatimList.get(0)) + "</pre>";
            m.appendReplacement(sb, StringUtils.encodeMatcherReplacement(replace));
            verbatimList.remove(0);
        }
        m.appendTail(sb);
        return sb;
    }
}
