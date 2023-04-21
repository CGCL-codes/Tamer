package at.jku.semwiq.mediator.examples;

import at.jku.semwiq.mediator.Mediator;
import at.jku.semwiq.mediator.MediatorException;
import at.jku.semwiq.mediator.MediatorImpl;
import at.jku.semwiq.mediator.conf.ConfigException;
import at.jku.semwiq.mediator.dataset.SemWIQVirtualModel;
import at.jku.semwiq.mediator.registry.RegistryException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * @author dorgon, Andreas Langegger, al@jku.at
 *
 */
public class SemWIQModelExample {

    /**
	 * @param args
	 * @throws MediatorException 
	 * @throws ConfigException 
	 * @throws RegistryException 
	 */
    public static void main(String[] args) throws ConfigException, MediatorException, RegistryException {
        Mediator m = new MediatorImpl();
        SemWIQVirtualModel model = new SemWIQVirtualModel(m);
        m.getDataSourceRegistry().getManager().register(ExampleConstants.testEndpoint, ExampleConstants.monitoringProfile);
        m.getDataSourceRegistry().getManager().register(ExampleConstants.testEndpoint2, ExampleConstants.monitoringProfile);
        m.getDataSourceRegistry().getMonitor().waitUntilFinished();
        QueryExecution qe = QueryExecutionFactory.create("SELECT DISTINCT * WHERE { ?s a ?o }", model);
        ResultSet r = qe.execSelect();
        ResultSetFormatter.out(r);
    }
}
