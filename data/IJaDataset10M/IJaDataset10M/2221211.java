package com.idna.gav.rules.international.postgav.chain.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanNameAware;
import com.idna.gav.rules.international.AbstractGavRule;
import com.idna.gav.rules.international.postgav.chain.data.ProcessingData;
import com.idna.gav.rules.international.postgav.chain.processor.DataProcessor;

/**
 * 
 * A reasonably generic processing manager based on the chain of responsibility pattern.
 * 
 * This class is an element (or link) in the chain. In this implementation this class does not
 * actually do the processing, but defers execution to one of a number of processors that it will 
 * decide between which to execute based on some rule that can be overridden. These processors 
 * and injected via dependency injection and will be null if this is not done.
 * 
 * This class contains two main types of {@link DataProcessor} with which to defer processing.
 * 
 * A default processor, and a Map of specific processors. 
 * 
 * For any data given to be processed, it should only run through a single processor (for 
 * efficiency in this case, rather executing than all of them like the typical chain 
 * of responsibility patten). 
 * Using a String from the input data, this class will try to find a processor in the map that
 * matches the key name (For example a country code, for a country specific processor). If no
 * matching key can be found, a default processor will be executed instead. This allows minimal
 * configuration for situations where only a few processors are needed to be called specifically.
 * 
 * This abstract class implements most of the logic except how to decide how to get the key. This
 * must be overridden. If any the of the processors are null this class should exit safely and log
 * to error.
 * 
 * @author gawain.hammond
 *
 */
public abstract class AbstractProcessorManager implements BeanNameAware {

    public final Logger logger = Logger.getLogger(this.getClass());

    DataProcessor defaultProcessor;

    Map<String, DataProcessor> countrySpecificProcessors;

    private String beanName;

    public AbstractProcessorManager() {
        countrySpecificProcessors = new HashMap<String, DataProcessor>();
    }

    /**
	 * The main processing method
	 * 
	 * @param data
	 */
    public void startProcessing(ProcessingData data) {
        String key = getDataKey(data);
        if (countrySpecificProcessors != null && countrySpecificProcessors.containsKey(key)) {
            logger.info("[" + beanName + "] key (" + key + ") exists in processor map, executing specific processor");
            DataProcessor preferedProcessor = countrySpecificProcessors.get(key);
            preferedProcessor.executeProcessing(data);
        } else {
            logger.info("[" + beanName + "] key (" + key + ") does not exists in processor map, executing default processor");
            executeDefaultProcessing(data);
        }
    }

    public void setCountrySpecificProcessors(Map<String, DataProcessor> countrySpecificProcessors) {
        this.countrySpecificProcessors = countrySpecificProcessors;
    }

    public void addCountrySpecificProcessor(String key, DataProcessor processor) {
        this.countrySpecificProcessors.put(key, processor);
    }

    public void setDefaultProcessor(DataProcessor defaultProcessor) {
        this.defaultProcessor = defaultProcessor;
    }

    private void executeDefaultProcessing(ProcessingData data) {
        String key = getDataKey(data);
        if (defaultProcessor != null) {
            logger.info("[" + beanName + "] defaultProcessor executing...");
            defaultProcessor.executeProcessing(data);
        } else {
            logger.info("[" + beanName + "] No specific processor found and default processor not set, no processing will be executed for key: [" + key + "]");
        }
    }

    /**
	 * Override this method to decide how to get the key for the processor Map.
	 * This key will be used to decide which processor to execute.
	 * 
	 * @param data the key should be extracted from the data. e.g: get the country code from the volt xml
	 * @return the key that is used to decide what processor to execute.
	 */
    public abstract String getDataKey(ProcessingData data);

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
