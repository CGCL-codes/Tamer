package eu.pisolutions.ocelot.document.io.isartor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import eu.pisolutions.io.Closeables;
import eu.pisolutions.lang.Strings;
import eu.pisolutions.nio.charset.Charsets;
import eu.pisolutions.ocelot.document.io.DocumentReader;
import eu.pisolutions.ocelot.document.io.DocumentReadingException;
import eu.pisolutions.ocelot.document.io.error.ThrowingDocumentReadingErrorHandler;
import eu.pisolutions.ocelot.parsing.PdfParsingException;
import static org.testng.Assert.assertNotNull;

public class IsartorTest extends Object {

    private DocumentReader reader;

    public IsartorTest() {
        super();
    }

    @BeforeClass
    public void init() {
        this.reader = new DocumentReader();
        this.reader.setErrorHandler(ThrowingDocumentReadingErrorHandler.INSTANCE);
    }

    @Test(dataProvider = "invalidDocumentProvider", expectedExceptions = { PdfParsingException.class, DocumentReadingException.class }, groups = "isartor")
    public void testInvalidDocument(String name) throws IOException {
        final InputStream in = IsartorTest.class.getResourceAsStream("/org/pdfa/isartor/" + name);
        assertNotNull(in);
        try {
            this.reader.readDocument(in);
        } finally {
            Closeables.closeQuietly(in);
        }
    }

    @DataProvider(name = "invalidDocumentProvider")
    public Object[][] provideInvalidDocuments() throws IOException {
        final InputStream in = IsartorTest.class.getResourceAsStream("/org/pdfa/isartor/isartor.txt");
        assertNotNull(in);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charsets.CHARSET_UTF8));
        final List<String> names = new ArrayList<String>();
        while (true) {
            final String line = reader.readLine();
            if (line == null) {
                break;
            }
            if (Strings.isBlank(line)) {
                continue;
            }
            names.add(line);
        }
        final Object[][] result = new Object[names.size()][];
        for (int i = 0; i < names.size(); ++i) {
            result[i] = new Object[] { names.get(i) };
        }
        return result;
    }
}
