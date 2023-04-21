package uk.ac.ebi.intact.view.webapp.controller.search;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.model.CvTopic;
import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Loads the values for the filters.
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: FilterPopulatorController.java 13274 2009-06-17 08:27:49Z baranda $
 */
@Controller("filterPopulator")
public class FilterPopulatorController {

    private static final Log log = LogFactory.getLog(FilterPopulatorController.class);

    public static final String NOT_SPECIFIED_VALUE = "Not specified";

    public static final String EXPANSION_SPOKE_VALUE = "Spoke";

    private List<String> datasets;

    private List<String> sources;

    private List<String> expansions;

    private List<SelectItem> datasetSelectItems;

    private List<SelectItem> sourceSelectItems;

    private List<SelectItem> expansionSelectItems;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public FilterPopulatorController() {
    }

    @PostConstruct
    public void loadFilters() {
        if (log.isInfoEnabled()) log.info("Preloading filters");
        if (log.isDebugEnabled()) log.debug("\tPreloading datasets");
        datasetSelectItems = listDatasets();
        datasets = getValues(datasetSelectItems);
        if (log.isDebugEnabled()) log.debug("\tPreloading sources");
        sourceSelectItems = listSources();
        sources = getValues(sourceSelectItems);
        if (log.isDebugEnabled()) log.debug("\tPreloading expansions");
        expansionSelectItems = listExpansionSelectItems();
        expansions = getValues(expansionSelectItems);
    }

    private List<String> getValues(Collection<SelectItem> selectItems) {
        List<String> values = new ArrayList<String>();
        for (SelectItem selectItem : selectItems) {
            values.add((String) selectItem.getValue());
        }
        return values;
    }

    @Transactional(readOnly = true)
    public List<SelectItem> listDatasets() {
        Query query = entityManagerFactory.createEntityManager().createQuery("select distinct a.annotationText " + "from Annotation a " + "where a.cvTopic.identifier = :datasetMi");
        query.setParameter("datasetMi", CvTopic.DATASET_MI_REF);
        List<String> datasetResults = query.getResultList();
        List<SelectItem> datasets = new ArrayList<SelectItem>(datasetResults.size() + 1);
        for (String dataset : datasetResults) {
            String[] ds = dataset.split("-");
            String value;
            String description;
            if (ds.length > 1) {
                value = ds[0].trim();
                description = ds[1].trim();
            } else {
                value = ds[0];
                description = ds[0];
            }
            datasets.add(new SelectItem(dataset, value, description));
        }
        return datasets;
    }

    @Transactional
    public List<SelectItem> listSources() {
        Query query = entityManagerFactory.createEntityManager().createQuery("select distinct i.owner.shortLabel from InteractionImpl i");
        List<String> sourceResults = query.getResultList();
        List<SelectItem> sources = new ArrayList<SelectItem>(sourceResults.size());
        for (String source : sourceResults) {
            sources.add(new SelectItem(source));
        }
        return sources;
    }

    private List<SelectItem> listExpansionSelectItems() {
        List<SelectItem> expansions = new ArrayList<SelectItem>(2);
        expansions.add(new SelectItem(NOT_SPECIFIED_VALUE, "Experimental binary (e.g. 2 hybrid)"));
        expansions.add(new SelectItem(EXPANSION_SPOKE_VALUE, "Spoke expanded co-complex (e.g. pulldown)"));
        return expansions;
    }

    public List<SelectItem> getDatasetSelectItems() {
        return datasetSelectItems;
    }

    public List<SelectItem> getSourceSelectItems() {
        return sourceSelectItems;
    }

    public List<String> getDatasets() {
        return new ArrayList<String>(datasets);
    }

    public void setDatasets(List<String> datasets) {
        this.datasets = datasets;
    }

    public List<String> getSources() {
        return new ArrayList<String>(sources);
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public List<String> getExpansions() {
        return new ArrayList<String>(expansions);
    }

    public void setExpansions(List<String> expansions) {
        this.expansions = expansions;
    }

    public List<SelectItem> getExpansionSelectItems() {
        return expansionSelectItems;
    }
}
