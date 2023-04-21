package com.entelience.servlet.audit;

import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.entelience.objects.audit.AuditDocumentId;
import com.entelience.objects.audit.DocumentMeta;
import com.entelience.objects.audit.Document;
import com.entelience.provider.audit.DbDocument;
import com.entelience.provider.audit.DbPerso;
import com.entelience.servlet.FileUploadServlet;
import com.entelience.sql.Db;
import com.entelience.sql.DbConnection;
import com.entelience.directory.Company;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

/**
 * servlet invoked when uploading a document
 *
 */
public class DocumentUpload extends FileUploadServlet {

    /**
     * parameters names :
     * documentId
     * file (uploaded file)
     *
     */
    public void buildOutput(HttpServletRequest request, HttpServletResponse response, Integer peopleId) throws Exception {
        Db db = getDb();
        try {
            db.begin();
            Integer documentId = null;
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (!isMultipart) {
                throw new IllegalArgumentException("No attached document");
            }
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iter = upload.getItemIterator(request);
            byte[] content = null;
            String fileName = null;
            String mimeType = null;
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    String value = Streams.asString(stream);
                    _logger.debug("Form field " + name + " with value " + value + " detected.");
                    if ("documentId".equals(name) && documentId == null) {
                        documentId = Integer.valueOf(value);
                    }
                } else {
                    content = getUploadedBinary(item);
                    mimeType = item.getContentType();
                    fileName = item.getName();
                }
            }
            if (content == null) {
                throw new IllegalArgumentException("No attached document found");
            }
            if (mimeType == null) {
                _logger.warn("No mime type found for uploaded attachment");
            }
            if (documentId == null) {
                documentId = getParamInteger(request, "documentId");
            }
            AuditDocumentId docId = new AuditDocumentId(documentId, 0);
            DocumentMeta meta = new DocumentMeta();
            meta.setMimeType(mimeType);
            meta.setFileName(fileName);
            Document doc = DbDocument.getDocumentDescription(db, docId);
            if (doc == null) throw new IllegalArgumentException("No document found for documentId " + documentId);
            DbPerso.checkUserCanModifyDocument(db, peopleId, doc.getDocumentId());
            DbDocument.updateDocument(db, doc, meta, content, peopleId);
            db.commit();
            response.getOutputStream().print(1);
        } finally {
            db.safeClose();
        }
    }
}
