package de.bwb.ekp.entities;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

public class DokumentenListTest {

    private static final String ROOT_VERZEICHNIS_NAME = System.getProperty("java.io.tmpdir");

    private DokumentenList dokumentenList = new DokumentenList();

    @Test
    public void testUpload() throws IOException, MissingDocumentException, FileUploadException {
        final File uploadDir = new File(ROOT_VERZEICHNIS_NAME + File.separator + "upload");
        uploadDir.mkdirs();
        final File fileToUpload = File.createTempFile("uploadTest", "txt");
        this.dokumentenList.setFilename(fileToUpload.getName());
        this.dokumentenList.setUploadData("Dummy".getBytes());
        final Dokument dokument = new Dokument();
        this.dokumentenList.upload(dokument, false);
        assertTrue(dokument.getDaten() != null);
    }

    @Test(expected = MissingDocumentException.class)
    public void testUploadOhneDatei() throws MissingDocumentException, FileUploadException {
        this.dokumentenList.upload(new Dokument(), false);
    }
}
