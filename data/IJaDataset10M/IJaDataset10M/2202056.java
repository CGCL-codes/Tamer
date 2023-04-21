package org.identifylife.character.store.oxm.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.identifylife.character.store.model.Dataset;
import org.identifylife.character.store.model.DatasetRef;

/**
 * @author dbarnier
 *
 */
public class DatasetRefAdapter extends XmlAdapter<DatasetRef, Dataset> {

    @Override
    public DatasetRef marshal(Dataset dataset) throws Exception {
        if (dataset == null) {
            return null;
        }
        DatasetRef datasetRef = new DatasetRef();
        datasetRef.setRef(dataset.getUuid());
        return datasetRef;
    }

    @Override
    public Dataset unmarshal(DatasetRef datasetRef) throws Exception {
        Dataset dataset = new Dataset();
        dataset.setUuid(datasetRef.getRef());
        return dataset;
    }
}
