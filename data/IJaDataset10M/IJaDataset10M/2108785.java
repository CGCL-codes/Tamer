package org.open18.report;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.faces.context.FacesContext;
import org.jboss.seam.annotations.Import;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Manager;
import org.jboss.seam.faces.RedirectException;
import org.jboss.seam.pdf.DocumentData;
import org.jboss.seam.pdf.DocumentStore;

@Name("reportGenerator")
@Import("org.jboss.seam.pdf")
public class ReportGenerator {

    @In
    private Manager manager;

    @In(create = true)
    private DocumentStore documentStore;

    @In
    private FacesContext facesContext;

    public void generate() throws DocumentException {
        byte[] binaryData = buildReport();
        DocumentData data = new DocumentData("report", new DocumentData.DocumentType("pdf", "application/pdf"), binaryData);
        String docId = documentStore.newId();
        documentStore.saveData(docId, data);
        String documentUrl = documentStore.preferredUrlForContent(data.getBaseName(), data.getDocumentType().getExtension(), docId);
        redirect(documentUrl);
    }

    protected void redirect(String url) {
        try {
            facesContext.getExternalContext().redirect(manager.encodeConversationId(url));
        } catch (IOException ioe) {
            throw new RedirectException(ioe);
        }
    }

    protected byte[] buildReport() throws DocumentException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, stream);
        document.open();
        document.add(new Paragraph("Open 18 Sample Report"));
        document.close();
        return stream.toByteArray();
    }
}
