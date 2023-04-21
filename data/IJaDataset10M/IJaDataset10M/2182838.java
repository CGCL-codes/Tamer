package com.idna.batchid.service.record.product.remoteservices;

import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import com.idna.batchid.model.ProductRequest;
import com.idna.batchid.model.RecordRequest;
import com.idna.batchid.model.database.Service;
import com.idna.batchid.service.record.product.ProductRequestGenerator;
import com.idna.batchid.util.log.BatchIdLoggerFactoryImpl;

/**
 * Factory to retrieve ProductTask objects (requires the name of the product 
 * to be specified in the RemoteService domain object WITHOUT spaces).
 * 
 * @author Matthew Cosgrove
 */
public class ProductTaskFactory implements BeanFactoryAware {

    protected final Logger logger = Logger.getLogger(this.getClass().getName(), new BatchIdLoggerFactoryImpl());

    protected Map<String, String> productTasks;

    protected ProductRequestGenerator productRequestGenerator;

    protected BeanFactory beanFactory;

    /**
	 * Factory method to retrieve a <tt>ProductTask</tt> object based on a <tt>Service</tt>.
	 * 
	 * @param service
	 * @param recordRequest
	 * @return the product task specific to one of our products
	 * @throws Exception
	 */
    public ProductTask getProductTask(Service service, RecordRequest recordRequest) throws Exception {
        ProductTask task = null;
        if (service != null) {
            task = new ProductTask();
            task.setService(service);
            if (service.getParameters() != null) {
                recordRequest.getProductFields().putAll(service.getParameters());
            }
            if (service.getXslt() == null) {
                logger.error(String.format("The [%s] service does not have any XSLT configured, it will not be run.", service.getTaskName()));
            } else if (service.getUrl() == null) {
                logger.error(String.format("The [%s] service does not have a URL configured, it will not be run", service.getTaskName()));
            } else {
                ProductRequest productRequest = productRequestGenerator.generateProductRequest(service.getXslt(), recordRequest);
                task.setProductRequest(productRequest);
                task.setRecordRequest(recordRequest);
            }
        }
        return task;
    }

    public void setProductRequestGenerator(ProductRequestGenerator productRequestGenerator) {
        this.productRequestGenerator = productRequestGenerator;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
