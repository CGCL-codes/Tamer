package com.j2biz.blogunity.render.filter;

import org.radeox.api.engine.context.InitialRenderContext;
import org.radeox.filter.CacheFilter;
import org.radeox.filter.context.FilterContext;
import org.radeox.filter.regex.RegexTokenFilter;
import org.radeox.regex.MatchResult;
import com.j2biz.blogunity.BlogunityManager;
import com.j2biz.blogunity.IConstants;

/**
 * @author Andreas Siebert / j2biz.com
 */
public final class SmileyFilter extends RegexTokenFilter implements CacheFilter {

    private SmileyContainer container;

    public SmileyFilter() {
    }

    public void setInitialContext(final InitialRenderContext ctx) {
        final Object value = ctx.get(IConstants.Radeox.SMILIES_KEY);
        if (!(value instanceof SmileyContainer)) {
            throw new IllegalStateException("context doesn't contain SmileyContainer. His key is occupied by an instance of another type. type == " + (value != null ? value.getClass().getName() : "null"));
        }
        this.container = (SmileyContainer) value;
        this.addRegex(container.getRegExp(), "");
        super.setInitialContext(ctx);
    }

    public final void handleMatch(StringBuffer buffer, MatchResult result, FilterContext context) {
        final String imgSrc = this.container.getImageSrc(result.group(0));
        if (!(imgSrc == null)) {
            buffer.append("<img src=\"");
            buffer.append(BlogunityManager.getContextPath());
            buffer.append("/images/smilies/");
            buffer.append(imgSrc);
            buffer.append("\" border=\"0\" class=\"smiley\" />");
        }
    }
}
