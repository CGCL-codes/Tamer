package variant.dl;

import java.io.Reader;
import junit.framework.TestCase;
import org.omwg.ontology.Ontology;
import org.wsml.reasoner.impl.WSMO4JManager;
import org.wsml.reasoner.transformation.OntologyNormalizer;
import org.wsmo.common.TopEntity;
import org.wsmo.factory.Factory;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.factory.WsmoFactory;
import org.wsmo.wsml.Parser;
import org.wsmo.wsml.Serializer;
import base.BaseReasonerTest;

/**
 * 
 * @author Nathalie Steinmetz, DERI Innsbruck
 *
 */
public abstract class BaseDLReasonerTest extends TestCase {

    protected static int ontologyCount = 1;

    protected OntologyNormalizer normalizer;

    protected WsmoFactory wsmoFactory;

    protected LogicalExpressionFactory leFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        WSMO4JManager wmsoManager = new WSMO4JManager();
        wsmoFactory = wmsoManager.getWSMOFactory();
        leFactory = wmsoManager.getLogicalExpressionFactory();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected Ontology createOntology() {
        return createOntology("http://LoR.wsml#");
    }

    protected Ontology createOntology(String namespace) {
        int ontologyNumber = ontologyCount++;
        Ontology ontology = wsmoFactory.createOntology(wsmoFactory.createIRI("urn://LoR" + Integer.toString(ontologyNumber)));
        ontology.setDefaultNamespace(wsmoFactory.createIRI(namespace));
        return ontology;
    }

    protected Ontology parseOntology(String fileName) throws Exception {
        Parser parser = Factory.createParser(null);
        Reader input = BaseReasonerTest.getReaderForFile(fileName);
        return (Ontology) parser.parse(input)[0];
    }

    public static String serializeOntology(Ontology ontology) {
        StringBuffer buf = new StringBuffer();
        Serializer serializer = Factory.createSerializer(null);
        serializer.serialize(new TopEntity[] { ontology }, buf);
        return buf.toString();
    }
}
