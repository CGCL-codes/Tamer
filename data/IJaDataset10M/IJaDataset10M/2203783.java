package org.pointrel.pointrel20090201.test;

import java.io.File;
import org.pointrel.pointrel20090201.ArchiveBackendUsingDirectory;
import org.pointrel.pointrel20090201.ArchiveWithNoIndex;
import org.pointrel.pointrel20090201.Transaction;

public class DomParserTest {

    static String filePath = "trunk/Pointrel20090201/TestArchive/";

    static String fileName = "PointrelTransaction_ca9fc309-c09f-4271-aca3-206f2bd3a94a.xml";

    public static void main(String[] args) {
        ArchiveWithNoIndex archive = new ArchiveWithNoIndex(new ArchiveBackendUsingDirectory(filePath));
        archive.load();
        System.out.println("Loaded files");
        for (int j = 0; j < 0; j++) {
            System.out.println("Making test transaction number " + j);
            Transaction transaction = new Transaction();
            transaction.generateIdentifier();
            for (int i = 0; i < 100; i++) {
                transaction.addTripleForFields("", "test", "", "a", "", "b", "", "c");
                transaction.addTripleForFields("testType", "test", "aType", "a", "bType", "b", "cType", "c");
            }
            String fileName = "PointrelTransaction_" + transaction.identifier + ".xml";
            System.out.println("Starting write for" + fileName);
            transaction.writeToXMLFile(new File(filePath, fileName).getAbsolutePath());
            System.out.println("Done");
        }
    }
}
