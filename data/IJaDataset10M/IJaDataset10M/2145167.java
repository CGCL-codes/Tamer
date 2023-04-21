package com.surfsoftconsulting.rss4jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

public class ItemPreviewTag extends TagSupport {

    private static final long serialVersionUID = -5827748956540780962L;

    private static final String PARAGRAPH_MODE = "paragraph";

    private static final String SENTENCE_MODE = "sentence";

    private String feedId;

    private String index;

    private String mode = PARAGRAPH_MODE;

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setMode(String mode) {
        this.mode = mode.toLowerCase();
    }

    @Override
    public int doStartTag() throws JspException {
        if (feedId == null) {
            throw new NullPointerException("feedId cannot be null");
        }
        if (mode != null && (!mode.equals(SENTENCE_MODE)) && (!mode.equals(PARAGRAPH_MODE))) {
            throw new IllegalArgumentException("mode can only be '" + SENTENCE_MODE + "' or '" + PARAGRAPH_MODE + "'");
        }
        SyndFeed feed = (SyndFeed) pageContext.getRequest().getAttribute(feedId);
        if (feed != null) {
            int itemIndex = ItemUtilities.deriveItemIndex(pageContext, feedId, index);
            if (itemIndex >= 0 && itemIndex <= feed.getEntries().size()) {
                SyndEntry entry = (SyndEntry) feed.getEntries().get(itemIndex);
                String text;
                if (mode.equals(PARAGRAPH_MODE)) {
                    text = extractParagraph(entry.getDescription().getValue().trim());
                } else {
                    text = extractSentence(entry.getDescription().getValue().trim());
                }
                TagUtilities.print(pageContext.getOut(), text);
            }
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    private String extractParagraph(String rawPost) {
        String post = HtmlUtilities.cleanup(rawPost);
        String trimmedPost = HtmlUtilities.removePreamble(post);
        String truncatedPost = HtmlUtilities.extractFirstParagraph(trimmedPost);
        return truncatedPost;
    }

    private String extractSentence(String rawPost) {
        String post = HtmlUtilities.cleanup(rawPost);
        String trimmedPost = HtmlUtilities.removePreamble(post);
        String truncatedPost = HtmlUtilities.extractFirstSentence(trimmedPost);
        return truncatedPost;
    }
}
