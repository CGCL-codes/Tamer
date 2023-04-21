package net.sf.webwarp.util.openoffice.odt;

import java.io.IOException;
import net.sf.webwarp.util.openoffice.odt.OpenDocument;
import net.sf.webwarp.util.openoffice.odt.OpenDocumentUtil;
import org.apache.commons.lang.Validate;
import org.junit.Test;

public class OpenDocumentTest {

    @Test
    public void testOpenOffice() throws IOException {
        OpenDocument openDocument = OpenDocumentUtil.readOpenDocumentFromResource("/ch/orcasys/util/openoffice/odt/test-good.odt");
        String content = openDocument.getContent();
        Validate.isTrue(content.length() > 100);
        openDocument.setContent("test");
        byte[] document = openDocument.getFileData();
        openDocument = OpenDocumentUtil.readOpenDocument(document);
        Validate.isTrue(openDocument.getContent().equals("test"));
    }

    public static void main(String[] args) {
        try {
            OpenDocumentTest test = new OpenDocumentTest();
            test.testOpenOffice();
            System.out.println("OK");
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
