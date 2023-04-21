package com.jaeksoft.searchlib.index;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import com.jaeksoft.searchlib.Logging;
import com.jaeksoft.searchlib.SearchLibException;
import com.jaeksoft.searchlib.index.osse.OsseErrorHandler;
import com.jaeksoft.searchlib.index.osse.OsseLibrary;
import com.jaeksoft.searchlib.index.term.Term;
import com.jaeksoft.searchlib.index.term.TermEnum;
import com.jaeksoft.searchlib.index.term.TermFreqVector;
import com.jaeksoft.searchlib.query.MoreLikeThis;
import com.jaeksoft.searchlib.query.Query;
import com.jaeksoft.searchlib.request.AbstractRequest;
import com.jaeksoft.searchlib.request.DocumentsRequest;
import com.jaeksoft.searchlib.request.SearchRequest;
import com.jaeksoft.searchlib.result.AbstractResult;
import com.jaeksoft.searchlib.result.ResultDocument;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

public class ReaderNativeOSSE extends ReaderAbstract {

    private Pointer index;

    private OsseErrorHandler err;

    protected ReaderNativeOSSE(File configDir, IndexConfig indexConfig) throws SearchLibException {
        err = new OsseErrorHandler();
        index = OsseLibrary.INSTANCE.OSSCLib_Index_Create(new WString(configDir.getPath()), err.getPointer());
        if (index == null) throw new SearchLibException(err.getError());
    }

    @Override
    public void close() {
        if (!OsseLibrary.INSTANCE.OSSCLib_Index_Close(index, err.getPointer())) Logging.warn(err.getError());
        err.release();
    }

    protected Pointer getIndex() {
        return index;
    }

    @Override
    public ResultDocument[] documents(DocumentsRequest documentsRequest) {
        return null;
    }

    @Override
    public int getDocFreq(Term term) {
        return 0;
    }

    @Override
    public IndexStatistics getStatistics() throws IOException {
        return new IndexStatistics();
    }

    @Override
    public TermFreqVector getTermFreqVector(int docId, String field) {
        return null;
    }

    @Override
    public long getVersion() {
        return 0;
    }

    @Override
    public void push(URI dest) {
    }

    @Override
    public void reload() {
    }

    @Override
    public boolean sameIndex(ReaderInterface reader) {
        return false;
    }

    @Override
    public void swap(long version, boolean deleteOld) {
    }

    @Override
    public TermEnum getTermEnum() {
        return null;
    }

    @Override
    public TermEnum getTermEnum(String field, String term) {
        return null;
    }

    @Override
    public Collection<?> getFieldNames() {
        return null;
    }

    @Override
    public String explain(SearchRequest searchRequest, int docId, boolean bHtml) {
        return null;
    }

    @Override
    public Query rewrite(Query query) {
        return null;
    }

    @Override
    public MoreLikeThis getMoreLikeThis() throws SearchLibException {
        return null;
    }

    @Override
    public AbstractResult<?> request(AbstractRequest request) {
        return null;
    }
}
