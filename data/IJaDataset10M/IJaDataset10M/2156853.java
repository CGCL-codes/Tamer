package com.cdbaby.lucene.handlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cdbaby.lucene.DocumentParser;
import com.cdbaby.lucene.IndexManager;
import com.cdbaby.lucene.LuceneDocument;
import com.cdbaby.lucene.LuceneWebService;
import com.cdbaby.lucene.ServletUtils;
import com.cdbaby.lucene.WebServiceException;

public class Remover implements Handler {

    public void execute(LuceneWebService ws, HttpServletRequest req, HttpServletResponse res, String index, String function) throws WebServiceException, IOException {
        IndexManager indexManager = ws.getIndexManager();
        PrintWriter out = res.getWriter();
        int count = 0;
        boolean err = false;
        List documents = new ArrayList();
        DocumentParser parse = new DocumentParser();
        parse.parse(req, documents);
        for (Iterator iter = documents.iterator(); iter.hasNext(); ) {
            LuceneDocument doc = (LuceneDocument) iter.next();
            String defaultField = doc.getDefaultField();
            String removeQuery = doc.getRemoveQuery();
            if (defaultField.equals("") || removeQuery.equals("")) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST, "To remove a document, you must specify the <removequery> element " + "with the defaultfield attribute.");
                err = true;
                break;
            }
            int ret = indexManager.removeDocuments(index, defaultField, removeQuery);
            if (ret < 0) {
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The given document(s) could not be removed: " + indexManager.getError());
                err = true;
                break;
            }
            count += ret;
        }
        if (count > 0) indexManager.indexModified(index);
        if (!err) ServletUtils.outputOK(out, count + " documents removed total");
    }
}
