package de.mogwai.reports.generic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;

public class GenericDocumentWriter<T extends GenericDocument<? extends GenericDocumentEntry>> {

    private static final Logger LOGGER = Logger.getLogger(GenericDocumentWriter.class);

    public void write(T aDocument, OutputStream aOutputStream) throws IOException {
        LOGGER.info("Writing document to stream");
        ZipOutputStream theZipOut = new ZipOutputStream(aOutputStream);
        for (GenericDocumentEntry theEntry : aDocument.getEntries()) {
            LOGGER.info("Writing entry " + theEntry.getName());
            ZipEntry theZipEntry = new ZipEntry(theEntry.getName());
            theZipOut.putNextEntry(theZipEntry);
            theZipOut.write(theEntry.getData());
            theZipOut.closeEntry();
        }
        theZipOut.close();
        LOGGER.info("Finished");
    }

    public void writeUnpacked(T aDocument, File aTargetDirectory) throws IOException {
        LOGGER.info("Writing unpacked document");
        aTargetDirectory.mkdirs();
        for (GenericDocumentEntry theEntry : aDocument.getEntries()) {
            LOGGER.info("Writing entry " + theEntry.getName());
            File theTargetFile = new File(aTargetDirectory, theEntry.getName());
            theTargetFile.getParentFile().mkdirs();
            FileOutputStream theFos = new FileOutputStream(theTargetFile);
            theFos.write(theEntry.getData());
            theFos.close();
        }
        LOGGER.info("Finished");
    }
}
