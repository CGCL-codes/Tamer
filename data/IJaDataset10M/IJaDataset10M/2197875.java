package com.goodcodeisbeautiful.archtea.search;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.TokenGroup;

/**
 * @author hata
 *
 */
public class MarkupTextFormatter implements Formatter {

    /** Default tag (html bold) */
    private static final String DEFAULT_TAG = "b";

    /** start tag. */
    private String m_preTag;

    /** end tag */
    private String m_postTag;

    /**
     * Default constructor.
     */
    public MarkupTextFormatter() {
        this(DEFAULT_TAG);
    }

    /**
     * Construct with tag.
     * @param tag is a tag text to hilight words.
     */
    public MarkupTextFormatter(String tag) {
        this(tag, "");
    }

    /**
     * Construct with tag and attributes.
     * @param tag is a tag text to hilight words.
     * @param attributes is an attribute text for preTag.
     * This will be use such as style sheet attributes.
     */
    public MarkupTextFormatter(String tag, String attributes) {
        m_preTag = "<" + (tag != null && tag.length() > 0 ? tag : DEFAULT_TAG) + (attributes != null && attributes.length() > 0 ? " " + attributes : "") + ">";
        m_postTag = "</" + (tag != null && tag.length() > 0 ? tag : DEFAULT_TAG) + ">";
    }

    public String highlightTerm(String originalText, TokenGroup tokenGroup) {
        if (tokenGroup.getTotalScore() <= 0.0) {
            return originalText;
        }
        StringBuffer buff = new StringBuffer();
        int startIndex = 0;
        int firstMatchedOffset = -1;
        int lastMatchedOffset = -1;
        int startPos = tokenGroup.getEndOffset();
        int endPos = tokenGroup.getStartOffset() - 1;
        int tokenCount = tokenGroup.getNumTokens();
        for (int i = 0; i < tokenCount; i++) {
            float score = tokenGroup.getScore(i);
            Token token = tokenGroup.getToken(i);
            if (score > 0.0) {
                if (firstMatchedOffset < 0) firstMatchedOffset = token.startOffset();
                lastMatchedOffset = token.endOffset();
            }
        }
        for (int i = 0; i < tokenCount; i++) {
            float score = tokenGroup.getScore(i);
            Token token = tokenGroup.getToken(i);
            if (score > 0.0) {
                startPos = Math.min(startPos, token.startOffset());
                endPos = Math.max(endPos, token.endOffset());
            } else if (startPos < lastMatchedOffset && (firstMatchedOffset - 1) < endPos) {
                buff.append(insertTag(originalText, startIndex, startPos - firstMatchedOffset, startIndex = endPos - firstMatchedOffset));
                startPos = tokenGroup.getEndOffset();
                endPos = tokenGroup.getStartOffset() - 1;
            }
        }
        if (startPos < lastMatchedOffset && (firstMatchedOffset - 1) < endPos) {
            buff.append(insertTag(originalText, startIndex, startPos - firstMatchedOffset, startIndex = endPos - firstMatchedOffset));
        }
        if (startIndex < originalText.length()) buff.append(originalText.substring(startIndex));
        return new String(buff);
    }

    private String insertTag(String originalText, int startIndex, int startTagIndex, int endTagIndex) {
        StringBuffer buff = new StringBuffer();
        buff.append(originalText.substring(startIndex, startTagIndex));
        buff.append(m_preTag);
        buff.append(originalText.substring(startTagIndex, endTagIndex));
        buff.append(m_postTag);
        return new String(buff);
    }
}
