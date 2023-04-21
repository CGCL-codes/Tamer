package au.edu.uq.itee.maenad.pronto.resources;

import au.edu.uq.itee.maenad.pronto.Pronto;
import au.edu.uq.itee.maenad.pronto.index.IndexException;
import au.edu.uq.itee.maenad.pronto.index.OntologyIndex;
import au.edu.uq.itee.maenad.pronto.model.Ontology;
import au.edu.uq.itee.maenad.pronto.model.User;
import au.edu.uq.itee.maenad.restlet.AbstractFreemarkerResource;
import au.edu.uq.itee.maenad.restlet.errorhandling.InitializationException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

public class QuickSearchResource extends AbstractFreemarkerResource<User> {

    /**
     * A map containing the search fields to search through and the matching
     * heading to use when rendering results.
     */
    @SuppressWarnings("serial")
    private static final Map<String, String> SEARCH_FIELDS = new LinkedHashMap<String, String>() {

        {
            put("title", "Title");
            put("class", "Class Names");
            put("property", "Property Names");
            put("description", "Description");
            put("keywords", "Keywords");
            put("inFileComment", "Comments in File");
        }
    };

    private OntologyIndex ontologyIndex;

    public QuickSearchResource() throws InitializationException {
        super();
        this.ontologyIndex = Pronto.getConfiguration().getOntologyIndex();
        setContentTemplateName("quickSearch.html");
        getVariants().add(new Variant(MediaType.TEXT_HTML));
    }

    @Override
    protected void fillDatamodel(Map<String, Object> datamodel) throws ResourceException {
        Form form = getRequest().getResourceRef().getQueryAsForm();
        String queryString = getFirstFormValue(form, "q", "");
        datamodel.put("query", queryString);
        Map<String, List<Ontology>> results = new HashMap<String, List<Ontology>>();
        if (!queryString.isEmpty()) {
            for (String fieldName : SEARCH_FIELDS.keySet()) {
                try {
                    QueryParser parser = new QueryParser(fieldName, new StandardAnalyzer());
                    Query result = parser.parse(queryString);
                    results.put(fieldName, ontologyIndex.findMatches(result.toString()));
                } catch (IndexException ex) {
                    Logger.getLogger(QuickSearchResource.class.getName()).log(Level.SEVERE, "Failed to query the index", ex);
                } catch (ParseException ex) {
                    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Illegal query syntax", ex);
                }
            }
        }
        datamodel.put("searchFields", SEARCH_FIELDS);
        datamodel.put("results", results);
    }

    @Override
    protected boolean getAllowed(User user, Variant variant) {
        return getAccessPolicy().getAccessLevelForClass(user, Ontology.class).canRead();
    }
}
