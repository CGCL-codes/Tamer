package net.bull.javamelody;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.management.JMException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;

/**
 * Rapports pdf secondaires (avec iText).
 * @author Emeric Vernat
 */
class PdfOtherReport {

    private final OutputStream output;

    private final Document document;

    private final PdfDocumentFactory pdfDocumentFactory;

    PdfOtherReport(String application, OutputStream output) throws IOException {
        super();
        assert output != null;
        this.output = output;
        try {
            pdfDocumentFactory = new PdfDocumentFactory(application, null, output);
            this.document = pdfDocumentFactory.createDocument();
        } catch (final DocumentException e) {
            throw createIOException(e);
        }
    }

    void close() throws IOException {
        output.close();
    }

    void writeSessionInformations(List<SessionInformations> sessionsInformations) throws IOException {
        try {
            document.open();
            addParagraph(getI18nString("Sessions"), "system-users.png");
            new PdfSessionInformationsReport(sessionsInformations, document).toPdf();
        } catch (final DocumentException e) {
            throw createIOException(e);
        }
        document.close();
    }

    void writeHeapHistogram(HeapHistogram heapHistogram) throws IOException {
        try {
            document.open();
            addParagraph(I18N.getFormattedString("heap_histo_du", I18N.createDateAndTimeFormat().format(heapHistogram.getTime())), "memory.png");
            new PdfHeapHistogramReport(heapHistogram, document).toPdf();
        } catch (final DocumentException e) {
            throw createIOException(e);
        }
        document.close();
    }

    void writeProcessInformations(List<ProcessInformations> processInformations) throws IOException {
        try {
            document.open();
            addParagraph(getI18nString("Processus"), "processes.png");
            new PdfProcessInformationsReport(processInformations, document).toPdf();
        } catch (final DocumentException e) {
            throw createIOException(e);
        }
        document.close();
    }

    void writeProcessInformations(Map<String, List<ProcessInformations>> processInformationsByTitle) throws IOException {
        try {
            document.open();
            for (final Map.Entry<String, List<ProcessInformations>> entry : processInformationsByTitle.entrySet()) {
                addParagraph(entry.getKey(), "processes.png");
                new PdfProcessInformationsReport(entry.getValue(), document).toPdf();
            }
        } catch (final DocumentException e) {
            throw createIOException(e);
        }
        document.close();
    }

    void writeDatabaseInformations(DatabaseInformations databaseInformations) throws IOException {
        try {
            document.open();
            final String selectedRequestName = databaseInformations.getSelectedRequestName();
            addParagraph(getI18nString("database") + " : " + getI18nString(selectedRequestName), "db.png");
            new PdfDatabaseInformationsReport(databaseInformations, document).toPdf();
        } catch (final DocumentException e) {
            throw createIOException(e);
        }
        document.close();
    }

    void writeMBeans() throws IOException {
        try {
            document.open();
            addParagraph(getI18nString("MBeans"), "mbeans.png");
            new PdfMBeansReport(document).toPdf();
        } catch (final DocumentException e) {
            throw createIOException(e);
        } catch (final JMException e) {
            throw createIOException(e);
        }
        document.close();
    }

    void writeRuntimeDependencies(Counter counter, Range range) throws IOException {
        try {
            final Document myDocument = pdfDocumentFactory.createDocument(true);
            myDocument.open();
            final String counterLabel = I18N.getString(counter.getName() + "Label");
            final String paragraphTitle = I18N.getFormattedString("Dependance_compteur", counterLabel) + " - " + range.getLabel();
            myDocument.add(pdfDocumentFactory.createParagraphElement(paragraphTitle, counter.getIconName()));
            new PdfRuntimeDependenciesReport(counter, myDocument).toPdf();
            myDocument.close();
        } catch (final DocumentException e) {
            throw createIOException(e);
        }
    }

    void writeCounterSummaryPerClass(Collector collector, Counter counter, String requestId, Range range) throws IOException {
        final List<CounterRequest> requestList = new CounterRequestAggregation(counter).getRequestsAggregatedOrFilteredByClassName(requestId);
        try {
            document.open();
            final String counterLabel = I18N.getString(counter.getName() + "Label");
            final String title = I18N.getFormattedString("Statistiques_compteur", counterLabel) + " - " + range.getLabel();
            addParagraph(title, counter.getIconName());
            new PdfCounterReport(collector, counter, range, false, document).writeRequests(counter.getChildCounterName(), requestList);
        } catch (final DocumentException e) {
            throw createIOException(e);
        }
        document.close();
    }

    private static IOException createIOException(Exception e) {
        final IOException ex = new IOException(e.getMessage());
        ex.initCause(e);
        return ex;
    }

    private void addParagraph(String paragraphTitle, String iconName) throws DocumentException, IOException {
        add(pdfDocumentFactory.createParagraphElement(paragraphTitle, iconName));
    }

    private static String getI18nString(String key) {
        return I18N.getString(key);
    }

    private void add(Element element) throws DocumentException {
        document.add(element);
    }
}
