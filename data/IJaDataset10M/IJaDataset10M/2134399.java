package uk.ac.ebi.intact.editor.controller.curate.publication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.editor.controller.JpaAwareController;
import uk.ac.ebi.intact.model.CvTopic;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: DatasetPopulator.java 17780 2012-01-24 13:17:28Z brunoaranda $
 */
@Controller("datasetPopulator")
@Lazy
public class DatasetPopulator extends JpaAwareController {

    private static final Log log = LogFactory.getLog(DatasetPopulator.class);

    private List<String> allDatasets;

    private List<SelectItem> allDatasetSelectItems;

    public DatasetPopulator() {
    }

    @PostConstruct
    public void loadData() {
        refresh(null);
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public void refresh(ActionEvent evt) {
        if (log.isInfoEnabled()) log.info("Loading datasets");
        final Query query = getCoreEntityManager().createQuery("select distinct(a.annotationText) from Annotation a where a.cvTopic.identifier = :datasetTopicId order by a.annotationText asc");
        query.setParameter("datasetTopicId", CvTopic.DATASET_MI_REF);
        allDatasets = query.getResultList();
        allDatasetSelectItems = new ArrayList<SelectItem>(allDatasets.size() + 1);
        allDatasetSelectItems.add(new SelectItem(null, "-- Select Dataset --"));
        for (String dataset : allDatasets) {
            if (dataset != null) {
                final SelectItem selectItem = createSelectItem(dataset);
                allDatasetSelectItems.add(selectItem);
            }
        }
    }

    public List<String> getAllDatasets() {
        return allDatasets;
    }

    public List<SelectItem> getAllDatasetSelectItems() {
        return allDatasetSelectItems;
    }

    public SelectItem createSelectItem(String dataset) {
        if (dataset == null) throw new IllegalArgumentException("null dataset passed");
        SelectItem selectItem = null;
        if (dataset.contains("-")) {
            String[] tokens = dataset.split("-");
            selectItem = new SelectItem(dataset, tokens[0].trim());
        } else {
            selectItem = new SelectItem(dataset);
        }
        return selectItem;
    }
}
