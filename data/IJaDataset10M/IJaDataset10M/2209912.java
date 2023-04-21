package org.s3b.search.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import org.corrib.jonto.JOnto;
import org.corrib.jonto.JOntoContext;
import org.corrib.jonto.TaxonomyContext;
import org.corrib.jonto.TaxonomyEntry;
import org.corrib.jonto.wordnet.AbstractMeaning;
import org.corrib.jonto.wordnet.LocalizedWord;
import org.corrib.jonto.wordnet.NotRecognizedSynsetException;
import org.foafrealm.manage.Person;
import org.foafrealm.manage.PersonFactory;
import org.s3b.search.db.SesameWrapper;
import org.openrdf.model.Graph;
import org.openrdf.sesame.config.AccessDeniedException;
import org.openrdf.sesame.constants.QueryLanguage;
import org.openrdf.sesame.query.QueryResultsTable;
import org.openrdf.sesame.repository.local.LocalRepository;
import org.openrdf.sesame.sail.StatementIterator;
import org.s3b.search.rdf.Literal;
import org.s3b.search.rdf.RDFObjectFactory;
import org.s3b.search.rdf.RDFProperty;
import org.s3b.search.rdf.Resource;
import org.s3b.search.rdf.Statement;
import org.s3b.search.rdf.URI;
import org.s3b.search.resource.RdfResource;
import org.s3b.search.resource.ResourceProperties;

/**
 * @author Jakub Demczuk
 *
 */
public class BookResourceLoader implements ResourceLoader<Person, TaxonomyEntry, AbstractMeaning> {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger("BookResourceLoader");

    public BookResourceLoader() {
    }

    public static List<String> loadBooks(String query) {
        QueryResultsTable table = SesameWrapper.performTableQuery(Repository.JEROMEDL_REPOSITORY.getLocalRepository(), QueryLanguage.SERQL, query);
        List<String> books = new ArrayList<String>(table.getRowCount());
        for (int i = 0; i < table.getRowCount(); i++) {
            books.add(table.getValue(i, 0).toString());
        }
        return books;
    }

    public org.s3b.search.resource.Resource<Person, TaxonomyEntry, AbstractMeaning> loadResource(String uri) {
        RdfResource result = new RdfResource(uri, 0.0d);
        String query = String.format(RDFQuery.RDFQ_CONSTRUCT_BOOK_BY_URI.toString(), uri);
        Collection<Statement> statements = SesameWrapper.performGraphQueryAsStatements(Repository.JEROMEDL_REPOSITORY.getLocalRepository(), QueryLanguage.SERQL, query);
        fillResource(result, statements);
        return result;
    }

    public org.s3b.search.resource.Resource<Person, TaxonomyEntry, AbstractMeaning> loadResource(String uri, Double rank) {
        RdfResource result = new RdfResource(uri, rank);
        String query = String.format(RDFQuery.RDFQ_CONSTRUCT_BOOK_BY_URI.toString(), uri);
        Collection<Statement> statements = SesameWrapper.performGraphQueryAsStatements(Repository.JEROMEDL_REPOSITORY.getLocalRepository(), QueryLanguage.SERQL, query);
        fillResource(result, statements);
        return result;
    }

    public List<org.s3b.search.resource.Resource<Person, TaxonomyEntry, AbstractMeaning>> loadResources(Map<String, Double> uris) {
        List<org.s3b.search.resource.Resource<Person, TaxonomyEntry, AbstractMeaning>> results = new ArrayList<org.s3b.search.resource.Resource<Person, TaxonomyEntry, AbstractMeaning>>();
        for (String uri : uris.keySet()) {
            Double rank = uris.get(uri);
            uri = uri.replace("library.deri.ie", "localhost:8080/jeromedl");
            org.s3b.search.resource.Resource<Person, TaxonomyEntry, AbstractMeaning> result = loadResource(uri, rank);
            results.add(result);
        }
        return results;
    }

    public List<org.s3b.search.resource.Resource<Person, TaxonomyEntry, AbstractMeaning>> loadResources(List<String> uris) {
        List<org.s3b.search.resource.Resource<Person, TaxonomyEntry, AbstractMeaning>> results = new ArrayList<org.s3b.search.resource.Resource<Person, TaxonomyEntry, AbstractMeaning>>();
        for (String uri : uris) {
            uri = uri.replace("library.deri.ie", "localhost:8080/jeromedl");
            org.s3b.search.resource.Resource<Person, TaxonomyEntry, AbstractMeaning> result = loadResource(uri);
            results.add(result);
        }
        return results;
    }

    /**
	 * Fills given resource with actual data from database
	 * @param resource <tt>RdfResource</tt> to fill with data
	 * @param statements <tt>Graph</tt> graph corresponding to the resource
	 */
    private void fillResource(RdfResource resource, Collection<Statement> statements) {
        for (Statement statement : statements) {
            if (ResourceProperties.CREATOR.getUri().equals(statement.getPredicate())) {
                addContributor(statements, resource, statement);
            } else if (ResourceProperties.DOMAIN.getUri().equals(statement.getPredicate())) {
                addCategory(statements, resource, statement);
            } else if (ResourceProperties.KEYWORD.getUri().equals(statement.getPredicate())) {
                addKeyword(statements, resource, statement);
            }
        }
    }

    /**
	 * Add keyword that given statement points to to the given resource 
	 * @param statements
	 * @param resource
	 * @param statement
	 */
    private void addKeyword(Collection<Statement> statements, RdfResource resource, Statement statement) {
        AbstractMeaning keyword = null;
        try {
            keyword = LocalizedWord.createLocalizedWord(statement.getObjectAsResource().toString());
        } catch (NotRecognizedSynsetException e) {
            logger.fine("A synset was not recognized, it must be a custom keyword: " + statement.getObjectAsResource().toString());
        } catch (AccessDeniedException e) {
            logger.severe("The access to WordNet is denied!!!");
        }
        if (keyword == null) {
            Statement st = RDFObjectFactory.getInstance().createStatement(statements, statement.getObjectAsResource(), new URI(RDFProperty.LABEL.getURI()), null);
            if (st != null) {
                Literal literal = st.getObjectAsLiteral();
                keyword = LocalizedWord.createLocalizedWord(literal.getLabel(), new Locale(literal.getLanguage()), null);
            }
        }
        if (keyword != null) {
            resource.addKeyword(keyword);
        }
    }

    /**
	 * Adds contributor that given statement points to to the given resource 
	 * @param statements
	 * @param resource
	 * @param statement
	 */
    private void addContributor(Collection<Statement> statements, RdfResource resource, Statement statement) {
        Person contributor = PersonFactory.getPerson(statement.getObject().toString());
        resource.addContributor(contributor);
    }

    /**
	 * Adds category that given statement points to to the given resource 
	 * @param statements
	 * @param resource
	 * @param statement
	 */
    private void addCategory(Collection<Statement> statements, RdfResource resource, Statement statement) {
        Resource res = statement.getSubject();
        URI jontoURI = new URI(JOnto.JONTO_JO_ISIN_TAXONOMY_CONTEXT.toString());
        TaxonomyEntry category = null;
        Statement context = RDFObjectFactory.getInstance().createStatement(statements, res, jontoURI, null);
        if (context == null) {
            category = JOntoContext.getEntry(res.toString());
        } else {
            String contextName = context.getObjectAsResource().toString();
            TaxonomyContext<TaxonomyEntry> taxContext = JOntoContext.getInstance().getContext(contextName);
            if (taxContext != null) {
                category = taxContext.getEntryByUri(statement.getObjectAsResource().toString());
            }
        }
        if (category != null) {
            resource.addCategory(category);
        }
    }
}
