package info.goldenorb.mining;

import static org.junit.Assert.assertEquals;
import info.goldenorb.lang.Entity;
import info.goldenorb.lang.Ontology;
import info.goldenorb.search.LuceneIndexSearch;
import org.junit.Test;

public class OntologyCorrelationTest {

    @Test
    public void correlate() throws Exception {
        LuceneIndexSearch searcher = null;
        try {
            final String directory = "test/files/index/is";
            searcher = new LuceneIndexSearch(directory);
            final String ontologyFile = "test/files/dic/ontology_is.xml";
            Ontology ontology = new Ontology(ontologyFile);
            System.out.println(ontology.getEntityList().size());
            Entity entity = new Entity();
            entity.setDescription("Crawler");
            entity.setEntityClass("research area");
            entity.addSynonym("Web crawler");
            entity.addSynonym("Web spider");
            if (ontology.canAdd(entity)) {
                ontology.addEntity(entity);
            }
            entity = new Entity();
            entity.setDescription("Data mining");
            entity.setEntityClass("research area");
            entity.addSynonym("Mineração de dados");
            if (ontology.canAdd(entity)) {
                ontology.addEntity(entity);
            }
            System.out.println(ontology.getEntityList().size());
            OntologyCorrelation tc = new OntologyCorrelation();
            tc.setNumDocs(440);
            tc.setQueryField("content");
            tc.setWindow(10);
            tc.setThreshold(1);
            tc.correlate(ontology, searcher);
            System.out.println(tc.getGraphML().getGraphMLFile());
            assertEquals("", "");
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            if (searcher != null) searcher.close();
        }
    }
}
