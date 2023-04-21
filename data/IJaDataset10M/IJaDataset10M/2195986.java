package org.inqle.data.rdf.jena;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.inqle.data.rdf.jenabean.Persister;
import com.hp.hpl.jena.query.DataSource;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sdb.Store;

/**
 * Contains all info pertinent to a given SPARQL query.  
 * To execute a SPARQL query, simply create a QueryCriteria then pass
 * this to Queryer, as in
 * 
 *  QueryCriteria queryCriteria = new QueryCriteria(appInfo);
		queryCriteria.addNamedModel(datamodel);
		queryCriteria.setQuery(mySparql);
		RdfTable resultTable = Queryer.querySelect(queryCriteria);
 * 
 * 
 * @author David Donohue
 * Aug 7, 2007
 * 
 * TODO consider dynamic generation of SPARQL, through adding "RDF step objects"
 * TODO consider rename to SDBQueryCirteria, implementing IQueryCriteria interface.
 * TODO consider add a QueryCriteriaFactory, which creates appropriate QueryCriteria implementation
 */
public class QueryCriteria {

    private Store store;

    private List<Model> models = new ArrayList<Model>();

    private List<NamedModel> namedModels = new ArrayList<NamedModel>();

    private DataSource dataSource = null;

    private String query = "";

    private Persister persister = null;

    static Logger log = Logger.getLogger(QueryCriteria.class);

    public QueryCriteria(Persister persister) {
        this.persister = persister;
        this.dataSource = DatasetFactory.create();
    }

    /**
	 * Add a NamedModel to the list of models to query, 
	 * given the URI of the NamedModel
	 * @param datamodelUri
	 */
    public void addNamedModel(String namedModelUri) {
        NamedModel namedModel = (NamedModel) Persister.reconstitute(NamedModel.class, namedModelUri, persister.getMetarepositoryModel(), true);
        addNamedModel(namedModel);
    }

    /**
	 * Add a NamedModel to the list of models to query
	 * @param aModel
	 */
    public void addNamedModel(NamedModel namedModel) {
        assert (namedModel != null);
        namedModels.add(namedModel);
        Model model = persister.getModel(namedModel);
        assert (model != null);
        log.trace("In QueryCriteria, adding model of size " + model.size());
        models.add(model);
        dataSource.addNamedModel(namedModel.getId(), model);
    }

    /**
	 * Add a List of NamedModel to query
	 * @param models
	 */
    public void addNamedModelIds(List<String> namedModelIds) {
        for (String namedModelId : namedModelIds) {
            NamedModel datamodel = persister.getNamedModel(namedModelId);
            addNamedModel(datamodel);
        }
    }

    /**
	 * Add a List of AModels to query
	 * @param models
	 */
    public void addNamedModels(List<NamedModel> addNamedModels) {
        for (NamedModel aNamedModel : addNamedModels) {
            addNamedModel(aNamedModel);
        }
    }

    /**
	 * Get the list of AModels which have been added to this
	 * @return
	 */
    public List<NamedModel> getNamedModels() {
        return namedModels;
    }

    /**
	 * retrieves the DataSource containing all models added
	 * @return
	 */
    public Dataset getDataset() {
        return dataSource;
    }

    /**
	 * Retrieve the SPARQL query which has been added to this
	 */
    public String getQuery() {
        return query;
    }

    /**
	 * Set the SPARQL query to use
	 * @param q
	 */
    public void setQuery(String q) {
        query = q;
    }

    /**
	 * Close any open objects, if any
	 */
    public void close() {
        Iterator<Model> modelsI = models.iterator();
        while (modelsI.hasNext()) {
            Model model = (Model) modelsI.next();
            if (!model.isClosed()) model.close();
        }
        if (store != null) store.close();
    }

    /**
	 * If a single model has been added to this object, return it.  Otherwise
	 * throw an exception
	 * @return
	 */
    public Model getModel() {
        if (models.size() != 1) {
            throw new RuntimeException("QueryCriteria has had " + models.size() + " models added to it.  Should have 1 model added to it if getModel() is to be called.");
        }
        return (Model) models.get(0);
    }
}
