package net.martinimix.beans.bind;

import java.util.LinkedHashMap;
import java.util.Map;
import net.martinimix.beans.BasePairPropertyEditorRegistry;
import net.martinimix.beans.MartiniMixBasePairPropertyEditorRegistry;
import net.martinimix.beans.bind.support.DNABasePairPropertyEditor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;
import com.bluemartini.dna.DNABasePair;
import com.bluemartini.dna.DNAList;
import com.bluemartini.dna.DNAListIterator;

/**
 * Provides an abstract bean binder.
 * 
 * @author Scott Rossillo
 *
 */
public abstract class AbstractBeanBinder implements BeanBinder {

    private static final Log log = LogFactory.getLog(AbstractBeanBinder.class);

    private BasePairPropertyEditorRegistry basePairPropertyEditorRegistry = new MartiniMixBasePairPropertyEditorRegistry();

    /**
	 * Creates a new abstract bean data binder.
	 */
    public AbstractBeanBinder() {
    }

    /**
	 * Binds the source lists properties to the target bean's fields using Spring's <code>DataBinder</code>
	 * plumbing.  This bind method facilitates binding source properties to fields with a 
	 * different name than those of the source properties.  Before binding the bean a
	 * {@link MutablePropertyValues} is created by calling {@link #prepareFieldValues(DNAList, Object)} to create
	 * a map of properties keyed by target field names.  
	 * 
	 * @see net.martinimix.dao.bind.BeanBinder#bind(com.bluemartini.dna.DNAList, java.lang.Object)
	 */
    public final Object bind(DNAList source, Object target) {
        DataBinder binder = new DataBinder(target);
        binder.bind(new MutablePropertyValues(prepareFieldValues(source, target)));
        return binder.getBindingResult().getTarget();
    }

    /**
	 * Maps the fields from the given source to the target object.
	 * 
	 * <p>
	 * The returned map contains field names of given
	 * <code>target</code> as keys mapped to the values of the
	 * <code>source</code>'s properties as determined by the 
	 * {@link #propertyToFieldNameMap(Object)} implementation.
	 * </p>
	 * 
	 * @param source a <code>DNAList</code> or subclass thereof containing
	 * properties to be mapped to fields on the given <code>target</code>
	 * 
	 * @param target the Java bean whose fields should be mapped to the
	 * properties set in the given <code>source</code>
	 * 
	 * @return a <code>Map</code> containing the field names of given
	 * <code>target</code> as keys mapped to the values of the
	 * <code>source</code>'s properties
	 * 
	 */
    protected final Map prepareFieldValues(DNAList source, Object target) {
        if (source == null) {
            throw new IllegalArgumentException("The DNAList [source] cannot be null!");
        }
        if (target == null) {
            throw new IllegalArgumentException("The [target] bean cannot be null!");
        }
        final Map propertyToFieldNameMap = propertyToFieldNameMap(target);
        final Map map = new LinkedHashMap();
        DNAListIterator it = source.iterator();
        String key;
        String fieldName;
        while (it.hasNext()) {
            key = it.nextName();
            fieldName = (String) propertyToFieldNameMap.get(key);
            if (fieldName == null) {
                continue;
            }
            if (log.isDebugEnabled()) {
                log.debug("Mapping property [" + key + "] to field [" + fieldName + "]");
            }
            map.put(fieldName, parseValue(source.getBasePair(key)));
        }
        return map;
    }

    /**
	 * Returns a map containing the business object property names
	 * as keys mapping to the field names on the given bean to
	 * which values can be assigned.
	 * 
	 * @param bean the <code>Object</code> whose property to field mappings
	 * should be returned
	 * 
	 * @return a <code>Map</code> containing the business object property
	 * names as keys mapping to the <code>bean</code> field names to
	 * which values can be assigned.
	 */
    protected abstract Map propertyToFieldNameMap(Object bean);

    /**
	 * Returns the encapsulated value of the given DNA base pair using
	 * this data binder's property editor registry.
	 */
    private Object parseValue(DNABasePair value) {
        DNABasePairPropertyEditor editor = basePairPropertyEditorRegistry.findCustomEditor(value.getType());
        return editor != null ? editor.setAsBasePair(value) : value.getValue();
    }

    /**
	 * Sets the property editor registry for this data binder.
	 * 
	 * @param basePairPropertyEditorRegistry the 
	 * <code>BasePairPropertyEditorRegistry</code> to use for binding
	 * properties to fields
	 */
    public final void setBasePairPropertyEditorRegistry(BasePairPropertyEditorRegistry basePairPropertyEditorRegistry) {
        this.basePairPropertyEditorRegistry = basePairPropertyEditorRegistry;
    }
}
