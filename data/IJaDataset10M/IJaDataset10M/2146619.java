package net.entropysoft.transmorph;

import java.util.HashMap;
import java.util.Map;
import net.entropysoft.transmorph.context.ConvertedObjectPool;
import net.entropysoft.transmorph.context.UsedConverters;

/**
 * The conversion context.
 * 
 * <p>
 * You can add custom objects to the context using add method.
 * </p>
 * 
 * @author Cedric Chabanois (cchabanois at gmail.com)
 * 
 */
public class ConversionContext {

    private ConvertedObjectPool convertedObjectPool = new ConvertedObjectPool();

    private Map<String, Object> map = new HashMap<String, Object>();

    private UsedConverters usedConverters = new UsedConverters();

    private boolean storeUsedConverters = false;

    public boolean isStoreUsedConverters() {
        return storeUsedConverters;
    }

    public void setStoreUsedConverters(boolean storeUsedConverters) {
        this.storeUsedConverters = storeUsedConverters;
    }

    /**
	 * get the pool of converted objects
	 * 
	 * @return
	 */
    public ConvertedObjectPool getConvertedObjectPool() {
        return convertedObjectPool;
    }

    /**
	 * get the converters that have been used for conversion
	 * 
	 * @return
	 */
    public UsedConverters getUsedConverters() {
        return usedConverters;
    }

    /**
	 * add an object to the context
	 * 
	 * @param key
	 * @param value
	 */
    public void add(String key, Object value) {
        map.put(key, value);
    }

    /**
	 * get object from the context
	 * 
	 * @param key
	 */
    public Object get(String key) {
        return map.get(key);
    }

    /**
	 * remove object from the context
	 * 
	 * @param key
	 */
    public void remove(String key) {
        map.remove(key);
    }
}
