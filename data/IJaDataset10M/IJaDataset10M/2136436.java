package issrg.editor2.ontology.importer;

import java.io.*;

public class TextFileImporter extends PolicyOntologyImporter {

    public TextFileImporter(String filename) throws PolicyImportException {
        try {
            StringBuffer fileData = new StringBuffer(1000);
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }
            reader.close();
            PolicyOntologyImporter importer = new TextImporter(fileData.toString());
            this.ontology = importer.getOntology();
        } catch (IOException e) {
            throw new PolicyImportException("Failed to open file: " + filename, e);
        }
    }
}
