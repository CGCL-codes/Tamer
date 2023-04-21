package com.germinus.xpression.cms.lucene.search;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import com.germinus.xpression.cms.lucene.ContentUriSyntaxException;
import com.germinus.xpression.cms.lucene.UnsupportedContentScheme;
import com.germinus.xpression.cms.lucene.beans.ContentBean;
import com.germinus.xpression.cms.lucene.beans.ContentDocument;

public class ContentHit implements Serializable, Comparable<ContentHit> {

    private static final String HIGHLIGHT_SEPARATOR = "...";

    private static final int MAX_HIGHLIGHT_FRAGMENTS = 3;

    private Document document;

    private ScoreDoc scoreDoc;

    private LuceneCMSQuery luceneQuery;

    private ContentBean cb;

    public ContentHit(Document doc, ScoreDoc scoreDoc, LuceneCMSQuery luceneQuery) throws ContentUriSyntaxException, UnsupportedContentScheme {
        this.document = doc;
        this.scoreDoc = scoreDoc;
        this.luceneQuery = luceneQuery;
        initContentBean();
    }

    @Override
    public int compareTo(ContentHit o) {
        return new Float(scoreDoc.score).compareTo(o.scoreDoc.score);
    }

    public ContentBean getContentBean() {
        return cb;
    }

    private void initContentBean() throws ContentUriSyntaxException, UnsupportedContentScheme {
        cb = new ContentBean(new ContentDocument(document));
        String fullText = document.get(ContentBean.FULL_TEXT);
        if (StringUtils.isEmpty(fullText)) fullText = cb.getFullText();
        try {
            cb.setBestFragments(bestFragments(fullText));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String bestFragments(String fullText) throws IOException {
        TokenStream tokenStream = luceneQuery.getAnalyzer().tokenStream(ContentBean.FULL_TEXT, new StringReader(fullText));
        String bestFragments = luceneQuery.getHighlighter().getBestFragments(tokenStream, fullText, MAX_HIGHLIGHT_FRAGMENTS, HIGHLIGHT_SEPARATOR);
        return bestFragments;
    }

    private static final long serialVersionUID = 1709415573579149525L;
}
