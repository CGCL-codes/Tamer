package bioevent;

import bioevent.core.Configuration;
import bioevent.core.Util;
import bioevent.datapreparation.CorefExampleBuilder;
import bioevent.datapreparation.DocumentProcessor;

public class ManageCoref {

    public static void main(String[] args) throws Exception {
        System.out.println("Coreference Resolution\n");
        String rootPath = "/media/LENOVO/COtest/";
        System.out.println(rootPath);
        Configuration.TrainingMode = true;
        Util.db.nestedQueries = true;
        DocumentProcessor doc_proc = new DocumentProcessor();
        doc_proc.processDocuments(rootPath);
        CorefExampleBuilder corefExampleBuilder = new CorefExampleBuilder();
        corefExampleBuilder.extractCorefExamples(rootPath);
    }
}
