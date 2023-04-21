package tsparql;

import java.net.URI;
import java.util.Iterator;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import arq.cmdline.CmdArgModule;
import com.hp.hpl.jena.query.ARQ;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.sparql.core.DataSourceGraph;
import org.trdf.trdf4jena.InformationConsumer;
import org.trdf.trdf4jena.TrustManager;
import org.trdf.trdf4jena.TrustValue;
import org.trdf.trdf4jena.impl.assmnt.aggregation.MinTrustAggregationFunction;
import org.trdf.trdf4jena.impl.assmnt.determination.TestTrustFunction;
import org.trdf.trdf4jena.tsparql.Constants;
import org.trdf.trdf4jena.tsparql.algebra.TrustMergeFunction;
import org.trdf.trdf4jena.tsparql.algebra.TrustMergeFunctionMin;
import org.trdf.trdf4jena.tsparql.engine.QueryEngineTrust;
import org.trdf.trdf4jena.tsparql.lang.ParserTSPARQL;

/**
 * For testing purposes
 *
 * @author Olaf Hartig
 */
public class qtest extends arq.qtest {

    public static void main(String args[]) {
        ParserTSPARQL.register();
        ARQ.getContext().setIfUndef(Constants.consumerSymbol, new InformationConsumer("http://example.org/testconsumer"));
        ARQ.getContext().setIfUndef(Constants.trustMgrSymbol, new TrustManager(new TestTrustFunction(new TrustValue(Float.parseFloat("0.8"))), new MinTrustAggregationFunction()));
        ARQ.getContext().setIfUndef(TrustMergeFunction.contextSymbol, new TrustMergeFunctionMin());
        new qtest(args).mainRun();
    }

    public qtest(String args[]) {
        super(args);
    }

    @Override
    protected void processModulesAndArgs() {
        super.processModulesAndArgs();
        QueryEngineTrust.register(true);
    }
}
