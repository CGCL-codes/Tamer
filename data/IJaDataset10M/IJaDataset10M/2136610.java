package backend.query.test;

import java.io.File;
import junit.framework.TestCase;
import backend.ONDEX;
import backend.core.persistent.test.DirUtils;
import backend.core.security.Session;
import backend.parser.AbstractONDEXParser;
import backend.parser.ParserArguments;
import backend.query.Query;
import backend.query.QueryEnv;

/**
 * @author sierenk This class tests queries on index graphs and normal graphs.
 */
public class KeggQueryTest extends TestCase {

    private static final Session s = Session.NONE;

    QueryEnv qenv = null;

    ONDEX ondex = null;

    private static final String ondexName = "keggquerytest";

    private static final String filenameDB = System.getProperty("ondex.dir") + File.separator + "dbs" + File.separator + ondexName + File.separator + "ondex.dbs";

    protected void setUp() throws Exception {
        DirUtils.deleteTree(filenameDB);
        String ondexDir = System.getProperty("ondex.dir");
        ondex = new ONDEX(s, ondexName);
        ParserArguments pa = new ParserArguments();
        pa.setInputDir(ondexDir + File.separator + "importdata" + File.separator + "kegg" + File.separator);
        pa.addOption("config", "ath");
        AbstractONDEXParser parser = new backend.parser.kegg.Parser(s);
        parser.addParserListener(ondex.getLogger());
        parser.setParserArguments(pa);
        parser.setONDEXGraph(ondex.getONDEXGraph());
        ondex.getPersistentEnv().commit();
        System.out.println("KEGG statistics concepts " + ondex.getONDEXGraph().getConcepts(s).size());
        System.out.println("KEGG statistics relations" + ondex.getONDEXGraph().getRelations(s).size());
        pa = new ParserArguments();
        pa.setInputDir(ondexDir + File.separator + "importdata" + File.separator + "tf" + File.separator);
        parser = new backend.parser.tf.Parser(s);
        parser.addParserListener(ondex.getLogger());
        parser.setParserArguments(pa);
        parser.setONDEXGraph(ondex.getONDEXGraph());
        ondex.getPersistentEnv().commit();
        System.out.println("KEGG+TF statistics concepts " + ondex.getONDEXGraph().getConcepts(s).size());
        System.out.println("KEGG+TF statistics relations" + ondex.getONDEXGraph().getRelations(s).size());
        ParserArguments.getValidator("Taxonomy").cleanup();
        ondex.getPersistentEnv().cleanup();
        long start = System.currentTimeMillis();
        qenv = new QueryEnv(s, filenameDB, true);
        System.out.println("Query init took " + (System.currentTimeMillis() - start) + " msec.");
    }

    public void testQueryForThisCV() throws Exception {
        Query query = qenv.getQuery();
        long start = System.currentTimeMillis();
        int size = query.queryForThisCV("KEGG").size();
        System.out.println(size);
        System.out.println("QUERY took: " + (System.currentTimeMillis() - start) + " msec.");
    }

    public void testGetDetailsAndGetDataOVTK() throws Exception {
        System.out.println("Performance Test OVTKQuery getDetails() query" + "\n");
        long start = System.currentTimeMillis();
        System.out.println(qenv.getAllDataSizeStatistics());
        System.out.println("getDetails() QUERY(normal graph)took: " + (System.currentTimeMillis() - start) + " msec.");
    }

    public void testQueryConceptClassPerformance() throws Exception {
        System.out.println("Performance Test Query for concept classes " + "\n");
        long start = System.currentTimeMillis();
        Query oq = qenv.getQuery();
        System.out.println("HITS " + oq.queryForThisConceptClass("Gene").size());
        System.out.println("HITS " + oq.queryRelationsForThisConceptClass("Gene").size());
        System.out.println("getData(Parameter gene): QUERY(normal graph)took: " + (System.currentTimeMillis() - start) + " msec.");
        start = System.currentTimeMillis();
        System.out.println("HITS " + oq.queryForThisConceptClass("Treatment").size());
        System.out.println("HITS " + oq.queryRelationsForThisConceptClass("Treatment").size());
        System.out.println("getData(Parameter treatment): QUERY(normal graph)took: " + (System.currentTimeMillis() - start) + " msec.");
    }

    /**
	 * Closes persistent environments/databases and deletes files. This method
	 * is excecuted for every test case(test method).
	 * 
	 */
    protected void tearDown() throws Exception {
        qenv.cleanup();
        qenv = null;
        ondex = null;
        DirUtils.deleteTree(filenameDB);
    }
}
