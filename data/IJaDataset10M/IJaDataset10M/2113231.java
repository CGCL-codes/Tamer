package net.sf.csutils.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import org.junit.Assert;
import org.junit.Test;
import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.utility.UndeclaredThrowableException;

public class DocumentProcessorTest {

    @Test
    public void testConditionalFormulasReplacement() throws Exception {
        final URLTemplateLoader urlTemplateLoader = new URLTemplateLoader() {

            @Override
            protected URL getURL(String pName) {
                try {
                    return new URL(pName);
                } catch (MalformedURLException e) {
                    throw new UndeclaredThrowableException(e);
                }
            }
        };
        final URL templateUrl = getClass().getResource("conditionalFormulasReplacement.xml");
        Assert.assertNotNull(templateUrl);
        Configuration configuration = new Configuration();
        configuration.setTemplateLoader(urlTemplateLoader);
        configuration.setLocale(Locale.US);
        configuration.setEncoding(Locale.US, "UTF8");
        configuration.setLocalizedLookup(false);
        final Template template = configuration.getTemplate(templateUrl.toExternalForm());
        final StringWriter sw = new StringWriter();
        template.process(null, sw);
        System.err.println("Template = " + sw);
        final String styleSheet = "ConditionalFormulaReplacementTest.xls";
        final DocumentProcessor processor = new DocumentProcessor();
        final URL spreadSheetUrl = getClass().getResource(styleSheet);
        Assert.assertNotNull(spreadSheetUrl);
        processor.setSpreadsheetUrl(spreadSheetUrl);
        processor.setSpreadsheetType(DocumentProcessor.Type.xls);
        OutputStream ostream = null;
        try {
            final File f = new File("target", styleSheet);
            if (!f.getParentFile().isDirectory()) {
                if (!f.getParentFile().mkdirs()) {
                    throw new IOException("Unable to create directory: " + f.getParentFile());
                }
            }
            ostream = new FileOutputStream(f);
            processor.process(new StringReader(sw.toString()), ostream);
            ostream.close();
            ostream = null;
        } finally {
            if (ostream != null) {
                try {
                    ostream.close();
                } catch (Throwable t) {
                }
            }
        }
    }
}
