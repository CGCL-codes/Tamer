package de.powerstaff.business.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import de.powerstaff.business.service.ProfileIndexerService;

public class ProfileExporter {

    public static void main(String[] args) throws CorruptIndexException, IOException {
        File theIndexDirectory = new File("C:\\Daten\\Arbeit\\Projekte\\Synekt\\IndexerPath");
        File theTargetDirectory = new File("C:\\Daten\\Arbeit\\Projekte\\Synekt\\Profile");
        IndexReader theReader = IndexReader.open(FSDirectory.getDirectory(theIndexDirectory));
        int errorCounter = 0;
        List<String> theKnownFiles = new ArrayList<String>();
        List<String> theKnownCodes = new ArrayList<String>();
        for (int i = 0; i < theReader.maxDoc(); i++) {
            Document theDocument = theReader.document(i);
            String theCode = theDocument.get(ProfileIndexerService.CODE);
            String theContent = theDocument.get(ProfileIndexerService.ORIG_CONTENT);
            String theStrippedPath = theDocument.get(ProfileIndexerService.STRIPPEDPATH);
            theStrippedPath = theStrippedPath.replace(".pdf", ".txt");
            theStrippedPath = theStrippedPath.replace(".doc", ".txt");
            theStrippedPath = theStrippedPath.replace(".DOC", ".txt");
            theStrippedPath = theStrippedPath.replace(".docx", ".txt");
            String theContentMD5 = DigestUtils.md5Hex(theContent);
            File theProfileFile = new File(theTargetDirectory, theStrippedPath);
            theProfileFile.getParentFile().mkdirs();
            FileWriter theWriter = new FileWriter(theProfileFile);
            theWriter.write(theContent);
            theWriter.close();
            theProfileFile.setLastModified(Long.parseLong(theDocument.get(ProfileIndexerService.MODIFIED)));
            if (i % 100 == 0) {
                System.out.println("Done with " + i + " documents");
            }
            if (theKnownFiles.contains(theContentMD5)) {
                System.out.println("File is duplicate " + theCode);
                errorCounter++;
            } else {
                theKnownFiles.add(theContentMD5);
            }
            if (theKnownCodes.contains(theCode)) {
                System.out.println("Code is duplicate " + theCode);
                errorCounter++;
            } else {
                theKnownCodes.add(theCode);
            }
        }
        System.out.println("Found " + errorCounter + " issues");
    }
}
