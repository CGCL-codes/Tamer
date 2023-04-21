package com.jaeksoft.searchlib.request;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import org.apache.lucene.queryParser.ParseException;
import com.jaeksoft.searchlib.SearchLibException;
import com.jaeksoft.searchlib.function.expression.SyntaxError;
import com.jaeksoft.searchlib.index.IndexAbstract;
import com.jaeksoft.searchlib.index.IndexGroup;
import com.jaeksoft.searchlib.result.ResultDocuments;

public class DocumentsGroup extends AbstractGroupRequest<DocumentsThread> {

    private DocumentsRequest documentsRequest;

    private ResultDocuments resultDocuments;

    public DocumentsGroup(IndexGroup indexGroup, DocumentsRequest documentsRequest, ExecutorService threadPool) throws IOException, URISyntaxException, ParseException, SyntaxError, ClassNotFoundException, InterruptedException, SearchLibException, IllegalAccessException, InstantiationException {
        super(indexGroup, threadPool, 60);
        this.documentsRequest = documentsRequest;
        DocumentRequest[] requestedDocuments = documentsRequest.getRequestedDocuments();
        if (requestedDocuments == null) return;
        resultDocuments = new ResultDocuments(requestedDocuments.length);
        run();
    }

    @Override
    protected boolean complete(DocumentsThread thread) {
        return true;
    }

    @Override
    protected void complete() throws IOException, URISyntaxException, ParseException, SyntaxError {
    }

    @Override
    protected DocumentsThread getNewThread(IndexAbstract index) throws ParseException, SyntaxError, IOException {
        return new DocumentsThread(resultDocuments, index, documentsRequest);
    }

    public ResultDocuments getDocuments() {
        return resultDocuments;
    }
}
