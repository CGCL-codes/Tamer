package backend.mapping.test;

import java.io.File;
import junit.framework.TestCase;
import backend.ONDEX;
import backend.core.AbstractONDEXIterator;
import backend.core.AbstractRelation;
import backend.core.searchable.LuceneEnv;
import backend.core.security.Session;
import backend.mapping.AbstractONDEXMapping;
import backend.mapping.MappingArguments;
import backend.parser.AbstractONDEXParser;
import backend.parser.ParserArguments;
import backend.tools.DirUtils;

/**
 * Test for different mapping methods.
 * 
 * @author taubertj
 *
 */
public abstract class AbstractMappingTest extends TestCase {

    protected Session session = Session.NONE;

    private String ondexDir = System.getProperty("ondex.dir");

    private ONDEX ondex;

    private LuceneEnv lenv;

    protected abstract AbstractONDEXMapping getMapping();

    protected abstract String getMappingName();

    @Override
    protected void setUp() throws Exception {
        ondex = new ONDEX(session, getMappingName() + "MappingTest", ONDEX.BERKLEY);
        ParserArguments pa = new ParserArguments();
        pa.setInputDir(ondexDir + File.separator + "importdata" + File.separator + "ec");
        AbstractONDEXParser parser = new backend.parser.ec.Parser(session);
        parser.setParserArguments(pa);
        parser.getParserArguments().setPersistentEnv(ondex.getPersistentEnv());
        parser.addParserListener(ondex.getLogger());
        parser.setONDEXGraph(ondex.getONDEXGraph());
        pa = new ParserArguments();
        pa.setInputDir(ondexDir + File.separator + "importdata" + File.separator + "go");
        parser = new backend.parser.go.Parser(session);
        parser.setParserArguments(pa);
        parser.getParserArguments().setPersistentEnv(ondex.getPersistentEnv());
        parser.addParserListener(ondex.getLogger());
        parser.setONDEXGraph(ondex.getONDEXGraph());
        ondex.getPersistentEnv().commit();
        String indexdir = ondexDir + File.separator + "dbs" + File.separator + getMappingName() + "MappingTest" + File.separator + "index";
        lenv = new LuceneEnv(session, indexdir, true);
        lenv.addONDEXListener(ondex.getLogger());
        lenv.setONDEXGraph(ondex.getONDEXGraph());
    }

    @Override
    protected void tearDown() throws Exception {
        lenv.cleanup();
        ondex.getPersistentEnv().cleanup();
        ParserArguments.getValidator("Taxonomy").cleanup();
        ParserArguments.getValidator("CVRegex").cleanup();
        ondex = null;
        DirUtils.deleteTree(System.getProperty("ondex.dir") + File.separator + "dbs" + File.separator + getMappingName() + "MappingTest");
    }

    /**
	 * Tests a given mapping method on the imported
	 * data sets.
	 *
	 */
    public void testMappingMethod() {
        AbstractONDEXIterator<AbstractRelation> rit = ondex.getONDEXGraph().getRelations(session);
        long before = rit.size();
        rit.close();
        rit = null;
        AbstractONDEXMapping mapping = getMapping();
        MappingArguments ma = mapping.getMappingArguments();
        ma.setIndexedEnv(lenv);
        ma.addOption("equivalentCC", "EC,BioProc");
        ma.addOption("equivalentCC", "EC,MolFunc");
        ma.addOption("equivalentCC", "EC,CelComp");
        mapping.setMappingArguments(ma);
        mapping.addMappingListener(ondex.getLogger());
        long start = System.currentTimeMillis();
        mapping.setONDEXGraph(ondex.getONDEXGraph());
        System.out.println(getMappingName() + " took " + (System.currentTimeMillis() - start) + " msec.");
        rit = ondex.getONDEXGraph().getRelations(session);
        long after = rit.size();
        rit.close();
        rit = null;
        System.out.println("Relations create: " + (after - before));
        assertTrue((after - before) > 0);
        before = after;
        AbstractONDEXMapping transitive = new backend.mapping.transitive.Mapping(session);
        start = System.currentTimeMillis();
        transitive.setONDEXGraph(ondex.getONDEXGraph());
        System.out.println("Transitive mapping took " + (System.currentTimeMillis() - start) + " msec.");
        rit = ondex.getONDEXGraph().getRelations(session);
        after = rit.size();
        rit.close();
        rit = null;
        System.out.println("Relations create by transitive: " + (after - before));
        assertTrue((after - before) > 0);
    }
}
