package backend.parser.aracyc2.test;

import junit.framework.TestCase;
import backend.ONDEX;
import backend.core.AbstractONDEXGraph;
import backend.core.AbstractONDEXGraphMetaData;
import backend.core.security.Session;
import backend.parser.ParserArguments;
import backend.parser.aracyc2.Parser;
import backend.parser.aracyc2.parser.transformer.GeneTransformer;
import backend.parser.aracyc2.parser.transformer.factory.TransformerFactory;

public class TransformerFactoryTest extends TestCase {

    private ONDEX ondex;

    private ParserArguments pa;

    protected AbstractONDEXGraph og;

    protected AbstractONDEXGraphMetaData omd;

    private static final Session session = Session.NONE;

    Parser aracycParser;

    private static final String ondexName = "aracyc2";

    public void testGetInstance() throws Exception {
        TransformerFactory.getInstance(GeneTransformer.class, aracycParser);
    }

    @Override
    protected void setUp() {
        ondex = new ONDEX(session, ondexName, ONDEX.BERKLEY);
        og = ondex.getONDEXGraph();
        omd = og.getONDEXGraphData(session);
        pa = new ParserArguments();
        pa.setInputDir("data/importdata/aracyc2");
        ondex.getPersistentEnv().commit();
        System.out.println("Starting Aracyc Parser....");
        long start = System.currentTimeMillis();
        aracycParser = new Parser(session);
        aracycParser.setArguments(pa);
        aracycParser.addParserListener(ondex.getLogger());
        long stop = System.currentTimeMillis();
        long time = stop - start;
        System.out.println("Aracyc Parser took " + time + "msec.");
    }
}
