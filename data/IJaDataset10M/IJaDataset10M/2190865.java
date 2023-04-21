package org.lc.support.report.manager;

import org.lc.service.BaseService;
import de.laures.cewolf.DatasetProducer;

public class CeWolfManager extends BaseService {

    public DatasetProducer getDatasetproducer(String producerId, Object obj) {
        DatasetProducerImpl datasetProducer = new DatasetProducerImpl();
        datasetProducer.setProducerId(producerId);
        datasetProducer.setDatasetObject(obj);
        return datasetProducer;
    }
}
