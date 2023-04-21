package radeox.filter;

import java.text.MessageFormat;
import cmspider.content.HeadLine;
import cmspider.utilities.string.StringUtilities;
import radeox.engine.context.InitialRenderContext;
import radeox.filter.context.FilterContext;
import radeox.filter.regex.LocaleRegexTokenFilter;
import radeox.regex.MatchResult;

/**
 * Transforms header style lines into subsections. A header starts with a 1 for
 * first level headers and 1.1 for secend level headers. Headers are numbered
 * automatically
 * 
 * @author leo
 * @team other
 * @version $Id: HeadingFilter.java,v 1.8 2004/04/15 13:56:14 stephan Exp $
 */
public class HeadingFilter extends LocaleRegexTokenFilter implements CacheFilter {

    private MessageFormat formatter;

    @Override
    protected String getLocaleKey() {
        return "filter.heading";
    }

    @Override
    public void handleMatch(StringBuffer buffer, MatchResult result, FilterContext context) {
        buffer.append(handleMatch(result));
    }

    @Override
    public void setInitialContext(InitialRenderContext context) {
        super.setInitialContext(context);
        String outputTemplate = outputMessages.getString(getLocaleKey() + ".print");
        formatter = new MessageFormat("");
        formatter.applyPattern(outputTemplate);
    }

    private String handleMatch(MatchResult result) {
        HeadLine headline = new HeadLine(result.group(0));
        StringBuffer sb = new StringBuffer();
        sb.append("<a name=\"");
        sb.append(StringUtilities.urlEncoding(headline.getName()));
        sb.append("\"></a>");
        sb.append(headline.getName());
        return formatter.format(new Object[] { headline.getFormat(), sb.toString() });
    }
}
