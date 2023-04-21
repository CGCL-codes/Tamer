package com.success.task.web.lucene.core;

import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermPositionVector;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TokenSources;
import com.success.task.web.lucene.data.ResultData;
import com.success.task.web.lucene.interfaces.IndexHighLighterInterface;
import com.success.task.web.utils.PublicSettings;

public class IndexHighLighterImpl implements IndexHighLighterInterface {

    private Highlighter highLighter = null;

    private static final String HTML_PREFIX = "<font color='color'>";

    private static final String HTML_SUFFIX = "</font>";

    private SimpleHTMLFormatter sh = null;

    @Override
    public List<ResultData> getHighLightedList(Hits hits, IndexReader reader, Query query, int page) throws Exception {
        if (null == sh) {
            sh = new SimpleHTMLFormatter(HTML_PREFIX, HTML_SUFFIX);
        }
        return getHighLightedList(hits, reader, query, sh, page);
    }

    @Override
    public List<ResultData> getHighLightedList(Hits hits, IndexReader reader, Query query, SimpleHTMLFormatter sh, int page) throws Exception {
        if (null == sh) {
            sh = new SimpleHTMLFormatter(HTML_PREFIX, HTML_SUFFIX);
        }
        highLighter = new Highlighter(sh, new QueryScorer(query));
        List<ResultData> resultData = null;
        if (null != hits && hits.length() > 0) {
            int startPage = PublicSettings.searchPageSize * (page - 1);
            int endPage = startPage + PublicSettings.searchPageSize;
            if (startPage > hits.length() || startPage > endPage || startPage < 0 || endPage <= 0) {
                startPage = 0;
                endPage = 10;
            }
            if (endPage > hits.length()) {
                endPage = hits.length();
            }
            resultData = new ArrayList<ResultData>();
            for (int i = startPage; i < endPage; i++) {
                String fileName = hits.doc(i).get(FolderIndexCreator.INDEX_FILE_NAME);
                String text = hits.doc(i).get(FolderIndexCreator.INDEX_NAME);
                String path = hits.doc(i).get(FolderIndexCreator.INDEX_RESULT_PATH);
                int type = Integer.parseInt(hits.doc(i).get("indexType"));
                long size = Integer.parseInt(hits.doc(i).get(FolderIndexCreator.INDEX_FILE_SIZE));
                int maxNumFragmentsRequired = 5;
                String fragmentSeparator = "...";
                String highlightedContents = "";
                TermPositionVector tpv = (TermPositionVector) reader.getTermFreqVector(hits.id(i), FolderIndexCreator.INDEX_NAME);
                if (null != tpv) {
                    TokenStream tokenStream = TokenSources.getTokenStream(tpv);
                    highlightedContents = highLighter.getBestFragments(tokenStream, text, maxNumFragmentsRequired, fragmentSeparator);
                }
                ResultData rd = new ResultData();
                rd.setResultContent(highlightedContents);
                rd.setResultAccessPath(path);
                rd.setResultSize(size);
                rd.setResultTitle(fileName);
                rd.setType(type);
                resultData.add(rd);
            }
        }
        return resultData;
    }

    public void setHighLighter(Highlighter hi) {
        this.highLighter = hi;
    }
}
