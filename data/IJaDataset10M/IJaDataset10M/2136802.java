package com.entelience.servlet.portal;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.entelience.export.DocumentHelper;
import com.entelience.objects.GeneratedDocument;
import com.entelience.servlet.BaseServlet;
import com.entelience.sql.Db;
import com.entelience.sql.DbConnection;

/**
 * Download a previously generated document
 */
public class GeneratedDocumentDownload extends PortalServlet {

    @Override
    public void buildOutput(HttpServletRequest request, HttpServletResponse response, Integer peopleId) throws Exception {
        int documentId = getParamInt(request, "id");
        Db db = getDb();
        try {
            db.enter();
            buildDocument(db, response, documentId);
        } finally {
            db.exit();
        }
    }

    @Override
    protected void addHeader(HttpServletResponse response, String fileName) throws Exception {
        response.setHeader("Content-Disposition", "attachment;filename=\"" + formatFileName(fileName) + "\"");
    }

    /**
     * buildDocument
     */
    public void buildDocument(Db db, HttpServletResponse response, int documentId) throws Exception {
        try {
            db.enter();
            ServletOutputStream out = response.getOutputStream();
            GeneratedDocument doc = DocumentHelper.getDocument(db, documentId);
            if (doc != null) {
                addHeader(response, doc.getFileName());
                DocumentHelper.fillOutputStreamWithDocument(db, documentId, out);
            }
        } finally {
            db.exit();
        }
    }
}
