package org.deri.xquery.saxon;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.value.SequenceType;
import org.deri.sparql.SPARQLQuery;
import com.hp.hpl.jena.graph.Factory;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.query.DataSource;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFReader;
import com.hp.hpl.jena.shared.NotFoundException;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.FileUtils;

/**
 * 
 * @author <a href="mailto:nuno [dot] lopes [at] deri [dot] org">Nuno Lopes</a>
 * @version 1.0
 */
public class sparqlQueryTDBExtFunction extends ExtensionFunctionDefinition {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4238279113552531635L;

    /**
   * Name of the function
   * 
   */
    private static StructuredQName funcname = new StructuredQName("_xsparql", "http://xsparql.deri.org/demo/xquery/xsparql.xquery", "_sparqlQueryTDB");

    private String location;

    public sparqlQueryTDBExtFunction() {
        this.location = EvaluatorExternalFunctions.getDefaultTDBDatasetLocation();
    }

    public sparqlQueryTDBExtFunction(String location) {
        this.location = location;
    }

    @Override
    public StructuredQName getFunctionQName() {
        return funcname;
    }

    @Override
    public int getMinimumNumberOfArguments() {
        return 1;
    }

    @Override
    public int getMaximumNumberOfArguments() {
        return 2;
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return new SequenceType[] { SequenceType.SINGLE_STRING, SequenceType.SINGLE_STRING };
    }

    @Override
    public SequenceType getResultType(SequenceType[] suppliedArgumentTypes) {
        return SequenceType.ANY_SEQUENCE;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return new ExtensionFunctionCall() {

            private static final long serialVersionUID = 6073685306203679400L;

            @Override
            public SequenceIterator call(SequenceIterator[] arguments, XPathContext context) throws XPathException {
                String queryString = arguments[0].next().getStringValue();
                String loc = arguments[1].next().getStringValue();
                if (!loc.equals("")) {
                    location = loc;
                }
                Dataset dataset = EvaluatorExternalFunctions.getTDBDataset(location);
                SPARQLQuery query = processQuery(queryString, dataset);
                ResultSet resultSet = query.getResults();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ResultSetFormatter.outputAsXML(outputStream, resultSet);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                dataset.close();
                return SingletonIterator.makeIterator(context.getConfiguration().buildDocument(new StreamSource(inputStream)));
            }
        };
    }

    private SPARQLQuery processQuery(String queryString, Dataset defaultDataset) {
        DataSource dataset = DatasetFactory.create(defaultDataset);
        queryString = getDatasets(queryString, dataset);
        SPARQLQuery query = new SPARQLQuery(queryString, dataset);
        return query;
    }

    private String getDatasets(String query, DataSource dataset) {
        if (query == null) {
            return null;
        }
        List<String> namedGraphURIs = new ArrayList<String>();
        List<String> dftGraphURI = new ArrayList<String>();
        String[] result = query.split("\\s");
        StringBuffer q = new StringBuffer();
        for (int x = 0; x < result.length; x++) {
            if (result[x].equalsIgnoreCase("FROM")) {
                if (result[x + 1].equalsIgnoreCase("NAMED")) {
                    x += 2;
                } else {
                    dftGraphURI.add(result[x + 1].replaceAll("[\\<\\>]", ""));
                    x += 1;
                }
            } else {
                q.append(" ");
                q.append(result[x]);
            }
        }
        try {
            addDefaultModel(dftGraphURI, dataset);
            addNamedModel(namedGraphURIs, dataset);
        } catch (Exception e) {
            System.err.println("error building the graphs: " + e.getMessage());
            System.exit(1);
        }
        return q.toString();
    }

    private void addDefaultModel(List<String> graphURLs, DataSource dataset) throws Exception {
        Model model = dataset.getDefaultModel();
        for (String uri : graphURLs) {
            if (uri == null) {
                continue;
            }
            if (uri.equals("")) {
                continue;
            }
            try {
                model = readModel(uri);
                dataset.setDefaultModel(model);
            } catch (Exception ex) {
                throw new Exception("Failed to load (default) URL " + uri + " : " + ex.getMessage());
            }
        }
        if (model != null) dataset.setDefaultModel(model);
    }

    private void addNamedModel(List<String> namedGraphs, DataSource dataset) throws Exception {
        if (namedGraphs != null) {
            for (String uri : namedGraphs) {
                if (uri == null) {
                    continue;
                }
                if (uri.equals("")) {
                    continue;
                }
                try {
                    if (!dataset.containsNamedModel(uri)) {
                        Model model2 = readModel(uri);
                        dataset.addNamedModel(uri, model2);
                    }
                } catch (Exception ex) {
                    throw new Exception("Failed to load URL " + uri);
                }
            }
        }
    }

    private static Model readModel(String uri) {
        Graph g = Factory.createGraphMem();
        String syntax = null;
        {
            String altURI = FileManager.get().mapURI(uri);
            if (altURI != null) syntax = FileUtils.guessLang(uri);
        }
        Model m = ModelFactory.createModelForGraph(g);
        RDFReader r = m.getReader(syntax);
        InputStream in = FileManager.get().open(uri);
        if (in == null) throw new NotFoundException("Not found: " + uri);
        r.read(m, in, uri);
        return m;
    }
}
